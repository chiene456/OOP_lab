/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package exception;

/** Ném ra khi giá trị đo (pin, tín hiệu) nằm ngoài [0, 100]. */
public class InvalidDeviceReadingException extends RuntimeException {
    private final String deviceId;
    private final int invalidValue;

    public InvalidDeviceReadingException(String deviceId, int invalidValue, String message) {
        super(message);
        this.deviceId = deviceId;
        this.invalidValue = invalidValue;
    }

    public String getDeviceId() { return deviceId; }
    public int getInvalidValue() { return invalidValue; }
}
