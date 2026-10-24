/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

// Một mã thiết bị được thêm nhiều lần vào cùng phiếu.
public class DuplicateDeviceException extends BorrowingException {
    private final String deviceId;

    public DuplicateDeviceException(String deviceId) {
        super("Thiết bị " + deviceId + " đã tồn tại trong phiếu mượn.");
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }
}