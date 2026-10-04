# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""sales_employee.py - Nhân viên kinh doanh: lương cơ bản + hoa hồng."""
from employee import Employee, format_money


class SalesEmployee(Employee):
    MAX_COMMISSION_RATE = 0.3

    def __init__(self, employee_id, full_name, department="Unassigned",
                 base_salary=0, sales_revenue=0, commission_rate=0, initial_bonus=0):
        super().__init__(employee_id, full_name, department)
        self._base_salary = self._require_non_negative(base_salary, "Lương cơ bản")
        self._sales_revenue = self._require_non_negative(sales_revenue, "Doanh số")
        self._commission_rate = self._require_in_range(
            commission_rate, 0, self.MAX_COMMISSION_RATE, "Tỷ lệ hoa hồng")
        self._apply_initial_bonus(initial_bonus)

    @property
    def base_salary(self):
        return self._base_salary

    @property
    def sales_revenue(self):
        return self._sales_revenue

    @property
    def commission_rate(self):
        return self._commission_rate

    def update_sales_revenue(self, new_revenue: float) -> None:
        """Cập nhật doanh số có kiểm soát."""
        self._sales_revenue = self._require_non_negative(new_revenue, "Doanh số")

    def calculate_gross_pay(self) -> float:
        # grossPay = lương cơ bản + doanh số x hoa hồng + thưởng
        return self._base_salary + self._sales_revenue * self._commission_rate + self.monthly_bonus

    def get_employee_type(self) -> str:
        return "SalesEmployee"

    def display_payroll_info(self) -> None:
        super().display_payroll_info()
        print(f"    Lương cơ bản: {format_money(self._base_salary)}"
              f" | Doanh số: {format_money(self._sales_revenue)}"
              f" | Hoa hồng: {self._commission_rate * 100:g}% = "
              f"{format_money(self._sales_revenue * self._commission_rate)}")
        print(f"    => Tổng thu nhập: {format_money(self.calculate_gross_pay())}")
