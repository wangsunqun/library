package com.wsq.library.netty;

import com.wsq.library.netty.protobuf.UserInfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import java.net.InetSocketAddress;

public class Server {
    private final int port;

    public static void main(String[] args) {
        new Server(8000).start();
    }

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap()
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("encoder", new ProtobufEncoder()); // protobuf 编码器
                            // 需要指定要对哪种对象进行解码
                            pipeline.addLast("decoder", new ProtobufDecoder(UserInfo.UserMsg.getDefaultInstance()));
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            // 绑定端口
            ChannelFuture future = server.bind(port).sync();
            System.out.println("server started and listen " + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }

    public static class NettyServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("server channel active");
        }

        @Override
        public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println("server channelRead...");
            // 读取客户端发送的数据 UserInfo.UserMsg
            UserInfo.UserMsg user = (UserInfo.UserMsg) msg;
            System.out.println("客户端发送的数据: " + user.getId() + "--" + user.getName());
        }

        /**
         * 数据读取完毕
         */
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            UserInfo.UserMsg user = UserInfo.UserMsg.newBuilder().setId(20).setName("服务器").build();
            ctx.writeAndFlush(user);
        }

        /**
         * 处理异常，关闭通道
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.channel().close();
        }
    }
}
