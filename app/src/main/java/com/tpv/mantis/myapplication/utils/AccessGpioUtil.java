package com.tpv.mantis.myapplication.utils;

import android.util.Log;

import com.tpv.mantis.myapplication.GpioActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AccessGpioUtil {

    // Led colors
    public static final String COLOR_RED = "red";
    public static final String COLOR_GREEN = "green";
    public static final String COLOR_BLUE = "blue";

    //sys_path 为节点映射到的实际路径
    public static String readSysFile(String sys_path) {
        String prop = "waiting";// 默认值
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(sys_path));
            prop = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            Log.w(GpioActivity.TAG, " ***ERROR*** Here is what I know: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.w(GpioActivity.TAG, "readFile cmd from" + sys_path + "data" + " -> prop = " + prop);
        return prop;
    }

    public static void writeSysFile(String filename, int value) {
        //FileOutputStream fos = null;
        DataOutputStream os = null;
        try {
        /*fos = new FileOutputStream(new File(filename), false);
        fos.write(value.getBytes());
        fos.flush();*/

            Process p;

            p = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(p.getOutputStream());

            os.writeBytes("echo " + value + " > " + filename + "\n");
            os.writeBytes("exit\n");

            Log.w(GpioActivity.TAG, value + " >> " + filename);

            os.flush();
        } catch (IOException e) {
            Log.w(GpioActivity.TAG, "Could not write to " + filename);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.d(GpioActivity.TAG, "Could not close " + filename, e);
                }
            }
        }
    }

//    public static void writeSysFile(String sys_path, int brightness) {
//        Log.d(GpioActivity.TAG, "==>brightness: " + brightness);
//        try {
//            BufferedWriter bufWriter = null;
//            bufWriter = new BufferedWriter(new FileWriter(sys_path));
//            bufWriter.write(brightness);
//            bufWriter.close();
//            Log.d(GpioActivity.TAG, "write successfully!");
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.e(GpioActivity.TAG,"can't write the " + sys_path);
//        }
//    }


//    //sys_path 为节点映射到的实际路径
//    public static String read(String sys_path) {
//        try {
//            Runtime runtime = Runtime.getRuntime();
//            Log.d(GpioActivity.TAG, "==>runtime exec");
////            runtime.exec("mount -o remount rw /system /sys\n" +
////                    "chmod 777 sys");
//            Process process = runtime.exec("cat " + sys_path);
//            Log.d(GpioActivity.TAG, "==>runtime exec end");
//            InputStream is = process.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            if(null != (line = br.readLine())) {
//                Log.w(GpioActivity.TAG, "read data ---> " + line);
//                return line;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.w(GpioActivity.TAG, "*** ERROR *** Here is what I know: " + e.getMessage());
//        }
//        return null;
//    }

}
