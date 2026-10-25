/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package app;

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

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {

    /**
     * Tạo tập mã phòng theo thứ tự truyền vào.
     */
    private static Set<String> rooms(String... roomIds) {
        return new LinkedHashSet<>(Arrays.asList(roomIds));
    }

    public static void main(String[] args) {
        IRepository<ProjectorRemote> projectorRepository = new InMemoryRepository<>();

        IRepository<AirConditionerRemote> airRepository = new InMemoryRepository<>();

        IRepository<WirelessMicrophone> microphoneRepository = new InMemoryRepository<>();

        // Khởi tạo ba điều khiển máy chiếu có mức pin khác nhau.
        projectorRepository.add(new ProjectorRemote("PR01", "Điều khiển máy chiếu 1", "Kệ A", 15));

        projectorRepository.add(new ProjectorRemote("PR02", "Điều khiển máy chiếu 2", "Kệ A", 60));

        projectorRepository.add(new ProjectorRemote("PR03", "Điều khiển máy chiếu 3", "Kệ A", 90));

        // Khởi tạo hai điều khiển điều hòa.
        airRepository.add(new AirConditionerRemote("AC01", "Điều khiển điều hòa 1", "Kệ B", rooms("A201", "A203")));

        airRepository.add(new AirConditionerRemote("AC02", "Điều khiển điều hòa 2", "Kệ B", rooms("A202", "A204")));

        // Khởi tạo ba micro có pin và chất lượng tín hiệu khác nhau.
        microphoneRepository.add(new WirelessMicrophone("MC01", "Micro không dây 1", "Kệ C", 20, 80));

        microphoneRepository.add(new WirelessMicrophone("MC02", "Micro không dây 2", "Kệ C", 70, 60));

        microphoneRepository.add(new WirelessMicrophone("MC03", "Micro không dây 3", "Kệ C", 90, 95));

        System.out.println("=== 1. DANH SÁCH THIẾT BỊ ===");

        projectorRepository.getAll().forEach(System.out::println);

        airRepository.getAll().forEach(System.out::println);

        microphoneRepository.getAll().forEach(System.out::println);

        System.out.println("\n=== 2. THÊM THIẾT BỊ TRÙNG MÃ ===");

        try {
            projectorRepository.add(new ProjectorRemote("PR01", "Điều khiển trùng mã", "Kệ A", 100));
        } catch (DuplicateIdException ex) {
            System.out.println("Đã xử lý lỗi: " + ex.getMessage());
        }

        System.out.println("\n=== 3. TÌM THIẾT BỊ THEO MÃ ===");
        System.out.println("Tìm PR02:");
        System.out.println(projectorRepository.findById("PR02"));

        System.out.println("Tìm mã không tồn tại:");
        System.out.println(projectorRepository.findById("PR99"));

        System.out.println("\n=== 4. ĐIỀU KHIỂN MÁY CHIẾU SẴN SÀNG ===");

        projectorRepository.findAll(ProjectorRemote::isReadyForUse).forEach(System.out::println);

        AllocationService<ProjectorRemote> projectorService = new AllocationService<>(projectorRepository,
                new HighestPriorityStrategy<>(ProjectorRemote::getDefaultPriorityScore));

        System.out.println("\n=== 5. CHỌN MÁY CHIẾU CÓ PIN CAO NHẤT ===");

        ProjectorRemote selectedProjector = projectorService.allocate();

        System.out.println("Thiết bị được chọn:");
        System.out.println(selectedProjector);

        System.out.println("Trạng thái sau cấp phát: " + selectedProjector.getStatus());

        System.out.println("\n=== 6. TRẢ THIẾT BỊ VỀ KHO ===");

        selectedProjector.returnDevice();

        System.out.println("Trạng thái sau khi trả: " + selectedProjector.getStatus());

        System.out.println("\n=== 7. ĐỔI CHIẾN LƯỢC TRONG THỜI GIAN CHẠY ===");

        projectorService.changeStrategy(new FirstAvailableStrategy<>());

        ProjectorRemote firstProjector = projectorService.allocate();

        System.out.println("Chuyển sang chiến lược chọn phần tử đầu tiên sẵn sàng.");

        System.out.println("Thiết bị được chọn:");
        System.out.println(firstProjector);

        AllocationService<AirConditionerRemote> airService = new AllocationService<>(airRepository,
                new HighestPriorityStrategy<>(AirConditionerRemote::getDefaultPriorityScore));

        System.out.println("\n=== 8. CHỌN ĐIỀU KHIỂN CHO PHÒNG A203 ===");

        AirConditionerRemote selectedAir = airService.allocate(device -> device.isCompatibleWith("A203"));

        System.out.println("Thiết bị được chọn:");
        System.out.println(selectedAir);

        AllocationService<WirelessMicrophone> microphoneService = new AllocationService<>(microphoneRepository,
                new FirstAvailableStrategy<>());

        System.out.println("\n=== 9. CHỌN MICRO ĐẦU TIÊN SẴN SÀNG ===");

        WirelessMicrophone selectedMicrophone = microphoneService.allocate();

        System.out.println("Thiết bị được chọn:");
        System.out.println(selectedMicrophone);

        System.out.println("\n=== 10. CẤP PHÁT KHI KHÔNG CÓ ỨNG VIÊN ===");

        try {
            airService.allocate(device -> device.isCompatibleWith("A999"));
        } catch (NoSuitableItemException ex) {
            System.out.println("Đã xử lý lỗi: " + ex.getMessage());
        }

        System.out.println("\n=== 11. TÁI SỬ DỤNG REPOSITORY GENERIC ===");

        System.out.println("Số điều khiển máy chiếu: " + projectorRepository.getAll().size());

        System.out.println("Số điều khiển điều hòa: " + airRepository.getAll().size());

        System.out.println("Số micro không dây: " + microphoneRepository.getAll().size());

        System.out.println("\nCùng một lớp InMemoryRepository<T> được sử dụng cho cả ba loại thiết bị.");
    }
}