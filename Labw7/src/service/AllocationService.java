/****************/
/*
 * Mã sinh viên: 202418853
 * Họ tên: Nguyễn Quang Chiến
 */
/****************/
package service;

import model.TeachingDevice;
import repository.IRepository;
import strategy.ISelectionStrategy;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Dịch vụ cấp phát thiết bị.
 *
 * Dịch vụ làm việc với interface kho và interface chiến lược,
 * không phụ thuộc vào loại thiết bị hoặc chiến lược cụ thể.
 *
 * @param <T> loại thiết bị được cấp phát
 */
public class AllocationService<T extends TeachingDevice> {
    private final IRepository<T> repository;
    private ISelectionStrategy<T> strategy;

    public AllocationService(IRepository<T> repository, ISelectionStrategy<T> strategy) {
        this.repository = Objects.requireNonNull(repository, "Repository không được null.");

        this.strategy = Objects.requireNonNull(strategy, "Chiến lược không được null.");
    }

    /**
     * Thay đổi chiến lược lựa chọn tại thời gian chạy.
     */
    public void changeStrategy(ISelectionStrategy<T> newStrategy) {

        this.strategy = Objects.requireNonNull(newStrategy, "Chiến lược mới không được null.");
    }

    /**
     * Cấp phát thiết bị không kèm điều kiện bổ sung.
     */
    public T allocate() {
        return allocate(null);
    }

    /**
     * Cấp phát thiết bị thỏa điều kiện.
     *
     * @param additionalCondition điều kiện bổ sung; có thể null
     * @return thiết bị đã chuyển sang trạng thái BORROWED
     */
    public T allocate(
            Predicate<T> additionalCondition) {

        // Chỉ các thiết bị sẵn sàng và thỏa điều kiện mới là ứng viên.
        List<T> candidates = repository.findAll(device -> device.isReadyForUse()
                && (additionalCondition == null || additionalCondition.test(device)));

        // Đa hình chiến lược lựa chọn.
        T selected = strategy.select(candidates);

        // Chỉ trả kết quả sau khi mượn thành công.
        selected.borrow();

        return selected;
    }
}