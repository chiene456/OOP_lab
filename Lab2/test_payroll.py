# ****************
# Mã sinh viên: 202418853
# Họ tên: Nguyễn Quang Chiến
# ****************
"""test_payroll.py - Kiểm thử mục C và C.1 (biên + lỗi). Chạy: python -m unittest -v"""
import unittest

from employee import Employee
from hourly_employee import HourlyEmployee
from main import build_sample_payroll
from payroll import Payroll
from salaried_employee import SalariedEmployee
from sales_employee import SalesEmployee


class TestSampleData(unittest.TestCase):
    """Dữ liệu mẫu theo đề."""

    def setUp(self):
        self.p = build_sample_payroll()

    def test_e001_salaried(self):
        self.assertAlmostEqual(self.p.find_employee("E001").calculate_gross_pay(), 18_000_000)

    def test_e002_hourly_no_overtime(self):
        self.assertAlmostEqual(self.p.find_employee("E002").calculate_gross_pay(), 15_500_000)

    def test_e003_hourly_overtime(self):
        self.assertAlmostEqual(self.p.find_employee("E003").calculate_gross_pay(), 17_500_000)

    def test_e004_sales_with_rate_bonus(self):
        self.assertAlmostEqual(self.p.find_employee("E004").calculate_gross_pay(), 19_000_000)

    def test_total_payroll(self):
        self.assertAlmostEqual(self.p.calculate_total_payroll(), 70_000_000)

    def test_department_support(self):
        self.assertAlmostEqual(self.p.calculate_payroll_by_department("Hỗ trợ"), 33_000_000)

    def test_unknown_department_is_zero(self):
        self.assertEqual(self.p.calculate_payroll_by_department("Không có"), 0)

    def test_highest_paid(self):
        self.assertEqual(self.p.find_highest_paid_employee().employee_id, "E004")

    def test_polymorphic_types(self):
        types = [self.p.find_employee(i).get_employee_type() for i in ("E001", "E002", "E004")]
        self.assertEqual(types, ["SalariedEmployee", "HourlyEmployee", "SalesEmployee"])


class TestHourly(unittest.TestCase):
    def test_exactly_160_hours_no_overtime(self):
        self.assertAlmostEqual(HourlyEmployee("H", "T", "D", 100_000, 160).calculate_gross_pay(), 16_000_000)

    def test_max_250_hours(self):
        # 160 x 100k + 90 x 150k = 29.5 triệu
        self.assertAlmostEqual(HourlyEmployee("H", "T", "D", 100_000, 250).calculate_gross_pay(), 29_500_000)

    def test_zero_hours(self):
        self.assertEqual(HourlyEmployee("H", "T", "D", 100_000, 0).calculate_gross_pay(), 0)

    def test_hours_above_250(self):
        with self.assertRaises(ValueError):
            HourlyEmployee("H", "T", "D", 100_000, 251)

    def test_negative_hours(self):
        with self.assertRaises(ValueError):
            HourlyEmployee("H", "T", "D", 100_000, -1)

    def test_negative_rate(self):
        with self.assertRaises(ValueError):
            HourlyEmployee("H", "T", "D", -1, 100)


class TestSales(unittest.TestCase):
    def test_commission_upper_bound(self):
        self.assertAlmostEqual(SalesEmployee("S", "T", "D", 0, 100_000_000, 0.3).calculate_gross_pay(), 30_000_000)

    def test_commission_above_bound(self):
        with self.assertRaises(ValueError):
            SalesEmployee("S", "T", "D", 0, 1, 0.31)

    def test_commission_negative(self):
        with self.assertRaises(ValueError):
            SalesEmployee("S", "T", "D", 0, 1, -0.01)

    def test_update_revenue(self):
        s = SalesEmployee("S", "T", "D", 1_000_000, 10_000_000, 0.1)
        s.update_sales_revenue(20_000_000)
        self.assertAlmostEqual(s.calculate_gross_pay(), 3_000_000)

    def test_update_revenue_negative(self):
        s = SalesEmployee("S", "T", "D", 1_000_000, 10_000_000, 0.1)
        with self.assertRaises(ValueError):
            s.update_sales_revenue(-5)
        self.assertEqual(s.sales_revenue, 10_000_000)       # trạng thái không đổi


