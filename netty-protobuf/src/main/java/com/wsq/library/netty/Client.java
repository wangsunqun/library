package com.wsq.library.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class Client {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8000;

    public static void main(String[] args) {
        new Client().start(HOST, PORT);
    }

    public void start(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap client = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encoder", new ProtobufEncoder()); // protobuf 编码器
                            // 需要指定要对哪种对象进行解码
                            pipeline.addLast("decoder", new ProtobufDecoder(UserInfo.UserMsg.getDefaultInstance()));
                            pipeline.addLast();
                            pipeline.addLast(new NettyClientHandler());
                        }
                    });

            ChannelFuture future = client.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static class NettyClientHandler extends ChannelInboundHandlerAdapter {
        /**
         * 通道就绪触发该方法
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("client channel Active");
            // 发送 User POJO 对象到服务器
            UserInfo.UserMsg user = UserInfo.UserMsg.newBuilder().setId(10).setName("客户端张三").build();
            ctx.writeAndFlush(user);
        }

        /**
         * 当通道有读取事件时触发该方法
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 读取服务器发送的数据 UserInfo.UserMsg
            UserInfo.UserMsg user = (UserInfo.UserMsg) msg;
            System.out.println("收到服务器响应: " + user.getId() + "--" + user.getName());
        }
    }
}
