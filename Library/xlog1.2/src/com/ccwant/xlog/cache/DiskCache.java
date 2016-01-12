package com.ccwant.xlog.cache;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.os.Environment;
import android.provider.Contacts.Extensions;

/**
 * https://github.com/CCwant/XLog
 * @author ccwant
 *
 */
public class DiskCache {


	 /**
     * 保存到日志文件
     * @param content
     */ 
    public static synchronized String save(String dirName,String fileName,String content) 
    { 
        try 
        { 
        	String filePath=getFile(dirName,fileName);
            FileWriter writer = new FileWriter(filePath, true); 
            writer.write(content); 
            writer.close(); 
            return filePath;
        } 
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        } 
        return null;
    } 
   
    /**
     * 获取日志文件路径
     * @return
     */ 
    public static String getFile(String dirName,String fileName) 
    {
		String sdDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			sdDir = Environment.getExternalStorageDirectory().toString();
		}
		File cacheDir = new File(sdDir + File.separator + dirName);
		if (!cacheDir.exists())
			cacheDir.mkdir();
		File filePath = new File(cacheDir + File.separator + fileName +".txt");
		return filePath.toString();
	} 
}
