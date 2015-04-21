/***
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

For more information, please refer to <http://unlicense.org/>
*/

package com.wechallenge.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.text.TextUtils;
import android.util.Log;


/**
 * @date 21.06.2012
 * @author Mustafa Ferhan Akman
 * 
 * Create a simple and more understandable Android logs. 
 * */

public class Logger{
	static ExecutorService executor = null;
	public static String logFilePath= "";//+FilePath.File_LOG;
	

	static String className;
	static String methodName;
	static int lineNumber;
	
    private Logger(){
        /* Protect from instantiations */
    }

	public static boolean isDebuggable() {
		return false;
	}

	private static String createLog( String log ) {

		StringBuilder buffer = new StringBuilder();
		buffer.append("[");
		buffer.append(className);
		buffer.append(".");
		buffer.append(methodName);
		buffer.append(":");
		buffer.append(lineNumber);
		buffer.append("]");
		buffer.append(log);

		return buffer.toString();
	}
	
	private static void getMethodNames(StackTraceElement[] sElements){
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}

	public static void d(String message){
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.d(className, msg);
	}
	
	public static void i(String message){
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.i(className, msg);
	}
	
	public static void v(String message){
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.v(className, msg);
	}
	
	public static void w(String message){
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.w(className, msg);
	}
	
	public static void w(String message,Throwable e){
		getMethodNames(e.getStackTrace());
		String msg = createLog(message+e);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.w(className, msg,e);
	}
	
	public static void e(String message){
		getMethodNames(new Throwable().getStackTrace());
		String msg = createLog(message);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.e(className, msg);
	}
	
	public static void e(String message,Throwable e){
		getMethodNames(e.getStackTrace());
		String msg = createLog(message+e);
		if (!isDebuggable()){
			log2file( msg);
		}
		Log.e(className, msg,e);
	}
	
	public static void wtf(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.wtf(className, createLog(message));
	}
	
	protected static void log2file(final String str) {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }

        if (executor != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    PrintWriter out = null;

                    File file = GetFileFromPath(logFilePath);

                    try {
                        out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
                        out.println(str);
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        out.close();
                    }
                }
            });
        }
    }

    /**
     * Get File form the file path.<BR>
     * if the file does not exist, create it and return it.
     *
     * @param path the file path
     * @return the file
     */
    private static File GetFileFromPath(String path) {
        boolean ret;
        boolean isExist;
        boolean isWritable;
        File file = null;

        if (TextUtils.isEmpty(path)) {
            Log.e("Error", "The path of Log file is Null.");
            return file;
        }

        file = new File(path);

        isExist = file.exists();
        isWritable = file.canWrite();

        if (isExist) {
            if (isWritable) {
                //Log.i("Success", "The Log file exist,and can be written! -" + file.getAbsolutePath());
            } else {
                Log.e("Error", "The Log file can not be written.");
            }
        } else {
            //create the log file
            try {
            	file.getParentFile().mkdir();
                ret = file.createNewFile();
                if (ret) {
                    Log.i("Success", "The Log file was successfully created! -" + file.getAbsolutePath());
                } else {
                    Log.i("Success", "The Log file exist! -" + file.getAbsolutePath());
                }

                isWritable = file.canWrite();
                if (!isWritable) {
                    Log.e("Error", "The Log file can not be written.");
                }
            } catch (IOException e) {
                Log.e("Error", "Failed to create The Log file.");
                e.printStackTrace();
            }
        }

        return file;
    }

}
