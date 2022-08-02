package nida.mmp.main;

import android.app.Activity;
import android.os.Bundle;

import nida.mmp.utils.NidaCommon;

public class NidaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NidaCommon.initializeLogic(this);
        NidaCommon.startMyLogic();
    }
}