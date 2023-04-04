

package com.blindstick.netty.service;

import com.blindstick.netty.handler.channel.SocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public void start(Integer port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        ((ServerBootstrap)((ServerBootstrap)bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class))
                .option(ChannelOption.SO_BACKLOG, 8192))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new IdleStateHandler(60L, 0L, 0L, TimeUnit.MINUTES));
                        /* socketChannel.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(2048,1,2,3,0,true));*/
                        socketChannel.pipeline()
                                .addLast(new SocketHandler());
                        /* 解决tcp拆包、粘包问题方案一：固定长度
                        socketChannel.pipeline()
                                .addLast(new FixedLengthFrameDecoder(16)); */
                        /* 解决tcp拆包、粘包问题方案二：分隔符
                         socketChannel.pipeline()
                                .addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.wrappedBuffer("ghh".getBytes()))); // 自定义分隔符
                        socketChannel.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024)); // 换行分隔符 */
                        /* 解决tcp拆包、粘包问题方案三：自定义结构体
                        自定义一个NettyMsgEncoder类继承MessageToByteEncoder<NettyMsg>重写编码方法
                         一个NettyMsgDecoder类继承ByteToMessageDecoder重写解码方法*/
                    }
                });
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("服务模块启动成功，端口："+port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException var10) {
            var10.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
