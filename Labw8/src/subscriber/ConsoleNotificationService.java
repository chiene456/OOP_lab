/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package subscriber;

import event.DeviceAlertEventArgs;

public class ConsoleNotificationService {
    public void handleAlert(Object sender, DeviceAlertEventArgs e) {
        System.out.println("[" + e.severity() + "] " + e.deviceId() + ": " + e.message());
    }
}
