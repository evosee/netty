package com.my.http;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @author chensai
 * @version 1.0
 * @date 2019/5/24 17:14
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        System.out.println(msg.uri());
        ctx.writeAndFlush(msg.uri()).addListener(ChannelFutureListener.CLOSE);
    }
}
