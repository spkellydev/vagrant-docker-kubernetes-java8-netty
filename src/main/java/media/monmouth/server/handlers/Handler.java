package media.monmouth.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import media.monmouth.server.controllers.Controller;
import media.monmouth.server.controllers.Route;
import java.util.Map;

/**
 * Handler is the default handler for inbound requests.
 *
 */
public class Handler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private Map<Route, Controller> endpoints;
    private String path;
    private HttpMethod method;

    protected Handler(Map<Route, Controller> endpoints) {
        this.endpoints = endpoints;
    }

    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        // avoid invalid http request
        validateContext(ctx, msg);
        // set path and method
        routeFilters(msg);
        endpoints.forEach((Route route, Controller controller) -> {
            // check request path against endpoint path
            if(path.equals(route.getEndpoint()))
            {
                // check request method against endpoint method
                if(method.equals(route.getMethod()))
                {
                    // get the request data
                    String jsonResponse = marshalJson(msg.content(), controller);

                    // set the response data
                    ByteBuf responseBytes = ctx.alloc().buffer();
                    responseBytes.writeBytes(jsonResponse.getBytes());

                    // controller.respond
                    FullHttpResponse response = new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        controller.getCode(),
                        responseBytes
                    );

                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.writeAndFlush(response);
                }
                else
                {
                    ctx.close();
                }
            }
        });

    }

    /**
     * validateContext
     * Check for invalid http data through the DecoderResult class.
     * Common implements which deal with the pack fragmentation and reassembly issues found in a stream-based
     * transport, such as TCP/IP.
     * @param ctx current request context
     * @param msg current request msg
     */
    private void validateContext(ChannelHandlerContext ctx, FullHttpRequest msg) {
        if(msg.decoderResult() != DecoderResult.SUCCESS)
        {
            ctx.close();
        }
    }

    /**
     * Filters for routes from request
     * @param msg current request msg
     * @return Route
     */
    private void routeFilters(FullHttpRequest msg) {
        this.path = msg.uri();
        this.method = msg.method();
    }

    /**
     * Takes an inbound message and maps it to a controller
     * @param msg inbound message
     * @param controller response controller
     * @return marshaled JSON
     */
    private String marshalJson(ByteBuf msg, Controller controller) {
        String jsonRequest = msg.toString(CharsetUtil.UTF_8);
        return controller.handle(jsonRequest);
    }
}
