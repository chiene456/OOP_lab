/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package model;

import enums.DeviceStatus;

/**
 * Micro không dây.
 * Điều kiện sẵn sàng:
 * - Trạng thái AVAILABLE.
 * - Mức pin từ 30% trở lên.
 * - Chất lượng tín hiệu từ 40% trở lên.
 */
public class WirelessMicrophone extends TeachingDevice {
    private final int batteryLevel;
    private final int signalQuality;

    public WirelessMicrophone(String id, String name, String storageLocation, int batteryLevel, int signalQuality) {

        super(id, name, storageLocation);

        this.batteryLevel = requirePercentage(batteryLevel, "Mức pin");

        this.signalQuality = requirePercentage(signalQuality, "Chất lượng tín hiệu");
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public int getSignalQuality() {
        return signalQuality;
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.AVAILABLE && batteryLevel >= 30 && signalQuality >= 40;
    }

    @Override
    public double getDefaultPriorityScore() {
        // Chia cho 2.0 để giữ phần thập phân.
        return (batteryLevel + signalQuality) / 2.0;
    }

    @Override
    public String toString() {
        return super.toString()
                + " [pin=" + batteryLevel + "%"
                + ", tín hiệu=" + signalQuality + "%]";
    }
}