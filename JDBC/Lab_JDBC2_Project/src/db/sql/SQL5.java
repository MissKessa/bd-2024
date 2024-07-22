package db.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQL5 {
	private static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@156.35.94.98:1521:desa19";
		String username = "";
		String password = "";
		return DriverManager.getConnection(url, username, password);
	}

	@SuppressWarnings("resource")
	private static String ReadString() {
		return new Scanner(System.in).nextLine();
	}

	@SuppressWarnings("resource")
	private static int ReadInt() {
		return new Scanner(System.in).nextInt();
	}

	public static void main(String[] args) throws SQLException {
		extraPLSQL2();
	}

//	1.Create a stored procedure that displays, for the department(s) with the
//	maximum number of employees: the department number, department name, and the total
//	number of employees working in it. Next, for that department, list the employees who work in
//	it as follows:
//	Department: department_id department_name num_employees_in_department
//	-- Employee: employee_id first_name job_title
//	-- Employee: employee_id first_name job_title
	public static void extraPLSQL1() throws SQLException {
		Connection con = getConnection();

		String maxDepartment = "SELECT e.department_id,department_name, COUNT(*) as num FROM Employees e, Departments d WHERE e.department_id=d.department_id GROUP BY e.department_id,department_name ORDER BY COUNT(*) DESC FETCH FIRST 1 ROW WITH TIES";
		Statement p1 = con.createStatement();
		ResultSet rsDeps = p1.executeQuery(maxDepartment);

		String employees = "SELECT employee_id,first_name,job_title FROM Employees e, Jobs j WHERE e.department_id=? AND e.job_id=j.job_id";
		PreparedStatement p2 = con.prepareStatement(employees);
		ResultSet rsEmp;

		while (rsDeps.next()) {
			System.out.println("Department: " + rsDeps.getString("department_id") + " "
					+ rsDeps.getString("department_name") + " " + rsDeps.getInt("num"));

			p2.setString(1, rsDeps.getString("department_id"));
			rsEmp = p2.executeQuery();

			while (rsEmp.next()) {
				System.out.println("-- Employee: " + rsEmp.getString("employee_id") + " "
						+ rsEmp.getString("first_name") + " " + rsEmp.getString("job_title"));
			}
			rsEmp.close();
		}

		rsDeps.close();
		p2.close();
		p1.close();
		con.close();
	}

//	2. Create a stored procedure that shows, for the employee who manages the
//	most employees: the employee code, their name, and the number of employees they manage.
//	For each of these employees under their supervision, indicate the commission percentage
//	(commission_pct), the department name, and the state or province where the department is located.
//	The employees should be sorted in descending order by employee_id. The requested information
//	should be displayed as follows:
//
//	Manager : employee_id first_name num_employees_managed
//	--Employee: employee_id first_name commission_pct department_name state_province_of_department
//	--Employee: employee_id first_name commission_pct department_name state_province_of_department
	public static void extraPLSQL2() throws SQLException {
		Connection con = getConnection();

		String superManager = "SELECT employee_id, first_name, numEm FROM Employees e, (SELECT manager_id, COUNT(*) as numEm FROM Employees GROUP BY manager_id) m WHERE e.employee_id=m.manager_id ORDER BY numEm DESC FETCH FIRST 1 ROW WITH TIES";
		Statement p1 = con.createStatement();
		ResultSet rsMan = p1.executeQuery(superManager);

		String employees = "SELECT e.employee_id, e.first_name, e.commission_pct, d.department_name, l.state_province FROM Employees e, Departments d, Locations l WHERE e.manager_id=? AND d.department_id=e.department_id AND d.location_id=l.location_id ORDER BY e.employee_id DESC";
		PreparedStatement p2 = con.prepareStatement(employees);
		ResultSet rsEmp;

		while (rsMan.next()) {
			System.out.println("Manager : " + rsMan.getString("employee_id") + " " + rsMan.getString("first_name") + " "
					+ rsMan.getInt("numEm"));

			p2.setString(1, rsMan.getString("employee_id"));
			rsEmp = p2.executeQuery();
			while (rsEmp.next()) {
				System.out.println("--Employee: " + rsEmp.getString("employee_id") + " " + rsEmp.getString("first_name")
						+ " " + rsEmp.getFloat("commission_pct") + " " + rsEmp.getString("department_name") + " "
						+ rsEmp.getString("state_province"));
			}
			rsEmp.close();
		}

		rsMan.close();
		p2.close();
		p1.close();
		con.close();
	}

	public static void inoutProcedure() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{call PRUEBARAQUEK(?,?,?)}");

		int a = 1;
		int c = 4;
		cs.setInt(1, a);

		cs.registerOutParameter(2, java.sql.Types.INTEGER);

		cs.setInt(3, c);
		cs.registerOutParameter(3, java.sql.Types.INTEGER);

		cs.execute();

		int b = cs.getInt(2);
		int newC = cs.getInt(3);

		System.out.println(a + " " + b + " " + newC);

		cs.close();
		con.close();
	}

