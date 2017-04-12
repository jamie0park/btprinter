package com.yefeng.night.btprinter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
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

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class MainActivity extends BluetoothActivity {

    static final int OPEN_BLUETOOTH_REQUEST = 100;
    ImageView mPrinterImg;
    TextView mPrinterTitle;
    TextView mPrinterSummary;
    LinearLayout mChangeDeviceLayout;
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
        setContentView(R.layout.activity_main);

        mPrinterImg = (ImageView) findViewById(R.id.img_printer_setting_icon);
        mPrinterTitle = (TextView) findViewById(R.id.txt_printer_setting_title);
        mPrinterSummary = (TextView) findViewById(R.id.txt_printer_setting_summary);
        mChangeDeviceLayout = (LinearLayout) findViewById(R.id.ll_printer_setting_change_device);
        mBtnTest = (Button) findViewById(R.id.btn_printer_setting_test);
        mBtnTestBitmap = (Button) findViewById(R.id.btn_printer_setting_test_bitmap);
        mBtnTestDrawPicture = (Button) findViewById(R.id.btn_printer_setting_test_draw_picture);

        mChangeDeviceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bondPrinter();
            }
        });

        mBtnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printTest();
            }
        });

        mBtnTestBitmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printBitmap();
            }
        });

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
        MainController.init(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_OK) {
            MainController.init(this);
        } else if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            showToast("You have declined using Bluetooth");
            finish();
        }
    }

    @Override
    public void btStatusChanged(Intent intent) {
        super.btStatusChanged(intent);
        MainController.init(this);
    }

    void bondPrinter() {
        if (mBtEnable) {
            startActivity(new Intent(getApplicationContext(), BondBtActivity.class));
            finish();
        }
    }

    void printTest() {
        showToast("Print test...");
        Intent intent = new Intent(getApplicationContext(), PrintService.class);
        intent.setAction(PrintUtil.ACTION_PRINT_TEST);
        startService(intent);
    }

    void printBitmap() {
        showToast("Print the picture...");
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
            showToast(event.msg);
        }
    }
}
