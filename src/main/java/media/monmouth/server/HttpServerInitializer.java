package media.monmouth.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.timeout.IdleStateHandler;
import media.monmouth.server.handlers.AuthenticationHandler;

public class HttpServerInitializer extends ChannelInitializer<Channel> {
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addFirst("idleStateHandler", new IdleStateHandler(60, 30, 0));
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(Short.MAX_VALUE));
        pipeline.addLast(new AuthenticationHandler());
    }
}
