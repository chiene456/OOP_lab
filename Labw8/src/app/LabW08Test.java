/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/

package app;

import coordinator.DeviceMonitoringCoordinator;
import device.AirConditionerRemote;
import device.ProjectorRemote;
import device.TeachingDevice;
import device.WirelessMicrophone;
import event.DeviceAlertEventArgs;
import event.DeviceStatusChangedEventArgs;
import exception.InvalidDeviceReadingException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import model.AlertSeverity;
import model.DeviceStatus;
import subscriber.ConsoleNotificationService;
import subscriber.DeviceAuditLogger;
import subscriber.FilteredAlertSubscriber;
import subscriber.MaintenanceService;

/**
 * Kiểm thử không dùng thư viện ngoài: in PASS/FAIL cho từng điều kiện,
 * kết thúc bằng tổng số đạt/không đạt (exit code 1 nếu có FAIL).
 */
public class LabW08Test {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }

    /** Đăng ký một handler ghi lại mọi cảnh báo của thiết bị. */
    private static List<DeviceAlertEventArgs> collectAlerts(TeachingDevice d) {
        List<DeviceAlertEventArgs> list = new ArrayList<>();
        BiConsumer<Object, DeviceAlertEventArgs> h = (s, e) -> list.add(e);
        d.addAlertListener(h);
        return list;
    }

    private static List<DeviceStatusChangedEventArgs> collectStatus(TeachingDevice d) {
        List<DeviceStatusChangedEventArgs> list = new ArrayList<>();
        BiConsumer<Object, DeviceStatusChangedEventArgs> h = (s, e) -> list.add(e);
        d.addStatusChangedListener(h);
        return list;
    }

    private static InvalidDeviceReadingException expectInvalid(Runnable r) {
        try {
            r.run();
        } catch (InvalidDeviceReadingException e) {
            return e;
        }
        return null;
    }

    public static void main(String[] args) {
        testStatusChange();
        testProjector();
        testAirConditioner();
        testMicrophone();
        testInvalidReading();
        testMultipleAndFailingSubscribers();
        testFilteredSubscriber();
        testCoordinator();
        testLambdaUnsubscribe();

        System.out.println("\nTổng kết: " + passed + " đạt, " + failed + " không đạt.");
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ---------- Đổi trạng thái ----------
    private static void testStatusChange() {
        System.out.println("== Đổi trạng thái ==");
        ProjectorRemote p = new ProjectorRemote("P1", "Máy chiếu");
        List<DeviceStatusChangedEventArgs> events = collectStatus(p);
        List<DeviceStatus> seenInHandler = new ArrayList<>();
        p.addStatusChangedListener((s, e) -> seenInHandler.add(p.getStatus()));

        check("Thiết bị mới có trạng thái AVAILABLE", p.getStatus() == DeviceStatus.AVAILABLE);

        p.changeStatus(DeviceStatus.AVAILABLE);
        check("Đổi sang cùng trạng thái: không phát event", events.isEmpty());

        p.changeStatus(DeviceStatus.BORROWED);
        check("Đổi trạng thái thật: phát đúng 1 event", events.size() == 1);

        check("Event chứa trạng thái cũ/mới đúng", events.get(0).oldStatus() == DeviceStatus.AVAILABLE
            && events.get(0).newStatus() == DeviceStatus.BORROWED && events.get(0).deviceId().equals("P1"));

        check("Trạng thái đã cập nhật TRƯỚC khi phát event",seenInHandler.size() == 1 && seenInHandler.get(0) == DeviceStatus.BORROWED);

        p.changeStatus(DeviceStatus.BORROWED);
        check("Đổi trùng lần nữa: vẫn chỉ 1 event", events.size() == 1);
    }

    // ---------- Máy chiếu ----------
    private static void testProjector() {
        System.out.println("== ProjectorRemote ==");
        ProjectorRemote p = new ProjectorRemote("P1", "Máy chiếu");
        List<DeviceAlertEventArgs> a = collectAlerts(p);

        p.updateBatteryLevel(100);
        p.updateBatteryLevel(20);
        check("Pin 100 -> 20 (biên an toàn): không cảnh báo", a.isEmpty());

        p.updateBatteryLevel(19);
        check("Pin 20 -> 19: 1 cảnh báo WARNING", a.size() == 1 && a.get(0).severity() == AlertSeverity.WARNING);

        p.updateBatteryLevel(10);
        check("Pin 19 -> 10 (cùng vùng): không cảnh báo lại", a.size() == 1);

        p.updateBatteryLevel(0);
        check("Pin 10 -> 0: thêm 1 cảnh báo CRITICAL", a.size() == 2 && a.get(1).severity() == AlertSeverity.CRITICAL);

        p.updateBatteryLevel(50);
        check("Pin tăng lại 0 -> 50: không cảnh báo", a.size() == 2);

        ProjectorRemote p2 = new ProjectorRemote("P2", "Máy chiếu 2", 15);
        List<DeviceAlertEventArgs> a2 = collectAlerts(p2);
        p2.updateBatteryLevel(0);
        check("Pin 15 -> 0: đúng MỘT cảnh báo (CRITICAL)", a2.size() == 1 && a2.get(0).severity() == AlertSeverity.CRITICAL);

        ProjectorRemote p3 = new ProjectorRemote("P3", "Máy chiếu 3");
        List<DeviceAlertEventArgs> a3 = collectAlerts(p3);
        p3.updateBatteryLevel(0);
        check("Pin 100 -> 0 trực tiếp: đúng MỘT cảnh báo CRITICAL", a3.size() == 1 && a3.get(0).severity() == AlertSeverity.CRITICAL);
    }

    // ---------- Điều hòa ----------
    private static void testAirConditioner() {
        System.out.println("== AirConditionerRemote ==");
        AirConditionerRemote ac = new AirConditionerRemote("AC1", "Điều hòa", "A101");
        List<DeviceAlertEventArgs> a = collectAlerts(ac);

        check("Thiết bị mới ở trạng thái phản hồi", ac.isResponsive());

        ac.updateResponseState(true);
        check("Phản hồi -> phản hồi: không cảnh báo", a.isEmpty());

        ac.updateResponseState(false);
        check("Phản hồi -> không phản hồi: 1 cảnh báo CRITICAL", a.size() == 1 && a.get(0).severity() == AlertSeverity.CRITICAL);

        ac.updateResponseState(false);
        check("Vẫn không phản hồi: không phát lặp", a.size() == 1);

        ac.updateResponseState(true);
        check("Không phản hồi -> phản hồi: không cảnh báo", a.size() == 1);

        ac.updateResponseState(false);
        check("Mất phản hồi lần hai: cảnh báo lại", a.size() == 2);
    }

    // ---------- Micro ----------
    private static void testMicrophone() {
        System.out.println("== WirelessMicrophone ==");
        WirelessMicrophone m = new WirelessMicrophone("M1", "Micro");
        List<DeviceAlertEventArgs> a = collectAlerts(m);

        m.updateBatteryLevel(30);
        check("Pin 30 (biên): không cảnh báo", a.isEmpty());
        m.updateBatteryLevel(29);
        check("Pin 29: WARNING", a.size() == 1 && a.get(0).severity() == AlertSeverity.WARNING);

        WirelessMicrophone m2 = new WirelessMicrophone("M2", "Micro 2");
        List<DeviceAlertEventArgs> a2 = collectAlerts(m2);
        m2.updateSignalQuality(40);
        check("Tín hiệu 40 (biên): không cảnh báo", a2.isEmpty());
        m2.updateSignalQuality(39);
        check("Tín hiệu 39: WARNING", a2.size() == 1 && a2.get(0).severity() == AlertSeverity.WARNING);

        WirelessMicrophone m3 = new WirelessMicrophone("M3", "Micro 3");
        List<DeviceAlertEventArgs> a3 = collectAlerts(m3);
        m3.updateBatteryLevel(25);
        m3.updateSignalQuality(30);
        check("Hai điều kiện cùng lúc (pin 25, tín hiệu 30): 2 cảnh báo WARNING", a3.size() == 2
            && a3.get(0).severity() == AlertSeverity.WARNING && a3.get(1).severity() == AlertSeverity.WARNING);

        WirelessMicrophone m4 = new WirelessMicrophone("M4", "Micro 4");
        List<DeviceAlertEventArgs> a4 = collectAlerts(m4);
        m4.updateBatteryLevel(0);
        check("Pin = 0: CRITICAL", a4.size() == 1 && a4.get(0).severity() == AlertSeverity.CRITICAL);

        WirelessMicrophone m5 = new WirelessMicrophone("M5", "Micro 5");
        List<DeviceAlertEventArgs> a5 = collectAlerts(m5);
        m5.updateSignalQuality(0);
        check("Tín hiệu = 0: CRITICAL", a5.size() == 1 && a5.get(0).severity() == AlertSeverity.CRITICAL);
    }

    // ---------- Ngoại lệ ----------
    private static void testInvalidReading() {
        System.out.println("== InvalidDeviceReadingException ==");
        ProjectorRemote p = new ProjectorRemote("P1", "Máy chiếu", 50);
        List<DeviceAlertEventArgs> a = collectAlerts(p);

        InvalidDeviceReadingException ex101 = expectInvalid(() -> p.updateBatteryLevel(101));
        check("Pin 101: ném InvalidDeviceReadingException", ex101 != null);
        check("Ngoại lệ lưu đúng deviceId và giá trị sai", ex101 != null && ex101.getDeviceId().equals("P1") && ex101.getInvalidValue() == 101);
        check("Sau khi ném lỗi: pin không đổi (đối tượng không hỏng)", p.getBatteryLevel() == 50);
        check("Sau khi ném lỗi: không có cảnh báo", a.isEmpty());

        check("Pin -1: ném ngoại lệ", expectInvalid(() -> p.updateBatteryLevel(-1)) != null);
        p.updateBatteryLevel(100);
        check("Pin 100 (biên trên) hợp lệ", p.getBatteryLevel() == 100);
        p.updateBatteryLevel(0);
        check("Pin 0 (biên dưới) hợp lệ", p.getBatteryLevel() == 0);

        WirelessMicrophone m = new WirelessMicrophone("M1", "Micro");
        check("Tín hiệu 101: ném ngoại lệ", expectInvalid(() -> m.updateSignalQuality(101)) != null);
        check("Tín hiệu không đổi sau lỗi", m.getSignalQuality() == 100);
        check("Constructor với pin 150: ném ngoại lệ",
                expectInvalid(() -> new WirelessMicrophone("M2", "Micro", 150, 50)) != null);
    }

    // ---------- Nhiều subscriber, subscriber lỗi ----------
    private static void testMultipleAndFailingSubscribers() {
        System.out.println("== Nhiều subscriber / subscriber ném lỗi ==");
        ProjectorRemote p = new ProjectorRemote("P1", "Máy chiếu");
        List<String> order = new ArrayList<>();
        p.addAlertListener((s, e) -> order.add("A"));
        p.addAlertListener((s, e) -> { throw new IllegalStateException("subscriber B lỗi (cố ý)"); });
        p.addAlertListener((s, e) -> order.add("C"));

        p.updateBatteryLevel(10);
        check("Một event có nhiều subscriber: A và C đều nhận", order.contains("A") && order.contains("C"));
        check("Subscriber ném lỗi không chặn subscriber sau nó", order.equals(List.of("A", "C")));
        check("Publisher không bị ảnh hưởng: pin đã cập nhật", p.getBatteryLevel() == 10);
    }

    // ---------- Subscriber thêm sau + delegate lọc ----------
    private static void testFilteredSubscriber() {
        System.out.println("== FilteredAlertSubscriber ==");
        WirelessMicrophone m = new WirelessMicrophone("M1", "Micro");
        List<String> critical = new ArrayList<>();
        FilteredAlertSubscriber only = new FilteredAlertSubscriber(a -> a.severity() == AlertSeverity.CRITICAL,
        a -> critical.add(a.deviceId() + ":" + a.severity())); BiConsumer<Object, DeviceAlertEventArgs> h = only::handleAlert;
        m.addAlertListener(h);

        m.updateBatteryLevel(25);
        check("Filter chặn cảnh báo WARNING", critical.isEmpty());
        m.updateBatteryLevel(0);
        check("Filter cho qua cảnh báo CRITICAL", critical.size() == 1);

        m.removeAlertListener(h);
        m.updateSignalQuality(0);
        check("Sau khi gỡ đăng ký: không nhận thêm", critical.size() == 1);

        boolean nullRejected = false;
        try {
            new FilteredAlertSubscriber(null, a -> { });
        } catch (NullPointerException e) {
            nullRejected = true;
        }
        check("Filter null bị từ chối", nullRejected);
    }

    // ---------- Coordinator ----------
    private static void testCoordinator() {
        System.out.println("== DeviceMonitoringCoordinator ==");
        ProjectorRemote p = new ProjectorRemote("P1", "Máy chiếu");
        AirConditionerRemote ac = new AirConditionerRemote("AC1", "Điều hòa", "A101");
        DeviceAuditLogger logger = new DeviceAuditLogger();
        MaintenanceService maintenance = new MaintenanceService();
        ConsoleNotificationService notifier = new ConsoleNotificationService();

        DeviceMonitoringCoordinator c = new DeviceMonitoringCoordinator(List.of(p, ac), notifier, logger, maintenance);
        c.subscribe();
        c.subscribe();
        c.subscribe();
        c.close(); // close() gọi unsubscribe() cho tất cả subscriber
        p.changeStatus(DeviceStatus.BORROWED);
        check("Subscribe nhiều lần: audit chỉ ghi 1 dòng", logger.getEntries().size() == 1);

        ac.updateResponseState(false);
        check("Subscribe nhiều lần: 1 yêu cầu bảo trì cho cảnh báo Critical",
                maintenance.getRequests().size() == 1);

        p.updateBatteryLevel(10);
        check("MaintenanceService bỏ qua cảnh báo Warning", maintenance.getRequests().size() == 1);

        c.unsubscribe();
        p.changeStatus(DeviceStatus.AVAILABLE);
        ac.updateResponseState(true);
        ac.updateResponseState(false);
        check("Unsubscribe: không ghi thêm audit", logger.getEntries().size() == 1);
        check("Unsubscribe: không tạo thêm yêu cầu bảo trì", maintenance.getRequests().size() == 1);

        c.subscribe();
        p.changeStatus(DeviceStatus.UNDER_MAINTENANCE);
        check("Subscribe lại sau Unsubscribe hoạt động", logger.getEntries().size() == 2);

        // close() (tương đương Dispose) phải gọi unsubscribe()
        DeviceAuditLogger logger2 = new DeviceAuditLogger();
        ProjectorRemote p2 = new ProjectorRemote("P2", "Máy chiếu 2");
        try (DeviceMonitoringCoordinator c2 = new DeviceMonitoringCoordinator(
                List.of(p2), notifier, logger2, new MaintenanceService())) {
            c2.subscribe();
            p2.changeStatus(DeviceStatus.BORROWED);
        }
        p2.changeStatus(DeviceStatus.AVAILABLE);
        check("close() gọi unsubscribe(): không ghi thêm sau khi đóng",
                logger2.getEntries().size() == 1);
    }

    // ---------- Hủy đăng ký bằng lambda ----------
    private static void testLambdaUnsubscribe() {
        System.out.println("== Hủy đăng ký bằng lambda ==");
        ProjectorRemote p = new ProjectorRemote("P1", "Máy chiếu");
        int[] count = {0};

        p.addAlertListener((s, e) -> count[0]++);
        p.removeAlertListener((s, e) -> count[0]++); // lambda mới, KHÔNG phải đối tượng cũ
        p.updateBatteryLevel(10);
        check("remove bằng lambda mới không gỡ được lambda cũ (vẫn nhận 1 lần)", count[0] == 1);

        ProjectorRemote p2 = new ProjectorRemote("P2", "Máy chiếu 2");
        int[] count2 = {0};
        BiConsumer<Object, DeviceAlertEventArgs> kept = (s, e) -> count2[0]++;
        p2.addAlertListener(kept);
        p2.addAlertListener(kept);
        p2.updateBatteryLevel(10);
        check("Giữ tham chiếu handler: đăng ký hai lần chỉ nhận 1 lần", count2[0] == 1);
        p2.removeAlertListener(kept);
        p2.updateBatteryLevel(0);
        check("Giữ tham chiếu handler: remove gỡ đúng", count2[0] == 1);
    }
}