//	1. Last_name, job, salary, and commission_pct of those employees that DO earn commissions.
//	Results should be ordered by job ascendingly and by salary descendingly.
	public static void ex1() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT last_name, job_id,salary, commission_pct FROM Employees WHERE commission_pct >0 ORDER BY job_id ASC, salary DESC";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("last_name") + " " + rs.getString("job_id") + " " + rs.getFloat("salary")
					+ " " + rs.getFloat("commission_pct"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	2. Employees with last_name beginning with J, K, L or M.
	public static void ex2() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT last_name FROM Employees WHERE last_name LIKE 'J%' OR last_name LIKE 'K%' OR last_name LIKE 'L%' OR last_name LIKE 'M%'";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("last_name"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	3. Show last_name, employee_id of employees, together with last_name, employee_id of their	managers.
//	4. Last_name and hire_date for employees hired after employee ‘Davies’.
//	5. Last_name and hire_date of employees hired before their managers. Show the last_name and
//	hire_date of their managers as well.

//	6. Department id, and minimum salary of departments with the greatest average salary.
	public static void ex6() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT department_id, MIN(salary) as min FROM Employees GROUP BY department_id HAVING AVG(salary)>=ALL(SELECT AVG(salary) FROM Employees GROUP BY department_id)";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("department_id") + " " + rs.getFloat("min"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	7. Department id, name, and location for departments where there are no ‘SA_REP’ (job_id) employees working.
	public static void ex7() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT department_id, department_name,location_id FROM Departments WHERE department_id NOT IN(SELECT department_id FROM Employees WHERE job_id='SA_REP')";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("department_id") + " " + rs.getString("department_name") + " "
					+ rs.getString("location_id"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	8. Department id, name, and number of employees working at it for departments having: a) less
//	than 3 employees, b) the maximum number of employees, c) the minimum number of employees.
	public static void ex8() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();

		String sentence = "SELECT d.department_id, d.department_name, COUNT(e.employee_id) as total FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name HAVING COUNT(e.employee_id)<3";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(
					rs.getString("department_id") + " " + rs.getString("department_name") + " " + rs.getFloat("total"));
		}
		System.out.println("---------");
		sentence = "SELECT d.department_id, d.department_name, COUNT(e.employee_id) as total FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name ORDER BY COUNT(e.employee_id) DESC FETCH FIRST 1 ROW WITH TIES";
		rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(
					rs.getString("department_id") + " " + rs.getString("department_name") + " " + rs.getFloat("total"));
		}

		System.out.println("---------");
		sentence = "SELECT d.department_id, d.department_name, COUNT(e.employee_id) as total FROM Departments d, Employees e WHERE d.department_id=e.department_id GROUP BY d.department_id, d.department_name ORDER BY COUNT(e.employee_id) ASC FETCH FIRST 1 ROW WITH TIES";
		rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(
					rs.getString("department_id") + " " + rs.getString("department_name") + " " + rs.getFloat("total"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	*************9. Job title (job_id) that was fulfilled (hire_date) in the first half of 1990, and also in the first half of 1991.
	public static void ex9() throws SQLException {
		Connection con = getConnection();
		String sentence = "(SELECT job_id FROM Employees WHERE hire_date BETWEEN to_date('1990-01-01','yyyy-mm-dd') AND to_date('1990-06-30','yyyy-mm-dd')) "
				+ "INTERSECT (SELECT job_id FROM Employees WHERE hire_date BETWEEN to_date('1991-01-01','yyyy-mm-dd') AND to_date('1991-06-30','yyyy-mm-dd'))";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("job_id"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	10. Show the top 3 employees that earn the most from the employees table
	public static void ex10() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT last_name FROM Employees ORDER BY salary DESC FETCH FIRST 3 ROWS ONLY";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("last_name"));
		}

		rs.close();
		stat.close();
		con.close();
	}
//	11. Employee_id and surname of employees that work in the state of California.

//	12. Show the employee with the least salary difference compared to his boss. Show the
//	employee_id and salary of both the employee and the boss and the salary difference.
	public static void ex12() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT e.employee_id as ei,m.employee_id as mi FROM Employees e, Employees m WHERE e.manager_id=m.employee_id AND m.employee_id IS NOT NULL ORDER BY m.salary-e.salary ASC FETCH FIRST 1 ROW WITH TIES";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("ei") + " " + rs.getString("mi"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	13. List employees whose salary is not in the appropriate range for their job (job_id). Show, in
//	addition to the employee_id, its salary and the minimum and maximum salaries of the job.
	public static void ex13() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT employee_id, salary, min_salary,max_salary FROM employees e, jobs j WHERE e.job_id=j.job_id AND salary not between min_salary and max_salary";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("employee_id"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	14. List the employee code, their name (first_name) and how many employees they are the
//	manager of, for all employees who are managers of someone else.
	public static void ex14() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT employee_id, first_name, b.cuantos FROM Employees e, (SELECT manager_id,COUNT(*) as cuantos FROM Employees GROUP BY manager_id) b WHERE e.employee_id=b.manager_id";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("employee_id"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	15. Show the employee code and name (first_name) of the employee who is the manager of more employees.
	public static void ex15() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT employee_id, first_name, b.cuantos FROM Employees e, (SELECT manager_id,COUNT(*) as cuantos FROM Employees GROUP BY manager_id) b WHERE e.employee_id=b.manager_id ORDER BY cuantos DESC FETCH FIRST 1 ROW WITH TIES";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("employee_id"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	16. The salary of employees working in a job whose minimum wage (min_salary) is 4,200 must be increased by 15%.
	public static void ex16() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT employee_id, salary*1.15 as new FROM Employees e, Jobs j WHERE e.job_id=j.job_id AND j.min_salary=4200";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("employee_id") + " " + rs.getFloat("new"));
		}

		rs.close();
		stat.close();
		con.close();
	}
}
