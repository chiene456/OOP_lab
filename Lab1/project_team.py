"""
/****************/
Mã sinh viên: 202418853
Họ tên: Nguyễn Quang Chiến
/****************/

Module: project_team.py
Lớp ProjectTeam - quản lý một nhóm dự án gồm mã, tên, một trưởng nhóm và
danh sách thành viên. Chỉ giữ liên kết KHÔNG SỞ HỮU đến Employee (aggregation)
- không chịu trách nhiệm về vòng đời của Employee.
"""

from typing import List, Optional
from employee import Employee


class ProjectTeam:
    def __init__(self, projectCode: str, projectName: str,
                 leader: Optional[Employee] = None):
        if not projectCode:
            raise ValueError("Mã dự án không được rỗng.")
        if not projectName:
            raise ValueError("Tên dự án không được rỗng.")
        self._projectCode = projectCode
        self._projectName = projectName
        self._leader: Optional[Employee] = None
        self._members: List[Employee] = []

        if leader is not None:
            # Constructor thứ hai: thiết lập trưởng nhóm và tự động
            # đưa trưởng nhóm vào danh sách thành viên.
            self._leader = leader
            self._members.append(leader)

    # ---- Getter ----
    def getProjectCode(self) -> str:
        return self._projectCode

    def getProjectName(self) -> str:
        return self._projectName

    def getLeader(self) -> Optional[Employee]:
        return self._leader

    def getMembers(self) -> List[Employee]:
        return list(self._members)  # trả bản sao, tránh sửa trực tiếp danh sách nội bộ

    # ---- Nạp chồng addMember ----
    def addMember(self, employee: Employee, makeLeader: bool = False) -> bool:
        """
        addMember(employee)              -> chỉ thêm thành viên.
        addMember(employee, makeLeader)  -> thêm (nếu chưa có) và đặt làm trưởng nhóm.
        Không thêm trùng nhân sự (theo id).
        """
        already_member = self.contains(employee.getId())
        if not already_member:
            self._members.append(employee)
        elif not makeLeader:
            # đã tồn tại và không yêu cầu đổi trưởng nhóm -> từ chối
            return False

        if makeLeader:
            self._leader = employee
        return True

    def removeMember(self, employeeId: str) -> bool:
        # Bất biến: không được xóa trưởng nhóm khi chưa chọn trưởng nhóm thay thế.
        if self._leader is not None and self._leader.getId() == employeeId:
            raise ValueError(
                "Không thể xóa trưởng nhóm hiện tại. Hãy đổi trưởng nhóm trước.")
        for i, m in enumerate(self._members):
            if m.getId() == employeeId:
                del self._members[i]
                return True
        return False

    def changeLeader(self, employee: Employee) -> None:
        # Trưởng nhóm mới phải được thêm vào nhóm nếu chưa phải thành viên.
        if not self.contains(employee.getId()):
            self._members.append(employee)
        self._leader = employee

    def contains(self, employeeId: str) -> bool:
        return any(m.getId() == employeeId for m in self._members)

    def calculateTotalMonthlyCost(self) -> float:
        return sum(m.calculateMonthlyCost() for m in self._members)

    def displayTeam(self) -> None:
        print(f"=== Nhóm dự án {self._projectCode} - {self._projectName} ===")
        leader_desc = self._leader.getId() if self._leader else "(chưa có)"
        print(f"Trưởng nhóm: {leader_desc}")
        print("Thành viên:")
        for m in self._members:
            m.displayInfo()  # gọi đa hình - tự động chọn đúng phiên bản ghi đè
        print(f"Tổng chi phí hàng tháng: {self.calculateTotalMonthlyCost():,.0f}")

    def __del__(self):
        # Chỉ hủy cấu trúc liên kết nội bộ (list tham chiếu),
        # KHÔNG hủy các đối tượng Employee mà nó tham chiếu (không sở hữu).
        print(f"[Destructor] Hủy ProjectTeam: {self._projectCode} "
              f"(các Employee liên quan KHÔNG bị hủy theo)")
