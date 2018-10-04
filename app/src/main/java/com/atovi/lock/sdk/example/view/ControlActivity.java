package com.atovi.lock.sdk.example.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atovi.lock.sdk.api.AtoviLockAPI;
import com.atovi.lock.sdk.ble.BleDeviceManager;
import com.atovi.lock.sdk.callback.AtoviLockCallback;
import com.atovi.lock.sdk.constant.BleState;
import com.atovi.lock.sdk.constant.LockStatus;
import com.atovi.lock.sdk.model.AtoviLock;
import com.atovi.lock.sdk.example.R;
import com.atovi.lock.sdk.example.constant.Constant;
import com.atovi.lock.sdk.example.custom.LockControlView;
import com.atovi.lock.sdk.example.dao.DeviceDAO;
import com.atovi.lock.sdk.example.model.Device;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class ControlActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();
    private LockControlView lockControlView;
    private Device device;
    private AtoviLock atoviLock;
    private AtoviLockAPI atoviLockAPI;
    private ImageView ivBluetooth;
    private Vibrator vibrator;
    private Animation bubbleAnimation;
    private SubActionButton btnChangeKey;
    private AlertDialog dialogChangeKey;
    private boolean isBluetoothOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        lockControlView = findViewById(R.id.lock_control);
        lockControlView.setOnClickListener(this);
        initDeviceData();
        initToolbar();

        ivBluetooth = findViewById(R.id.iv_bluetooth);
        ivBluetooth.setOnClickListener(this);
        bubbleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_power);

        //fab menu
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_tools);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setBackgroundDrawable(R.drawable.circler_background_active)
                .build();
        FloatingActionButton.LayoutParams layoutParams = (FloatingActionButton.LayoutParams) actionButton.getLayoutParams();
        layoutParams.width = getResources().getDimensionPixelSize(R.dimen.fab_menu_size);
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.fab_menu_size);
        int margin = getResources().getDimensionPixelSize(R.dimen.fab_menu_margin);
        layoutParams.setMargins(margin, margin, margin, margin);
        actionButton.setLayoutParams(layoutParams);
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        // repeat many times:
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageResource(R.drawable.ic_key_in_the_lock);
        itemIcon.setPadding(4, 4, 4, 4);
        btnChangeKey = itemBuilder.setContentView(itemIcon).setBackgroundDrawable(getResources().getDrawable(R.drawable.circler_background_inactive)).build();
        btnChangeKey.setOnClickListener(view -> {
            if (atoviLockAPI.isConnected()) {
                showDialogChangeKey();
            } else {
                Toast.makeText(this, "Please connect to lock!", Toast.LENGTH_SHORT).show();
            }
        });
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btnChangeKey)
                .attachTo(actionButton)
                .setStartAngle(270)
                .setEndAngle(180)
                .setRadius(getResources().getDimensionPixelSize(R.dimen.fab_menu_radius))
                .build();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isBluetoothOn) {
            atoviLockAPI.disConnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isBluetoothOn) {
            atoviLockAPI.connect();
        }
    }

    private void initToolbar() {
        findViewById(R.id.ctl_toolbar_left).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(device.getDevice_name());
    }

    private void initDeviceData() {
        final Intent intent = getIntent();
        String deviceName = intent.getStringExtra(Constant.DEVICE_NAME);
        String deviceAddress = intent.getStringExtra(Constant.DEVICE_ADDRESS);
        int key = intent.getIntExtra(Constant.DEVICE_KEY, Device.KEY_DEFAULT);
        boolean isNew = intent.getBooleanExtra(Constant.DEVICE_NEW, false);
        Log.d(TAG, "initDeviceData: " + key + " " + isNew);
        if (isNew) {
            device = new Device(deviceAddress,
                    deviceName,
                    key,
                    false);
        } else {
            device = DeviceDAO.getInstance().get(deviceAddress);
        }

        atoviLock = new AtoviLock(device.getDevice_id(),
                device.getDevice_name(),
                device.getKey(),
                device.isAdmin());
        Log.d(TAG, "initDeviceData: " + atoviLock.toMap());

        atoviLockAPI = new AtoviLockAPI(this, atoviLock, atoviLockCallback);
    }

    private AtoviLockCallback atoviLockCallback = new AtoviLockCallback() {

        @Override
        public void bleState(BleState bleState) {
            Toast.makeText(ControlActivity.this, "ble state: " + bleState, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void updateLock(int result, AtoviLock atoviLock) {
            if (atoviLock != null) {
                Toast.makeText(ControlActivity.this, "update lock " + atoviLock.getDevice_id(), Toast.LENGTH_SHORT).show();
                if (!DeviceDAO.getInstance().isExisted(atoviLock.getDevice_id())) {
                    device.copy(atoviLock);
                } else {
                    Device deviceLocal = DeviceDAO.getInstance().get(atoviLock.getDevice_id());
                    device = deviceLocal.copy(atoviLock);
                }
                Log.d(TAG, "updateLock: " + device.toMap());
                DeviceDAO.getInstance().save(device);
            }
        }

        @Override
        public void connectStatus(BleDeviceManager.ConnectStatus status) {
            Toast.makeText(ControlActivity.this, "connect status: " + status, Toast.LENGTH_SHORT).show();
            switch (status) {
                case CONNECTING:
                    lockControlView.setStatus(LockStatus.CONNECTING);
                    changeStatusBluetooth(false);
                    ivBluetooth.startAnimation(bubbleAnimation);
                    break;
                case CONNECTED:
                    break;
                case DISCONNECT:
                    changeStatusBluetooth(false);
                    ivBluetooth.setAnimation(null);
                    lockControlView.setStatus(LockStatus.DISCONNECT);
                    break;
            }
        }

        @Override
        public void lockStatus(LockStatus lockStatus) {
            Toast.makeText(ControlActivity.this, "lock status: " + lockStatus, Toast.LENGTH_SHORT).show();
            if (vibrator == null) {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            }
            if (lockStatus == LockStatus.LOCK || lockStatus == LockStatus.UNLOCK) {
                vibrator.vibrate(500);
                changeStatusBluetooth(true);
            } else {
                changeStatusBluetooth(false);
            }

            ivBluetooth.setAnimation(null);

            lockControlView.setStatus(lockStatus);
            Log.d(TAG, "lockStatus: " + lockStatus);
        }

        @Override
        public void changeKey(boolean isSuccess, Integer newKey) {
            Toast.makeText(ControlActivity.this, isSuccess ? "Change key success" : "Change key unsuccess", Toast.LENGTH_SHORT).show();
            device.setKey(newKey);
            DeviceDAO.getInstance().save(device);
        }

        @Override
        public void wrongKey(int result) {
            Toast.makeText(ControlActivity.this, "wrong key", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeStatusBluetooth(boolean isBluetoothOn) {
        ivBluetooth.setImageResource(isBluetoothOn ? R.drawable.ic_bluetooth_on : R.drawable.ic_bluetooth_off);
        btnChangeKey.setBackgroundResource(isBluetoothOn ? R.drawable.circler_background_active : R.drawable.circler_background_inactive);
        this.isBluetoothOn = isBluetoothOn;
    }

    private void showDialogChangeKey() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = View.inflate(this, R.layout.layout_dialog, null);
        TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
        tvTitle.setText("Change Key");

        EditText edtOldKey = dialogView.findViewById(R.id.edt_one);
        edtOldKey.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtOldKey.setHint("Old Key");
        edtOldKey.setText(String.valueOf(device.getKey()));
        EditText edtNewKey = dialogView.findViewById(R.id.edt_two);
        edtNewKey.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtNewKey.setHint("New Key");
        dialogView.findViewById(R.id.btn_negative).setOnClickListener(v -> dialogChangeKey.dismiss());
        dialogView.findViewById(R.id.btn_positive).setOnClickListener(v -> {
            dialogChangeKey.dismiss();
            atoviLockAPI.changeKey(Integer.parseInt(edtOldKey.getText().toString()), Integer.parseInt(edtNewKey.getText().toString()));
        });
        builder.setView(dialogView);
        dialogChangeKey = builder.create();
        dialogChangeKey.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_bluetooth:
                if (!atoviLockAPI.isConnected()) {
                    atoviLockAPI.connect();
                } else {
                    atoviLockAPI.disConnect();
                }
                break;
            case R.id.lock_control:
                atoviLockAPI.control();
                break;
        }
    }
}
