package com.ccwant.xlog.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * https://github.com/CCwant/XLog
 * @author ccwant
 *
 */
public class XLogUtils {

	
	
	 /**
     * 标识每条日志产生的时间
     * @return
     */ 
	public static String time() 
    { 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); 
        return "[" + sdf.format(new Date(System.currentTimeMillis())) + "] "; 
    } 
   
    /**
     * 以年月日作为日志文件名称
     * @return
     */ 
	public static String date() 
    { 
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai")); 
        return sdf.format(new Date(System.currentTimeMillis())); 
    } 
}
