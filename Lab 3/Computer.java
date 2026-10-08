/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

public class Computer extends Device implements INetworkable {
    // 3 thuộc tính mới của riêng máy tính
    private int Dung_luong_ram;
    private String Loai_bo_xu_ly;
    private boolean Co_gpu_roi;
    private String ipAddress = null;

    // Hàm constructor
    public Computer(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua, int Dung_luong_ram,
            String Loai_bo_xu_ly, boolean Co_gpu_roi,
            DeviceStatus Trang_thai_hoat_dong) {
        super(Ma_thiet_bi, Ten_thiet_bi, Nam_su_dung, Gia_mua, Trang_thai_hoat_dong);
        if (Dung_luong_ram <= 0) {
            throw new IllegalArgumentException("Dung lượng RAM phải lớn hơn 0");
        }
        if (Loai_bo_xu_ly == null || Loai_bo_xu_ly.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại bộ xử lý không được rỗng");
        }

        this.Dung_luong_ram = Dung_luong_ram;
        this.Loai_bo_xu_ly = Loai_bo_xu_ly.trim();
        this.Co_gpu_roi = Co_gpu_roi;
    }

    @Override
    public double Tinh_chi_phi_bao_tri() {
        // Lấy giá mua từ lớp cha (phải dùng hàm getter)
        double Gia_mua = this.Get_gia_mua();

        // Mức phí cơ bản là 5% giá mua
        double Chi_phi_bao_tri = Gia_mua * 0.05;

        // Kiểm tra điều kiện 1: Có GPU rời không?
        if (this.Co_gpu_roi) {
            Chi_phi_bao_tri += Gia_mua * 0.02;
        }

        // Kiểm tra điều kiện 2: Thời gian sử dụng trên 5 năm không?
        if (this.Get_so_nam_su_dung() > 5) {
            Chi_phi_bao_tri += Gia_mua * 0.01;
        }

        return Chi_phi_bao_tri;
    }

    @Override
    public String toString() {
        return super.toString()
                + String.format(" - RAM: %d GB - CPU: %s - GPU rời: %s",
                        this.Dung_luong_ram,
                        this.Loai_bo_xu_ly,
                        this.Co_gpu_roi ? "Có" : "Không");
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
        // Kiểm tra địa chỉ IP
        if (ipAddress == null || ipAddress.trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ IP không được rỗng");
        }
        // Kiểm tra thiết bị đã kết nối chưa
        if (this.isConnected()) {
            throw new IllegalStateException("Thiết bị đang kết nối mạng");
        }
        this.ipAddress = ipAddress.trim();
    }

    @Override
    public void disconnect() {
        this.ipAddress = null;
    }
}
