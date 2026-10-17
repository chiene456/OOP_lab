/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import interfaces.IBorrower;

// Giảng viên được mượn tối đa 3 thiết bị trên một phiếu.
public class Lecturer implements IBorrower {
    private static final int MAXIMUM_DEVICES = 3;
    private final String borrowerId;
    private final String fullName;

    public Lecturer(String borrowerId, String fullName) {
        if (borrowerId == null || borrowerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã giảng viên không được rỗng.");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên giảng viên không được rỗng.");
        }
        this.borrowerId = borrowerId.trim();
        this.fullName = fullName.trim();
    }

    @Override
    public String getBorrowerId() {
        return borrowerId;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public int getMaximumDevices() {
        return MAXIMUM_DEVICES;
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "borrowerId='" + borrowerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", maximumDevices=" + MAXIMUM_DEVICES +
                '}';
    }
}