/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

import java.time.Year;

public class Computer extends Device {
    // 3 thuộc tính mới của riêng máy tính
    private int Dung_luong_ram;
    private String Loai_bo_xu_ly;
    private boolean Co_gpu_roi;

    // Hàm constructor
    public Computer(String Ma_thiet_bi, String Ten_thiet_bi, int Nam_su_dung, double Gia_mua, int Dung_luong_ram,
            String Loai_bo_xu_ly, boolean Co_gpu_roi,
            DeviceStatus Trang_thai_hoat_dong) {
        super(Ma_thiet_bi, Ten_thiet_bi, Nam_su_dung, Gia_mua, Trang_thai_hoat_dong);

        this.Dung_luong_ram = Dung_luong_ram;
        this.Loai_bo_xu_ly = Loai_bo_xu_ly;
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
        int Nam_hien_tai = Year.now().getValue();
        if ((Nam_hien_tai - this.Get_nam_su_dung()) > 5) {
            Chi_phi_bao_tri += Gia_mua * 0.01;
        }

        return Chi_phi_bao_tri;
    }
}
