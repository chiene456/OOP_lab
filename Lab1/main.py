"""
/****************/
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
/****************/

Module: main.py
Kịch bản kiểm thử 15 bước cho Employee / SoftwareEngineer / ProjectTeam.

Sơ đồ lớp (dạng văn bản):

    Employee
        - id: str
        - fullName: str
        - baseSalary: float
        + increaseSalary(amount, byPercentage=None)
        + calculateMonthlyCost() -> float           [đa hình / virtual]
        + displayInfo() -> None                     [đa hình / virtual]

    SoftwareEngineer(Employee)  <|-- kế thừa công khai
        - primaryLanguage: str
        - technicalAllowance: float
        + calculateMonthlyCost() -> float           [ghi đè]
        + displayInfo() -> None                     [ghi đè]

    ProjectTeam
        - projectCode: str
        - projectName: str
        - leader: Employee            o-- (liên kết KHÔNG sở hữu, 0..1)
        - members: List[Employee]     o-- (liên kết KHÔNG sở hữu, 0..*)
        + addMember(employee, makeLeader=False) -> bool
        + removeMember(employeeId) -> bool
        + changeLeader(employee) -> None
        + contains(employeeId) -> bool
        + calculateTotalMonthlyCost() -> float
        + displayTeam() -> None
"""

import gc

from employee import Employee
from software_engineer import SoftwareEngineer
from project_team import ProjectTeam


def demo_scope_block(shared_employee: Employee) -> None:
    """
    Mô phỏng một 'khối lệnh cục bộ' như trong C++ ({ ... }):
    project_team2 chỉ tồn tại trong phạm vi hàm này. Khi hàm return,
    biến hết phạm vi, refcount về 0 và __del__ được gọi.
    """
    print("\n--- (13) Tạo nhóm thứ hai, thêm nhân sự đã có ở nhóm thứ nhất ---")
    team2 = ProjectTeam("PRJ02", "Dự án phụ trợ")
    team2.addMember(shared_employee)
    team2.displayTeam()
    print("--- (Kết thúc khối lệnh cục bộ: project_team2 sắp ra khỏi phạm vi) ---")


def main() -> None:
    # (1) Tạo hai Employee bằng hai constructor khác nhau
    print("--- (1) Tạo Employee ---")
    emp1 = Employee("NV01", "Nguyen Van A")                    # constructor 2 tham số
    emp2 = Employee("NV02", "Tran Thi B", 12_000_000)           # constructor 3 tham số

    # (2) Tạo hai SoftwareEngineer bằng hai constructor khác nhau
    print("\n--- (2) Tạo SoftwareEngineer ---")
    se1 = SoftwareEngineer("KS01", "Le Van C", "Python")        # dạng ngắn
    se2 = SoftwareEngineer("KS02", "Pham Thi D", baseSalary=15_000_000,
                            primaryLanguage="C++", technicalAllowance=2_000_000)  # dạng đầy đủ

    # (3) Tăng lương emp1 bằng số tiền cố định
    print("\n--- (3) Tăng lương cố định cho emp1 ---")
    emp1.increaseSalary(1_000_000)
    emp1.displayInfo()

    # (4) Tăng lương emp2 theo phần trăm
    print("\n--- (4) Tăng lương 10% cho emp2 ---")
    emp2.increaseSalary(10, byPercentage=True)
    emp2.displayInfo()

    # (5) Tạo nhóm dự án chưa có trưởng nhóm
    print("\n--- (5) Tạo ProjectTeam chưa có trưởng nhóm ---")
    team1 = ProjectTeam("PRJ01", "Du an chinh")

    # (6) Thêm một nhân sự bằng addMember(employee)
    print("\n--- (6) Thêm emp1 vào nhóm ---")
    print("Kết quả:", team1.addMember(emp1))

    # (7) Thêm một kỹ sư bằng addMember(employee, True) để đặt làm trưởng nhóm
    print("\n--- (7) Thêm se2 và đặt làm trưởng nhóm ---")
    print("Kết quả:", team1.addMember(se2, True))

    # (8) Thử thêm lại một thành viên đã tồn tại
    print("\n--- (8) Thử thêm lại emp1 (đã tồn tại) ---")
    print("Kết quả (phải là False):", team1.addMember(emp1))

    # (9) Hiển thị danh sách bằng lời gọi đa hình
    print("\n--- (9) Hiển thị nhóm (đa hình) ---")
    team1.displayTeam()

    # (10) Tính tổng chi phí nhân sự hằng tháng
    print("\n--- (10) Tổng chi phí hằng tháng ---")
    print("Tổng:", f"{team1.calculateTotalMonthlyCost():,.0f}")

    # (11) Thử xóa trưởng nhóm hiện tại -> phải bị từ chối
    print("\n--- (11) Thử xóa trưởng nhóm hiện tại (se2) ---")
    try:
        team1.removeMember(se2.getId())
    except ValueError as e:
        print("Bị từ chối như mong đợi:", e)

    # (12) Đổi trưởng nhóm rồi xóa người từng là trưởng nhóm
    print("\n--- (12) Đổi trưởng nhóm sang emp1, sau đó xóa se2 ---")
    team1.changeLeader(emp1)
    print("Xóa se2 thành công:", team1.removeMember(se2.getId()))
    team1.displayTeam()

    # (13) + (14) Tạo nhóm thứ hai (chứa nhân sự đã có ở nhóm 1),
    # rồi hủy nhóm thứ hai bằng cách kết thúc khối lệnh cục bộ
    demo_scope_block(emp1)
    gc.collect()  # đảm bảo garbage collector chạy ngay để thấy destructor

    # (15) Chứng minh emp1 (dùng chung) vẫn tồn tại sau khi nhóm 2 bị hủy
    print("\n--- (15) Kiểm tra emp1 vẫn tồn tại sau khi team2 bị hủy ---")
    print("emp1 vẫn thuộc team1:", team1.contains(emp1.getId()))
    emp1.displayInfo()

    print("\n--- Kết thúc chương trình (các Employee còn lại sẽ bị hủy khi thoát) ---")


if __name__ == "__main__":
    main()
