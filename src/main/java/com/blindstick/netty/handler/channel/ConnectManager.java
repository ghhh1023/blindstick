package com.blindstick.netty.handler.channel;



import com.blindstick.utils.HexUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Socket连接通道管理器
 */
public class ConnectManager {

    private static Logger logger = LoggerFactory.getLogger(ConnectManager.class);

    private static final Map<String, Map> map = new HashMap<>();

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        /**
         * 数据管理器
         */
        map.put("data", new HashMap<String, String>());
        /**
         * 通道管理器
         */
        map.put("aisle", new HashMap<String, ChannelHandlerContext>());
    }

    public static synchronized String getData(ChannelHandlerContext chc) throws NullPointerException {
        if (chc == null) {
            throw new NullPointerException();
        }
        return (String) map.get("data").get(getAddress(chc));
    }

    public static synchronized void putData(ChannelHandlerContext chc, String str) throws NullPointerException {
        if (chc == null || str == null) {
            throw new NullPointerException();
        }
        map.get("data").put(getAddress(chc), str);
    }

    public static void deleteData(ChannelHandlerContext chc) throws NullPointerException {
        if (chc == null) {
            throw new NullPointerException();
        }
        if (map.get("data").get(getAddress(chc)) != null) {
            map.get("data").remove(getAddress(chc));
        }
    }

    public static synchronized String getAisle(ChannelHandlerContext chc) throws NullPointerException {
        if (chc == null) {
            throw new NullPointerException();
        }
        Map<String, ChannelHandlerContext> aisle = ConnectManager.map.get("aisle");
        Iterator<String> iterator = aisle.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            ChannelHandlerContext chcTemp = aisle.get(next);
            if (getAddress(chc).equals(getAddress(chcTemp))) {
                return next;
            }
        }
        return null;
    }

    public static synchronized void putAisle(ChannelHandlerContext chc, String No) {
        if (chc == null || No == null) {
            throw new NullPointerException();
        }
        ChannelHandlerContext aisle = (ChannelHandlerContext) map.get("aisle").get(No);
        //TODO 设备首次上线，发消息
        if (aisle == null) {
            logger.info(df.format(new Date()) + "设备编号:" + No + "上线");
        }

        map.get("aisle").put(No, chc);
    }

    public static synchronized void deleteAisle(ChannelHandlerContext chc) throws NullPointerException {
        if (chc == null) {
            throw new NullPointerException();
        }
        //设备编号
        String device = getAisle(chc);

        map.get("aisle").remove(device);

        logger.info(df.format(new Date()) + "设备编号:" + device + "下线");
        if (map.get("data").get(device) != null) {
            map.get("data").remove(device);
        }
    }

    public static boolean sendMessage(String device, String content) {

        if ((StringUtils.isBlank(device)) || (StringUtils.isBlank(content))) {
            return false;
        }

        ChannelHandlerContext ctx = getCtx(device);

        if (ctx == null) {
            return false;
        }

        return sendMessageCtx(ctx, content);
    }

    public static boolean sendMessageCtx(ChannelHandlerContext ctx, String content) {
        synchronized (ctx) {
            if (ctx != null) {
                try {
                    byte[] req= HexUtil.hexString2Bytes(content);
                    ByteBuf buf = Unpooled.buffer(req.length);
                    buf.writeBytes(req);
                    ctx.writeAndFlush(buf);
                    logger.info(df.format(new Date()) + "设备号:" + getAisle(ctx) + "/数据方向:发送/数据:" + content);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
            return false;
        }
    }

    private static synchronized ChannelHandlerContext getCtx(String device) {
        Map<String, ChannelHandlerContext> aisle = map.get("aisle");
        return aisle.get(device);
    }

    private static String getAddress(ChannelHandlerContext ctx) {
        return ctx.channel().remoteAddress().toString();
    }

    public static  List<String>  getDeviceAll() {
        Map aisle = map.get("aisle");
        return new ArrayList<>(aisle.keySet());
    }

}
