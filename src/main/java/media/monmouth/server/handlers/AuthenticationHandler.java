package media.monmouth.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import media.monmouth.server.controllers.Controller;
import media.monmouth.server.controllers.LoginController;
import media.monmouth.server.controllers.LogoutController;

public class AuthenticationHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        // check for invalid http data through the DecoderResult class.
        // Common implements which deal with the pack fragmentation and reassembly issues found in a stream-based
        // transport, such as TCP/IP
        if(msg.decoderResult() != DecoderResult.SUCCESS) {
            ctx.close();
        }

        // harvest some of our HTTP data
        HttpMethod method = msg.method();
        String uri = msg.uri();

        Controller controller;
        if(uri.equals("/login"))
        {
            controller = new LoginController();
        }
        else if (uri.equals("/logout"))
        {
            controller = new LogoutController();
        }
        else
        {
            //TODO: set a 501 - not implement header
            ctx.close();
            return;
        }

        // set the response
        ByteBuf responseBytes;
        if(method.equals(HttpMethod.POST))
        {
            ByteBuf data = msg.content();
            String jsonRequest = data.toString(CharsetUtil.UTF_8);
            String jsonResponse = controller.handle(jsonRequest);

            responseBytes = ctx.alloc().buffer();
            responseBytes.writeBytes(jsonResponse.getBytes());
        }
        else
        {
            // TODO: 501 - not implemented
            ctx.close();
            return;
        }

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
}
