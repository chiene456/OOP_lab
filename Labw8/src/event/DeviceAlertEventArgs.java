/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package event;

import java.time.LocalDateTime;
import model.AlertSeverity;

/** Dữ liệu sự kiện cảnh báo (bất biến). */
public record DeviceAlertEventArgs(
        String deviceId,
        String message,
        AlertSeverity severity,
        LocalDateTime occurredAt) {
}
