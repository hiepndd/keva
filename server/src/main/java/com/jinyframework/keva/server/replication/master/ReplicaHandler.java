package com.jinyframework.keva.server.replication.master;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

public class ReplicaHandler extends SimpleChannelInboundHandler<String> {
    private final CompletableFuture<Object> resPromise;

    public ReplicaHandler(CompletableFuture<Object> resPromise) {
        this.resPromise = resPromise;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        resPromise.complete(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.channel().pipeline().remove(this);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        resPromise.completeExceptionally(cause);
        ctx.close();
    }

}
