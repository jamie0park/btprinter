package com.yefeng.night.btprinter;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.text.TextUtils;

import com.yefeng.night.btprinter.print.PrintUtil;

/**
 * Created by yefeng on 7/9/15.
 * github:yefengfreedom
 */
public class MainController {

    public static void init(MainActivity activity) {
        if (null == activity.mAdapter) {
            activity.mAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (null == activity.mAdapter) {
            activity.mPrinterTitle.setText("The device does not have a Bluetooth module");
            activity.mPrinterImg.setImageResource(R.drawable.ic_bluetooth_off);
            activity.mBtEnable = false;
            return;
        }
        if (!activity.mAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, MainActivity.OPEN_BLUETOOTH_REQUEST);
            activity.showToast("Turning on Bluetooth");
            activity.mPrinterTitle.setText("Bluetooth is not open");
            activity.mPrinterImg.setImageResource(R.drawable.ic_bluetooth_off);
            return;
        }
        String address = PrintUtil.getDefaultBluethoothDeviceAddress(activity.getApplicationContext());
        if (TextUtils.isEmpty(address)) {
            activity.mPrinterTitle.setText("Bluetooth device not yet bound");
            activity.mPrinterImg.setImageResource(R.drawable.ic_bluetooth_off);
            return;
        }
        String name = PrintUtil.getDefaultBluetoothDeviceName(activity.getApplicationContext());
        activity.mPrinterTitle.setText("Bluetooth is bound：" + name);
        activity.mPrinterSummary.setText(address);
        activity.mPrinterImg.setImageResource(R.drawable.ic_bluetooth_device_connected);
    }
}
