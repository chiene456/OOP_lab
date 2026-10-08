/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

public class Projector extends Device {
    // 2 thuộc tính của máy chiếu
    private int Do_sang;
    private int So_gio_su_dung_bong_den;

    // Hàm constructor
    public Projector(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua, int Do_sang,
            int So_gio_su_dung_bong_den,
            DeviceStatus Trang_thai_hoat_dong) {
        super(Ma_thiet_bi, Ten_thiet_bi, Nam_su_dung, Gia_mua, Trang_thai_hoat_dong);
        this.Do_sang = Do_sang;
        this.So_gio_su_dung_bong_den = So_gio_su_dung_bong_den;
    }

    @Override
    public double Tinh_chi_phi_bao_tri() {
        double chi_phi_bao_tri = this.Get_gia_mua() * 0.03;
        if (this.So_gio_su_dung_bong_den > 3000) {
            chi_phi_bao_tri += 1500000;
        }
        return chi_phi_bao_tri;
    }
}
