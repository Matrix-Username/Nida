package nida.mmp.utils;

import android.content.ContextWrapper;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class NidaLog {
    public static void log(String message){
        logToFile(message);
        Log.i("NidaLogging", message);
    }
    public static void logError(String message){
        logToFile(message);
        Log.e("NidaLogging", message);
    }

    private static void logToFile(String message){
        String path = NidaCommon.getLogFilePath();

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.append(message);
            writer.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
