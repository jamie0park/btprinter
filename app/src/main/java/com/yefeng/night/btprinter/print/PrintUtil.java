package com.yefeng.night.btprinter.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.yefeng.night.btprinter.MyApplication;
import com.yefeng.night.btprinter.bt.BtUtil;
import com.yefeng.night.btprinter.data.BtPrinter;

import java.util.Set;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 * <p>
 * printer util
 */
public class PrintUtil {

    private static final String FILENAME = "bt";
    private static final String DEFAULT_BLUETOOTH_DEVICE_ADDRESS = "default_bluetooth_device_address";
    private static final String DEFAULT_BLUETOOTH_DEVICE_NAME = "default_bluetooth_device_name";
    private static final String DEFAULT_BLUETOOTH_DEVICE_CODE = "default_bluetooth_device_code";

    public static final String ACTION_PRINT_TEST = "action_print_test";
    public static final String ACTION_PRINT = "action_print";
    public static final String ACTION_PRINT_TICKET = "action_print_ticket";
    public static final String ACTION_PRINT_BITMAP = "action_print_bitmap";
    public static final String ACTION_PRINT_PAINTING = "action_print_painting";

    public static final String PRINT_EXTRA = "print_extra";

    public static void setDefaultPrinter(Context mContext, BtPrinter printer) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, printer.getMacAddr());
        editor.putString(DEFAULT_BLUETOOTH_DEVICE_NAME, printer.getBtNm());
        editor.putString(DEFAULT_BLUETOOTH_DEVICE_CODE, printer.getPrintCd());
        editor.apply();
        MyApplication.mDefaultPrinter = printer;
    }

    public static boolean isBondPrinter(Context mContext, BluetoothAdapter bluetoothAdapter) {
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            return false;
        }
        String defaultBluetoothDeviceAddress = getDefaultPrinter(mContext).getMacAddr();
        if (TextUtils.isEmpty(defaultBluetoothDeviceAddress)) {
            return false;
        }
        Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
        if (deviceSet == null || deviceSet.isEmpty()) {
            return false;
        }
        for (BluetoothDevice bluetoothDevice : deviceSet) {
            if (bluetoothDevice.getAddress().equals(defaultBluetoothDeviceAddress)) {
                return true;
            }
        }
        return false;

    }

    public static BtPrinter getDefaultPrinter(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);

        BtPrinter printer = new BtPrinter();
        printer.setMacAddr(sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_ADDRESS, ""));
        printer.setBtNm(sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_NAME, ""));
        printer.setPrintCd(sharedPreferences.getString(DEFAULT_BLUETOOTH_DEVICE_CODE, ""));

        return printer;
    }

//    public static boolean isBondPrinterIgnoreBluetooth(Context mContext) {
//        String defaultBluetoothDeviceAddress = getDefaultBluethoothDeviceAddress(mContext);
//        return !(TextUtils.isEmpty(defaultBluetoothDeviceAddress)
//                || TextUtils.isEmpty(getDefaultBluetoothDeviceName(mContext)));
//    }

    /**
     * use new api to reduce file operate
     *
     * @param editor editor
     */
    public static void apply(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
