/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package event;

import java.time.LocalDateTime;
import model.DeviceStatus;

/** Dữ liệu sự kiện đổi trạng thái (bất biến). */
public record DeviceStatusChangedEventArgs(
        String deviceId,
        DeviceStatus oldStatus,
        DeviceStatus newStatus,
        LocalDateTime changedAt) {
}
