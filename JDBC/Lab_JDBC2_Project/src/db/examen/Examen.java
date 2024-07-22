package db.examen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Examen {
	private static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@156.35.94.98:1521:desa19";
		String username = "";
		String password = "";
		return DriverManager.getConnection(url, username, password);
	}

	public static void main(String[] args) throws SQLException {
		ejerPLSQLEX();
	}

	public static void ejerPLSQLEX() throws SQLException {
		Connection con = getConnection();

		String departments = "SELECT d.dept_name,budget,COUNT(*) as numCourses, SUM(credits) as totCredits FROM Department d,Course c WHERE c.dept_name=d.dept_name AND c.type='Compulsory' AND d.dept_name NOT IN (SELECT DISTINCT dept_name FROM Course WHERE type!='Compulsory') GROUP BY d.dept_name,budget ORDER BY d.dept_name DESC, numCourses DESC";
		Statement p1 = con.createStatement();
		ResultSet rsDepartments = p1.executeQuery(departments);

		String instructor = "SELECT i.instructor_name, t.course_id, c.title,t.sec_id,t.semester,t.year, s.max_students FROM Instructor i, Teaches t, Course c, Section s WHERE i.dept_name=? AND t.instructor_id=i.instructor_id AND c.course_id=t.course_id AND total_hours>(SELECT AVG(total_hours) FROM Instructor) AND s.sec_id=t.sec_id AND s.course_id=t.course_id AND s.semester=t.semester AND s.year=t.year ORDER BY instructor_name DESC";
		PreparedStatement p2 = con.prepareStatement(instructor);
		ResultSet rsInstructors;

		while (rsDepartments.next()) {
			System.out.println(
					"DEPARTMENT: " + rsDepartments.getString("dept_name") + " " + rsDepartments.getFloat("budget") + " "
							+ rsDepartments.getInt("numCourses") + " " + rsDepartments.getInt("totCredits"));

			p2.setString(1, rsDepartments.getString("dept_name"));
			rsInstructors = p2.executeQuery();

			while (rsInstructors.next()) {
				System.out.println("--INSTRUCTOR/SECTION: " + rsInstructors.getString("instructor_name") + " "
						+ rsInstructors.getString("course_id") + " " + rsInstructors.getString("title") + " "
						+ rsInstructors.getString("sec_id") + " " + rsInstructors.getString("semester") + " "
						+ rsInstructors.getString("year") + " " + rsInstructors.getString("max_students"));
			}
			rsInstructors.close();
		}

		rsDepartments.close();
		p2.close();
		p1.close();
		con.close();
	}

	public static void infoCourses() throws SQLException {
		Connection con = getConnection();

		String courses = "SELECT c.course_id, title,dept_name, COUNT(*) as sec FROM Course c, Section s WHERE c.course_id=s.course_id GROUP BY c.course_id, title,dept_name";
		Statement p1 = con.createStatement();
		ResultSet rsCourses = p1.executeQuery(courses);

		String sections = "SELECT sec_id,semester,year,class_building, room_number FROM Section WHERE course_id=? ORDER BY year ASC, sec_id ASC";
		PreparedStatement p2 = con.prepareStatement(sections);
		ResultSet rsSections;

		String teachers = "SELECT i.instructor_id, instructor_name FROM Instructor i, Teaches t WHERE i.instructor_id=t.instructor_id AND course_id=? AND sec_id=? AND semester=? AND year=?";
		PreparedStatement p3 = con.prepareStatement(teachers);
		ResultSet rsTeachers;

		String students = "SELECT s.student_id,student_name FROM Student s, Takes t WHERE s.student_id=t.student_id AND course_id=? AND sec_id=? AND semester=? AND year=?";
		PreparedStatement p4 = con.prepareStatement(students);
		ResultSet rsStudents;

		while (rsCourses.next()) {
			System.out.println("Course: " + rsCourses.getString("course_id") + " " + rsCourses.getString("title") + " "
					+ rsCourses.getInt("sec") + " " + rsCourses.getString("dept_name"));

			p2.setString(1, rsCourses.getString("course_id"));
			rsSections = p2.executeQuery();
			while (rsSections.next()) {
				System.out.println("  Section: " + rsSections.getString("sec_id") + " "
						+ rsSections.getString("semester") + " " + rsSections.getInt("year") + " -> "
						+ rsSections.getString("class_building") + " " + rsSections.getString("room_number"));

				p3.setString(1, rsCourses.getString("course_id"));
				p3.setString(2, rsSections.getString("sec_id"));
				p3.setString(3, rsSections.getString("semester"));
				p3.setInt(4, rsSections.getInt("year"));
				rsTeachers = p3.executeQuery();

				while (rsTeachers.next()) {
					System.out.println("    Teachers: " + rsTeachers.getString("instructor_id") + " "
							+ rsTeachers.getString("instructor_name"));
				}
				rsTeachers.close();

				p4.setString(1, rsCourses.getString("course_id"));
				p4.setString(2, rsSections.getString("sec_id"));
				p4.setString(3, rsSections.getString("semester"));
				p4.setInt(4, rsSections.getInt("year"));
				rsStudents = p4.executeQuery();
				System.out.println("    -----------");
				while (rsStudents.next()) {
					System.out.println("    Students: " + rsStudents.getString("student_id") + " "
							+ rsStudents.getString("student_name"));
				}
				rsStudents.close();

			}
			System.out.println();
			rsSections.close();
		}

		rsCourses.close();
		p4.close();
		p3.close();
		p2.close();
		p1.close();
		con.close();
	}
}
