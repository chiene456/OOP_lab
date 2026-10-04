# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""hourly_employee.py - Nhân viên theo giờ (giờ > 160 nhân 1,5)."""
from employee import Employee, format_money


class HourlyEmployee(Employee):
    STANDARD_HOURS = 160
    OVERTIME_MULTIPLIER = 1.5
    MAX_HOURS = 250

    def __init__(self, employee_id, full_name, department="Unassigned",
                 hourly_rate=0, worked_hours=0, initial_bonus=0):
        super().__init__(employee_id, full_name, department)
        self._hourly_rate = self._require_non_negative(hourly_rate, "Đơn giá giờ")
        self._worked_hours = self._require_in_range(worked_hours, 0, self.MAX_HOURS, "Số giờ làm")
        self._apply_initial_bonus(initial_bonus)

    @property
    def hourly_rate(self):
        return self._hourly_rate

    @property
    def worked_hours(self):
        return self._worked_hours

    # Giờ thường / giờ vượt được tính từ worked_hours, không lưu riêng
    @property
    def regular_hours(self):
        return min(self._worked_hours, self.STANDARD_HOURS)

    @property
    def overtime_hours(self):
        return max(0, self._worked_hours - self.STANDARD_HOURS)

    def calculate_base_pay(self) -> float:
        return (self.regular_hours * self._hourly_rate
                + self.overtime_hours * self._hourly_rate * self.OVERTIME_MULTIPLIER)

    def calculate_gross_pay(self) -> float:
        return self.calculate_base_pay() + self.monthly_bonus

    def get_employee_type(self) -> str:
        return "HourlyEmployee"

    def display_payroll_info(self) -> None:
        super().display_payroll_info()
        print(f"    Đơn giá: {format_money(self._hourly_rate)}/giờ | Giờ thường: "
              f"{self.regular_hours} | Giờ vượt: {self.overtime_hours}")
        print(f"    Lương cơ sở: {format_money(self.calculate_base_pay())}")
        print(f"    => Tổng thu nhập: {format_money(self.calculate_gross_pay())}")
