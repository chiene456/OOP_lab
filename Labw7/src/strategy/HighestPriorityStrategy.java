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
import java.util.function.ToDoubleFunction;

/**
 * Chọn đối tượng có điểm ưu tiên lớn nhất.
 *
 * Hàm tính điểm được truyền từ bên ngoài để chiến lược
 * không phụ thuộc vào một loại thiết bị cụ thể.
 *
 * @param <T> kiểu đối tượng được lựa chọn
 */
public class HighestPriorityStrategy<T>
        implements ISelectionStrategy<T> {

    private final ToDoubleFunction<T> scoreSelector;

    public HighestPriorityStrategy(ToDoubleFunction<T> scoreSelector) {
        this.scoreSelector = Objects.requireNonNull(scoreSelector, "Hàm tính điểm không được null.");
    }

    /**
     * Tính điểm và kiểm tra điểm là số hữu hạn.
     */
    private double getScore(T item) {
        double score = scoreSelector.applyAsDouble(item);

        if (Double.isNaN(score) || Double.isInfinite(score)) {
            throw new IllegalArgumentException("Điểm ưu tiên phải là số hữu hạn.");
        }

        return score;
    }

    @Override
    public T select(List<T> candidates) {
        Objects.requireNonNull(candidates, "Danh sách ứng viên không được null.");
        if (candidates.isEmpty()) {
            throw new NoSuitableItemException();
        }

        T best = candidates.get(0);
        double bestScore = getScore(best);

        for (int i = 1; i < candidates.size(); i++) {
            T candidate = candidates.get(i);
            double score = getScore(candidate);

            // Giữ ứng viên đầu tiên nếu có nhiều ứng viên bằng điểm.
            if (score > bestScore) {
                best = candidate;
                bestScore = score;
            }
        }

        return best;
    }
}