package com.blindstick.netty.handler.channel;

import com.blindstick.netty.handler.receive.GetDeviceHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class SocketHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(SocketHandler.class);


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            ConnectManager.deleteAisle(ctx);//删除链接信息
            ctx.fireChannelActive();
        } catch (Exception e) {
            e.printStackTrace();
            ctx.channel().close();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        GetDeviceHandler.getDeviceInfo(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        try {
            ctx.channel().read();
            ctx.flush();
        } catch (Exception e) {
            ctx.close();
        }
    }

    /**
     * 通道异常调用该方法
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info(new Date() + "设备编号:" + ConnectManager.getAisle(ctx) + "客户端关闭");
        ConnectManager.deleteAisle(ctx);
        ConnectManager.deleteData(ctx);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        logger.info("用户监听::" + evt.toString());
        super.userEventTriggered(ctx, evt);
        if ((evt instanceof IdleStateEvent)) {
            IdleStateEvent event = (IdleStateEvent) evt;

            if (event.state().equals(IdleState.READER_IDLE)) {
                logger.info("READER_IDLE:未进行读操作,关闭通道");

                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                logger.info("WRITER_IDLE:未进行写操作");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                logger.info("ALL_IDLE:未进行进行读写操作");
            }
        }
    }

}
