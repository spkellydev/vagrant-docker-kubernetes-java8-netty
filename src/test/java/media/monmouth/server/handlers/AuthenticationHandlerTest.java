package media.monmouth.server.handlers;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import media.monmouth.server.Server;
import org.junit.Test;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthenticationHandlerTest {
    public EmbeddedChannel channel;
    @Test
    public void shouldAcceptUTF8() {
        EmbeddedChannel channel = new EmbeddedChannel(new StringDecoder(CharsetUtil.UTF_8));
        channel.writeInbound(Unpooled.wrappedBuffer(new byte[]{(byte)0xE2,(byte)0x98,(byte)0x95}));
        String myObject = channel.readInbound();
        // Perform checks on your object
        assertEquals("☕", myObject);
    }

    public void setChannel(EmbeddedChannel ch) {
        channel = ch;
    }

    @Test
    public void shouldAcceptPostOnLogoutRoute() throws MalformedURLException {

    }
}
