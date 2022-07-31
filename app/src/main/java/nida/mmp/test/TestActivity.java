package nida.mmp.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import nida.mmp.R;
import nida.mmp.utils.NidaCommon;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nida_test);

        NidaCommon.initializeLogic(this);
        NidaCommon.startMyLogic();
    }
}