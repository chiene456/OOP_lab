/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package repository;

import model.IIdentifiable;

import java.util.List;
import java.util.function.Predicate;

/**
 * Interface kho lưu trữ tổng quát.
 * 
 * @param <T> kiểu đối tượng có mã định danh
 */
public interface IRepository<T extends IIdentifiable> {

    /**
     * Thêm đối tượng vào kho.
     */
    void add(T item);

    /**
     * Xóa đối tượng theo mã.
     * 
     * @return true nếu xóa thành công, false nếu không tìm thấy
     */
    boolean remove(String id);

    /**
     * Tìm đối tượng theo mã.
     * 
     * @return đối tượng tìm được hoặc null nếu không tồn tại
     */
    T findById(String id);

    /**
     * Lấy bản sao danh sách đối tượng, không cho chỉnh sửa collection.
     */
    List<T> getAll();

    /**
     * Lọc đối tượng theo điều kiện.
     * 
     * @param condition điều kiện lọc khác null
     * @return danh sách kết quả không cho chỉnh sửa collection
     */
    List<T> findAll(Predicate<T> condition);
}