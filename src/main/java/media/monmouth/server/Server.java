package media.monmouth.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import media.monmouth.server.handlers.AuthenticationHandler;

/**
 * Server class
 * The server class is the entry point to the application, to kickstart Netty services;
 */
public class Server {
    private int port;
    private Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        // if the port doesn't come from command line, default to 8080
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        new Server(port).run();
    }

    private void run() {
        // Create bossGroup and workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // kickstart server
        try
        {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer())
                    .channel(NioServerSocketChannel.class);
            Channel ch = b.bind(port).sync().channel();
            ch.closeFuture().sync();
        }
        catch(InterruptedException e)
        {
            System.out.println(e.getStackTrace());
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
