/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package interfaces;

// Hợp đồng chung của các đối tượng được phép mượn thiết bị.
public interface IBorrower {
    String getBorrowerId();

    String getFullName();

    int getMaximumDevices();
}