class TestBonusOverload(unittest.TestCase):
    def setUp(self):
        self.e = SalariedEmployee("B", "Test")

    def test_three_versions_accumulate(self):
        self.e.add_bonus(1_000_000)
        self.e.add_bonus(500_000, "Hoàn thành dự án")
        self.e.add_bonus(0.1, 2_000_000, "Theo doanh thu")
        self.assertAlmostEqual(self.e.monthly_bonus, 1_700_000)
        self.assertEqual(len(self.e.bonus_history), 3)

    def test_reason_as_keyword(self):
        self.e.add_bonus(0.02, 50_000_000, reason="x")
        self.assertAlmostEqual(self.e.monthly_bonus, 1_000_000)

    def test_reset(self):
        self.e.add_bonus(100)
        self.e.reset_monthly_bonus()
        self.assertEqual((self.e.monthly_bonus, len(self.e.bonus_history)), (0, 0))

    def test_amount_zero(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(0)

    def test_amount_negative(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(-100, "lý do")

    def test_empty_reason(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(100, "")

    def test_blank_reason(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(100, "   ")

    def test_rate_zero(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(0, 100, "x")

    def test_rate_above_half(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(0.51, 100, "x")

    def test_reference_zero(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(0.1, 0, "x")

    def test_rate_half_boundary(self):
        self.e.add_bonus(0.5, 1_000_000, "biên")
        self.assertAlmostEqual(self.e.monthly_bonus, 500_000)

    def test_failed_bonus_does_not_change_state(self):
        with self.assertRaises(ValueError):
            self.e.add_bonus(0.9, 100, "x")
        self.assertEqual(self.e.monthly_bonus, 0)

    def test_wrong_arity(self):
        with self.assertRaises(TypeError):
            self.e.add_bonus()
        with self.assertRaises(TypeError):
            self.e.add_bonus(1, 2, 3, 4)

    def test_two_numbers_is_ambiguous(self):
        with self.assertRaises(TypeError):
            self.e.add_bonus(0.1, 1000)


class TestInvariantsAndConstructors(unittest.TestCase):
    def test_employee_is_abstract(self):
        with self.assertRaises(TypeError):
            Employee("X", "Test")

    def test_empty_id(self):
        with self.assertRaises(ValueError):
            SalariedEmployee("", "Test")

    def test_empty_name(self):
        with self.assertRaises(ValueError):
            SalariedEmployee("X", "")

    def test_empty_department(self):
        with self.assertRaises(ValueError):
            SalariedEmployee("X", "Test", "")

    def test_negative_salary(self):
        with self.assertRaises(ValueError):
            SalariedEmployee("X", "T", "D", -1, 0)

    def test_negative_allowance(self):
        with self.assertRaises(ValueError):
            SalariedEmployee("X", "T", "D", 0, -1)

    def test_negative_initial_bonus(self):
        with self.assertRaises(ValueError):
            SalariedEmployee("X", "T", "D", 0, 0, -1)

    def test_short_constructor_defaults(self):
        e = SalariedEmployee("X", "Test")
        self.assertEqual((e.department, e.monthly_bonus, e.calculate_gross_pay()), ("Unassigned", 0, 0))


class TestPayroll(unittest.TestCase):
    def test_duplicate_id_rejected(self):
        p = build_sample_payroll()
        with self.assertRaises(ValueError):
            p.add_employee(SalariedEmployee("E001", "Người trùng mã"))
        self.assertEqual(len(p), 4)

    def test_find_missing_returns_none(self):
        self.assertIsNone(build_sample_payroll().find_employee("E999"))

    def test_add_non_employee(self):
        with self.assertRaises(TypeError):
            build_sample_payroll().add_employee(None)

    def test_empty_payroll(self):
        p = Payroll("2026-10")
        p.display_payroll()                                  # không được lỗi
        self.assertEqual(p.calculate_total_payroll(), 0)
        self.assertIsNone(p.find_highest_paid_employee())

    def test_empty_period(self):
        with self.assertRaises(ValueError):
            Payroll("  ")


if __name__ == "__main__":
    unittest.main(verbosity=2)
