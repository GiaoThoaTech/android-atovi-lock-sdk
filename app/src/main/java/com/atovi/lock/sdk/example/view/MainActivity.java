package com.atovi.lock.sdk.example.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atovi.lock.sdk.example.R;
import com.atovi.lock.sdk.example.constant.Constant;
import com.atovi.lock.sdk.example.dao.DeviceDAO;
import com.atovi.lock.sdk.example.model.Device;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DeviceAdapter.DeviceListener {
    private String TAG = this.getClass().getSimpleName();
    private List<Device> listDevice = new ArrayList<>();
    private DeviceAdapter deviceAdapter;
    private AlertDialog dialogOpenLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rcLock = findViewById(R.id.rc_lock);
        rcLock.setLayoutManager(new LinearLayoutManager(this));
        rcLock.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        deviceAdapter = new DeviceAdapter(this, listDevice);
        rcLock.setAdapter(deviceAdapter);
        initToolbar();

        //fab menu
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_add);
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
        //btn scan
        ImageView icScan = new ImageView(this);
        icScan.setImageResource(R.drawable.ic_glass_finder);
        icScan.setPadding(4, 4, 4, 4);
        SubActionButton btnScan = itemBuilder.setContentView(icScan).setBackgroundDrawable(getResources().getDrawable(R.drawable.circler_background_active)).build();
        btnScan.setOnClickListener(view -> startActivity(new Intent(this, ScanActivity.class)));

        //btn open lock
        ImageView icOpenLock = new ImageView(this);
        icOpenLock.setImageResource(R.drawable.ic_door_lock);
        icOpenLock.setPadding(4, 4, 4, 4);
        SubActionButton btnOpenLock = itemBuilder.setContentView(icOpenLock).setBackgroundDrawable(getResources().getDrawable(R.drawable.circler_background_active)).build();
        btnOpenLock.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = View.inflate(this, R.layout.layout_dialog, null);
            TextView tvTitle = dialogView.findViewById(R.id.tv_dialog_title);
            tvTitle.setText("Add Lock");

            EditText edtAddress = dialogView.findViewById(R.id.edt_one);
            edtAddress.setInputType(InputType.TYPE_CLASS_TEXT);
            edtAddress.setHint("Lock Address");
            edtAddress.setText("7C:01:0A:61:9C:9A");
            EditText edtKey = dialogView.findViewById(R.id.edt_two);
            edtKey.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtKey.setHint("Key");
            edtKey.setText("123456");
            dialogView.findViewById(R.id.btn_negative).setOnClickListener(v -> dialogOpenLock.dismiss());
            dialogView.findViewById(R.id.btn_positive).setOnClickListener(v -> {
                dialogOpenLock.dismiss();
                startControlActivity(edtAddress.getText().toString(), edtAddress.getText().toString(),
                        Integer.parseInt(edtKey.getText().toString()), true);
            });
            builder.setView(dialogView);
            dialogOpenLock = builder.create();
            dialogOpenLock.show();
        });


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(btnScan)
                .addSubActionView(btnOpenLock)
                .attachTo(actionButton)
                .setStartAngle(270)
                .setEndAngle(180)
                .setRadius(getResources().getDimensionPixelSize(R.dimen.fab_menu_radius))
                .build();
    }

    private void initToolbar() {
        findViewById(R.id.ctl_toolbar_left).setVisibility(View.INVISIBLE);
        ((TextView) findViewById(R.id.tv_toolbar_title)).setText(R.string.app_name);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDataLocal();
    }

    private void loadDataLocal() {
        listDevice = DeviceDAO.getInstance().getAll();
        if (listDevice != null) {
            deviceAdapter.updateData(listDevice);
        }
    }

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
        Log.d(TAG, "onClickDevice: " + device.toMap());
        startControlActivity(device.getDevice_id(), device.getDevice_name(), device.getKey(), false);
    }

    @Override
    public void onLongClickDevice(Device device) {
        new AlertDialog.Builder(this).setTitle("Delete Lock")
                .setMessage("Are you sure want to delete \"" + device.getDevice_name() + "\"?")
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    DeviceDAO.getInstance().delete(device.getDevice_id());
                    dialogInterface.dismiss();
                    loadDataLocal();
                }).show();
    }
}
