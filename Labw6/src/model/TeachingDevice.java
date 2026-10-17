/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import enums.DeviceStatus;
import java.math.BigDecimal;
import exception.DeviceUnavailableException;

// Lớp cơ sở trừu tượng của các thiết bị được phép mượn.
public abstract class TeachingDevice extends Device {
    private final int maximumBorrowDays;
    private final BigDecimal lateFeePerDay;

    protected TeachingDevice(String deviceId, String name, int yearInService, BigDecimal purchasePrice,
            DeviceStatus status,
            int maximumBorrowDays, BigDecimal lateFeePerDay) {
        super(deviceId, name, yearInService, purchasePrice, status);

        // Giới hạn thời gian mượn phải dương.
        if (maximumBorrowDays <= 0) {
            throw new IllegalArgumentException("Số ngày mượn tối đa phải lớn hơn 0.");
        }

        // Cho phép phí bằng 0, nhưng không được null hoặc âm.
        if (lateFeePerDay == null || lateFeePerDay.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Phí trả muộn mỗi ngày không được null hoặc âm.");
        }
        this.maximumBorrowDays = maximumBorrowDays;
        this.lateFeePerDay = lateFeePerDay;
    }

    public int getMaximumBorrowDays() {
        return maximumBorrowDays;
    }

    public BigDecimal getLateFeePerDay() {
        return lateFeePerDay;
    }

    // Chỉ cho mượn thiết bị đang sẵn sàng.
    public final void markAsBorrowed() {
        if (getStatus() != DeviceStatus.AVAILABLE) {
            throw new DeviceUnavailableException(getDeviceId(), getStatus());
        }
        changeStatus(DeviceStatus.BORROWED);
    }

    // Chỉ chuyển về sẵn sàng khi thiết bị đang được mượn.
    public final void markAsAvailable() {
        if (getStatus() != DeviceStatus.BORROWED) {
            throw new IllegalStateException("Thiết bị không ở trạng thái đang mượn: " + getDeviceId());
        }
        changeStatus(DeviceStatus.AVAILABLE);
    }
}