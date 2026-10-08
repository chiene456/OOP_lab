/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

import java.util.ArrayList;
import java.util.List;

public class LabRoom {

    private final String Ma_phong;
    private final String Ten_phong;
    private final int Suc_chua;
    private final List<Device> Danh_sach_thiet_bi;

    public LabRoom(String Ma_phong, String Ten_phong, int Suc_chua) {
        if (Ma_phong == null || Ma_phong.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phòng không được rỗng");
        }

        if (Ten_phong == null || Ten_phong.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên phòng không được rỗng");
        }

        if (Suc_chua <= 0) {
            throw new IllegalArgumentException("Sức chứa phải lớn hơn 0");
        }

        this.Ma_phong = Ma_phong.trim();
        this.Ten_phong = Ten_phong.trim();
        this.Suc_chua = Suc_chua;
        this.Danh_sach_thiet_bi = new ArrayList<>();
    }

    // Tìm thiết bị theo mã; không tìm thấy thì trả về null
    public Device FindDevice(String deviceId) {
        if (deviceId == null || deviceId.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã thiết bị không được rỗng");
        }

        String Ma_can_tim = deviceId.trim();

        for (Device device : Danh_sach_thiet_bi) {
            if (device.Get_ma_thiet_bi().trim().equals(Ma_can_tim)) {
                return device;
            }
        }
        return null;
    }

    // Thêm thiết bị vào phòng
    public void AddDevice(Device device) {
        if (device == null) {
            throw new IllegalArgumentException("Thiết bị không được null");
        }

        if (FindDevice(device.Get_ma_thiet_bi()) != null) {
            throw new IllegalArgumentException("Đã có thiết bị mang mã này trong phòng");
        }

        if (Danh_sach_thiet_bi.size() >= Suc_chua) {
            throw new IllegalStateException("Phòng đã đầy");
        }

        Danh_sach_thiet_bi.add(device);
    }

    // Xóa thiết bị theo mã
    public boolean RemoveDevice(String deviceId) {
        Device device = FindDevice(deviceId);

        if (device == null) {
            return false;
        }
        return Danh_sach_thiet_bi.remove(device);
    }

    // Tính tổng phí bằng tính đa hình
    public double CalculateAnnualMaintenanceCost() {
        double Tong_chi_phi = 0;

        for (Device device : Danh_sach_thiet_bi) {
            Tong_chi_phi += device.Tinh_chi_phi_bao_tri();
        }
        return Tong_chi_phi;
    }

    // Lọc thiết bị đang bảo trì hoặc đã sử dụng trên 5 năm
    public List<Device> GetDevicesRequiringMaintenance() {
        List<Device> Ket_qua = new ArrayList<>();

        for (Device device : Danh_sach_thiet_bi) {
            if (device.Get_trang_thai_hoat_dong() == Device.DeviceStatus.UnderMaintenance
                    || device.Get_so_nam_su_dung() > 5) {
                Ket_qua.add(device);
            }
        }
        return Ket_qua;
    }

    public String Get_ma_phong() {
        return Ma_phong;
    }

    public String Get_ten_phong() {
        return Ten_phong;
    }

    public int Get_suc_chua() {
        return Suc_chua;
    }

    public List<Device> Get_danh_sach_thiet_bi() {
        return new ArrayList<>(Danh_sach_thiet_bi);
    }

    @Override
    public String toString() {
        return String.format(
                "Mã phòng: %s - Tên phòng: %s - Sức chứa: %d - Số thiết bị: %d",
                Ma_phong, Ten_phong, Suc_chua, Danh_sach_thiet_bi.size());
    }
}