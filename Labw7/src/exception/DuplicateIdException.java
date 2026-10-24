/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package exception;

/**
 * Ngoại lệ khi thêm đối tượng có mã đã tồn tại trong kho.
 */
public class DuplicateIdException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String duplicateId;

    public DuplicateIdException(String duplicateId) {
        super("Mã đã tồn tại trong kho: " + duplicateId);
        this.duplicateId = duplicateId;
    }

    public String getDuplicateId() {
        return duplicateId;
    }
}