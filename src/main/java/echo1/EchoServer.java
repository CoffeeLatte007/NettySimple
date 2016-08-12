package echo1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import messageEncoder.KryoDecoder;
import messageEncoder.KryoEncoder;
import messageEncoder.UserInfo;
import time3.TimeServerHandler;

/**
 * Created by lz on 2016/8/10.
 */
public class EchoServer {
    public void bind(int port) throws Exception{
        //配置服务端的NIO线程组,一个用于接收连接，一个用于处理连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024) //设置accept的最大 也就是三次握手成功的队列长度
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChildChannelHandler());
            //绑定端口，同步等待
            ChannelFuture f = b.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出,释放线程资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            System.out.println("已经绑定");
            socketChannel.pipeline().addLast("frameDecpder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
            socketChannel.pipeline().addLast("kryo decoder",new KryoDecoder(UserInfo.class));
            socketChannel.pipeline().addLast("frameencoder",new LengthFieldPrepender(4));
            socketChannel.pipeline().addLast("kryo encoder",new KryoEncoder());
            socketChannel.pipeline().addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new EchoServer().bind(port);
    }
}
