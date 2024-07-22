package es.uniovi.eii.bbdd.jdbc.exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * (ES) Desde el main tienen que poder ejecutarse los mÃƒÂ©todos
 * implementados para el examen
 *
 * Si al importar el proyecto te da un problema con la versiÃƒÂ³n de Java:
 * BotÃƒÂ³n secundario sobre el proyecto > "Build Path", "Configure Build
 * Path" > "Libraries". En "Modulepath" elimina la versiÃƒÂ³n de JRE que te
 * estÃƒÂ¡ dando problemas y aÃƒÂ±ade la que tengas en tu equipo desde
 * el botÃƒÂ³n de "Add Library".
 * 
 * (EN)
 * 
 * From this main class it must be possible to execute the methods implemented
 * during the test.
 * 
 * If when importing the project you have a problem with the Java version:
 * Right-click on the project > "Build Path", "Configure Build Path" >
 * "Libraries". In "Modulepath" delete the JRE version that is giving you
 * problems and add the one you have in your computer from the "Add Library"
 * button.
 * 
 * @author Databases 2024 Teaching Staff.
 */
public class Program {

	public static void main(String[] args) throws SQLException { // UO294067-71789634X
		ex1();
		// ex2();

	}

	@SuppressWarnings({ "resource", "unused" })
	private static String ReadString() {
		return new Scanner(System.in).nextLine();
	}

	@SuppressWarnings({ "resource", "unused" })
	private static int ReadInt() {
		return new Scanner(System.in).nextInt();
	}

	private static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@156.35.94.98:1521:desa19";
		String username = "";
		String password = "";
		return DriverManager.getConnection(url, username, password);
	}

	public static void ex1() throws SQLException {
		Connection con = getConnection();
		System.out.println("Department name: ");
		String department = ReadString();

		// String courses="SELECT c.course_id,title,type, dept_building FROM Course c,
		// Department d WHERE d.dept_name=? AND d.dept_name=c.dept_name AND (SELECT
		// COUNT(i.course_id) FROM Is_prerequisite i WHERE i.course_id=c.course_id )>2
		// ORDER BY c.course_id DESC, title DESC";
		String courses = "SELECT c.course_id,title,type, dept_building " + "FROM Course c, Department d "
				+ "WHERE d.dept_name=? AND d.dept_name=c.dept_name AND "
				+ "(SELECT COUNT(i.course_id) FROM Is_prerequisite i WHERE i.course_id=c.course_id )>2 "
				+ "ORDER BY c.course_id DESC, title DESC";

		PreparedStatement p1 = con.prepareStatement(courses);
		p1.setString(1, department);
		ResultSet rsCourses = p1.executeQuery();

//		String sections="SELECT s.sec_id,s.year,s.class_building, i.instructor_name FROM Teaches t, Instructor i, Section s WHERE t.instructor_id=i.instructor_id AND s.course_id=t.course_id AND s.sec_id=t.sec_id AND s.semester=t.semester AND s.year=t.year AND s.course_id=? AND s.semester='Spring' AND i.instructor_name LIKE 'S%' ORDER BY sec_id ASC";
		String sections = "SELECT s.sec_id,s.year,s.class_building, i.instructor_name "
				+ "FROM Teaches t, Instructor i, Section s " + "WHERE t.instructor_id=i.instructor_id AND "
				+ "s.course_id=t.course_id AND s.sec_id=t.sec_id AND s.semester=t.semester AND s.year=t.year AND "
				+ "s.course_id=? AND s.semester='Spring' AND i.instructor_name LIKE 'S%' " + "ORDER BY sec_id ASC";
		PreparedStatement p2 = con.prepareStatement(sections);
		ResultSet rsSections;

		while (rsCourses.next()) {
			System.out.println("COURSE: " + rsCourses.getString("course_id") + " -- " + rsCourses.getString("title")
					+ " --" + rsCourses.getString("type") + "-- " + rsCourses.getString("dept_building"));

			p2.setString(1, rsCourses.getString("course_id"));
			rsSections = p2.executeQuery();

			while (rsSections.next()) {
				System.out.println("->SECTION: " + rsSections.getString("sec_id") + " -- "
						+ rsSections.getString("year") + " -- " + rsSections.getString("class_building") + " --"
						+ rsSections.getString("instructor_name"));
			}

			rsSections.close();
		}

		rsCourses.close();
		p2.close();
		p1.close();
		con.close();
	}

	public static void ex2() throws SQLException {
		Connection con = getConnection();
		String updateSalary = "UPDATE Instructor SET salary=? WHERE instructor_id=?";
		PreparedStatement update = con.prepareStatement(updateSalary);
		int updatedRows = 0;

		// String instructors="SELECT DISTINCT instructor_id FROM Teaches";
		String instructors = "SELECT t.instructor_id, d.budget, i.total_hours, SUM(s.nstudents) as nstudents "
				+ "FROM Teaches t, Instructor i, Department d, "
				+ "(SELECT sec_id,semester,year, course_id,COUNT(*) as nstudents FROM Takes GROUP BY sec_id,semester,year, course_id) s "
				+ "WHERE i.dept_name=d.dept_name " + "AND i.instructor_id=t.instructor_id "
				+ "AND t.sec_id=s.sec_id AND t.semester=s.semester AND t.year=s.year AND t.course_id=s.course_id "
				+ "GROUP BY t.instructor_id, d.budget, i.total_hours";
		Statement p1 = con.createStatement();
		ResultSet rsInstructors = p1.executeQuery(instructors);

		while (rsInstructors.next()) { // for each instructor that teaches
			float budget = rsInstructors.getFloat("budget");
			float total_hours = rsInstructors.getFloat("total_hours");
			int nstudents = rsInstructors.getInt("nstudents");

			float new_Salary_instructor = budget / (total_hours * nstudents);
			update.setFloat(1, new_Salary_instructor);
			update.setString(2, rsInstructors.getString("instructor_id"));
			updatedRows += update.executeUpdate();
		}

		System.out.println("Updated rows: " + updatedRows);

		rsInstructors.close();

		update.close();
		p1.close();

		con.close();
	}
}
