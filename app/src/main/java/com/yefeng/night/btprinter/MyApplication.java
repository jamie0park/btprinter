package com.yefeng.night.btprinter;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.yefeng.night.btprinter.bt.BtService;
import com.yefeng.night.btprinter.data.BtPrinter;
import com.yefeng.night.btprinter.print.PrintUtil;
import com.yefeng.night.btprinter.util.AppInfo;
import com.yefeng.night.btprinter.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by jamie0park on 12/04/2017.
 */

public class MyApplication extends Application {


    private static Context mContext;

    /**
     * bluetooth
     */
    public static BluetoothAdapter mBtAdapter = null;
    public static BtService mBtService = null;
    public static BluetoothDevice mBtDevice = null;
    public static boolean isBtAvailable = false;
    /**
     * print queue
     */
    public static ArrayList<byte[]> mMainQueue = new ArrayList<byte[]>();
    public static ArrayList<byte[]> mSubQueue = new ArrayList<byte[]>();


    public static BtPrinter mDefaultPrinter = new BtPrinter();


    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        AppInfo.init(getApplicationContext());
        mDefaultPrinter = PrintUtil.getDefaultPrinter(this);

        // bluetooth
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mBtService = new BtService(mContext);
        // check available
        if( mBtService.isAvailable() == false ){
            isBtAvailable = false;
            showToast("Bluetooth is not available");
        } else {
            isBtAvailable = true;
        }
    }

    public void showToast(String message) {
        ToastUtil.showToast(this, message);
    }

    public void showToast(int message) {
        ToastUtil.showToast(this, message);
    }


}
