# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""main.py - Chạy dữ liệu mẫu của đề (mục C): bốn nhân sự E001-E004."""
from hourly_employee import HourlyEmployee
from payroll import Payroll
from salaried_employee import SalariedEmployee
from sales_employee import SalesEmployee


def build_sample_payroll() -> Payroll:
    payroll = Payroll("2026-09")
    payroll.add_employee(SalariedEmployee("E001", "Nguyễn Minh An", "Đào tạo",
                                          15_000_000, 2_000_000, 1_000_000))
    payroll.add_employee(HourlyEmployee("E002", "Trần Thu Bình", "Hỗ trợ",
                                        100_000, 150, 500_000))
    payroll.add_employee(HourlyEmployee("E003", "Lê Hoàng Chi", "Hỗ trợ", 100_000, 170))
    e004 = SalesEmployee("E004", "Phạm Quốc Dũng", "Kinh doanh", 8_000_000, 200_000_000, 0.05)
    e004.add_bonus(0.02, 50_000_000, "Thưởng theo tỷ lệ")   # phiên bản 3 đối số
    payroll.add_employee(e004)
    return payroll


if __name__ == "__main__":
    payroll = build_sample_payroll()
    payroll.display_payroll()
    top = payroll.find_highest_paid_employee()
    print(f"Thu nhập cao nhất: {top.employee_id} - {top.full_name}")
    print(f"Tổng phòng Hỗ trợ: "
          f"{payroll.calculate_payroll_by_department('Hỗ trợ'):,.0f}".replace(",", "."))
