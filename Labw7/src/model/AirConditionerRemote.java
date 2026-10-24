/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package model;

import enums.DeviceStatus;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Điều khiển điều hòa có tập phòng tương thích.
 * Sự tương thích với phòng được kiểm tra qua điều kiện lọc
 * khi dịch vụ thực hiện cấp phát.
 */
public class AirConditionerRemote extends TeachingDevice {
    private final Set<String> compatibleRooms;

    public AirConditionerRemote(String id, String name, String storageLocation, Set<String> compatibleRooms) {

        super(id, name, storageLocation);

        if (compatibleRooms == null) {
            throw new IllegalArgumentException("Tập phòng tương thích không được null.");
        }

        Set<String> copy = new LinkedHashSet<>();

        for (String roomId : compatibleRooms) {
            copy.add(requireText(roomId, "Mã phòng"));
        }

        // Sao chép và bảo vệ tập phòng khỏi thay đổi từ bên ngoài.
        this.compatibleRooms = Collections.unmodifiableSet(copy);
    }

    public Set<String> getCompatibleRooms() {
        return compatibleRooms;
    }

    /**
     * Kiểm tra điều khiển có tương thích với phòng yêu cầu hay không.
     */
    public boolean isCompatibleWith(String roomId) {
        String normalizedRoomId = requireText(roomId, "Mã phòng");
        return compatibleRooms.contains(normalizedRoomId);
    }

    @Override
    public boolean isReadyForUse() {
        return getStatus() == DeviceStatus.AVAILABLE;
    }

    @Override
    public double getDefaultPriorityScore() {
        // Thiết bị không tương thích phải được loại ở bước lọc ứng viên.
        return 100.0;
    }

    @Override
    public String toString() {
        return super.toString()
                + " [phòng tương thích=" + compatibleRooms + "]";
    }
}