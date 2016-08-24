package websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;


/**
 * Created by lz on 2016/8/12.
 */
public class WebSocketServer {
    public void run(int port) throws Exception{
        //创建两组线程，监听连接和工作
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            //Netty用于启动Nio服务端的启动类
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    //注册NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                     //注册处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //用于Http请求的编码或者解码
                            pipeline.addLast("http-codec", new HttpServerCodec());
                            //把Http消息组成完整地HTTP消息
                            pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
                            //向客户端发送HTML5文件
                            pipeline.addLast("http-chunked", new ChunkedWriteHandler());
                            //实际处理的Handler
                            pipeline.addLast("handler", new WebSocketServerHandler());
                        }
                    });
            Channel ch = b.bind(port).sync().channel();
            System.out.println("Web socket server started at port " + port + '.');
            System.out.println("Open your browser and navigate to http://localhost:" + port + '/');
            ch.closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
         new WebSocketServer().run(port);
    }
}
