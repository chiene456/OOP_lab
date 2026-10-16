/****************
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
****************/

package app;

import enums.DeviceStatus;
import model.Device;
import java.math.BigDecimal;
import java.time.Year;

public class DeviceValidationTest {

    private static class TestDevice extends Device {
        TestDevice(String id, String name, int year, BigDecimal price, DeviceStatus status) {
            super(id, name, year, price, status);
        }

        @Override
        public BigDecimal calculateAnnualMaintenanceCost() {
            return BigDecimal.ZERO;
        }
    }

    private static void expectInvalid(String description, Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException ex) {
            System.out.println("PASS: " + description + " -> " + ex.getMessage());
            return;
        }
        throw new AssertionError("FAIL: Chưa từ chối dữ liệu: " + description);
    }

    public static void main(String[] args) {
        int year = Year.now().getValue();
        BigDecimal price = new BigDecimal("500000");
        DeviceStatus status = DeviceStatus.AVAILABLE;

        expectInvalid("Mã null", () -> new TestDevice(null, "Micro", year, price, status));

        expectInvalid("Mã chỉ có khoảng trắng", () -> new TestDevice("   ", "Micro", year, price, status));

        expectInvalid("Tên null", () -> new TestDevice("MC01", null, year, price, status));

        expectInvalid("Tên rỗng", () -> new TestDevice("MC01", "", year, price, status));

        expectInvalid("Năm bằng 0", () -> new TestDevice("MC01", "Micro", 0, price, status));

        expectInvalid("Năm trong tương lai", () -> new TestDevice("MC01", "Micro", year + 1, price, status));

        expectInvalid("Giá null", () -> new TestDevice("MC01", "Micro", year, null, status));

        expectInvalid("Giá bằng 0", () -> new TestDevice("MC01", "Micro", year, BigDecimal.ZERO, status));

        expectInvalid("Giá âm", () -> new TestDevice("MC01", "Micro", year, new BigDecimal("-100"), status));

        expectInvalid("Trạng thái null", () -> new TestDevice("MC01", "Micro", year, price, null));

        // Kiểm tra dữ liệu hợp lệ và xử lý khoảng trắng.
        Device valid = new TestDevice(" MC01 ", " Micro ", year, price, status);

        if (!valid.getDeviceId().equals("MC01") || !valid.getName().equals("Micro")
                || valid.getYearInService() != year || valid.getPurchasePrice().compareTo(price) != 0
                || valid.getStatus() != status) {
            throw new AssertionError("FAIL: Dữ liệu hợp lệ bị lưu sai.");
        }
        System.out.println("PASS: Khởi tạo hợp lệ");
        System.out.println(valid);
        System.out.println("Hoàn thành 11 trường hợp kiểm thử.");
    }
}