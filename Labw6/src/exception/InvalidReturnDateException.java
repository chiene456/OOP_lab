/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package exception;

import java.time.LocalDate;

// Ngày trả bị thiếu hoặc trước ngày mượn.
public class InvalidReturnDateException extends BorrowingException {
    private final LocalDate borrowedDate;
    private final LocalDate returnedDate;

    public InvalidReturnDateException(LocalDate borrowedDate, LocalDate returnedDate) {

        super("Ngày trả không hợp lệ: " + returnedDate + ". Ngày trả không được null hoặc trước ngày mượn "
                + borrowedDate + ".");
        this.borrowedDate = borrowedDate;
        this.returnedDate = returnedDate;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }
}