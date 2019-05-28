package com.my.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/24 16:51
 */
public class HttpServer {
    private int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup eventExecutors = new NioEventLoopGroup(2);

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,eventExecutors).localAddress(port).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());// http 编解码
                            pipeline.addLast("httpAggregator", new HttpObjectAggregator(512 * 1024)); // http 消息聚合器                                                                     512*1024为接收的最大contentlength

                            pipeline.addLast(new HttpResponseHandler());
                            pipeline.addLast(new HttpRequestHandler());// 请求处理器
                        }
                    });

             ChannelFuture future = b.bind().sync();
            future.channel().closeFuture().sync();

        }finally {
            eventExecutors.shutdownGracefully().sync();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        new HttpServer(8888).start();
    }
}
