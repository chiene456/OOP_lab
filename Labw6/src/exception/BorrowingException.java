/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

// Lớp cơ sở của các ngoại lệ nghiệp vụ mượn trả.
public class BorrowingException extends RuntimeException {

    public BorrowingException(String message) {
        super(message);
    }

    public BorrowingException(String message, Throwable cause) {
        super(message, cause);
    }
}