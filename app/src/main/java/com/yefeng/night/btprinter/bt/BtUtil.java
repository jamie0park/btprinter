package com.yefeng.night.btprinter.bt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

/**
 * Created by yefeng on 6/2/15.
 * github:yefengfreedom
 */
public class BtUtil {
    /**
     * To determine whether Bluetooth is open
     *
     * @return boolean
     */
    public static boolean isOpen(BluetoothAdapter adapter) {
        if (null != adapter) {
            return adapter.isEnabled();
        }
        return false;
    }

    /**
     * Search for Bluetooth devices
     */
    public static void searchDevices(BluetoothAdapter adapter) {
        // Find a Bluetooth device, android will be found in the form of equipment to broadcast to the form
        if (null != adapter) {
            adapter.startDiscovery();
        }
    }

    /**
     * Cancel search for Bluetooth devices
     */
    public static void cancelDiscovery(BluetoothAdapter adapter) {
        if (null != adapter) {
            adapter.cancelDiscovery();
        }
    }

    /**
     * register bluetooth receiver
     *
     * @param receiver bluetooth broadcast receiver
     * @param activity activity
     */
    public static void registerBluetoothReceiver(BroadcastReceiver receiver, Activity activity) {
        if (null == receiver || null == activity) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        //start discovery
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //finish discovery
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //bluetooth status change
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        //found device
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        //bond status change
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //pairing device
        intentFilter.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
        activity.registerReceiver(receiver, intentFilter);
    }

    /**
     * unregister bluetooth receiver
     *
     * @param receiver bluetooth broadcast receiver
     * @param activity activity
     */
    public static void unregisterBluetoothReceiver(BroadcastReceiver receiver, Activity activity) {
        if (null == receiver || null == activity) {
            return;
        }
        activity.unregisterReceiver(receiver);
    }

}
