/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import enums.DeviceStatus;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Objects;

// Lớp cơ sở trừu tượng của các thiết bị.

public abstract class Device {
    private final String deviceId;
    private final String name;
    private final int yearInService;
    private final BigDecimal purchasePrice;
    private DeviceStatus status;

    protected Device(String deviceId, String name, int yearInService, BigDecimal purchasePrice, DeviceStatus status) {

        // Kiểm tra toàn bộ dữ liệu trước khi gán thuộc tính.
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng.");
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thiết bị không được rỗng.");
        }

        int currentYear = Year.now().getValue();

        if (yearInService <= 0 || yearInService > currentYear) {
            throw new IllegalArgumentException("Năm sử dụng phải từ 1 đến " + currentYear + ".");
        }

        if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Giá mua phải lớn hơn 0.");
        }

        if (status == null) {
            throw new IllegalArgumentException("Trạng thái thiết bị không được null.");
        }

        this.deviceId = deviceId.trim();
        this.name = name.trim();
        this.yearInService = yearInService;
        this.purchasePrice = purchasePrice;
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getName() {
        return name;
    }

    public int getYearInService() {
        return yearInService;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    // Dành cho lớp con thực hiện chuyển trạng thái theo nghiệp vụ.

    protected final void changeStatus(DeviceStatus newStatus) {
        Objects.requireNonNull(newStatus, "Trạng thái thiết bị không được null.");
        this.status = newStatus;
    }

    // Mỗi loại thiết bị có cách tính chi phí bảo trì riêng.

    public abstract BigDecimal calculateAnnualMaintenanceCost();

    @Override
    public String toString() {
        return "Device{" +
                "deviceId='" + deviceId + '\'' +
                ", name='" + name + '\'' +
                ", yearInService=" + yearInService +
                ", purchasePrice=" + purchasePrice.toPlainString() +
                ", status=" + status +
                '}';
    }
}