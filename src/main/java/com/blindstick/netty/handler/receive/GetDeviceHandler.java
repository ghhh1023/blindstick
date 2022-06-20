package com.blindstick.netty.handler.receive;


import com.blindstick.netty.handler.channel.ConnectManager;
import com.blindstick.utils.BaiduAPI;
import com.blindstick.utils.FileUtil;
import com.blindstick.utils.HexUtil;
import com.blindstick.utils.HuaweiAPI;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GetDeviceHandler {
    private static Logger logger = LoggerFactory.getLogger(GetDeviceHandler.class);

    public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public  static boolean imgStart=false;
    private static final String startRegex="ffd8";
    private static final String endRegex="ffd9$";

    // 静态变量
    private static HuaweiAPI huaweiAPI;
    @Autowired
    private HuaweiAPI huaweiAPI2;
    @PostConstruct
    public void init() {
        GetDeviceHandler.huaweiAPI = this.huaweiAPI2;
    }

    // 静态变量
    private static BaiduAPI baiduAPI;
    @Autowired
    private BaiduAPI baiduAPI2;
    @PostConstruct
    public void init2() {
        GetDeviceHandler.baiduAPI = this.baiduAPI2;
    }



    /**
     * 数据接收处理器
     *
     * @param ctx 通道内容
     * @param msg 信息对象
     */
    public static void getDeviceInfo(ChannelHandlerContext ctx, Object msg) {
        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String receiveStr = new String(bytes, StandardCharsets.UTF_8);
            getMessage(bytes, ctx);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.close();
        } finally {
            //如果指定的消息实现了ReferenceCounted，尝试调用ReferenceCounted.release(int)。如果指定的消息没有实现ReferenceCounted，则此方法不执行任何操作。
            //ReferenceCounted.release(int)将引用计数按指定的递减量减少，并在引用计数达到0时释放此对象。返回:当且仅当引用计数为0且该对象已被释放时为真
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 数据处理
     */
    public static void getMessage(byte[] bytes, ChannelHandlerContext ctx) {
        //字节数据--》16进制数据
        String receiveHex = HexUtil.receiveHexToString(bytes);

        DateFormat df7 = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.CHINA);
        String time3 = df7.format(new Date());
        logger.info(time3 + "上传原始数据:" + receiveHex);
        //接收图片并识别返回
        if(!imgStart){
            Matcher matcher= Pattern.compile(startRegex).matcher(receiveHex);
            if(matcher.find()){
                int start = matcher.start();
                String tempStr=receiveHex.substring(start);
                imgStart=true;
                FileUtil.saveAsFileWriter("/tmp/images/image.txt",tempStr,false);
            }
        }
        else{
            Matcher matcher= Pattern.compile(endRegex).matcher(receiveHex);
            if(matcher.find()){
                do {
                    int start = matcher.start();
                    if (start % 2 == 0) {
                        String tempStr = receiveHex.substring(0, start + 4);
                        imgStart = false;
                        FileUtil.saveAsFileWriter("/tmp/images/image.txt", tempStr, true);
                        FileUtil.saveToImgFile(FileUtil.readToString("/tmp/images/image.txt"), "/tmp/images/image.jpeg");
                        String localPath = "/tmp/images/image.jpeg";
                        String obsPath = "bind/image.jpeg";
                        String obsPath2 = "image.jpeg";
                        huaweiAPI.uploadImage(localPath, obsPath);
                        tempStr = huaweiAPI.getImageTag(obsPath2);
                        tempStr=tempStr.replaceAll("[^\\u4e00-\\u9fa5]+","");
                        System.out.println(tempStr);
                        String wordsStr= baiduAPI.getWordMessage(localPath);
                        // 发送识别信息给硬件
                        List<String> addrs = ConnectManager.getDeviceAll();
                        for (String addr : addrs) {

                            String message1="{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+"\""+tempStr+"\""+"}";
                            String message2="{\"cmd\":"+"\"soundPlay\""+",\"msg\""+":"+wordsStr+"}";
                            System.out.println(message1);
                            System.out.println(message2);
                            byte[] strBytes1=null;
                            byte[] strBytes2=null;
                            try {
                                strBytes1=message1.getBytes("GBK");
                                strBytes2=message2.getBytes("GBK");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //下发指令
                            ConnectManager.sendMessage(addr,HexUtil.bytes2HexString(strBytes1));
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ConnectManager.sendMessage(addr,HexUtil.bytes2HexString(strBytes2));
                        }
                        break;
                    }
                }while (matcher.find());
            }
            else {
                FileUtil.saveAsFileWriter("/tmp/images/image.txt",receiveHex,true);
            }
        }
        String strReceiveASCII = HexUtil.convertHexToString(receiveHex);
//        System.out.println(strReceiveASCII);
        //注册
        if(strReceiveASCII.contains("zc_")){
            logger.info(time3 + "注册:" + strReceiveASCII);

            String receiveHex_Da = receiveHex.toUpperCase(Locale.ROOT);

            String[] split = receiveHex_Da.split("5F");
            String addressId = split[1];
            ConnectManager.putAisle(ctx, addressId);
        }

        //心跳
        if(strReceiveASCII.contains("xt_")){
            logger.info(time3 + "心跳:" + strReceiveASCII);
        }

        if(receiveHex.contains("fefefe")){
            logger.info(time3 + "数据包:" + receiveHex);
        }
    }


}
