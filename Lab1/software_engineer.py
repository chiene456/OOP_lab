"""
/****************/
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
/****************/

Module: software_engineer.py
Lớp SoftwareEngineer - kế thừa công khai từ Employee, bổ sung ngôn ngữ lập
trình chính và phụ cấp kỹ thuật.
"""

from employee import Employee


class SoftwareEngineer(Employee):
    def __init__(self, id: str, fullName: str, primaryLanguage: str = "",
                 baseSalary: float = 0.0, technicalAllowance: float = 0.0):
        # Hai constructor C++ được gộp thành một __init__:
        #   - gọi ngắn: SoftwareEngineer(id, fullName, primaryLanguage)
        #   - gọi đầy đủ: SoftwareEngineer(id, fullName,
        #         baseSalary=..., primaryLanguage=..., technicalAllowance=...)
        super().__init__(id, fullName, baseSalary)
        if not primaryLanguage:
            raise ValueError("Ngôn ngữ lập trình chính không được rỗng.")
        if technicalAllowance < 0:
            raise ValueError("Phụ cấp kỹ thuật không được âm.")
        self._primaryLanguage = primaryLanguage
        self._technicalAllowance = technicalAllowance

    def getPrimaryLanguage(self) -> str:
        return self._primaryLanguage

    def getTechnicalAllowance(self) -> float:
        return self._technicalAllowance

    # ---- Ghi đè phương thức đa hình ----
    def calculateMonthlyCost(self) -> float:
        return self._baseSalary + self._technicalAllowance

    def displayInfo(self) -> None:
        print(f"[SoftwareEngineer] {self._id} - {self._fullName} - "
              f"Ngôn ngữ: {self._primaryLanguage} - "
              f"Lương: {self._baseSalary:,.0f} + Phụ cấp: {self._technicalAllowance:,.0f} "
              f"= Tổng: {self.calculateMonthlyCost():,.0f}")

    def __del__(self):
        print(f"[Destructor] Hủy SoftwareEngineer: {self._id} - {self._fullName}")
        super().__del__()  # gọi tiếp destructor lớp cha, giống chuỗi hủy trong C++
