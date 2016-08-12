package echo1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import messageEncoder.KryoDecoder;
import messageEncoder.KryoEncoder;
import messageEncoder.UserInfo;
import time3.TimeClientHandler;

/**
 * Created by lz on 2016/8/10.
 */
public class EchoClient {
    public void connect(int port,String host) throws Exception{
        //配置客户端NIO线程组
        EventLoopGroup group =new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>(){

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("frameDecpder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            socketChannel.pipeline().addLast("kryo decoder",new KryoDecoder(UserInfo.class));
                            socketChannel.pipeline().addLast("frameencoder",new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast("kryo encoder",new KryoEncoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        new EchoClient().connect(port,"127.0.0.1");
    }
}
