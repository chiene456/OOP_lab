/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package app;

import enums.DeviceStatus;
import exception.DeviceNotReadyException;
import exception.DuplicateIdException;
import exception.NoSuitableItemException;
import model.AirConditionerRemote;
import model.ProjectorRemote;
import model.WirelessMicrophone;
import repository.IRepository;
import repository.InMemoryRepository;
import service.AllocationService;
import strategy.FirstAvailableStrategy;
import strategy.HighestPriorityStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Kiểm thử tự động các quy tắc nghiệp vụ của Lab W07.
 *
 * Không sử dụng thư viện kiểm thử ngoài.
 */
public class LabW07Test {
    private static int passed;
    private static int failed;

    /**
     * Kiểm tra điều kiện và ghi nhận kết quả.
     */
    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + testName);
        } else {
            failed++;
            System.out.println("[FAIL] " + testName);
        }
    }

    /**
     * Kiểm tra một thao tác phát sinh đúng loại ngoại lệ.
     */
    private static void expectException(String testName, Class<? extends RuntimeException> expectedType,
            Runnable action) {
        try {
            action.run();
            check(testName + " - chưa phát sinh ngoại lệ", false);
        } catch (RuntimeException ex) {
            check(testName + " - " + ex.getClass().getSimpleName(), expectedType.isInstance(ex));
        }
    }

    private static ProjectorRemote projector(String id, int batteryLevel) {
        return new ProjectorRemote(id, "Điều khiển máy chiếu " + id, "Kệ A", batteryLevel);
    }

    private static WirelessMicrophone microphone(String id, int batteryLevel, int signalQuality) {
        return new WirelessMicrophone(id, "Micro " + id, "Kệ C", batteryLevel, signalQuality);
    }

    /**
     * Kiểm tra tính hợp lệ của dữ liệu khởi tạo.
     */
    private static void testValidation() {
        System.out.println("\n=== KIỂM TRA DỮ LIỆU ĐẦU VÀO ===");

        expectException("Từ chối mã thiết bị rỗng", IllegalArgumentException.class, () -> projector(" ", 50));
        expectException("Từ chối tên thiết bị null", IllegalArgumentException.class,
                () -> new ProjectorRemote("P1", null, "Kệ A", 50));
        expectException("Từ chối vị trí lưu trữ rỗng", IllegalArgumentException.class,
                () -> new ProjectorRemote("P1", "Thiết bị", " ", 50));
        expectException("Từ chối mức pin âm", IllegalArgumentException.class, () -> projector("P1", -1));
        expectException("Từ chối mức pin vượt 100", IllegalArgumentException.class, () -> projector("P1", 101));
        expectException("Từ chối tín hiệu âm", IllegalArgumentException.class, () -> microphone("M1", 50, -1));
        expectException("Từ chối tín hiệu vượt 100", IllegalArgumentException.class, () -> microphone("M1", 50, 101));
        expectException("Từ chối tập phòng null", IllegalArgumentException.class,
                () -> new AirConditionerRemote("A1", "Điều hòa", "Kệ B", null));
        expectException("Từ chối mã phòng rỗng", IllegalArgumentException.class, () -> new AirConditionerRemote("A1",
                "Điều hòa", "Kệ B", new LinkedHashSet<>(Arrays.asList("A201", " "))));
    }

    /**
     * Kiểm tra điều kiện sẵn sàng và chuyển trạng thái.
     */
    private static void testReadinessAndStatus() {
        System.out.println("\n=== KIỂM TRA SẴN SÀNG VÀ TRẠNG THÁI ===");
        check("Máy chiếu pin 19 chưa sẵn sàng", !projector("P19", 19).isReadyForUse());
        check("Máy chiếu pin 20 sẵn sàng", projector("P20", 20).isReadyForUse());
        check("Micro pin 29 chưa sẵn sàng", !microphone("M29", 29, 80).isReadyForUse());
        check("Micro tín hiệu 39 chưa sẵn sàng", !microphone("M39", 80, 39).isReadyForUse());
        check("Micro pin 30 và tín hiệu 40 sẵn sàng", microphone("M3040", 30, 40).isReadyForUse());
        check("Điểm micro giữ phần thập phân", microphone("M1", 71, 60).getDefaultPriorityScore() == 65.5);

        ProjectorRemote device = projector("P1", 80);
        check("Thiết bị mới có trạng thái AVAILABLE", device.getStatus() == DeviceStatus.AVAILABLE);
        device.borrow();
        check("Mượn chuyển trạng thái thành BORROWED", device.getStatus() == DeviceStatus.BORROWED);
        check("Thiết bị đã mượn không sẵn sàng", !device.isReadyForUse());
        expectException("Không cho mượn lại thiết bị đang mượn", DeviceNotReadyException.class, device::borrow);
        expectException("Không cho bảo trì thiết bị đang mượn", IllegalStateException.class, device::startMaintenance);
        device.returnDevice();
        check("Trả chuyển trạng thái thành AVAILABLE", device.getStatus() == DeviceStatus.AVAILABLE);
        expectException("Không cho trả thiết bị hai lần", IllegalStateException.class, device::returnDevice);
        expectException("Không cho mượn thiết bị thiếu pin", DeviceNotReadyException.class,
                () -> projector("LOW", 10).borrow());

        device.startMaintenance();
        check("Bắt đầu bảo trì chuyển đúng trạng thái", device.getStatus() == DeviceStatus.UNDER_MAINTENANCE);
        check("Thiết bị bảo trì không sẵn sàng", !device.isReadyForUse());
        expectException("Không cho mượn thiết bị đang bảo trì", DeviceNotReadyException.class, device::borrow);
        device.finishMaintenance();
        check("Kết thúc bảo trì có thể sử dụng",
                device.getStatus() == DeviceStatus.AVAILABLE && device.isReadyForUse());
        expectException("Không kết thúc bảo trì khi chưa bảo trì", IllegalStateException.class,
                device::finishMaintenance);
    }

    /**
     * Kiểm tra kho lưu trữ tổng quát.
     */
    private static void testRepository() {
        System.out.println("\n=== KIỂM TRA REPOSITORY ===");

        IRepository<ProjectorRemote> repository = new InMemoryRepository<>();

        ProjectorRemote first = projector("P1", 50);
        repository.add(first);
        check("Thêm và tìm đúng đối tượng theo mã", repository.findById("P1") == first);
        expectException("Từ chối thêm đối tượng null", NullPointerException.class, () -> repository.add(null));
        expectException("Từ chối mã thiết bị trùng", DuplicateIdException.class,
                () -> repository.add(projector("P1", 90)));
        check("Thêm mã trùng không thay đổi dữ liệu cũ",
                repository.getAll().size() == 1 && repository.findById("P1") == first);
        check("Tìm mã không tồn tại trả null", repository.findById("UNKNOWN") == null);
        check("Xóa mã không tồn tại trả false", !repository.remove("UNKNOWN"));

        List<ProjectorRemote> snapshot = repository.getAll();
        expectException("Không sửa được danh sách từ getAll", UnsupportedOperationException.class, snapshot::clear);
        repository.add(projector("P2", 10));
        check("Danh sách getAll là bản chụp độc lập", snapshot.size() == 1 && repository.getAll().size() == 2);

        List<ProjectorRemote> readyDevices = repository.findAll(ProjectorRemote::isReadyForUse);
        check("Lọc đúng thiết bị sẵn sàng", readyDevices.size() == 1 && readyDevices.get(0) == first);
        expectException("Không sửa được danh sách từ findAll", UnsupportedOperationException.class,
                readyDevices::clear);

        expectException("Từ chối điều kiện lọc null", NullPointerException.class, () -> repository.findAll(null));

        check("Xóa thiết bị tồn tại thành công", repository.remove("P2") && repository.findById("P2") == null);

        IRepository<WirelessMicrophone> microphoneRepository = new InMemoryRepository<>();
        microphoneRepository.add(microphone("M1", 80, 80));
        check("Repository dùng được cho loại thiết bị khác", microphoneRepository.findById("M1") != null);
    }

    /**
     * Kiểm tra chiến lược lựa chọn và dịch vụ cấp phát.
     */
    private static void testStrategiesAndAllocation() {
        System.out.println("\n=== KIỂM TRA CHIẾN LƯỢC VÀ CẤP PHÁT ===");

        IRepository<ProjectorRemote> repository = new InMemoryRepository<>();

        ProjectorRemote low = projector("P1", 10);
        ProjectorRemote medium = projector("P2", 60);
        ProjectorRemote high = projector("P3", 90);

        repository.add(low);
        repository.add(medium);
        repository.add(high);

        AllocationService<ProjectorRemote> service = new AllocationService<>(repository,
                new HighestPriorityStrategy<>(ProjectorRemote::getDefaultPriorityScore));

        ProjectorRemote selected = service.allocate();

        check("Chọn điều khiển có pin cao nhất", selected == high);
        check("Thiết bị cấp phát chuyển sang BORROWED", selected.getStatus() == DeviceStatus.BORROWED);

        selected.returnDevice();

        service.changeStrategy(new FirstAvailableStrategy<>());
        check("Đổi chiến lược chọn thiết bị đầu tiên sẵn sàng", service.allocate() == medium);

        expectException("Không có ứng viên thỏa điều kiện bổ sung", NoSuitableItemException.class,
                () -> service.allocate(device -> device.getBatteryLevel() > 100));

        check("Cấp phát thất bại không đổi trạng thái thiết bị khác",
                high.getStatus() == DeviceStatus.AVAILABLE && low.getStatus() == DeviceStatus.AVAILABLE);

        expectException("Chiến lược đầu tiên từ chối danh sách rỗng", NoSuitableItemException.class,
                () -> new FirstAvailableStrategy<ProjectorRemote>().select(Collections.emptyList()));

        HighestPriorityStrategy<ProjectorRemote> priority = new HighestPriorityStrategy<>(
                ProjectorRemote::getDefaultPriorityScore);

        expectException("Chiến lược ưu tiên từ chối danh sách rỗng", NoSuitableItemException.class,
                () -> priority.select(Collections.emptyList()));

        ProjectorRemote tie1 = projector("T1", 80);
        ProjectorRemote tie2 = projector("T2", 80);

        List<ProjectorRemote> candidates = new ArrayList<>(Arrays.asList(tie1, tie2));

        check("Bằng điểm chọn ứng viên đầu tiên", priority.select(candidates) == tie1);

        check("Chiến lược không sửa danh sách đầu vào",
                candidates.size() == 2 && candidates.get(0) == tie1 && candidates.get(1) == tie2);

        expectException("Từ chối thay chiến lược bằng null", NullPointerException.class,
                () -> service.changeStrategy(null));

        expectException("Từ chối hàm tính điểm null", NullPointerException.class,
                () -> new HighestPriorityStrategy<ProjectorRemote>(null));

        HighestPriorityStrategy<ProjectorRemote> invalidPriority = new HighestPriorityStrategy<>(device -> Double.NaN);

        expectException("Từ chối điểm không phải số hữu hạn", IllegalArgumentException.class,
                () -> invalidPriority.select(candidates));
    }

    /**
     * Kiểm tra tập phòng và cấp phát điều khiển điều hòa.
     */
    private static void testAirConditioners() {
        System.out.println("\n=== KIỂM TRA ĐIỀU KHIỂN ĐIỀU HÒA ===");

        Set<String> originalRooms = new LinkedHashSet<>(Arrays.asList("A201", "A203"));

        AirConditionerRemote first = new AirConditionerRemote("AC01", "Điều khiển điều hòa 1", "Kệ B", originalRooms);

        originalRooms.clear();

        check("Tập phòng được sao chép khi khởi tạo", first.isCompatibleWith("A203"));

        expectException("Không sửa được tập phòng qua getter", UnsupportedOperationException.class,
                () -> first.getCompatibleRooms().clear());

        check("Nhận diện phòng không tương thích", !first.isCompatibleWith("A999"));

        IRepository<AirConditionerRemote> repository = new InMemoryRepository<>();

        repository.add(first);

        repository.add(new AirConditionerRemote("AC02", "Điều khiển điều hòa 2", "Kệ B",
                new LinkedHashSet<>(Arrays.asList("A202", "A204"))));

        AllocationService<AirConditionerRemote> service = new AllocationService<>(repository,
                new FirstAvailableStrategy<>());

        AirConditionerRemote selected = service.allocate(device -> device.isCompatibleWith("A203"));

        check("Chọn đúng điều khiển tương thích A203", selected == first);

        check("Điều khiển điều hòa được chọn chuyển sang BORROWED", selected.getStatus() == DeviceStatus.BORROWED);

        expectException("Không cấp phát cho phòng không tương thích", NoSuitableItemException.class,
                () -> service.allocate(device -> device.isCompatibleWith("A999")));
    }

    /**
     * Kiểm tra lựa chọn micro đầu tiên sẵn sàng.
     */
    private static void testMicrophones() {
        System.out.println("\n=== KIỂM TRA MICRO KHÔNG DÂY ===");

        IRepository<WirelessMicrophone> repository = new InMemoryRepository<>();

        repository.add(microphone("MC01", 20, 80));

        WirelessMicrophone firstReady = microphone("MC02", 70, 60);

        repository.add(firstReady);
        repository.add(microphone("MC03", 90, 95));

        AllocationService<WirelessMicrophone> service = new AllocationService<>(repository,
                new FirstAvailableStrategy<>());

        WirelessMicrophone selected = service.allocate();

        check("Chọn micro đầu tiên đủ điều kiện", selected == firstReady);

        check("Micro được chọn chuyển sang BORROWED", selected.getStatus() == DeviceStatus.BORROWED);
    }

    public static void main(String[] args) {
        testValidation();
        testReadinessAndStatus();
        testRepository();
        testStrategiesAndAllocation();
        testAirConditioners();
        testMicrophones();

        System.out.println("\n=== TỔNG KẾT KIỂM THỬ ===");
        System.out.println("Số kiểm thử đạt: " + passed);
        System.out.println("Số kiểm thử không đạt: " + failed);
        System.out.println("Tổng số kiểm thử: " + (passed + failed));

        if (failed > 0) {
            throw new AssertionError("Có " + failed + " kiểm thử thất bại.");
        }
    }
}