/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package model;

import enums.DeviceStatus;

/**
 * Điều khiển máy chiếu.
 * Điều kiện sẵn sàng:
 * - Trạng thái AVAILABLE.
 * - Mức pin từ 20% trở lên.
 */
public class ProjectorRemote extends TeachingDevice {
    private final int batteryLevel;

    public ProjectorRemote(
            String id,
            String name,
            String storageLocation,
            int batteryLevel) {

        super(id, name, storageLocation);

        this.batteryLevel = requirePercentage(batteryLevel, "Mức pin");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.AVAILABLE && batteryLevel >= 20;
    }

    @Override
    public double getDefaultPriorityScore() {
        return batteryLevel;
    }

    @Override
    public String toString() {
        return super.toString() + " [pin=" + batteryLevel + "%]";
    }
}