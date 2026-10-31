/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package subscriber;

import event.DeviceAlertEventArgs;

/** "Delegate" tự định nghĩa: trong Java là functional interface. */
@FunctionalInterface
public interface AlertFilter {
    boolean test(DeviceAlertEventArgs alert);
}
