/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package repository;

import exception.DuplicateIdException;
import model.IIdentifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Kho lưu trữ đối tượng trong bộ nhớ.
 * LinkedHashMap hỗ trợ tìm theo mã và giữ thứ tự thêm.
 * 
 * @param <T> kiểu đối tượng có mã định danh ổn định
 */
public class InMemoryRepository<T extends IIdentifiable>
        implements IRepository<T> {

    private final Map<String, T> items = new LinkedHashMap<>();

    /**
     * Kiểm tra và chuẩn hóa mã dùng làm khóa trong kho.
     */
    private String validateId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã định danh không được để trống.");
        }

        return id.trim();
    }

    @Override
    public void add(T item) {
        Objects.requireNonNull(item, "Đối tượng thêm vào kho không được null.");
        String id = validateId(item.getId());
        if (items.containsKey(id)) {
            throw new DuplicateIdException(id);
        }
        items.put(id, item);
    }

    @Override
    public boolean remove(String id) {
        return items.remove(validateId(id)) != null;
    }

    @Override
    public T findById(String id) {
        return items.get(validateId(id));
    }

    @Override
    public List<T> getAll() {
        // Không trả trực tiếp collection nội bộ.
        return Collections.unmodifiableList(
                new ArrayList<>(items.values()));
    }

    @Override
    public List<T> findAll(Predicate<T> condition) {
        Objects.requireNonNull(condition, "Điều kiện lọc không được null.");
        List<T> result = new ArrayList<>();
        for (T item : items.values()) {
            if (condition.test(item)) {
                result.add(item);
            }
        }
        return Collections.unmodifiableList(result);
    }
}