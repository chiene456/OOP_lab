/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

public interface INetworkable {
    // Địa chỉ IP hiện tại; null nếu chưa kết nối / đã ngắt kết nối.
    String getIpAddress();

    // Kết nối với địa chỉ IP cho trước.
    void connect(String ipAddress);

    // Ngắt kết nối và xóa địa chỉ IP đang hoạt động.
    void disconnect();

    boolean isConnected();
}
