/****************
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 ****************/
package device;

import model.AlertSeverity;

public class AirConditionerRemote extends TeachingDevice {
    private final String assignedRoom;
    private boolean responsive;

    public AirConditionerRemote(String id, String name, String assignedRoom) {
        super(id, name);
        this.assignedRoom = assignedRoom;
        this.responsive = true;
    }

    public String getAssignedRoom() { return assignedRoom; }
    public boolean isResponsive() { return responsive; }

    /** Chỉ cảnh báo khi chuyển từ phản hồi sang không phản hồi. */
    public void updateResponseState(boolean isResponsive) {
        boolean wasResponsive = responsive;
        responsive = isResponsive;

        if (wasResponsive && !isResponsive) {
            raiseAlert("Điều hòa phòng " + assignedRoom + " không phản hồi.", AlertSeverity.CRITICAL);
        }
    }
}
