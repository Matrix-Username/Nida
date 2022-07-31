package nida.mmp.utils;

import android.util.Log;

public class NidaLog {
    public static void log(String message){
        Log.i("NidaLogging", message);
    }
    public static void logError(String message){
        Log.e("NidaLogging", message);
    }
}
