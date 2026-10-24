/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package model;

import enums.DeviceStatus;
import exception.DeviceNotReadyException;

/**
 * Lớp trừu tượng biểu diễn thiết bị phục vụ giảng dạy.
 * Dữ liệu và thao tác chuyển trạng thái được quản lý tại lớp cơ sở.
 * Điều kiện sẵn sàng và cách tính điểm do các lớp con cài đặt.
 */
public abstract class TeachingDevice implements IIdentifiable {
    private final String id;
    private final String name;
    private final String storageLocation;

    private DeviceStatus status;

    protected TeachingDevice(
            String id,
            String name,
            String storageLocation) {

        this.id = requireText(id, "Mã thiết bị");
        this.name = requireText(name, "Tên thiết bị");
        this.storageLocation = requireText(storageLocation, "Vị trí lưu trữ");
        this.status = DeviceStatus.AVAILABLE;
    }

    /**
     * Kiểm tra chuỗi khác null, rỗng hoặc chỉ chứa khoảng trắng.
     * 
     * @param value     giá trị cần kiểm tra
     * @param fieldName tên trường dữ liệu
     * @return chuỗi đã loại bỏ khoảng trắng ở hai đầu
     */
    protected static String requireText(
            String value,
            String fieldName) {

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " không được để trống.");
        }
        return value.trim();
    }

    /**
     * Kiểm tra một giá trị phần trăm thuộc đoạn [0, 100].
     */
    protected static int requirePercentage(
            int value,
            String fieldName) {

        if (value < 0 || value > 100) {
            throw new IllegalArgumentException(fieldName + " phải nằm trong đoạn [0, 100].");
        }
        return value;
    }

    @Override
    public final String getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final String getStorageLocation() {
        return storageLocation;
    }

    public final DeviceStatus getStatus() {
        return status;
    }

    /**
     * Kiểm tra khả năng sẵn sàng theo quy tắc của từng loại thiết bị.
     * 
     * @return true nếu thiết bị có thể được cấp phát
     */
    public abstract boolean isReadyForUse();

    /**
     * Tính điểm ưu tiên mặc định của thiết bị.
     * 
     * @return điểm ưu tiên
     */
    public abstract double getDefaultPriorityScore();

    /**
     * Mượn thiết bị nếu thiết bị đáp ứng điều kiện sử dụng.
     * 
     * @throws DeviceNotReadyException nếu thiết bị chưa sẵn sàng
     */
    public final void borrow() {
        // Đa hình: điều kiện cụ thể được xác định tại lớp con.
        if (!isReadyForUse()) {
            throw new DeviceNotReadyException(id, status);
        }
        status = DeviceStatus.BORROWED;
    }

    /**
     * Trả thiết bị về kho.
     * 
     * @throws IllegalStateException nếu thiết bị không đang được mượn
     */
    public final void returnDevice() {
        if (status != DeviceStatus.BORROWED) {
            throw new IllegalStateException(
                    "Không thể trả thiết bị " + id + " vì thiết bị không ở trạng thái BORROWED.");
        }
        status = DeviceStatus.AVAILABLE;
    }

    /**
     * Đưa thiết bị đang ở kho vào bảo trì.
     */
    public final void startMaintenance() {
        if (status != DeviceStatus.AVAILABLE) {
            throw new IllegalStateException("Chỉ có thể bảo trì thiết bị đang AVAILABLE.");
        }
        status = DeviceStatus.UNDER_MAINTENANCE;
    }

    /**
     * Kết thúc bảo trì và đưa thiết bị về trạng thái có sẵn trong kho.
     */
    public final void finishMaintenance() {
        if (status != DeviceStatus.UNDER_MAINTENANCE) {
            throw new IllegalStateException("Thiết bị không ở trạng thái UNDER_MAINTENANCE.");
        }
        status = DeviceStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + "{id='" + id + '\''
                + ", name='" + name + '\''
                + ", storageLocation='" + storageLocation + '\''
                + ", status=" + status
                + '}';
    }
}