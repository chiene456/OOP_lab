/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import exception.DeviceAlreadyReturnedException;
import exception.InvalidReturnDateException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowItem {
    private final TeachingDevice device;
    private final LocalDate borrowedDate;
    private final LocalDate dueDate;
    private LocalDate returnedDate;

    // Phạm vi package: việc tạo dòng phiếu do BorrowingSlip quản lý.
    BorrowItem(TeachingDevice device, LocalDate borrowedDate, LocalDate dueDate) {

        if (device == null) {
            throw new IllegalArgumentException("Thiết bị không được null.");
        }

        if (borrowedDate == null || dueDate == null) {
            throw new IllegalArgumentException("Ngày mượn và hạn trả không được null.");
        }

        if (dueDate.isBefore(borrowedDate)) {
            throw new IllegalArgumentException("Hạn trả không được trước ngày mượn.");
        }
        this.device = device;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = null;
    }

    public TeachingDevice getDevice() {
        return device;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public boolean isReturned() {
        return returnedDate != null;
    }

    // Chỉ gọi qua BorrowingSlip để quản lý thao tác trả.
    void returnItem(LocalDate date) {
        if (isReturned()) {
            throw new DeviceAlreadyReturnedException(device.getDeviceId());
        }

        if (date == null || date.isBefore(borrowedDate)) {
            throw new InvalidReturnDateException(borrowedDate, date);
        }
        // Nếu chuyển trạng thái thất bại, ngày trả vẫn giữ nguyên.
        device.markAsAvailable();
        returnedDate = date;
    }

    // Tính phí đã chốt cho thiết bị đã trả.
    public BigDecimal calculateLateFee() {
        if (!isReturned()) {
            return BigDecimal.ZERO;
        }
        long lateDays = Math.max(0, ChronoUnit.DAYS.between(dueDate, returnedDate));
        return device.getLateFeePerDay().multiply(BigDecimal.valueOf(lateDays));
    }

    @Override
    public String toString() {
        return "BorrowItem{" +
                "deviceId='" + device.getDeviceId() + '\'' +
                ", borrowedDate=" + borrowedDate +
                ", dueDate=" + dueDate +
                ", returnedDate=" + returnedDate +
                '}';
    }
}