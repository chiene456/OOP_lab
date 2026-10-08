/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

public class Printer extends Device {
    public enum Printer_type {
        LASER,
        INKJET
    }

    // 3 thuộc tính mới của riêng máy in
    private Printer_type Loai_may_in;
    private int So_trang_da_in;
    private boolean co_ho_tro_mang;
    private boolean co_in_mau;

    // Hàm constructor
    public Printer(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua, String Loai_may_in,
            int So_trang_da_in, boolean co_ho_tro_mang, boolean co_in_mau, DeviceStatus Trang_thai_hoat_dong) {
        super(Ma_thiet_bi, Ten_thiet_bi, Nam_su_dung, Gia_mua, Trang_thai_hoat_dong);

        this.Loai_may_in = Printer_type.valueOf(Loai_may_in);
        this.So_trang_da_in = So_trang_da_in;
        this.co_ho_tro_mang = co_ho_tro_mang;
        this.co_in_mau = co_in_mau;
    }

    @Override
    public double Tinh_chi_phi_bao_tri() {
        // Phí cơ bản 4%
        double chi_phi_bao_tri = this.Get_gia_mua() * 0.04;
        // Điều kiện 1:
        if (this.So_trang_da_in > 100000) {
            chi_phi_bao_tri += 500000;
        }
        // Điều kiện 2:
        if (this.co_in_mau) {
            chi_phi_bao_tri += 300000;
        }
        return chi_phi_bao_tri;
    }
}
