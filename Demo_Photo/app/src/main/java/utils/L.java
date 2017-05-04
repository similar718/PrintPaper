package utils;

import android.util.Log;

public class L {

    private static boolean debug = true;
    private static final String TAG = "printpaper";

    public static void v(String msg){
        if (debug)
            Log.v(TAG,msg);
    }

    public static void d(String msg){
        if (debug)
            Log.d(TAG,msg);
    }

    public static void i(String msg){
        if (debug)
            Log.i(TAG,msg);
    }

    public static void w(String msg){
        if (debug)
            Log.w(TAG,msg);
    }

    public static void e(String msg){
        if (debug)
            Log.e(TAG,msg);
    }
}
