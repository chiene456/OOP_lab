/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

// Số thiết bị trên phiếu vượt giới hạn của người mượn.
public class BorrowLimitExceededException extends BorrowingException {
    private final String borrowerId;
    private final int maximumDevices;

    public BorrowLimitExceededException(String borrowerId, int maximumDevices) {

        super("Người mượn " + borrowerId + " chỉ được mượn tối đa " + maximumDevices + " thiết bị trên một phiếu.");
        this.borrowerId = borrowerId;
        this.maximumDevices = maximumDevices;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public int getMaximumDevices() {
        return maximumDevices;
    }
}