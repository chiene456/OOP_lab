"""
/****************/
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
/****************/

Module: employee.py
Lớp Employee - đại diện một nhân sự, quản lý thông tin cá nhân và lương cơ bản.
Quan hệ: được ProjectTeam tham chiếu (aggregation - liên kết không sở hữu).
"""

from typing import Optional


class Employee:
    def __init__(self, id: str = "UNKNOWN", fullName: str = "Unnamed employee",
                 baseSalary: float = 0.0):
        # Ba constructor C++ (Employee(); Employee(id, fullName);
        # Employee(id, fullName, baseSalary)) được gộp thành một __init__
        # nhờ tham số mặc định.
        if not id:
            raise ValueError("Mã nhân sự không được rỗng.")
        if not fullName:
            raise ValueError("Họ tên không được rỗng.")
        if baseSalary < 0:
            raise ValueError("Lương cơ bản không được âm.")
        self._id = id
        self._fullName = fullName
        self._baseSalary = baseSalary

    # ---- Getter ----
    def getId(self) -> str:
        return self._id

    def getFullName(self) -> str:
        return self._fullName

    def getBaseSalary(self) -> float:
        return self._baseSalary

    # ---- Nạp chồng increaseSalary (mô phỏng bằng tham số byPercentage) ----
    def increaseSalary(self, amount: float, byPercentage: Optional[bool] = None) -> None:
        """
        increaseSalary(amount)                    -> tăng một số tiền cố định.
        increaseSalary(value, byPercentage=True)   -> tăng theo tỷ lệ phần trăm.
        increaseSalary(value, byPercentage=False)  -> tăng theo số tiền cố định.
        """
        if amount <= 0:
            raise ValueError("Giá trị tăng lương phải dương.")
        if byPercentage:
            self._baseSalary += self._baseSalary * amount / 100.0
        else:
            self._baseSalary += amount

    # ---- Phương thức đa hình (virtual trong C++) ----
    def calculateMonthlyCost(self) -> float:
        return self._baseSalary

    def displayInfo(self) -> None:
        print(f"[Employee] {self._id} - {self._fullName} - "
              f"Lương: {self._baseSalary:,.0f}")

    def __del__(self):
        # Mô phỏng destructor virtual có in thông báo quan sát vòng đời.
        print(f"[Destructor] Hủy Employee: {self._id} - {self._fullName}")
