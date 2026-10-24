/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

// Yêu cầu trả lại một thiết bị đã được trả trên phiếu.
public class DeviceAlreadyReturnedException extends BorrowingException {
    private final String deviceId;

    public DeviceAlreadyReturnedException(String deviceId) {
        super("Thiết bị " + deviceId + " đã được trả trước đó.");
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}