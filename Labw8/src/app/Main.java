/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package app;

import coordinator.DeviceMonitoringCoordinator;
import device.AirConditionerRemote;
import device.ProjectorRemote;
import device.WirelessMicrophone;
import event.DeviceAlertEventArgs;
import exception.InvalidDeviceReadingException;
import java.util.List;
import java.util.function.BiConsumer;
import model.AlertSeverity;
import model.DeviceStatus;
import subscriber.ConsoleNotificationService;
import subscriber.DeviceAuditLogger;
import subscriber.FilteredAlertSubscriber;
import subscriber.MaintenanceService;

/** Chương trình minh họa: ba thiết bị, ba subscriber, một subscriber lọc thêm sau. */
public class Main {
    public static void main(String[] args) {
        ProjectorRemote projector = new ProjectorRemote("PRJ-01", "Máy chiếu A101");
        AirConditionerRemote aircon = new AirConditionerRemote("AC-01", "Điều hòa A101", "A101");
        WirelessMicrophone mic = new WirelessMicrophone("MIC-01", "Micro 1");

        ConsoleNotificationService notifier = new ConsoleNotificationService();
        DeviceAuditLogger logger = new DeviceAuditLogger();
        MaintenanceService maintenance = new MaintenanceService();

        try (DeviceMonitoringCoordinator coordinator = new DeviceMonitoringCoordinator(
                List.of(projector, aircon, mic), notifier, logger, maintenance)) {

            coordinator.subscribe();
            coordinator.subscribe(); // lần 2: không được nhân đôi thông báo

            // Subscriber thêm sau, không sửa publisher
            FilteredAlertSubscriber criticalOnly = new FilteredAlertSubscriber(
                    a -> a.severity() == AlertSeverity.CRITICAL,
                    a -> System.out.println("CRITICAL: " + a.deviceId() + " - " + a.message()));
            BiConsumer<Object, DeviceAlertEventArgs> criticalHandler = criticalOnly::handleAlert;
            mic.addAlertListener(criticalHandler);

            System.out.println("--- Đổi trạng thái ---");
            projector.changeStatus(DeviceStatus.BORROWED);
            projector.changeStatus(DeviceStatus.BORROWED); // trùng: không log

            System.out.println("--- Máy chiếu ---");
            projector.updateBatteryLevel(15); // Warning
            projector.updateBatteryLevel(10); // cùng vùng: không cảnh báo
            projector.updateBatteryLevel(0);  // Critical (1 cảnh báo)

            System.out.println("--- Điều hòa ---");
            aircon.updateResponseState(false); // Critical
            aircon.updateResponseState(false); // lặp: không cảnh báo

            System.out.println("--- Micro ---");
            mic.updateBatteryLevel(25);  // Warning
            mic.updateSignalQuality(0);  // Critical

            System.out.println("--- Giá trị không hợp lệ ---");
            try {
                projector.updateBatteryLevel(101);
            } catch (InvalidDeviceReadingException ex) {
                System.out.println("Lỗi: " + ex.getDeviceId() + ", giá trị " + ex.getInvalidValue() + ": " + ex.getMessage());
                System.out.println("Pin vẫn là " + projector.getBatteryLevel() + " (đối tượng không hỏng).");
            }

            System.out.println("\n--- Audit ---");
            logger.getEntries().forEach(System.out::println);
            System.out.println("\n--- Maintenance ---");
            maintenance.getRequests().forEach(System.out::println);
        }
    }
}
