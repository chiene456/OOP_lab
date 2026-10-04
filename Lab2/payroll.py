# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""payroll.py - Bảng lương của một kỳ.

Payroll KHÔNG kế thừa Employee (quan hệ "có", không phải "là"). Danh sách giữ
tham chiếu tới đối tượng gốc nên hành vi lớp dẫn xuất được bảo toàn (không slicing).
"""
from typing import Dict, List, Optional

from employee import Employee, format_money


class Payroll:
    def __init__(self, period: str):
        if not isinstance(period, str) or not period.strip():
            raise ValueError("Kỳ lương không được rỗng")
        self._period = period
        self._employees: List[Employee] = []          # giữ thứ tự thêm vào
        self._by_id: Dict[str, Employee] = {}         # tra cứu / kiểm tra trùng mã O(1)

    @property
    def period(self) -> str:
        return self._period

    def __len__(self) -> int:
        return len(self._employees)

    def add_employee(self, employee: Employee) -> None:
        if not isinstance(employee, Employee):
            raise TypeError("Chỉ thêm được đối tượng Employee")
        if employee.employee_id in self._by_id:
            raise ValueError(f"Trùng mã nhân sự: {employee.employee_id}")
        self._employees.append(employee)
        self._by_id[employee.employee_id] = employee

    def find_employee(self, employee_id: str) -> Optional[Employee]:
        return self._by_id.get(employee_id)

    # Mọi phép tổng hợp gọi calculate_gross_pay() qua kiểu chung Employee, không if/else theo loại
    def calculate_total_payroll(self) -> float:
        return sum(e.calculate_gross_pay() for e in self._employees)

    def calculate_payroll_by_department(self, department: str) -> float:
        return sum(e.calculate_gross_pay() for e in self._employees
                   if e.department == department)

    def find_highest_paid_employee(self) -> Optional[Employee]:
        """None nếu rỗng; nếu hòa trả người đứng trước."""
        return max(self._employees, key=lambda e: e.calculate_gross_pay(), default=None)

    def display_payroll(self) -> None:
        print(f"===== BẢNG LƯƠNG KỲ {self._period} =====")
        if not self._employees:
            print("(Bảng lương trống)")
            return
        for e in self._employees:
            e.display_payroll_info()                  # gọi đa hình
        print("-----------------------------------")
        print(f"Tổng bảng lương: {format_money(self.calculate_total_payroll())}")
