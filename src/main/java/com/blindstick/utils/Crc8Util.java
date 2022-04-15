package com.blindstick.utils;


public class Crc8Util {

    public static String crc8(String hexCommond) {
        int crc8 = calcCrc8(HexUtil.hexString2Bytes(hexCommond));
        String crc= HexUtil.numToHex8(crc8).toUpperCase();
        crc=crc.substring(crc.length()-2);
        return crc;
    }

    /**
     * CRC8 校验 多项式  x8+x6+x4+x3+x2+x1       0x5E
     * @param data
     * @return  校验和
     */
    public static  byte calcCrc8(byte[] data){
        byte crc = 0;
        for (int j = 0; j < data.length; j++) {
            crc ^= data[j];
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x80) != 0) {
                    crc = (byte) ((crc)<< 1);
                    crc ^= 0xE5;
                } else {
                    crc = (byte) ((crc)<< 1);
                }
            }
        }
        return crc;
    }


}
