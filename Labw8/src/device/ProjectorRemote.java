/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package device;

import model.AlertSeverity;

public class ProjectorRemote extends TeachingDevice {
    private static final int WARNING_THRESHOLD = 20;

    private int batteryLevel;

    public ProjectorRemote(String id, String name) {
        this(id, name, 100);
    }

    public ProjectorRemote(String id, String name, int batteryLevel) {
        super(id, name);
        validateReading("Mức pin", batteryLevel);
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryLevel() { return batteryLevel; }

    /** Chỉ cảnh báo khi chuyển sang vùng NGHIÊM TRỌNG HƠN. */
    public void updateBatteryLevel(int newLevel) {
        validateReading("Mức pin", newLevel); // kiểm tra trước khi gán

        AlertSeverity oldZone = classify(batteryLevel, WARNING_THRESHOLD);
        AlertSeverity newZone = classify(newLevel, WARNING_THRESHOLD);
        batteryLevel = newLevel;

        if (newZone.compareTo(oldZone) > 0) {
            String msg = (newZone == AlertSeverity.CRITICAL) ? "Pin đã hết (0%)." : "Pin xuống thấp (" + newLevel + "%).";
            raiseAlert(msg, newZone);
        }
    }
}
