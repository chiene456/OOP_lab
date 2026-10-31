/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package device;

import event.DeviceAlertEventArgs;
import event.DeviceStatusChangedEventArgs;
import exception.InvalidDeviceReadingException;
import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import model.AlertSeverity;
import model.DeviceStatus;

/** Publisher: chỉ phát hiện và công bố sự kiện, không biết ai lắng nghe. */
public abstract class TeachingDevice {

    /**
     * Mô phỏng "event" của C#: lớp lồng private nên bên ngoài không
     * tham chiếu được, không thể tự gọi fire().
     */
    private static final class EventSource<T> {
        // CopyOnWriteArrayList: an toàn khi handler đăng ký/hủy ngay trong lúc đang phát
        private final CopyOnWriteArrayList<BiConsumer<Object, T>> handlers =
                new CopyOnWriteArrayList<>();

        void add(BiConsumer<Object, T> h)    { handlers.addIfAbsent(h); } // không trùng
        void remove(BiConsumer<Object, T> h) { handlers.remove(h); }

        /** Gọi từng handler riêng lẻ: một handler lỗi không chặn các handler còn lại. */
        void fire(Object sender, T e) {
            for (BiConsumer<Object, T> h : handlers) {
                try {
                    h.accept(sender, e);
                } catch (Exception ex) {
                    System.err.println("[Lỗi subscriber] " + ex.getMessage());
                }
            }
        }
    }

    private final String id;
    private final String name;
    private DeviceStatus status;

    private final EventSource<DeviceStatusChangedEventArgs> statusChanged = new EventSource<>();
    private final EventSource<DeviceAlertEventArgs> alertRaised = new EventSource<>();

    protected TeachingDevice(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = DeviceStatus.AVAILABLE;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public DeviceStatus getStatus() { return status; }

    // ----- Đăng ký / hủy đăng ký (tương đương += và -=) -----
    public void addStatusChangedListener(BiConsumer<Object, DeviceStatusChangedEventArgs> h) {
        statusChanged.add(h);
    }
    public void removeStatusChangedListener(BiConsumer<Object, DeviceStatusChangedEventArgs> h) {
        statusChanged.remove(h);
    }
    public void addAlertListener(BiConsumer<Object, DeviceAlertEventArgs> h) {
        alertRaised.add(h);
    }
    public void removeAlertListener(BiConsumer<Object, DeviceAlertEventArgs> h) {
        alertRaised.remove(h);
    }

    /** Đổi trạng thái; không phát event nếu trạng thái không đổi. */
    public void changeStatus(DeviceStatus newStatus) {
        if (newStatus == status) {
            return;
        }
        DeviceStatus oldStatus = status;
        status = newStatus; // cập nhật trước, phát event sau

        onStatusChanged(new DeviceStatusChangedEventArgs(id, oldStatus, newStatus, LocalDateTime.now()));
    }

    protected void raiseAlert(String message, AlertSeverity severity) {
        onAlertRaised(new DeviceAlertEventArgs(id, message, severity, LocalDateTime.now()));
    }

    protected void onStatusChanged(DeviceStatusChangedEventArgs e) {
        statusChanged.fire(this, e);
    }

    protected void onAlertRaised(DeviceAlertEventArgs e) {
        alertRaised.fire(this, e);
    }

    /** Kiểm tra [0,100] TRƯỚC khi gán để đối tượng không hỏng khi ném ngoại lệ. */
    protected void validateReading(String readingName, int value) {
        if (value < 0 || value > 100) {
            throw new InvalidDeviceReadingException(id, value, readingName + " phải trong khoảng 0-100, nhận được " + value + ".");
        }
    }

    /** 0 => CRITICAL; dưới ngưỡng => WARNING; còn lại => INFORMATION (an toàn). */
    protected static AlertSeverity classify(int value, int warningThreshold) {
        if (value == 0) return AlertSeverity.CRITICAL;
        if (value < warningThreshold) return AlertSeverity.WARNING;
        return AlertSeverity.INFORMATION;
    }
}
