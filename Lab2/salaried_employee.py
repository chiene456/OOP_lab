# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""salaried_employee.py - Nhân viên lương cố định."""
from employee import Employee, format_money


class SalariedEmployee(Employee):
    def __init__(self, employee_id, full_name, department="Unassigned",
                 monthly_salary=0, responsibility_allowance=0, initial_bonus=0):
        # Chỉ truyền (id, tên) = constructor rút gọn; truyền đủ = constructor đầy đủ
        super().__init__(employee_id, full_name, department)
        self._monthly_salary = self._require_non_negative(monthly_salary, "Lương tháng")
        self._responsibility_allowance = self._require_non_negative(
            responsibility_allowance, "Phụ cấp trách nhiệm")
        self._apply_initial_bonus(initial_bonus)

    @property
    def monthly_salary(self):
        return self._monthly_salary

    @property
    def responsibility_allowance(self):
        return self._responsibility_allowance

    def calculate_gross_pay(self) -> float:
        # grossPay = lương tháng + phụ cấp + thưởng
        return self._monthly_salary + self._responsibility_allowance + self.monthly_bonus

    def get_employee_type(self) -> str:
        return "SalariedEmployee"

    def display_payroll_info(self) -> None:
        super().display_payroll_info()
        print(f"    Lương tháng: {format_money(self._monthly_salary)}"
              f" | Phụ cấp: {format_money(self._responsibility_allowance)}")
        print(f"    => Tổng thu nhập: {format_money(self.calculate_gross_pay())}")
