/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import enums.DeviceStatus;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Quản lý thiết bị của một phòng học theo quan hệ kết tập.
public class ClassRoom {
    private static final int OLD_DEVICE_YEARS = 5;
    private final String roomId;
    private final String roomName;
    private final int capacity;
    private final List<Device> devices;

    public ClassRoom(String roomId, String roomName, int capacity) {
        if (roomId == null || roomId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được rỗng.");
        }

        if (roomName == null || roomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được rỗng.");
        }

        if (capacity <= 0) {
            throw new IllegalArgumentException("Sức chứa phải lớn hơn 0.");
        }
        this.roomId = roomId.trim();
        this.roomName = roomName.trim();
        this.capacity = capacity;
        this.devices = new ArrayList<>();
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    // Kiểm tra dữ liệu trước khi thêm vào danh sách.
    public void addDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Thiết bị không được null.");
        }

        if (findDevice(device.getDeviceId()) != null) {
            throw new IllegalArgumentException("Mã thiết bị đã tồn tại trong phòng: " + device.getDeviceId());
        }
        devices.add(device);
    }

    // Trả về null nếu mã hợp lệ nhưng không tìm thấy thiết bị.
    public Device findDevice(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng.");
        }
        String normalizedId = deviceId.trim();

        for (Device device : devices) {
            if (device.getDeviceId().equals(normalizedId)) {
                return device;
            }
        }
        return null;
    }

    // Chỉ xóa tham chiếu khỏi phòng, không thay đổi trạng thái thiết bị.
    public boolean removeDevice(String deviceId) {
        Device device = findDevice(deviceId);

        if (device == null) {
            return false;
        }
        return devices.remove(device);
    }

    // Gọi phương thức bảo trì theo tính đa hình.
    public BigDecimal calculateAnnualMaintenanceCost() {
        BigDecimal total = BigDecimal.ZERO;

        for (Device device : devices) {
            total = total.add(device.calculateAnnualMaintenanceCost());
        }
        return total;
    }

    // Lọc thiết bị đang bảo trì hoặc có thời gian sử dụng trên 5 năm.
    public List<Device> getDevicesRequiringMaintenance() {
        List<Device> result = new ArrayList<>();
        int currentYear = Year.now().getValue();

        for (Device device : devices) {
            if (device.getStatus() == DeviceStatus.UNDER_MAINTENANCE
                    || currentYear - device.getYearInService() > OLD_DEVICE_YEARS) {
                result.add(device);
            }
        }
        return Collections.unmodifiableList(result);
    }

    // Trả bản sao không cho bên ngoài thêm, xóa hoặc thay thế phần tử.
    public List<Device> getDevices() {
        return Collections.unmodifiableList(new ArrayList<>(devices));
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "roomId='" + roomId + '\'' +
                ", roomName='" + roomName + '\'' +
                ", capacity=" + capacity +
                ", deviceCount=" + devices.size() +
                '}';
    }
}