package com.yefeng.night.btprinter;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yefeng.night.btprinter.bt.BluetoothActivity;
import com.yefeng.night.btprinter.bt.BtDeviceAdapter;
import com.yefeng.night.btprinter.bt.BtUtil;
import com.yefeng.night.btprinter.print.PrintQueue;
import com.yefeng.night.btprinter.print.PrintUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by yefeng on 6/1/15.
 * github:yefengfreedom
 */
public class DeviceActivity extends BluetoothActivity {

    // application class
    private MyApplication myApp;

    private static final int OPEN_BLUETOOTH_REQUEST = 100;
    LinearLayout llBondSearch;
    ImageView imgBondIcon;
    TextView txtBondTitle;
    TextView txtBondSummary;
    ListView listBondDevice;
    private BtDeviceAdapter deviceAdapter;
    private BluetoothAdapter bluetoothAdapter;
    private AdapterView.OnItemClickListener mDeviceClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        // Application class
        myApp = (MyApplication) getApplicationContext();

        llBondSearch = (LinearLayout) findViewById(R.id.ll_bond_search);
        imgBondIcon = (ImageView) findViewById(R.id.img_bond_icon);
        txtBondTitle = (TextView) findViewById(R.id.txt_bond_title);
        txtBondSummary = (TextView) findViewById(R.id.txt_bond_summary);
        listBondDevice = (ListView) findViewById(R.id.list_bond_device);

        llBondSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDevice();
            }
        });

        mDeviceClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bondDevice(i);
            }
        };

        deviceAdapter = new BtDeviceAdapter(getApplicationContext(), null);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listBondDevice.setAdapter(deviceAdapter);
        listBondDevice.setOnItemClickListener(mDeviceClickListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
        searchDeviceOrOpenBluetooth();
    }

    private void init() {
        if (null == deviceAdapter) {
            deviceAdapter = new BtDeviceAdapter(getApplicationContext(), null);
        }
        if (null != bluetoothAdapter) {
            Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
            if (null != deviceSet) {
                deviceAdapter.addDevices(new ArrayList<BluetoothDevice>(deviceSet));
            }
        }
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            txtBondTitle.setText("The Bluetooth printer is not connected");
            txtBondSummary.setText("System Bluetooth is off, click to open");
            imgBondIcon.setImageResource(R.drawable.ic_bluetooth_off);
        } else {
            if (!PrintUtil.isBondPrinter(this, bluetoothAdapter)) {
                //Unmatched Bluetooth printer
                txtBondTitle.setText("The Bluetooth printer is not connected");
                txtBondSummary.setText("Click to search for a Bluetooth printer");
                imgBondIcon.setImageResource(R.drawable.ic_bluetooth_off);
            } else {
                //The Bluetooth device is bound
                txtBondTitle.setText(getPrinterName() + "connected");
                String blueAddress = PrintUtil.getDefaultBluethoothDeviceAddress(this);
                if (TextUtils.isEmpty(blueAddress)) {
                    blueAddress = "Click to search for a Bluetooth printer";
                }
                txtBondSummary.setText(blueAddress);
                imgBondIcon.setImageResource(R.drawable.ic_bluetooth_device_connected);
            }
        }
    }

    /**
     * search device, if bluetooth is closed, open it
     */
    private void searchDeviceOrOpenBluetooth() {
        if (!BtUtil.isOpen(bluetoothAdapter)) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, OPEN_BLUETOOTH_REQUEST);
        } else {
            BtUtil.searchDevices(bluetoothAdapter);
        }
    }

    private String getPrinterName() {
        String dName = PrintUtil.getDefaultBluetoothDeviceName(this);
        if (TextUtils.isEmpty(dName)) {
            dName = "Unknown device";
        }
        return dName;
    }

    @Override
    protected void onStop() {
        super.onStop();
        BtUtil.cancelDiscovery(bluetoothAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_OK) {
            init();
        } else if (requestCode == OPEN_BLUETOOTH_REQUEST && resultCode == Activity.RESULT_CANCELED) {
            myApp.showToast("You have declined using Bluetooth");
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceAdapter = null;
        bluetoothAdapter = null;
    }

    void searchDevice() {
        searchDeviceOrOpenBluetooth();
    }

    void bondDevice(final int position) {
        if (null == deviceAdapter) {
            return;
        }
        final BluetoothDevice bluetoothDevice = deviceAdapter.getItem(position);
        if (null == bluetoothDevice) {
            return;
        }

        /*
        new AlertDialog.Builder(this)
                .setTitle("Bind" + getPrinterName(bluetoothDevice.getName()) + "?")
                .setMessage("Click 'OK' to bind the Bluetooth device")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            BtUtil.cancelDiscovery(bluetoothAdapter);
                            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), bluetoothDevice.getAddress());
                            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), bluetoothDevice.getName());
                            if (null != deviceAdapter) {
                                deviceAdapter.setConnectedDeviceAddress(bluetoothDevice.getAddress());
                            }
                            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                                init();
                                goPrinterSetting();
                            } else {
                                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                                createBondMethod.invoke(bluetoothDevice);
                            }
                            PrintQueue.getQueue(getApplicationContext()).disconnect();
                            String name = bluetoothDevice.getName();
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), "");
                            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), "");
                            myApp.showToast("Bluetooth binding failed, please try again");
                        }
                    }
                })
                .create()
                .show();
        */

        try {
            BtUtil.cancelDiscovery(bluetoothAdapter);
            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), bluetoothDevice.getAddress());
            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), bluetoothDevice.getName());
            if (null != deviceAdapter) {
                deviceAdapter.setConnectedDeviceAddress(bluetoothDevice.getAddress());
            }
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                init();
                goPrinterSetting();
            } else {
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                createBondMethod.invoke(bluetoothDevice);
            }
            PrintQueue.getQueue(getApplicationContext()).disconnect();
            String name = bluetoothDevice.getName();
        } catch (Exception e) {
            e.printStackTrace();
            PrintUtil.setDefaultBluetoothDeviceAddress(getApplicationContext(), "");
            PrintUtil.setDefaultBluetoothDeviceName(getApplicationContext(), "");
            myApp.showToast("Bluetooth binding failed, please try again");
        }
    }

    private String getPrinterName(String dName) {
        if (TextUtils.isEmpty(dName)) {
            dName = "Unknown device";
        }
        return dName;
    }

    /**
     * go printer setting activity
     */
    private void goPrinterSetting() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void btStartDiscovery(Intent intent) {
        txtBondTitle.setText("Searching for Bluetooth device…");
        txtBondSummary.setText("");
    }

    @Override
    public void btFinishDiscovery(Intent intent) {
        txtBondTitle.setText("Search is complete");
        txtBondSummary.setText("Click to search again");
    }

    @Override
    public void btStatusChanged(Intent intent) {
        init();
    }

    @Override
    public void btFoundDevice(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (null != deviceAdapter && device != null) {
            deviceAdapter.addDevices(device);
        }
    }

    @Override
    public void btBondStatusChange(Intent intent) {
        init();
        if (PrintUtil.isBondPrinter(getApplicationContext(), bluetoothAdapter)) {
            goPrinterSetting();
        }
    }

    @Override
    public void btPairingRequest(Intent intent) {
        myApp.showToast("The printer is being bound");
    }
}
