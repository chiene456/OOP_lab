public class Projector extends Device {
    // Hàm constructor
    public Projector(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua,
            DeviceStatus Trang_thai_hoat_dong) {
        super(Ma_thiet_bi, Ten_thiet_bi, Nam_su_dung, Gia_mua, Trang_thai_hoat_dong);
    }

    @Override
    public double Tinh_chi_phi_bao_tri() {
        return this.Get_gia_mua() * 0.07;
    }
}
