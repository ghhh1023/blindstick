package com.blindstick.utils;

import java.io.*;
import java.util.Locale;

public class FileUtil {
    //读取文件内容并返回
    public static String readToString(String path){
        InputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = null;
        if (is != null) {
            isr = new InputStreamReader(is);
        }
        BufferedReader br = new BufferedReader(isr);
        String str = null;
        StringBuilder sb = new StringBuilder();
        while (true)
        {
            try {
                if (!((str = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(str);
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }

    //向文件里写入内容
    public static void saveAsFileWriter(String path, String content,boolean notCover){
        File file = new File(path);
        //如果目录不存在 创建目录
        try {
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        FileWriter fileWriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fileWriter = new FileWriter(path, notCover);
            fileWriter.write(content);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void saveToImgFile(String src, String output)
    {
        if (src == null || src.length() == 0)
        {
            return;
        }
        try
        {
            FileOutputStream out = new FileOutputStream(output);
            byte[] bytes = src.getBytes("utf-8");
            for (int i = 0; i < bytes.length; i += 2)
            {
                out.write(charToInt(bytes[i]) * 16 + charToInt(bytes[i + 1]));
            }
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static int charToInt(byte ch)
    {
        int val = 0;
        if (ch >= 0x30 && ch <= 0x39)
        {
            val = ch - 0x30;
        }
        else if (ch >= 0x41 && ch <= 0x46)
        {
            val = ch - 0x41 + 10;
        }
        return val;
    }
}