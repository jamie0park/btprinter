package com.yefeng.night.btprinter.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;

import com.yefeng.night.btprinter.MyApplication;
import com.yefeng.night.btprinter.util.AppInfo;
import com.yefeng.night.btprinter.bt.BtService;

import java.util.ArrayList;

/**
 * Created by yefeng on 5/28/15.
 * github:yefengfreedom
 * <p/>
 * this is print queue.
 * you can simple add print bytes to queue. and this class will send those bytes to bluetooth device
 */
public class PrintQueue {

    /**
     * instance
     */
    private static PrintQueue mInstance;
    /**
     * context
     */
    private static Context mContext;

    // application
    private static MyApplication myApp;


    private PrintQueue() {
    }

    public static PrintQueue getQueue(Context context) {
        if (null == mInstance) {
            mInstance = new PrintQueue();
        }
        if (null == mContext) {
            mContext = context;
        }
        if (null == myApp) {
            myApp = (MyApplication) context.getApplicationContext();
        }
        return mInstance;
    }

    /**
     * add print bytes to queue. and call print
     *
     * @param bytes bytes
     */
    public synchronized void add(byte[] bytes) {
        if (null == myApp.mMainQueue) {
            myApp.mMainQueue = new ArrayList<byte[]>();
        }
        if (null != bytes) {
            myApp.mMainQueue.add(bytes);
        }
        print();
    }

    /**
     * add print bytes to queue. and call print
     *
     * @param bytesList bytesList
     */
    public synchronized void add(ArrayList<byte[]> bytesList) {
        if (null == myApp.mMainQueue) {
            myApp.mMainQueue = new ArrayList<byte[]>();
        }
        if (null != bytesList) {
            myApp.mMainQueue.addAll(bytesList);
        }
        print();
    }


    /**
     * print queue
     */
    public synchronized void print() {
        try {
            if (null == myApp.mMainQueue || myApp.mMainQueue.size() <= 0) {
                return;
            }
            if (null == myApp.mBtAdapter) {
                myApp.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (null == myApp.mBtService) {
                myApp.mBtService = new BtService(mContext);
            }
            if (myApp.mBtService.getState() != BtService.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(AppInfo.btAddress)) {
                    BluetoothDevice device = myApp.mBtAdapter.getRemoteDevice(AppInfo.btAddress);
                    myApp.mBtService.connect(device);
                    return;
                }
            }
            while (myApp.mMainQueue.size() > 0) {
                myApp.mBtService.write(myApp.mMainQueue.get(0));
                myApp.mMainQueue.remove(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * disconnect remote device
     */
    public void disconnect() {
        try {
            if (null != myApp.mBtService) {
                myApp.mBtService.stop();
                myApp.mBtService = null;
            }
            if (null != myApp.mBtAdapter) {
                myApp.mBtAdapter = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * when bluetooth status is changed, if the printer is in use,
     * connect it,else do nothing
     */
    public void tryConnect() {
        try {
            if (TextUtils.isEmpty(AppInfo.btAddress)) {
                return;
            }
            if (null == myApp.mBtAdapter) {
                myApp.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (null == myApp.mBtAdapter) {
                return;
            }
            if (null == myApp.mBtService) {
                myApp.mBtService = new BtService(mContext);
            }
            if (myApp.mBtService.getState() != BtService.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(AppInfo.btAddress)) {
                    BluetoothDevice device = myApp.mBtAdapter.getRemoteDevice(AppInfo.btAddress);
                    myApp.mBtService.connect(device);
                    return;
                }
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a print order to the printer
     *
     * @param bytes bytes
     */
    public void write(byte[] bytes) {
        try {
            if (null == bytes || bytes.length <= 0) {
                return;
            }
            if (null == myApp.mBtAdapter) {
                myApp.mBtAdapter = BluetoothAdapter.getDefaultAdapter();
            }
            if (null == myApp.mBtService) {
                myApp.mBtService = new BtService(mContext);
            }
            if (myApp.mBtService.getState() != BtService.STATE_CONNECTED) {
                if (!TextUtils.isEmpty(AppInfo.btAddress)) {
                    BluetoothDevice device = myApp.mBtAdapter.getRemoteDevice(AppInfo.btAddress);
                    myApp.mBtService.connect(device);
                    return;
                }
            }
            myApp.mBtService.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
