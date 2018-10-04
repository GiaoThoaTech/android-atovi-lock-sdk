package com.atovi.lock.sdk.example.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.atovi.lock.sdk.api.ScanLockAPI;
import com.atovi.lock.sdk.callback.ScanLockCallback;
import com.atovi.lock.sdk.constant.BleState;
import com.atovi.lock.sdk.example.R;
import com.atovi.lock.sdk.example.constant.Constant;
import com.atovi.lock.sdk.example.model.Device;

import java.util.ArrayList;

public class ScanActivity extends AppCompatActivity implements DeviceAdapter.DeviceListener {
    private DeviceAdapter deviceAdapter;
    private ScanLockAPI scanLockAPI;
    private Dialog dialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        RecyclerView rcScanLock = findViewById(R.id.rc_scan_lock);
        rcScanLock.setLayoutManager(new LinearLayoutManager(this));
        rcScanLock.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        deviceAdapter = new DeviceAdapter(this, new ArrayList<>());
        rcScanLock.setAdapter(deviceAdapter);
        scanLockAPI = new ScanLockAPI(this, scanLockCallback);
        initToolbar();
    }

    private void initToolbar() {
        findViewById(R.id.ctl_toolbar_left).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.title_activity_scan);
    }

    @Override
    protected void onStart() {
        super.onStart();
        scanLockAPI.startScan();
        deviceAdapter.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLockAPI.stopScan();
    }

    private ScanLockCallback scanLockCallback = new ScanLockCallback() {

        @Override
        public void bleState(BleState bleState) {
            Toast.makeText(ScanActivity.this, "ble State: " + bleState, Toast.LENGTH_SHORT).show();
            switch (bleState) {
                case READY:
                case BLUETOOTH_NOT_ENABLED:
                case BLUETOOTH_NOT_AVAILABLE:
                case LOCATION_SERVICES_NOT_ENABLED:
                case LOCATION_PERMISSION_NOT_GRANTED:
            }
        }

        @Override
        public void startScan() {
            Toast.makeText(ScanActivity.this, "start scan", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void stopScan() {
            Toast.makeText(ScanActivity.this, "stop scan", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void foundLock(ScanLockAPI.ScanLockModel scanLockModel) {
            Toast.makeText(ScanActivity.this, "found lock: " + scanLockModel.getMacAddress(), Toast.LENGTH_SHORT).show();
            deviceAdapter.addLock(new Device(scanLockModel.getMacAddress(),
                    scanLockModel.getName(),
                    0,
                    false));
        }
    };

    private void startControlActivity(String deviceAddress, String deviceName, int key, boolean isNew) {
        Intent intent = new Intent(this, ControlActivity.class);
        intent.putExtra(Constant.DEVICE_ADDRESS, deviceAddress);
        intent.putExtra(Constant.DEVICE_NAME, deviceName);
        intent.putExtra(Constant.DEVICE_KEY, key);
        intent.putExtra(Constant.DEVICE_NEW, isNew);
        startActivity(intent);
    }

    @Override
    public void onClickDevice(Device device) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.layout_dialog, null);
        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        tvTitle.setText("Add Lock");

        dialogView.findViewById(R.id.edt_one).setVisibility(View.GONE);
        EditText edtKey = dialogView.findViewById(R.id.edt_two);
        edtKey.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtKey.setHint("Key");
        edtKey.setText("123456");
        dialogView.findViewById(R.id.btn_negative).setOnClickListener(v -> dialogInput.dismiss());
        dialogView.findViewById(R.id.btn_positive).setOnClickListener(v -> {
            dialogInput.dismiss();
            startControlActivity(device.getDevice_id(), device.getDevice_name(),
                    Integer.parseInt(edtKey.getText().toString()), true);
            finish();
        });
        builder.setView(dialogView);
        dialogInput = builder.create();
        dialogInput.show();
    }

    @Override
    public void onLongClickDevice(Device device) {

    }
}
