package com.yefeng.night.btprinter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yefeng.night.btprinter.bt.BluetoothActivity;
import com.yefeng.night.btprinter.print.PrintMsgEvent;
import com.yefeng.night.btprinter.print.PrintService;
import com.yefeng.night.btprinter.print.PrintUtil;
import com.yefeng.night.btprinter.print.PrinterMsgType;

public class PrinterSettingActivity extends BluetoothActivity {

    // application class
    private MyApplication myApp;

    static final int OPEN_BLUETOOTH_REQUEST = 100;
    ImageView mImgPrinter;
    TextView mTxtPrinterTitle;
    TextView mTxtPrinterSummary;
    LinearLayout mLlChangeDevice;
    Button mBtnTest;
    Button mBtnTestBitmap;
    Button mBtnTestDrawPicture;
    boolean mBtEnable = true;
    /**
     * bluetooth adapter
     */
    BluetoothAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setting);

        // Application class
        myApp = (MyApplication) getApplicationContext();

        mImgPrinter = (ImageView) findViewById(R.id.img_printer_setting_icon);
        mTxtPrinterTitle = (TextView) findViewById(R.id.txt_printer_setting_title);
        mTxtPrinterSummary = (TextView) findViewById(R.id.txt_printer_setting_summary);
        
        mLlChangeDevice = (LinearLayout) findViewById(R.id.ll_printer_setting_change_device);
        mLlChangeDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bondPrinter();
            }
        });

        mBtnTest = (Button) findViewById(R.id.btn_printer_setting_test);
        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printTest();
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                printTest();
            }
        });

        mBtnTestBitmap = (Button) findViewById(R.id.btn_printer_setting_test_bitmap);
        mBtnTestBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printBitmap();
            }
        });

        mBtnTestDrawPicture = (Button) findViewById(R.id.btn_printer_setting_test_draw_picture);
        mBtnTestDrawPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDrawPicture();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        init(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_OK) {
            init(this);
        } else if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            myApp.showToast("You have declined using Bluetooth");
            finish();
        }
    }

    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        init(this);
    }

    void bondPrinter() {
        if (mBtEnable) {
            startActivity(new Intent(getApplicationContext(), DeviceActivity.class));
            finish();
        }
    }

    void printTest() {
        myApp.showToast("Print test...");
        Intent intent = new Intent(getApplicationContext(), PrintService.class);
        intent.setAction(PrintUtil.ACTION_PRINT_TEST);
        startService(intent);
    }

    void printBitmap() {
        myApp.showToast("Print the picture...");
        Intent intent = new Intent(getApplicationContext(), PrintService.class);
        intent.setAction(PrintUtil.ACTION_PRINT_BITMAP);
        startService(intent);
    }

    void printDrawPicture() {
        startActivity(new Intent(getApplicationContext(), PaintActivity.class));
    }

    /**
     * handle printer message
     *
     * @param event print msg event
     */
    public void onEventMainThread(PrintMsgEvent event) {
        if (event.type == PrinterMsgType.MESSAGE_TOAST) {
            myApp.showToast(event.msg);
        }
    }

    public static void init(PrinterSettingActivity activity) {
        if (null == activity.mAdapter) {
            activity.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (null == activity.mAdapter) {
            activity.mTxtPrinterTitle.setText("The device does not have a Bluetooth module");
            activity.mImgPrinter.setImageResource(R.drawable.ic_bluetooth_off);
            activity.mBtEnable = false;
            return;
        }
        if (!activity.mAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, PrinterSettingActivity.OPEN_BLUETOOTH_REQUEST);
            activity.myApp.showToast("Turning on Bluetooth");
            activity.mTxtPrinterTitle.setText("Bluetooth is not open");
            activity.mImgPrinter.setImageResource(R.drawable.ic_bluetooth_off);
            return;
        }
        String address = PrintUtil.getDefaultPrinter(activity.getApplicationContext()).getMacAddr();
        if (TextUtils.isEmpty(address)) {
            activity.mTxtPrinterTitle.setText("Bluetooth device not yet bound");
            activity.mImgPrinter.setImageResource(R.drawable.ic_bluetooth_off);
            return;
        }
        String name = PrintUtil.getDefaultPrinter(activity.getApplicationContext()).getBtNm();
        activity.mTxtPrinterTitle.setText("Bluetooth is bound：" + name);
        activity.mTxtPrinterSummary.setText(address);
        activity.mImgPrinter.setImageResource(R.drawable.ic_bluetooth_device_connected);
    }
}
