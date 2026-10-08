/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

public class NetworkPrinter extends Printer implements INetworkable {

    private String ipAddress = null;

    public NetworkPrinter(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua, String Loai_may_in,
            int So_trang_da_in, boolean co_in_mau, DeviceStatus Trang_thai_hoat_dong) {
        super(Ma_thiet_bi, Ten_thiet_bi, Nam_su_dung, Gia_mua, Loai_may_in, So_trang_da_in, true, co_in_mau,
                Trang_thai_hoat_dong);
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public boolean isConnected() {
        return this.ipAddress != null;
    }

    @Override
    public void connect(String ipAddress) {
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ IP không được rỗng");
        }

        if (this.isConnected()) {
            throw new IllegalStateException("Máy in đang kết nối mạng");
        }

        this.ipAddress = ipAddress.trim();
    }

    @Override
    public void disconnect() {
        this.ipAddress = null;
    }

    @Override
    public String toString() {
        return super.toString() + " - Kết nối mạng: "
                + (isConnected() ? "Đang kết nối - IP: " + getIpAddress() : "Chưa kết nối");
    }
}