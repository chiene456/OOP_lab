/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package app;

import enums.DeviceStatus;
import exception.*;
import model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BorrowingValidationTest {
    private static int passed = 0;
    private static final LocalDate BORROWED_DATE = LocalDate.of(2026, 10, 17);

    private static void check(String description, boolean condition) {
        if (!condition) {
            throw new AssertionError("FAIL: " + description);
        }
        passed++;
        System.out.println("PASS: " + description);
    }

    private static Microphone microphone(String id, DeviceStatus status) {
        return new Microphone(id, "Micro " + id, Year.now().getValue(),
                new BigDecimal("500000"), status, 1, new BigDecimal("10000"), true);
    }

    private static BorrowingSlip lecturerSlip(String id) {
        return new BorrowingSlip(id, new Lecturer("GV01", "Nguyễn Văn An"), "P101", BORROWED_DATE);
    }

    private static void expectFailure(String description, Class<? extends RuntimeException> expectedType,
            Runnable action, BorrowingSlip slip, TeachingDevice... relatedDevices) {

        List<BorrowItem> beforeItems = new ArrayList<>(slip.getItems());
        Map<Device, DeviceStatus> beforeStatuses = new IdentityHashMap<>();
        Map<BorrowItem, LocalDate> beforeReturnDates = new IdentityHashMap<>();

        for (BorrowItem item : beforeItems) {
            beforeStatuses.put(item.getDevice(), item.getDevice().getStatus());
            beforeReturnDates.put(item, item.getReturnedDate());
        }

        for (TeachingDevice device : relatedDevices) {
            beforeStatuses.put(device, device.getStatus());
        }

        boolean thrown = false;

        try {
            action.run();
        } catch (RuntimeException ex) {
            if (!expectedType.equals(ex.getClass())) {
                throw new AssertionError("FAIL: " + description + "; mong đợi " + expectedType.getSimpleName()
                        + ", thực tế " + ex.getClass().getSimpleName(), ex);
            }
            thrown = true;
        }

        check(description + " — đúng ngoại lệ", thrown);

        boolean unchanged = beforeItems.equals(slip.getItems());

        for (Map.Entry<Device, DeviceStatus> entry : beforeStatuses.entrySet()) {
            unchanged &= entry.getKey().getStatus() == entry.getValue();
        }

        for (Map.Entry<BorrowItem, LocalDate> entry : beforeReturnDates.entrySet()) {
            unchanged &= Objects.equals(entry.getKey().getReturnedDate(), entry.getValue());
        }

        check(description + " — dữ liệu giữ nguyên", unchanged);
    }

    public static void main(String[] args) {
        testSuccessfulBorrowing();
        testBorrowingFailures();
        testReturnFailures();
        testClassRoom();
        System.out.println("\nHoàn thành " + passed + " phép kiểm tra.");
    }

    private static void testSuccessfulBorrowing() {
        System.out.println("\n=== LUỒNG THÀNH CÔNG ===");

        BorrowingSlip slip = lecturerSlip("PM01");
        Microphone first = microphone("MC01", DeviceStatus.AVAILABLE);
        Microphone second = microphone("MC02", DeviceStatus.AVAILABLE);

        slip.addDevice(first, 1);
        slip.addDevice(second, 1);

        check("Thêm đủ hai thiết bị", slip.getItems().size() == 2);
        check("Thiết bị chuyển sang BORROWED",
                first.getStatus() == DeviceStatus.BORROWED && second.getStatus() == DeviceStatus.BORROWED);
        check("Ngày trả ban đầu là null", slip.getItems().get(0).getReturnedDate() == null);
        check("Hạn trả được tính đúng", slip.getItems().get(0).getDueDate().equals(BORROWED_DATE.plusDays(1)));
        BigDecimal onTimeFee = slip.returnDevice("MC01", BORROWED_DATE.plusDays(1));
        check("Trả đúng hạn có phí bằng 0", onTimeFee.compareTo(BigDecimal.ZERO) == 0);
        check("Thiết bị trả xong về AVAILABLE", first.getStatus() == DeviceStatus.AVAILABLE);
        check("Phiếu vẫn còn thiết bị chưa trả", slip.hasUnreturnedDevices());

        BigDecimal lateFee = slip.returnDevice("MC02", BORROWED_DATE.plusDays(3));
        check("Trả muộn hai ngày có phí 20000", lateFee.compareTo(new BigDecimal("20000")) == 0);
        check("Tổng phí phiếu bằng 20000", slip.calculateTotalLateFee().compareTo(new BigDecimal("20000")) == 0);
        check("Phiếu ghi nhận trả muộn", slip.hasLateReturns());

        check("Không còn thiết bị chưa trả", !slip.hasUnreturnedDevices());
    }

    private static void testBorrowingFailures() {
        System.out.println("\n=== LỖI KHI MƯỢN ===");

        BorrowingSlip slip = lecturerSlip("PM02");
        Microphone maintenance = microphone("MC03", DeviceStatus.UNDER_MAINTENANCE);
        expectFailure("Mượn thiết bị đang bảo trì", DeviceUnavailableException.class,
                () -> slip.addDevice(maintenance, 1),
                slip, maintenance);

        Microphone available = microphone("MC04", DeviceStatus.AVAILABLE);

        expectFailure("Mượn quá giới hạn ngày", InvalidBorrowPeriodException.class, () -> slip.addDevice(available, 3),
                slip, available);

        expectFailure("Số ngày mượn bằng 0", InvalidBorrowPeriodException.class, () -> slip.addDevice(available, 0),
                slip, available);

        expectFailure("Số ngày mượn âm", InvalidBorrowPeriodException.class, () -> slip.addDevice(available, -1),
                slip, available);

        expectFailure("Thiết bị null", IllegalArgumentException.class, () -> slip.addDevice(null, 1), slip);

        slip.addDevice(available, 1);

        expectFailure("Thêm cùng thiết bị hai lần", DuplicateDeviceException.class, () -> slip.addDevice(available, 1),
                slip, available);

        Microphone sameId = microphone("MC04", DeviceStatus.AVAILABLE);

        expectFailure("Thêm đối tượng khác nhưng trùng mã", DuplicateDeviceException.class,
                () -> slip.addDevice(sameId, 1), slip, sameId);

        BorrowingSlip assistantSlip = new BorrowingSlip("PM03", new TeachingAssistant("TG01", "Trần Thị Bình"), "P101",
                BORROWED_DATE);

        assistantSlip.addDevice(microphone("MC05", DeviceStatus.AVAILABLE), 1);

        assistantSlip.addDevice(microphone("MC06", DeviceStatus.AVAILABLE), 1);

        Microphone third = microphone("MC07", DeviceStatus.AVAILABLE);

        expectFailure("Trợ giảng mượn thiết bị thứ ba", BorrowLimitExceededException.class,
                () -> assistantSlip.addDevice(third, 1), assistantSlip, third);
    }

    private static void testReturnFailures() {
        System.out.println("\n=== LỖI KHI TRẢ ===");

        BorrowingSlip slip = lecturerSlip("PM04");

        Microphone device = microphone("MC08", DeviceStatus.AVAILABLE);

        slip.addDevice(device, 1);

        expectFailure("Trả thiết bị không thuộc phiếu", DeviceNotInSlipException.class,
                () -> slip.returnDevice("KHONG_CO", BORROWED_DATE), slip, device);

        expectFailure("Ngày trả trước ngày mượn", InvalidReturnDateException.class,
                () -> slip.returnDevice("MC08", BORROWED_DATE.minusDays(1)), slip, device);

        expectFailure("Ngày trả null", InvalidReturnDateException.class, () -> slip.returnDevice("MC08", null), slip,
                device);

        slip.returnDevice("MC08", BORROWED_DATE.plusDays(1));

        expectFailure("Trả cùng thiết bị hai lần", DeviceAlreadyReturnedException.class,
                () -> slip.returnDevice("MC08", BORROWED_DATE.plusDays(2)), slip, device);
    }

    private static void testClassRoom() {
        System.out.println("\n=== QUẢN LÝ PHÒNG ===");

        int year = Year.now().getValue();
        BigDecimal price = new BigDecimal("500000");
        BigDecimal fee = new BigDecimal("10000");

        ClassRoom room = new ClassRoom("P101", "Phòng 101", 50);

        Microphone exactlyFiveYears = new Microphone("MC09", "Micro 5 năm", year - 5, price, DeviceStatus.AVAILABLE, 1,
                fee, true);

        ProjectorRemote oldDevice = new ProjectorRemote("PR01", "Điều khiển cũ", year - 6, price,
                DeviceStatus.AVAILABLE, 1, fee);

        AirConditionerRemote maintenance = new AirConditionerRemote("AC01", "Điều khiển đang bảo trì", year, price,
                DeviceStatus.UNDER_MAINTENANCE, 1, fee);

        room.addDevice(exactlyFiveYears);
        room.addDevice(oldDevice);
        room.addDevice(maintenance);

        check("Phòng có ba thiết bị", room.getDevices().size() == 3);

        check("Tìm thiết bị theo mã", room.findDevice("MC09") == exactlyFiveYears);

        check("Tìm mã không có trả null", room.findDevice("KHONG_CO") == null);

        check("Tính bảo trì bằng đa hình",
                room.calculateAnnualMaintenanceCost().compareTo(new BigDecimal("50000")) == 0);

        List<Device> requiringMaintenance = room.getDevicesRequiringMaintenance();

        check("Đúng 5 năm chưa thuộc nhóm thiết bị cũ", !requiringMaintenance.contains(exactlyFiveYears));

        check("Trên 5 năm cần bảo trì", requiringMaintenance.contains(oldDevice));

        check("Đang bảo trì được đưa vào danh sách", requiringMaintenance.contains(maintenance));

        boolean duplicateRejected = false;

        try {
            room.addDevice(exactlyFiveYears);
        } catch (IllegalArgumentException ex) {
            duplicateRejected = true;
        }

        check("Không thêm thiết bị trùng mã vào phòng", duplicateRejected && room.getDevices().size() == 3);

        boolean nullRejected = false;

        try {
            room.addDevice(null);
        } catch (IllegalArgumentException ex) {
            nullRejected = true;
        }

        check("Không thêm thiết bị null vào phòng", nullRejected && room.getDevices().size() == 3);

        boolean listProtected = false;

        try {
            room.getDevices().clear();
        } catch (UnsupportedOperationException ex) {
            listProtected = true;
        }

        check("Không sửa trực tiếp danh sách bên ngoài", listProtected && room.getDevices().size() == 3);

        check("Xóa thiết bị thành công", room.removeDevice("MC09"));

        check("Thiết bị không còn trong phòng", room.findDevice("MC09") == null);

        check("Xóa mã không tồn tại trả false", !room.removeDevice("MC09"));

        check("Xóa khỏi phòng không đổi trạng thái thiết bị", exactlyFiveYears.getStatus() == DeviceStatus.AVAILABLE);
    }
}