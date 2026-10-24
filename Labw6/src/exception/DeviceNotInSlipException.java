/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

// Yêu cầu trả thiết bị không thuộc phiếu mượn.
public class DeviceNotInSlipException extends BorrowingException {
    private final String deviceId;

    public DeviceNotInSlipException(String deviceId) {
        super("Thiết bị " + deviceId + " không thuộc phiếu mượn.");
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}