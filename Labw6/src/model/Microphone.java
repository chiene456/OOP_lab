/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import enums.DeviceStatus;
import java.math.BigDecimal;

// Thiết bị micro có dây hoặc không dây.
public class Microphone extends TeachingDevice {
    // Giả định: chi phí bảo trì mỗi năm bằng 5% giá mua.
    private static final BigDecimal MAINTENANCE_RATE = new BigDecimal("0.05");

    private final boolean wireless;

    public Microphone(String deviceId, String name, int yearInService, BigDecimal purchasePrice, DeviceStatus status,
            int maximumBorrowDays, BigDecimal lateFeePerDay, boolean wireless) {

        super(deviceId, name, yearInService, purchasePrice, status, maximumBorrowDays, lateFeePerDay);

        this.wireless = wireless;
    }

    public boolean isWireless() {
        return wireless;
    }

    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPurchasePrice().multiply(MAINTENANCE_RATE);
    }
}