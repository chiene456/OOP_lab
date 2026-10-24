/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import enums.DeviceStatus;
import java.math.BigDecimal;

// Thiết bị điều khiển điều hòa.
public class AirConditionerRemote extends TeachingDevice {
    private static final BigDecimal MAINTENANCE_RATE = new BigDecimal("0.02");

    public AirConditionerRemote(String deviceId, String name, int yearInService, BigDecimal purchasePrice,
            DeviceStatus status, int maximumBorrowDays, BigDecimal lateFeePerDay) {
        super(deviceId, name, yearInService, purchasePrice, status, maximumBorrowDays, lateFeePerDay);
    }

    // Giả định: chi phí bảo trì mỗi năm bằng 2% giá mua.
    @Override
    public BigDecimal calculateAnnualMaintenanceCost() {
        return getPurchasePrice().multiply(MAINTENANCE_RATE);
    }
}