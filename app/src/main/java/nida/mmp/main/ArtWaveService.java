package nida.mmp.main;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Build;
import android.os.IBinder;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import nida.mmp.R;
import nida.mmp.utils.NidaCommon;
import nida.mmp.utils.NidaLog;

public class ArtWaveService extends Service {
    public ArtWaveService() {
    }
    String classSelected;
    String methodSelected;
    String fieldsSelected;
    String viewTag;

    LinearLayout mainContainer;
    LinearLayout searchClassContainer;
    LinearLayout methodsContainer;
    LinearLayout fieldsContainer;
    LinearLayout logsContainer;

    EditText typePackageName;
    EditText typeMethodsName;
    EditText typeFieldsName;
    EditText typeLogName;

    ListView listView;
    ListView listMethods;
    ListView listFields;
    ListView listLogs;

    TextView selectedClassName;

    Button buttonClass;
    Button buttonMethods;
    Button buttonFields;
    Button buttonLogs;

    WindowManager wm;

    ArrayList<String> logs = new ArrayList<String>();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize lib
        NidaCommon.initializeLogic(this);

        //Set main view tag
        viewTag = "classSearch";

        NidaLog.log("Artwave service started");

        captureLogs();

        //Initialize floating window
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.RIGHT | Gravity.TOP;

        wm.addView(mainView(), params);

        mainContainer.addView(searchClasses());

        buttonClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainContainer.removeView(getContainerView(viewTag));
                mainContainer.addView(searchClasses());
                viewTag = "classSearch";
            }
        });

        buttonMethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainContainer.removeView(getContainerView(viewTag));
                mainContainer.addView(methodsView());
                viewTag = "methods";
            }
        });

        buttonFields.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainContainer.removeView(getContainerView(viewTag));
                mainContainer.addView(fieldsView());
                viewTag = "fields";
            }
        });

        buttonLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainContainer.removeView(getContainerView(viewTag));
                mainContainer.addView(logsView());
                viewTag = "logs";
            }
        });


    }

    private LinearLayout getContainerView(String tag){
        switch (tag){
            case ("logs"):
                return logsContainer;
            case ("fields"):
                return fieldsContainer;
            case ("methods"):
                return methodsContainer;
            case ("classSearch"):
                return searchClassContainer;
        }
        return null;
    }

    private View mainView(){
        mainContainer = new LinearLayout(this);
        mainContainer.setBackgroundColor(Color.parseColor("#141414"));
        mainContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout_349 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        mainContainer.setLayoutParams(layout_349);

        LinearLayout linearLayout_129 = new LinearLayout(this);
        LinearLayout.LayoutParams layout_133 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout_129.setLayoutParams(layout_133);

        TextView textView2 = new TextView(this);
        textView2.setId(R.id.textView2);
        textView2.setText("Nida");
        textView2.setTextColor(Color.parseColor("#FFFFFF"));
        textView2.setTextSize(24);
        textView2.setTypeface(textView2.getTypeface(), Typeface.BOLD);
        LinearLayout.LayoutParams layout_868 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wm.removeView(mainContainer);
            }
        });
        textView2.setLayoutParams(layout_868);
        linearLayout_129.addView(textView2);

        selectedClassName = new TextView(this);
        selectedClassName.setText("Please select class name");
        selectedClassName.setTextColor(Color.parseColor("#FFFFFF"));
        selectedClassName.setTextSize(10);
        selectedClassName.setTranslationX(5);
        LinearLayout.LayoutParams layout_8 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        selectedClassName.setLayoutParams(layout_8);
        linearLayout_129.addView(selectedClassName);
        mainContainer.addView(linearLayout_129);
        LinearLayout linearLayout_360 = new LinearLayout(this);

        LinearLayout.LayoutParams layout_957 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout_360.setLayoutParams(layout_957);

        LinearLayout.LayoutParams layout_12 = new LinearLayout.LayoutParams(
                calculatePx(77),
                calculatePx(35)
        );

        buttonClass = new Button(this);
        buttonClass.setText("Class");
        buttonClass.setTextSize(8);
        buttonClass.setLayoutParams(layout_12);
        linearLayout_360.addView(buttonClass);

        buttonMethods = new Button(this);
        buttonMethods.setText("Methods");
        buttonMethods.setTextSize(8);
        buttonMethods.setLayoutParams(layout_12);
        linearLayout_360.addView(buttonMethods);

        buttonFields = new Button(this);
        buttonFields.setText("Fields");
        buttonFields.setTextSize(8);
        buttonFields.setLayoutParams(layout_12);
        linearLayout_360.addView(buttonFields);

        buttonLogs = new Button(this);
        buttonLogs.setText("Logs");
        buttonLogs.setTextSize(8);
        buttonLogs.setLayoutParams(layout_12);
        linearLayout_360.addView(buttonLogs);

        mainContainer.addView(linearLayout_360);

        return mainContainer;
    }
    private View searchClasses(){

        searchClassContainer = new LinearLayout(this);
        searchClassContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout_293 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
        );

        searchClassContainer.setLayoutParams(layout_293);

        typePackageName = new EditText(this);
        typePackageName.setEms(10);
        typePackageName.setHint("Type package name");
        typePackageName.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        typePackageName.setHintTextColor(Color.parseColor("#FFFFFF"));
        typePackageName.setTextColor(Color.WHITE);
        typePackageName.setTextSize(12);
        LinearLayout.LayoutParams layout_391 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
        calculatePx(34)
        );
        typePackageName.setLayoutParams(layout_391);
        searchClassContainer.addView(typePackageName);

        listView = new ListView(this);
        LinearLayout.LayoutParams layout_405 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT
        );
        listView.setLayoutParams(layout_405);
        searchClassContainer.addView(listView);
        typePackageName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable mEdit)
            {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, NidaCommon.getClassesOfPackage(mEdit.toString())) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.WHITE);

                        return view;
                    }
                };

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                            long id) {
                        Toast.makeText(getApplicationContext(), "Class selected!", Toast.LENGTH_SHORT).show();
                        String itemData = ((TextView) itemClicked).getText().toString();
                        classSelected = itemData;
                        selectedClassName.setText(classSelected);
                        for (String f :
                                NidaCommon.getMethods(classSelected)) {
                            System.out.println(NidaCommon.wrapString(f, classSelected));
                        }
                        //System.out.println(libCommon.getMethods(classSelected).get(0));
                    }
                });
            }
        });
        return searchClassContainer;
    }

    private View methodsView(){

        methodsContainer = new LinearLayout(this);
        methodsContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout_293 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        methodsContainer.setLayoutParams(layout_293);

        typeMethodsName = new EditText(this);
        typeMethodsName.setEms(10);
        typeMethodsName.setHint("Type method name");
        typeMethodsName.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        typeMethodsName.setHintTextColor(Color.parseColor("#FFFFFF"));
        typeMethodsName.setTextColor(Color.WHITE);
        typeMethodsName.setTextSize(12);
        LinearLayout.LayoutParams layout_391 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                calculatePx(34)
        );
        typeMethodsName.setLayoutParams(layout_391);
        methodsContainer.addView(typeMethodsName);

        listMethods = new ListView(this);
        LinearLayout.LayoutParams layout_405 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        listMethods.setLayoutParams(layout_405);
        methodsContainer.addView(listMethods);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, NidaCommon.getMethods(classSelected)){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);

                return view;
            }
        };

        listMethods.setAdapter(adapter);
        listMethods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String itemData = ((TextView) itemClicked).getText().toString();
                methodSelected = NidaCommon.getMethodName(itemData);
                dialogActionMethod();
            }
        });
        typeMethodsName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable mEdit)
            {

            }
        });
        return methodsContainer;
    }

    private View fieldsView(){

        fieldsContainer = new LinearLayout(this);
        fieldsContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout_293 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        fieldsContainer.setLayoutParams(layout_293);

        typeFieldsName = new EditText(this);
        typeFieldsName.setEms(10);
        typeFieldsName.setHint("Type field name");
        typeFieldsName.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        typeFieldsName.setHintTextColor(Color.parseColor("#FFFFFF"));
        typeFieldsName.setTextColor(Color.WHITE);
        typeFieldsName.setTextSize(12);
        LinearLayout.LayoutParams layout_391 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                calculatePx(34)
        );
        typeFieldsName.setLayoutParams(layout_391);
        fieldsContainer.addView(typeFieldsName);

        listFields = new ListView(this);
        LinearLayout.LayoutParams layout_405 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        listFields.setLayoutParams(layout_405);
        fieldsContainer.addView(listFields);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, NidaCommon.getFields(classSelected)){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);

                return view;
            }
        };

        listFields.setAdapter(adapter);
        listFields.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String itemData = ((TextView) itemClicked).getText().toString();
                fieldsSelected = NidaCommon.getFieldName(itemData);
                dialogActionField();
            }
        });
        typeFieldsName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable mEdit)
            {

            }
        });
        return fieldsContainer;
    }

    private View logsView(){

        logsContainer = new LinearLayout(this);
        logsContainer.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout_293 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        logsContainer.setLayoutParams(layout_293);

        typeLogName = new EditText(this);
        typeLogName.setEms(10);
        typeLogName.setHint("Type log");
        typeLogName.setRawInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        typeLogName.setHintTextColor(Color.parseColor("#FFFFFF"));
        typeLogName.setTextColor(Color.WHITE);
        typeLogName.setTextSize(12);
        LinearLayout.LayoutParams layout_391 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                calculatePx(34)
        );
        typeLogName.setLayoutParams(layout_391);
        logsContainer.addView(typeLogName);

        listLogs = new ListView(this);
        LinearLayout.LayoutParams layout_405 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        listLogs.setLayoutParams(layout_405);
        logsContainer.addView(listLogs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, logs){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.WHITE);
                text.setTextSize(10);

                return view;
            }
        };

        listLogs.setAdapter(adapter);
        listLogs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {

            }
        });
        typeLogName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable mEdit)
            {

            }
        });
        return logsContainer;
    }

    public void dialogActionMethod(){
        Context dialogContext = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose action");
        String[] action = {"Invoke"};
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(dialogContext);
                        alertDialog.setTitle("Invoke settings");
                        alertDialog.setMessage("Set invoke count (if needed, default = 1)");

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        EditText inputCount = new EditText(dialogContext);

                        inputCount.setLayoutParams(lp);


                        alertDialog.setView(inputCount);

                        alertDialog.setPositiveButton("Invoke!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String nfor = inputCount.getText().toString();
                                        NidaCommon.nFor(Integer.parseInt(nfor), classSelected, methodSelected, null);
                                    }
                                });
                        AlertDialog d = alertDialog.create();
                        d.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                        d.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        dialog.show();
    }

    public void dialogActionField(){
        Context dialogContext = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose action");
        String[] action = {"Get value", "Set value"};
        builder.setItems(action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Use switch = bugs
                if(which == 1){
                    try {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(dialogContext);
                        alertDialog.setTitle("Set value");

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        EditText fieldSetData = new EditText(dialogContext);

                        fieldSetData.setLayoutParams(lp);

                        alertDialog.setView(fieldSetData);

                        alertDialog.setPositiveButton("Set value!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        String data = fieldSetData.getText().toString();
                                        NidaCommon.setFieldValue(fieldsSelected, classSelected, data);
                                    }
                                });

                        AlertDialog d = alertDialog.create();
                        d.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                        d.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(which == 0){

                    try {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(dialogContext);
                        alertDialog.setTitle("Get value");

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        EditText fieldData = new EditText(dialogContext);

                        fieldData.setLayoutParams(lp);

                        fieldData.setText(String.valueOf(NidaCommon.getFieldValue(fieldsSelected, classSelected)));

                        alertDialog.setView(fieldData);

                        AlertDialog d = alertDialog.create();
                        d.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
                        d.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        dialog.show();
    }

    public void captureLogs(){
        NidaLog.log("Log capture started");

        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            public void run()
            {
                try
                {
                    try{
                        Runtime.getRuntime().exec("logcat -c").waitFor();
                        Process process = Runtime.getRuntime().exec("logcat -v long *:*");
                        BufferedReader reader =
                                new BufferedReader(new InputStreamReader(process.getInputStream()));
                        while (true) {
                            String nextLine = reader.readLine();
                            logs.add(nextLine);
                            // Process line
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    {

                }

                } finally
                {

                }
            }
        }, 0L);

    }

    public int calculatePx(int dp){
        Resources r = getResources();
         return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,r.getDisplayMetrics()));
    }
    public void test(){
        Toast.makeText(this, "Worked!!!!!!!", Toast.LENGTH_SHORT).show();
    }
}