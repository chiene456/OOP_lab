# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""employee.py - Lớp cơ sở trừu tượng Employee của hệ thống tính lương.

Bất biến: mã, họ tên, phòng ban không rỗng; thưởng tháng >= 0.
Python không có nạp chồng theo chữ ký (def sau ghi đè def trước), nên:
  * nạp chồng constructor  -> tham số mặc định (department = "Unassigned");
  * nạp chồng add_bonus()  -> một hàm phân phối theo số đối số, khai báo ba
    chữ ký bằng typing.overload để IDE/kiểm tra kiểu hiểu đúng.
"""
from abc import ABC, abstractmethod
from dataclasses import dataclass
from typing import List, overload


def format_money(value: float) -> str:
    """Định dạng tiền VND: 18000000 -> '18.000.000'."""
    return f"{round(value):,}".replace(",", ".")


@dataclass(frozen=True)
class BonusRecord:
    """Một dòng lịch sử thưởng (thay cho hai danh sách song song)."""
    amount: float
    reason: str


class Employee(ABC):
    """Nhân sự chung: giữ dữ liệu chung và quản lý thưởng."""

    def __init__(self, employee_id: str, full_name: str, department: str = "Unassigned"):
        # Constructor rút gọn = bỏ department; mọi kiểm tra nằm ở đây (một chỗ duy nhất)
        self._employee_id = self._require_not_blank(employee_id, "Mã nhân sự")
        self._full_name = self._require_not_blank(full_name, "Họ tên")
        self._department = self._require_not_blank(department, "Phòng ban")
        self._monthly_bonus = 0.0                      # luôn bằng tổng _bonus_history
        self._bonus_history: List[BonusRecord] = []

    # ---------- Thuộc tính chỉ đọc ----------
    @property
    def employee_id(self) -> str:
        return self._employee_id

    @property
    def full_name(self) -> str:
        return self._full_name

    @property
    def department(self) -> str:
        return self._department

    @property
    def monthly_bonus(self) -> float:
        return self._monthly_bonus

    @property
    def bonus_history(self) -> tuple:
        return tuple(self._bonus_history)              # bản sao bất biến

    # ---------- Nạp chồng add_bonus ----------
    @overload
    def add_bonus(self, amount: float) -> None: ...
    @overload
    def add_bonus(self, amount: float, reason: str) -> None: ...
    @overload
    def add_bonus(self, rate: float, reference_amount: float, reason: str) -> None: ...

    def add_bonus(self, *args, reason=None):
        """Phân phối theo số đối số (có thể truyền reason=... bằng từ khóa).

        1 đối số : (amount)                         -> thưởng cố định
        2 đối số : (amount, reason)                 -> thưởng cố định + lý do
        3 đối số : (rate, reference_amount, reason) -> thưởng theo tỷ lệ
        """
        args = list(args)
        if reason is not None:
            args.append(reason)
        if len(args) == 1:
            self._add_fixed_bonus(args[0], None)
        elif len(args) == 2:
            if not isinstance(args[1], str):
                raise TypeError("add_bonus(amount, reason): reason phải là chuỗi")
            self._add_fixed_bonus(args[0], args[1])
        elif len(args) == 3:
            self._add_rate_bonus(*args)
        else:
            raise TypeError("add_bonus nhận 1, 2 hoặc 3 đối số")

    def _add_fixed_bonus(self, amount, reason):
        if not amount > 0:
            raise ValueError("Số tiền thưởng phải > 0")
        if reason is None:
            reason = "(không ghi lý do)"
        else:
            self._require_not_blank(reason, "Lý do thưởng")
        self._apply_bonus(amount, reason)

    def _add_rate_bonus(self, rate, reference_amount, reason):
        if not (0 < rate <= 0.5):
            raise ValueError("Tỷ lệ thưởng phải trong (0, 0.5]")
        if not reference_amount > 0:
            raise ValueError("Giá trị tham chiếu phải > 0")
        self._require_not_blank(reason, "Lý do thưởng")
        self._apply_bonus(rate * reference_amount, reason)

    def _apply_bonus(self, amount: float, reason: str) -> None:
        """Hàm lõi duy nhất ghi nhận thưởng."""
        self._bonus_history.append(BonusRecord(amount, reason))
        self._monthly_bonus += amount

    def reset_monthly_bonus(self) -> None:
        """Đặt lại thưởng khi bắt đầu kỳ lương mới."""
        self._monthly_bonus = 0.0
        self._bonus_history.clear()

    # ---------- Hành vi đa hình ----------
    @abstractmethod
    def calculate_gross_pay(self) -> float:
        """Thu nhập trước khấu trừ - mỗi lớp con ghi đè theo công thức riêng."""

    @abstractmethod
    def get_employee_type(self) -> str:
        """Tên loại nhân sự."""

    def display_payroll_info(self) -> None:
        """In phần chung; lớp con gọi super() rồi in thêm phần riêng."""
        print(f"[{self.get_employee_type()}] {self._employee_id} - {self._full_name}"
              f" | Phòng: {self._department}")
        print(f"    Thưởng tháng: {format_money(self._monthly_bonus)}")

    # ---------- Hàm kiểm tra dùng chung cho lớp con ----------
    @staticmethod
    def _require_not_blank(value, field: str) -> str:
        if not isinstance(value, str) or not value.strip():
            raise ValueError(f"{field} không được rỗng")
        return value

    @staticmethod
    def _require_non_negative(value, field: str) -> float:
        if not value >= 0:                              # bắt cả NaN
            raise ValueError(f"{field} không được âm")
        return value

    @staticmethod
    def _require_in_range(value, low: float, high: float, field: str) -> float:
        if not low <= value <= high:
            raise ValueError(f"{field} phải nằm trong [{low}, {high}]")
        return value

    def _apply_initial_bonus(self, initial_bonus: float) -> None:
        """Dùng trong constructor lớp con: thưởng ban đầu (0 = không thưởng)."""
        self._require_non_negative(initial_bonus, "Thưởng ban đầu")
        if initial_bonus > 0:
            self.add_bonus(initial_bonus, "Thưởng ban đầu")
