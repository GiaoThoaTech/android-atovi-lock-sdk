package com.atovi.locksdk.example.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.atovi.lock.sdk.model.AtoviLock;

import java.util.HashMap;
import java.util.Map;

@Table(name = "Device")
public class Device extends Model {
    public final static int KEY_DEFAULT = -1;

    @Column(name = "device_id", index = true)
    private String device_id;

    @Column(name = "device_name")
    private String device_name;

    @Column(name = "build_name")
    private String build_name;

    @Column(name = "key")
    private int key = KEY_DEFAULT;

    @Column(name = "temp_key")
    private String temp_key;

    @Column(name = "temp_key_index")
    private int temp_key_index = 0;

    @Column(name = "serial_number")
    private String serial_number;

    @Column(name = "admin")
    private boolean admin;

    @Column(name = "auto_lock")
    private boolean auto_lock;

    @Column(name = "active_touch")
    private boolean active_touch;

    @Column(name = "volume")
    private int volume;

    @Column(name = "warning")
    private int warning;

    @Column(name = "battery")
    private int battery = -1;

    @Column(name = "battery_show_all")
    private boolean battery_show_all = true;

    @Column(name = "battery_always_show")
    private boolean battery_always_show = true;

    @Column(name = "battery_threshold_alert")
    private int battery_threshold_alert = 20;

    @Column(name = "key_changed")
    private boolean key_changed;

    @Column(name = "last_log_sync")
    private long last_log_sync;

    @Column(name = "ble_version")
    private String ble_version;

    @Column(name = "private_mode")
    private boolean private_mode;

    private boolean lock = false;

    public Device() {
        super();
    }

    public Device(String device_id, String device_name, String build_name, int key, String temp_key, int temp_key_index, String serial_number, boolean admin, boolean auto_lock, boolean active_touch, int volume, int warning, int battery, boolean battery_show_all, boolean battery_always_show, int battery_threshold_alert, boolean key_changed, long last_log_sync, String ble_version, boolean private_mode, boolean lock) {
        super();
        this.device_id = device_id;
        this.device_name = device_name;
        this.build_name = build_name;
        this.key = key;
        this.temp_key = temp_key;
        this.temp_key_index = temp_key_index;
        this.serial_number = serial_number;
        this.admin = admin;
        this.auto_lock = auto_lock;
        this.active_touch = active_touch;
        this.volume = volume;
        this.warning = warning;
        this.battery = battery;
        this.battery_show_all = battery_show_all;
        this.battery_always_show = battery_always_show;
        this.battery_threshold_alert = battery_threshold_alert;
        this.key_changed = key_changed;
        this.last_log_sync = last_log_sync;
        this.ble_version = ble_version;
        this.private_mode = private_mode;
        this.lock = lock;
    }

    public Device(String device_id, String device_name, int key, boolean admin) {
        super();
        this.device_id = device_id;
        this.device_name = device_name;
        this.key = key;
        this.admin = admin;
    }

    public Device copy(AtoviLock atoviLock) {
        this.device_name = atoviLock.getDevice_name();
        this.build_name = atoviLock.getBuild_name();
        this.key = atoviLock.getKey();
        this.temp_key = atoviLock.getTemp_key();
        this.temp_key_index = atoviLock.getTemp_key_index();
        this.serial_number = atoviLock.getSerial_number();
        this.admin = atoviLock.isAdmin();
        this.auto_lock = atoviLock.isAuto_lock();
        this.active_touch = atoviLock.isActive_touch();
        this.volume = atoviLock.getVolume();
        this.warning = atoviLock.getWarning();
        this.battery = atoviLock.getBattery();
        this.battery_show_all = atoviLock.isBattery_show_all();
        this.battery_always_show = atoviLock.isBattery_always_show();
        this.battery_threshold_alert = atoviLock.getBattery_threshold_alert();
        this.key_changed = atoviLock.isKey_changed();
        this.last_log_sync = atoviLock.getLast_log_sync();
        this.ble_version = atoviLock.getBle_version();
        this.private_mode = atoviLock.isPrivate_mode();
        this.lock = atoviLock.isLock();
        return this;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("device_id", device_id);
        result.put("key", key);
        result.put("device_name", device_name);
        result.put("build_name", build_name);
        result.put("serial_number", serial_number);
        result.put("admin", admin);
        result.put("auto_lock", auto_lock);
        result.put("active_touch", active_touch);
        result.put("volume", volume);
        result.put("warning", warning);
        result.put("battery", battery);
        result.put("key_changed", key_changed);
        result.put("ble_version", ble_version);
        result.put("private_mode", private_mode);
        return result;
    }

    public static int getKeyDefault() {
        return KEY_DEFAULT;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getBuild_name() {
        return build_name;
    }

    public void setBuild_name(String build_name) {
        this.build_name = build_name;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTemp_key() {
        return temp_key;
    }

    public void setTemp_key(String temp_key) {
        this.temp_key = temp_key;
    }

    public int getTemp_key_index() {
        return temp_key_index;
    }

    public void setTemp_key_index(int temp_key_index) {
        this.temp_key_index = temp_key_index;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isAuto_lock() {
        return auto_lock;
    }

    public void setAuto_lock(boolean auto_lock) {
        this.auto_lock = auto_lock;
    }

    public boolean isActive_touch() {
        return active_touch;
    }

    public void setActive_touch(boolean active_touch) {
        this.active_touch = active_touch;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public boolean isBattery_show_all() {
        return battery_show_all;
    }

    public void setBattery_show_all(boolean battery_show_all) {
        this.battery_show_all = battery_show_all;
    }

    public boolean isBattery_always_show() {
        return battery_always_show;
    }

    public void setBattery_always_show(boolean battery_always_show) {
        this.battery_always_show = battery_always_show;
    }

    public int getBattery_threshold_alert() {
        return battery_threshold_alert;
    }

    public void setBattery_threshold_alert(int battery_threshold_alert) {
        this.battery_threshold_alert = battery_threshold_alert;
    }

    public boolean isKey_changed() {
        return key_changed;
    }

    public void setKey_changed(boolean key_changed) {
        this.key_changed = key_changed;
    }

    public long getLast_log_sync() {
        return last_log_sync;
    }

    public void setLast_log_sync(long last_log_sync) {
        this.last_log_sync = last_log_sync;
    }

    public String getBle_version() {
        return ble_version;
    }

    public void setBle_version(String ble_version) {
        this.ble_version = ble_version;
    }

    public boolean isPrivate_mode() {
        return private_mode;
    }

    public void setPrivate_mode(boolean private_mode) {
        this.private_mode = private_mode;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
