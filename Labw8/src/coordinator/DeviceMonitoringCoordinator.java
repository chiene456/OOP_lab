/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package coordinator;

import device.TeachingDevice;
import event.DeviceAlertEventArgs;
import event.DeviceStatusChangedEventArgs;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import subscriber.ConsoleNotificationService;
import subscriber.DeviceAuditLogger;
import subscriber.MaintenanceService;

/** AutoCloseable là tương đương IDisposable (dùng với try-with-resources). */
public class DeviceMonitoringCoordinator implements AutoCloseable {
    private final List<TeachingDevice> devices;

    
    private final BiConsumer<Object, DeviceStatusChangedEventArgs> auditHandler;
    private final BiConsumer<Object, DeviceAlertEventArgs> notifyHandler;
    private final BiConsumer<Object, DeviceAlertEventArgs> maintenanceHandler;

    public DeviceMonitoringCoordinator(
            Iterable<? extends TeachingDevice> devices,
            ConsoleNotificationService notificationService,
            DeviceAuditLogger auditLogger,
            MaintenanceService maintenanceService) {
        this.devices = new ArrayList<>();
        devices.forEach(this.devices::add);
        this.auditHandler = auditLogger::handleStatusChanged;
        this.notifyHandler = notificationService::handleAlert;
        this.maintenanceHandler = maintenanceService::handleCriticalAlert;
    }

    /** Idempotent: addIfAbsent trong EventSource nên gọi nhiều lần vẫn chỉ đăng ký một lần. */
    public void subscribe() {
        for (TeachingDevice d : devices) {
            d.addStatusChangedListener(auditHandler);
            d.addAlertListener(notifyHandler);
            d.addAlertListener(maintenanceHandler);
        }
    }

    public void unsubscribe() {
        for (TeachingDevice d : devices) {
            d.removeStatusChangedListener(auditHandler);
            d.removeAlertListener(notifyHandler);
            d.removeAlertListener(maintenanceHandler);
        }
    }

    @Override
    public void close() {
        unsubscribe();
    }
}
