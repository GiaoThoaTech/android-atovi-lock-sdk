package com.atovi.locksdk.example.dao;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.atovi.locksdk.example.model.Device;

import java.util.List;

public class DeviceDAO {

    private static final String TAG = DeviceDAO.class.getSimpleName();
    private static DeviceDAO instance;

    private DeviceDAO() {
    }

    public static DeviceDAO getInstance() {
        if (instance == null) {
            instance = new DeviceDAO();
        }
        return instance;
    }

    public boolean isExisted(String address) {
        return new Select()
                .from(Device.class)
                .where("device_id = ?", address)
                .count() > 0;
    }

    public Device get(String address) {
        return new Select()
                .from(Device.class)
                .where("device_id = ?", address)
                .executeSingle();
    }

    public List<Device> getAll() {
        return new Select()
                .from(Device.class)
                .execute();
    }

    public Long save(Device device) {
        return device.save();
    }

    public void delete(String address) {
        new Delete().from(Device.class).where("device_id = ?", address).execute();
    }
}
