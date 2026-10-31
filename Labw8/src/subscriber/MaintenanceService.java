/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package subscriber;

import event.DeviceAlertEventArgs;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.AlertSeverity;

public class MaintenanceService {
    private final List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
        return Collections.unmodifiableList(requests);
    }

    /** Chỉ quan tâm cảnh báo CRITICAL. */
    public void handleCriticalAlert(Object sender, DeviceAlertEventArgs e) {
        if (e.severity() != AlertSeverity.CRITICAL) {
            return;
        }
        requests.add("Maintenance request for " + e.deviceId() + ": " + e.message());
    }
}
