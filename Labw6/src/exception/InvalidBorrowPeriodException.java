/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

// Số ngày mượn không dương hoặc vượt giới hạn thiết bị.
public class InvalidBorrowPeriodException extends BorrowingException {
    private final String deviceId;
    private final int requestedDays;
    private final int maximumDays;

    public InvalidBorrowPeriodException(String deviceId, int requestedDays, int maximumDays) {

        super("Thiết bị " + deviceId + ": số ngày mượn yêu cầu là " + requestedDays + ", phải từ 1 đến " + maximumDays
                + " ngày.");
        this.deviceId = deviceId;
        this.requestedDays = requestedDays;
        this.maximumDays = maximumDays;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public int getRequestedDays() {
        return requestedDays;
    }

    public int getMaximumDays() {
        return maximumDays;
    }
}