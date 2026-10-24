/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

import enums.DeviceStatus;

// Thiết bị không ở trạng thái sẵn sàng cho mượn.
public class DeviceUnavailableException extends BorrowingException {
    private final String deviceId;
    private final DeviceStatus currentStatus;

    public DeviceUnavailableException(String deviceId, DeviceStatus currentStatus) {

        super("Thiết bị " + deviceId + " không sẵn sàng cho mượn. Trạng thái: " + currentStatus);
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