package nida.mmp.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dalvik.system.DexFile;
import nida.mmp.main.ArtWaveService;

public class NidaCommon {
    private static Context mainContext;

    public static void initializeLogic(Context context) {
        mainContext = context;
        NidaLog.log("lib initialized " + context.toString());
    }

    public static ArrayList<String> getMethods(String className) {
        NidaLog.log("Called getMethods(); | Class name: " + className);
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Class thisClass = Class.forName(className);
            Method[] methods = thisClass.getDeclaredMethods();

            for (int i = 0; i < methods.length; i++) {
               arrayList.add(wrapString(methods[i].toString(), className));
            }
        } catch (Throwable e) {
            System.err.println(e);

        }
        return arrayList;
    }

    public static String getMethodName(String fullName){
        StringBuffer stringBuffer = new StringBuffer(fullName.substring(0, fullName.indexOf("(")));
        stringBuffer.reverse();
        StringBuffer stringBuffer1 = new StringBuffer(stringBuffer.toString().substring(0, stringBuffer.toString().indexOf(" ")));
        stringBuffer1.reverse();
        return stringBuffer1.toString();
    }

    /**
     * Get method type
     */
    public static String getMethodType(String fullMethodStr){
        Pattern p = Pattern.compile("(\\w+)\\W+" + getMethodName(fullMethodStr) + "\\W+(\\w+)");

        Matcher m = p.matcher(fullMethodStr.replace(")", "TEST"));
        if (m.find())
            return m.group(1);

        return null;
    }

    public static String getFieldName(String fullName){
        return fullName.substring(fullName.lastIndexOf(" ") + 1);
    }

    public static Object getFieldValue(String fieldName, String className) {
        NidaLog.log("Get field " + fieldName + " class " + className);

        Object value = null;
        try {
            Class classObj = Class.forName(className);
            Field fieldObj = classObj.getDeclaredField(fieldName);
            fieldObj.setAccessible(true);
            value = (Object) fieldObj.get(mainContext);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static void setFieldValue(String fieldName, String className, Object value) {
        NidaLog.log("Set field " + fieldName + " class " + className);

        try {
            Class classObj = Class.forName(className);
            Field fieldObj = classObj.getDeclaredField(fieldName);
            fieldObj.setAccessible(true);
            fieldObj.set(classObj.newInstance(), value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get fields array in class
     */
    public static ArrayList<String> getFields(String className) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Field[] fields = Class.forName(className).getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                arrayList.add(wrapString(fields[i].toString(), className));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     * Used for method invoke certain number time
     */
    public static void nFor(int n, String funClass, String methodName, Object... args) {
        for (int i = 0; i < n; i++) {
            invokeMethod(funClass, methodName, args);
        }
    }

    /**
     * Invoke method in class
     */
    public static void invokeMethod(String funClass, String methodName, Object... args){
        try{
            //Get class by name
            Class c = Class.forName(funClass);

            //Create instance
            Object o = c.newInstance();

            //Invoking
            Method m = c.getDeclaredMethod(methodName, null);
            Object output = m.invoke(o, args);

            methodOutputDialog(mainContext, String.valueOf(output));



            NidaLog.log("Invoked method " + methodName + "in class " + funClass);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static void Test(){
        System.out.println("Test");
    }

    /**
     * Show dialog with method output data
     */
    public static void methodOutputDialog(Context contextDialog, String outputStr){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(contextDialog);
        alertDialog.setTitle("Output:");

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        EditText fieldSetData = new EditText(contextDialog);
        fieldSetData.setText(outputStr);

        fieldSetData.setLayoutParams(lp);

        alertDialog.setView(fieldSetData);

        AlertDialog d = alertDialog.create();
        d.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        d.show();
    }

    /**
     * Start activity by name
     */
    public static void startActivityByName(String className) {
        try {
            Intent artWaveIntent = new Intent(mainContext, Class.forName(className));
            mainContext.startActivity(artWaveIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wrap string
     */
    public static String wrapString(String s, String className) {
        return s.replace(className, "").replace(", ", "\n").replace(" .", " ").replace("[", "").replace("]", "").replace("java.lang.", "");
    }

    /**
     * Get all classes of package
     */
    public static String[] getClassesOfPackage(String packageName) {
        ArrayList<String> classes = new ArrayList<String>();
        try {
            String packageCodePath = mainContext.getPackageCodePath();
            DexFile df = new DexFile(packageCodePath);
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
                String className = iter.nextElement();
                if (className.contains(packageName)) {
                    classes.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes.toArray(new String[classes.size()]);
    }

    /**
     * Check Nida initialization & run main service
     */
    public static void startMyLogic(){

        if(mainContext == null){
            NidaLog.logError("Error! Please initialize library using 'NidaCommon.initializeLogic'");
            return;
        }

        if(!checkOverlayDisplayPermission()){
            NidaLog.logError("Warning! App does not have permission for floating windows");
            requestOverlayDisplayPermission();
            return;
        }
        if(!isMyServiceRunning()){
            mainContext.startService(new Intent(mainContext, ArtWaveService.class));
        }
    }

    /**
     * Check is Main Service running
     */
    private static boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) mainContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ArtWaveService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check is overlay window permission allowed
     */
    private static boolean checkOverlayDisplayPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mainContext)) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Getting log file path
     */
    public static String getLogFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(mainContext);
        return contextWrapper.getFilesDir().getAbsolutePath() + "/nida_log.txt";
    }

    /**
     * Create dialog with request overlay window
     */
    private static void requestOverlayDisplayPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainContext);
        builder.setCancelable(false);
        builder.setTitle("Screen Overlay Permission Needed");
        builder.setMessage("Enable 'Display over other apps' from System Settings.");
        builder.setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + mainContext.getPackageName()));
                mainContext.startActivity(intent);
            }
        });
        builder.create();
        builder.show();
    }

}
