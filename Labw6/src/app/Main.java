/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package app;

import enums.DeviceStatus;
import exception.BorrowingException;
import interfaces.IBorrower;
import model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            runDemo();
        } catch (BorrowingException ex) {
            System.out.println("Lỗi nghiệp vụ: " + ex.getMessage());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            System.out.println("Lỗi dữ liệu hoặc trạng thái: " + ex.getMessage());
        } finally {
            System.out.println("\nKết thúc chương trình.");
        }
    }

    private static void runDemo() {
        LocalDate borrowedDate = LocalDate.of(2026, 10, 17);
        Microphone microphone = new Microphone("MC01", "Micro không dây", 2024, new BigDecimal("500000"),
                DeviceStatus.AVAILABLE, 1, new BigDecimal("10000"), true);
        ProjectorRemote projectorRemote = new ProjectorRemote("PR01", "Điều khiển máy chiếu", 2020,
                new BigDecimal("300000"), DeviceStatus.AVAILABLE, 2, new BigDecimal("5000"));
        AirConditionerRemote airRemote = new AirConditionerRemote("AC01", "Điều khiển điều hòa", 2025,
                new BigDecimal("200000"), DeviceStatus.AVAILABLE, 1, new BigDecimal("5000"));
        ClassRoom room = new ClassRoom("P101", "Phòng học 101", 50);

        room.addDevice(microphone);
        room.addDevice(projectorRemote);
        room.addDevice(airRemote);

        System.out.println("=== THÔNG TIN PHÒNG HỌC ===");
        System.out.println(room);

        for (Device device : room.getDevices()) {
            System.out.println(device);
        }

        System.out.println("Tổng chi phí bảo trì năm: " + room.calculateAnnualMaintenanceCost().toPlainString());

        System.out.println("\n=== THIẾT BỊ CẦN BẢO TRÌ ===");

        for (Device device : room.getDevicesRequiringMaintenance()) {
            System.out.println(device);
        }

        IBorrower lecturer = new Lecturer("GV01", "Nguyễn Văn An");
        IBorrower assistant = new TeachingAssistant("TG01", "Trần Thị Bình");

        BorrowingSlip slip1 = new BorrowingSlip("PM01", lecturer, room.getRoomId(), borrowedDate);

        slip1.addDevice(projectorRemote, 2);
        slip1.addDevice(microphone, 1);

        System.out.println("\n=== MƯỢN VÀ TRẢ THIẾT BỊ ===");

        // Hạn trả điều khiển máy chiếu: 19/10/2026.
        BigDecimal projectorFee = slip1.returnDevice("PR01", borrowedDate.plusDays(2));
        System.out.println("Trả điều khiển máy chiếu đúng hạn, phí: " + projectorFee.toPlainString());

        // Hạn trả micro: 18/10/2026; trả ngày 20/10, muộn 2 ngày.
        BigDecimal microphoneFee = slip1.returnDevice("MC01", borrowedDate.plusDays(3));
        System.out.println("Trả micro muộn 2 ngày, phí: " + microphoneFee.toPlainString());
        System.out.println("Tổng phí phiếu PM01: " + slip1.calculateTotalLateFee().toPlainString());

        BorrowingSlip slip2 = new BorrowingSlip("PM02", assistant, room.getRoomId(), borrowedDate);

        slip2.addDevice(airRemote, 1);

        List<BorrowingSlip> slips = new ArrayList<>();
        slips.add(slip1);
        slips.add(slip2);

        System.out.println("\n=== PHIẾU CÓ THIẾT BỊ ĐÃ TRẢ MUỘN ===");

        for (BorrowingSlip slip : slips) {
            if (slip.hasLateReturns()) {
                System.out.println(slip);
            }
        }

        System.out.println("\n=== PHIẾU CÓ THIẾT BỊ CHƯA TRẢ ===");

        for (BorrowingSlip slip : slips) {
            if (slip.hasUnreturnedDevices()) {
                System.out.println(slip);
                for (BorrowItem item : slip.getItems()) {
                    if (!item.isReturned()) {
                        System.out.println("  " + item);
                    }
                }
            }
        }

        System.out.println("\n=== TÌM VÀ XÓA THIẾT BỊ KHỎI PHÒNG ===");
        System.out.println("Tìm MC01: " + room.findDevice("MC01"));
        System.out.println("Xóa MC01: " + room.removeDevice("MC01"));
        System.out.println("Tìm MC01 sau khi xóa: " + room.findDevice("MC01"));
        // Đối tượng vẫn tồn tại độc lập sau khi bị loại khỏi phòng.
        System.out.println("Micro vẫn tồn tại: " + microphone);
    }
}