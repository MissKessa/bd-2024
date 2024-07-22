
package db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQL1 {
	private static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@156.35.94.98:1521:desa19";
		String username = "";
		String password = "";
		return DriverManager.getConnection(url, username, password);
	}

	public static void main(String[] args) throws SQLException {
		ex5();
	}

//	1) Get all the data about the cars stored in the entity (table) CARS.
	public static void ex1() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT * FROM Cars";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("codecar") + " - " + rs.getString("namec") + " - " + rs.getString("model"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	2) Get all the data about the cars in the CARS table with ‘gtd’ model.
	public static void ex2() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT * FROM Cars WHERE model='gtd'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("codecar") + " - " + rs.getString("namec") + " - " + rs.getString("model"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	3) Insert a new car into the CARS entity.
	public static void ex3() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "INSERT INTO Cars VALUES('40','a','a')";
		int rs = stat.executeUpdate(sentence);

		System.out.println(rs);

		stat.close();
		con.close();
	}

//	4) Delete an existing car from the CARS entity.
	public static void ex4() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "DELETE FROM Cars WHERE codecar='40'";
		int rs = stat.executeUpdate(sentence);

		System.out.println(rs);

		stat.close();
		con.close();
	}

//	5) Update some data about a given car in the CARS entity.
	public static void ex5() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "INSERT INTO Cars VALUES('40','a','a')";
		int rs = stat.executeUpdate(sentence);
		System.out.println(rs);

		sentence = "UPDATE Cars SET codecar='4000' WHERE codecar='40'";
		rs = stat.executeUpdate(sentence);
		System.out.println(rs);

		sentence = "DELETE FROM Cars WHERE codecar='4000'";
		rs = stat.executeUpdate(sentence);
		System.out.println(rs);

		stat.close();
		con.close();
	}
}
