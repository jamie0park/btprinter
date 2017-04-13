package com.yefeng.night.btprinter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    // application class
    private MyApplication myApp;

    private Button mPrinterSettingBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Application class
        myApp = (MyApplication) getApplicationContext();

        mPrinterSettingBtn = (Button) findViewById(R.id.go_printer_setting_btn);
        mPrinterSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrinterSettingActivity.class);
                startActivity(intent);

            }
        });

    }
}
