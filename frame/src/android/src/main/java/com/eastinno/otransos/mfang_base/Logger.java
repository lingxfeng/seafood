package com.eastinno.otransos.mfang_base;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志管理
 * tag固定（mfang_log),便于统一查看流程等
 * 支持日志缓存
 * 
 * @author wxc
 */
public class Logger {  

	private final static boolean DEBUG = true;
	private final static boolean SAVELOGFILE = false;
	private final static String TAG = "mfang_log";

	/**
	 * Send an INFO log message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, Object msg) {
		if (DEBUG) {
			android.util.Log.i(TAG, tag + " -> " + msg);
			if (SAVELOGFILE) {
				saveLoggerFile(tag,msg);
			}
			
		}
	}

	/**
	 * Send an ERROR log message.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, Object msg) {
		if (DEBUG) {
			android.util.Log.e(TAG, tag + " -> " + msg);
			if (SAVELOGFILE) {
				saveLoggerFile(tag,msg);
			}
		}
	}
	
	public static void e(String tag, Object msg ,Throwable tr){
		if(DEBUG){
			android.util.Log.e(TAG, tag +" -> "+msg, tr);
			if (SAVELOGFILE) {
				saveLoggerFile(tag,msg);
			}
		}
	}

	/**
	 * Send a DEBUG log message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, Object msg) {
		if (DEBUG) {
			android.util.Log.d(TAG, tag + " -> " + msg);
			if(SAVELOGFILE)
				saveLoggerFile(tag,msg);
		}
	}
	
	public static void write(String tag,Object msg){
		if(DEBUG){
			saveLoggerFile(tag, msg);
		}
	}
	
	private static void saveLoggerFile(String tag ,Object msg) {

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString(); // 取得SD根路径

		String fString = extStorageDirectory + "/"+TAG;
		File nFile = new File(fString);
		if (!nFile.exists()) {
			nFile.mkdirs();
		} else {

		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fileName = TAG+"_"+sdf.format(new Date())+".log";
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(fString + "/"+fileName, "rw");
			accessFile.seek(accessFile.length() + 1);
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			String loggerMsg = "["+sdf2.format(new Date() )+"]  "+ tag+" :" +msg; 
			
			accessFile.write(loggerMsg.getBytes("UTF-8"));
			accessFile.write('\n');
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}