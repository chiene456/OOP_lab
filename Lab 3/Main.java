/*
Họ và tên: Nguyễn Quang Chiến
MSSV: 202418853
 */

import java.io.PrintStream;
import java.io.IOException;
import java.time.Year;

public class Main {

public static void main(String[] args) throws IOException {
    PrintStream Man_hinh = System.out;

    try (PrintStream File_out = new PrintStream("out.txt", "UTF-8")) {
        System.setOut(File_out);
        int Nam_hien_tai = Year.now().getValue();

        // 1. Tạo hai máy tính, một máy có GPU rời
        Device d1 = new Computer("C001", "Máy tính 1", Nam_hien_tai - 6, 10000000, 16, "Intel Core i5", false, Device.DeviceStatus.Active);
        Device d2 = new Computer("C002", "Máy tính 2", Nam_hien_tai - 2, 12000000, 32, "Intel Core i7", true, Device.DeviceStatus.Active);

        // 2. Tạo hai máy in, một máy đã in trên 100.000 trang
        Device d3 = new Printer("PR001", "Máy in thường", Nam_hien_tai - 3, 4000000,"INKJET", 80000, false, true, Device.DeviceStatus.UnderMaintenance);
        Device d4 = new NetworkPrinter("NP001", "Máy in mạng", Nam_hien_tai - 2, 8000000,"LASER", 150000, true, Device.DeviceStatus.Active);

        // 3. Máy chiếu có bóng đèn đã sử dụng trên 3.000 giờ
        Device d5 = new Projector("PJ001", "Máy chiếu 1", Nam_hien_tai - 7, 5000000, 3500, 3501, Device.DeviceStatus.Active);

        // 4. Tạo hai phòng thực hành
        LabRoom lab1 = new LabRoom("P001", "Phòng máy 1", 30);
        LabRoom lab2 = new LabRoom("P002", "Phòng máy 2", 20);

        // 5. Phân bổ thiết bị vào phòng
        lab1.AddDevice(d1);
        lab1.AddDevice(d3);
        lab1.AddDevice(d5);
        
        lab2.AddDevice(d2);
        lab2.AddDevice(d4);

        // 6. Thử thêm một đối tượng khác có mã trùng C001
        System.out.println("KIỂM TRA MÃ TRÙNG");

        Device Thiet_bi_trung_ma = new Computer("C001", "Máy tính trùng mã", Nam_hien_tai, 15000000, 16, "AMD Ryzen 5", false, Device.DeviceStatus.Active);

        try {
            lab1.AddDevice(Thiet_bi_trung_ma);
            System.out.println("FAIL: Phòng đã chấp nhận mã trùng");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 7. In thông tin, danh sách và phí bảo trì của mỗi phòng
        In_thong_tin_phong(lab1);
        In_thong_tin_phong(lab2);

        // Đối chiếu kết quả tính phí với dữ liệu trên
        System.out.println("\nĐỐI CHIẾU CHI PHÍ");
        Kiem_tra_chi_phi(lab1, 2710000);
        Kiem_tra_chi_phi(lab2, 1960000);

        // 8. Duyệt và kết nối các thiết bị qua kiểu INetworkable
        System.out.println("\nKIỂM TRA KẾT NỐI MẠNG");

        Device[] Tat_ca_thiet_bi = {d1, d2, d3, d4, d5};
        int So_cuoi_ip = 10;

        for (Device device : Tat_ca_thiet_bi) {
            if (device instanceof INetworkable) {
                INetworkable Thiet_bi_mang = (INetworkable) device;

                System.out.println("\nThiết bị: " + device.Get_ma_thiet_bi());

                System.out.println("Ban đầu chưa kết nối: " + !Thiet_bi_mang.isConnected());

                // Thử IP rỗng
                try {
                    Thiet_bi_mang.connect("   ");
                    System.out.println("FAIL: Đã chấp nhận IP rỗng");
                } catch (IllegalArgumentException e) {
                    System.out.println("PASS: " + e.getMessage());
                }

                // Kết nối hợp lệ
                String ip = "192.168.1." + So_cuoi_ip++;
                Thiet_bi_mang.connect(ip);

                System.out.println("Đang kết nối: " + Thiet_bi_mang.isConnected());
                System.out.println("IP hiện tại: " + Thiet_bi_mang.getIpAddress());

                // Thử kết nối lại khi đang kết nối
                try {
                    Thiet_bi_mang.connect("192.168.1.200");
                    System.out.println("FAIL: Đã cho phép kết nối lại");
                } catch (IllegalStateException e) {
                    System.out.println("PASS: " + e.getMessage());
                }

                // Ngắt kết nối và kiểm tra cả hai điều kiện
                Thiet_bi_mang.disconnect();

                boolean Ngat_thanh_cong = !Thiet_bi_mang.isConnected() && Thiet_bi_mang.getIpAddress() == null;

                System.out.println((Ngat_thanh_cong ? "PASS" : "FAIL") + ": Ngắt kết nối và xóa IP");

                // Kiểm tra có thể kết nối lại sau khi ngắt
                Thiet_bi_mang.connect(ip);
                System.out.println("Kết nối lại sau khi ngắt: " + Thiet_bi_mang.isConnected());
                Thiet_bi_mang.disconnect();
            }
        }

        // 9. Kiểm tra thêm thiết bị null
        System.out.println("\nKIỂM TRA THIẾT BỊ NULL");

        try {
            lab1.AddDevice(null);
            System.out.println("FAIL: Đã chấp nhận thiết bị null");
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + e.getMessage());
        }

        // 10. Kiểm tra tìm và xóa thiết bị
        System.out.println("\nKIỂM TRA TÌM VÀ XÓA");

        System.out.println("Tìm được C001: " + (lab1.FindDevice("C001") == d1));

        System.out.println("Mã không tồn tại trả về null: " + (lab1.FindDevice("KHONG_CO") == null));

        System.out.println("Xóa C001 thành công: " + lab1.RemoveDevice("C001"));

        System.out.println("C001 không còn trong phòng: " + (lab1.FindDevice("C001") == null));

        System.out.println("Xóa lại C001 trả về false: " + !lab1.RemoveDevice("C001"));

        // Đối tượng vẫn tồn tại sau khi bị xóa khỏi phòng
        System.out.println("Thiết bị vẫn tồn tại: " + d1);

        // Thêm lại để khôi phục danh sách ban đầu
        lab1.AddDevice(d1);
    }
    // Khôi phục đầu ra về console và thông báo
    System.setOut(Man_hinh);
    System.out.println("Kết quả đã được ghi vào file out.txt");
}

    private static void In_thong_tin_phong(LabRoom lab) {
        System.out.println("\n" + lab.Get_ten_phong());
        System.out.println(lab);

        System.out.println("\nDanh sách thiết bị:");
        for (Device device : lab.Get_danh_sach_thiet_bi()) {
            System.out.println(device);
        }

        System.out.printf("\nTổng chi phí bảo trì: %,.0f đồng%n", lab.CalculateAnnualMaintenanceCost());

        System.out.println("\nThiết bị cần bảo trì:");
        for (Device device : lab.GetDevicesRequiringMaintenance()) {
            System.out.println(device);
        }
    }

    private static void Kiem_tra_chi_phi(LabRoom lab, double Chi_phi_mong_doi) {

        double Chi_phi_thuc_te = lab.CalculateAnnualMaintenanceCost();

        boolean Dung = Math.abs(Chi_phi_thuc_te - Chi_phi_mong_doi) < 0.01;

        System.out.printf("%s: %s — mong đợi %,.0f, thực tế %,.0f đồng%n", lab.Get_ma_phong(), Dung ? "PASS" : "FAIL", Chi_phi_mong_doi, Chi_phi_thuc_te);
    }
}


       