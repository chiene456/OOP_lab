/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package model;

import interfaces.IBorrower;

// Trợ giảng được mượn tối đa 2 thiết bị trên một phiếu.
public class TeachingAssistant implements IBorrower {
    private static final int MAXIMUM_DEVICES = 2;
    private final String borrowerId;
    private final String fullName;

    public TeachingAssistant(String borrowerId, String fullName) {
        if (borrowerId == null || borrowerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã trợ giảng không được rỗng.");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Họ tên trợ giảng không được rỗng.");
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
        return "TeachingAssistant{" +
                "borrowerId='" + borrowerId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", maximumDevices=" + MAXIMUM_DEVICES +
                '}';
    }
}