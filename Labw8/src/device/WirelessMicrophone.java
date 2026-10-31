/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package device;

import model.AlertSeverity;

public class WirelessMicrophone extends TeachingDevice {
    private static final int BATTERY_WARNING = 30;
    private static final int SIGNAL_WARNING = 40;

    private int batteryLevel;
    private int signalQuality;

    public WirelessMicrophone(String id, String name) {
        this(id, name, 100, 100);
    }

    public WirelessMicrophone(String id, String name, int batteryLevel, int signalQuality) {
        super(id, name);
        validateReading("Mức pin", batteryLevel);
        validateReading("Chất lượng tín hiệu", signalQuality);
        this.batteryLevel = batteryLevel;
        this.signalQuality = signalQuality;
    }

    public int getBatteryLevel() { return batteryLevel; }
    public int getSignalQuality() { return signalQuality; }

    public void updateBatteryLevel(int newLevel) {
        validateReading("Mức pin", newLevel);

        AlertSeverity oldZone = classify(batteryLevel, BATTERY_WARNING);
        AlertSeverity newZone = classify(newLevel, BATTERY_WARNING);
        batteryLevel = newLevel;

        if (newZone.compareTo(oldZone) > 0) {
            raiseAlert(newZone == AlertSeverity.CRITICAL ? "Micro hết pin (0%)." : "Pin micro thấp (" + newLevel + "%).", newZone);
        }
    }

    public void updateSignalQuality(int newQuality) {
        validateReading("Chất lượng tín hiệu", newQuality);

        AlertSeverity oldZone = classify(signalQuality, SIGNAL_WARNING);
        AlertSeverity newZone = classify(newQuality, SIGNAL_WARNING);
        signalQuality = newQuality;

        if (newZone.compareTo(oldZone) > 0) {
            raiseAlert(newZone == AlertSeverity.CRITICAL ? "Micro mất tín hiệu (0%)." : "Chất lượng tín hiệu micro thấp (" + newQuality + "%).", newZone);
        }
    }
}
