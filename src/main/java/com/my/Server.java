package com.my;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;



/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/22 16:40
 */
public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

      try {
          ServerBootstrap b = new ServerBootstrap();
          b.group(eventExecutors).channel(NioServerSocketChannel.class).localAddress(port)
                  .childHandler(new ChannelInitializer<Channel>() {
                      @Override
                      protected void initChannel(Channel ch) throws Exception {
                          ch.pipeline().addLast(new EchoServerHandler());
                      }
                  });
          ChannelFuture future = b.bind().sync();
          System.out.println(Server.class.getName() + "started and listen on " + future.channel().localAddress());
          future.channel().closeFuture().sync();
      }finally {
          eventExecutors.shutdownGracefully().sync();
      }

    }

    public static void main(String[] args) throws InterruptedException {
        new Server(7777).start();
    }
}
