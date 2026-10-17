/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import enums.DeviceStatus;
import exception.BorrowLimitExceededException;
import exception.DeviceNotInSlipException;
import exception.DeviceUnavailableException;
import exception.DuplicateDeviceException;
import exception.InvalidBorrowPeriodException;
import interfaces.IBorrower;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BorrowingSlip {
    private final String slipId;
    private final IBorrower borrower;
    private final String teachingRoom;
    private final LocalDate borrowedDate;
    private final List<BorrowItem> items;

    public BorrowingSlip(String slipId, IBorrower borrower, String teachingRoom, LocalDate borrowedDate) {
        if (slipId == null || slipId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu không được rỗng.");
        }

        if (borrower == null) {
            throw new IllegalArgumentException("Người mượn không được null.");
        }

        if (borrower.getBorrowerId() == null || borrower.getBorrowerId().trim().isEmpty()
                || borrower.getFullName() == null || borrower.getFullName().trim().isEmpty()
                || borrower.getMaximumDevices() <= 0) {
            throw new IllegalArgumentException("Thông tin người mượn không hợp lệ.");
        }

        if (teachingRoom == null || teachingRoom.trim().isEmpty()) {
            throw new IllegalArgumentException("Phòng dạy không được rỗng.");
        }

        if (borrowedDate == null) {
            throw new IllegalArgumentException("Ngày mượn không được null.");
        }
        this.slipId = slipId.trim();
        this.borrower = borrower;
        this.teachingRoom = teachingRoom.trim();
        this.borrowedDate = borrowedDate;
        this.items = new ArrayList<>();
    }

    public String getSlipId() {
        return slipId;
    }

    public IBorrower getBorrower() {
        return borrower;
    }

    public String getTeachingRoom() {
        return teachingRoom;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    // Không cho bên ngoài thêm, xóa hoặc thay thế dòng phiếu.
    public List<BorrowItem> getItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public void addDevice(TeachingDevice device, int borrowDays) {
        if (device == null) {
            throw new IllegalArgumentException("Thiết bị không được null.");
        }

        if (borrowDays <= 0 || borrowDays > device.getMaximumBorrowDays()) {
            throw new InvalidBorrowPeriodException(device.getDeviceId(), borrowDays, device.getMaximumBorrowDays());
        }

        // Kiểm tra trùng mã trước khi kiểm tra trạng thái.
        for (BorrowItem item : items) {
            if (item.getDevice().getDeviceId().equals(device.getDeviceId())) {
                throw new DuplicateDeviceException(device.getDeviceId());
            }
        }

        if (items.size() >= borrower.getMaximumDevices()) {
            throw new BorrowLimitExceededException(borrower.getBorrowerId(), borrower.getMaximumDevices());
        }

        if (device.getStatus() != DeviceStatus.AVAILABLE) {
            throw new DeviceUnavailableException(device.getDeviceId(), device.getStatus());
        }

        LocalDate dueDate;

        try {
            dueDate = borrowedDate.plusDays(borrowDays);
        } catch (DateTimeException | ArithmeticException ex) {
            throw new InvalidBorrowPeriodException(device.getDeviceId(), borrowDays, device.getMaximumBorrowDays());
        }

        // Tạo dòng phiếu sau khi dữ liệu đã hợp lệ.
        BorrowItem item = new BorrowItem(device, borrowedDate, dueDate);
        device.markAsBorrowed();
        items.add(item);
    }

    public BigDecimal returnDevice(String deviceId, LocalDate returnedDate) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng.");
        }
        String normalizedId = deviceId.trim();
        for (BorrowItem item : items) {
            if (item.getDevice().getDeviceId().equals(normalizedId)) {
                // BorrowItem kiểm tra trả hai lần và ngày trả.
                item.returnItem(returnedDate);
                return item.calculateLateFee();
            }
        }
        throw new DeviceNotInSlipException(normalizedId);
    }

    // Tổng phí của các thiết bị đã trả.
    public BigDecimal calculateTotalLateFee() {
        BigDecimal total = BigDecimal.ZERO;
        for (BorrowItem item : items) {
            total = total.add(item.calculateLateFee());
        }
        return total;
    }

    public boolean hasUnreturnedDevices() {
        for (BorrowItem item : items) {
            if (!item.isReturned()) {
                return true;
            }
        }
        return false;
    }

    // Kiểm tra phiếu có thiết bị đã trả muộn.
    public boolean hasLateReturns() {
        for (BorrowItem item : items) {
            if (item.isReturned() && item.getReturnedDate().isAfter(item.getDueDate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "BorrowingSlip{" +
                "slipId='" + slipId + '\'' +
                ", borrower='" + borrower.getFullName() + '\'' +
                ", teachingRoom='" + teachingRoom + '\'' +
                ", borrowedDate=" + borrowedDate +
                ", itemCount=" + items.size() +
                '}';
    }
}