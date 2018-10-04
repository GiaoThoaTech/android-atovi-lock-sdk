package com.atovi.locksdk.example.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atovi.locksdk.example.R;
import com.atovi.locksdk.example.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.LockViewHolder> {
    private Context context;
    private List<Device> listDevice = new ArrayList<>();
    private DeviceListener deviceListener;

    public DeviceAdapter(Context context, List<Device> listDevice) {
        this.context = context;
        this.listDevice = listDevice;
        try {
            deviceListener = (DeviceListener) context;
        } catch (ClassCastException e) {
            throw new NullPointerException("must element callback");
        }
    }

    @NonNull
    @Override

    public LockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LockViewHolder(LayoutInflater.from(context).inflate(R.layout.item_lock, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LockViewHolder holder, int position) {
        Device device = listDevice.get(position);
        holder.tvLockAddress.setText(device.getDevice_name());
        holder.itemView.setOnClickListener(v -> {
            if (deviceListener != null) {
                deviceListener.onClickDevice(device);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (deviceListener != null) {
                    deviceListener.onLongClickDevice(device);
                }
                return true;
            }
        });
    }

    public void clear() {
        listDevice.clear();
        notifyDataSetChanged();
    }

    public void updateData(List<Device> devices) {
        listDevice.clear();
        listDevice.addAll(devices);
        notifyDataSetChanged();
    }

    public void addLock(Device device) {
        listDevice.add(device);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return listDevice == null ? 0 : listDevice.size();
    }

    static class LockViewHolder extends RecyclerView.ViewHolder {
        TextView tvLockAddress;

        public LockViewHolder(View itemView) {
            super(itemView);
            tvLockAddress = itemView.findViewById(R.id.tv_lock_address);
        }
    }

    public interface DeviceListener {
        void onClickDevice(Device device);

        void onLongClickDevice(Device device);
    }
}
