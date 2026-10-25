/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package strategy;

import java.util.List;

/**
 * Interface mô tả thuật toán lựa chọn một ứng viên.
 * 
 * @param <T> kiểu đối tượng được lựa chọn
 */
public interface ISelectionStrategy<T> {

    /**
     * Chọn một đối tượng từ danh sách ứng viên.
     * Phương thức không thay đổi danh sách đầu vào.
     * 
     * @param candidates danh sách ứng viên
     * @return đối tượng được chọn
     */
    T select(List<T> candidates);
}