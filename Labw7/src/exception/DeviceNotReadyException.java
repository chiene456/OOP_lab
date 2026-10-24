/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package exception;

import enums.DeviceStatus;

/**
 * Ngoại lệ khi thiết bị chưa đáp ứng điều kiện để được mượn.
 */
public class DeviceNotReadyException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String deviceId;
    private final DeviceStatus currentStatus;

    public DeviceNotReadyException(String deviceId, DeviceStatus currentStatus) {
        super("Thiết bị " + deviceId + " chưa sẵn sàng để cấp phát. Trạng thái hiện tại: " + currentStatus);
        this.deviceId = deviceId;
        this.currentStatus = currentStatus;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DeviceStatus getCurrentStatus() {
        return currentStatus;
    }
}