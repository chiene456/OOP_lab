/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package exception;

/**
 * Ngoại lệ khi không có ứng viên phù hợp để lựa chọn.
 */
public class NoSuitableItemException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NoSuitableItemException() {
        super("Không có thiết bị phù hợp để cấp phát.");
    }
}