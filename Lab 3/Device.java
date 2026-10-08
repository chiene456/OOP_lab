/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

import java.time.Year;

public abstract class Device {

    public enum DeviceStatus {
        Active,
        UnderMaintenance,
        Retired
    }

    // Mã thiết bị chỉ được thiết lập khi khởi tạo, không thể thay đổi sau đó.
    private final String Ma_thiet_bi;
    private String Ten_thiet_bi;
    private int Nam_su_dung;
    private double Gia_mua;
    private DeviceStatus Trang_thai_hoat_dong;

    // Hàm constructor
    public Device(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua,
            DeviceStatus Trang_thai_hoat_dong) {

        // Kiểm tra mã thiết bị không được rỗng
        if (Ma_thiet_bi == null || Ma_thiet_bi.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng");
        }
        // Kiểm tra giá mua phải > 0
        if (Gia_mua <= 0) {
            throw new IllegalArgumentException("Giá mua phải lớn hơn 0");
        }
        // Năm sử dụng không được lớn hơn năm hiện tại
        int Nam_hien_tai = Year.now().getValue();
        if (Nam_su_dung > Nam_hien_tai) {
            throw new IllegalArgumentException("Năm sử dụng không được lớn hơn năm hiện tại");
        }

        this.Ma_thiet_bi = Ma_thiet_bi;
        this.Ten_thiet_bi = Ten_thiet_bi;
        this.Nam_su_dung = Nam_su_dung;
        this.Gia_mua = Gia_mua;
        this.Trang_thai_hoat_dong = Trang_thai_hoat_dong;
    }

    // Hàm getter và setter
    public String Get_ma_thiet_bi() {
        return Ma_thiet_bi;
    }

    public String Get_ten_thiet_bi() {
        return Ten_thiet_bi;
    }

    public void Set_ten_thiet_bi(String Ten_thiet_bi) {
        if (Ten_thiet_bi == null || Ten_thiet_bi.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên thiết bị không được rỗng");
        }
        this.Ten_thiet_bi = Ten_thiet_bi.trim();
    }

    public int Get_nam_su_dung() {
        return Nam_su_dung;
    }

    public void Set_nam_su_dung(int Nam_su_dung) {
        int Nam_hien_tai = Year.now().getValue();

        if (Nam_su_dung <= 0 || Nam_su_dung > Nam_hien_tai) {
            throw new IllegalArgumentException("Năm sử dụng không hợp lệ");
        }
        this.Nam_su_dung = Nam_su_dung;
    }

    public int Get_so_nam_su_dung() {
        return Year.now().getValue() - this.Nam_su_dung;
    }

    public double Get_gia_mua() {
        return Gia_mua;
    }

    public void Set_gia_mua(double Gia_mua) {
        if (!Double.isFinite(Gia_mua) || Gia_mua <= 0) {
            throw new IllegalArgumentException("Giá mua phải là số hữu hạn lớn hơn 0");
        }
        this.Gia_mua = Gia_mua;
    }

    public DeviceStatus Get_trang_thai_hoat_dong() {
        return Trang_thai_hoat_dong;
    }

    public void Set_trang_thai_hoat_dong(DeviceStatus Trang_thai_hoat_dong) {
        if (Trang_thai_hoat_dong == null) {
            throw new IllegalArgumentException("Trạng thái hoạt động không được null");
        }
        this.Trang_thai_hoat_dong = Trang_thai_hoat_dong;
    }

    // Ghi đè phương thức in thông tin
    @Override
    public String toString() {
        String format = "Mã thiết bị: %s - Tên thiết bị: %s - Năm sử dụng: %d - Giá mua: %.2f - Trạng thái: %s";
        return String.format(format, this.Ma_thiet_bi, this.Ten_thiet_bi, this.Nam_su_dung, this.Gia_mua,
                this.Trang_thai_hoat_dong);
    }

    // Phương thức trừu tượng
    public abstract double Tinh_chi_phi_bao_tri();
}