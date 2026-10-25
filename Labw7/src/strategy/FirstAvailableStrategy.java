/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package strategy;

import exception.NoSuitableItemException;

import java.util.List;
import java.util.Objects;

/**
 * Chọn phần tử đầu tiên của danh sách ứng viên.
 * 
 * Dịch vụ cấp phát chịu trách nhiệm lọc các thiết bị sẵn sàng
 * trước khi gọi chiến lược này.
 * 
 * @param <T> kiểu đối tượng được lựa chọn
 */
public class FirstAvailableStrategy<T> implements ISelectionStrategy<T> {

    @Override
    public T select(List<T> candidates) {
        Objects.requireNonNull(candidates, "Danh sách ứng viên không được null.");

        if (candidates.isEmpty()) {
            throw new NoSuitableItemException();
        }
        return candidates.get(0);
    }
}