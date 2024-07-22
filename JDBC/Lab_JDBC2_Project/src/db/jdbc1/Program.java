package db.jdbc1;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Program {

	private static Connection getConnection() throws SQLException {
		String url = "jdbc:oracle:thin:@156.35.94.98:1521:desa19";
		String username = "";
		String password = "";
		return DriverManager.getConnection(url, username, password);
	}

//	//Examples to read by keyboard
//	System.out.println("Read an integer by keyboard");	
//	int integ = ReadInt();
//	System.out.println("Read a string by keyboard");	
//	String str = ReadString();

	public static void main(String[] args) throws SQLException {
//		exercise1_1();
//		exercise1_2();
//		exercise2();
//		exercise3();
//		exercise4();
//		exercise5_1();
//		exercise5_2();
//		exercise5_3();
//		exercise6_1();
//		exercise6_2();
//		exercise7_1();
//		exercise7_2();
		exercise8();
	}

	/*
	 * 1. Develop a Java method that shows the results of queries 21 and 32 from lab
	 * session SQL2. 1.1. (21) Name and surname of customers that have bought a car
	 * in a 'madrid' dealer that has 'gti' cars.
	 */
	public static void exercise1_1() throws SQLException {
		Connection conn = getConnection();
		Statement stat = conn.createStatement();
		String sentence = " SELECT DISTINCT name, surname FROM Sales s, Dealers d, Customers c "
				+ "WHERE s.dni=c.dni AND s.cifd=d.cifd AND d.cityd='madrid' AND d.cifd IN "
				+ "(SELECT cifd FROM Distribution i, Cars c WHERE i.codecar=c.codecar AND c.model='gti') ";
		ResultSet rs = stat.executeQuery(sentence);

		System.out.println("-------EXERCISE 1_1-------");
		while (rs.next()) {
			System.out.println(rs.getString("name") + " " + rs.getString("surname"));
		}

		// IN THIS ORDER THE CLOSES!!!!
		rs.close();
		stat.close();
		conn.close();
	}

	/*
	 * 1.2. (32) Dealers having an average stockage greater than the average
	 * stockage of all dealers together.
	 */
	public static void exercise1_2() throws SQLException {
		Connection conn = getConnection();
		Statement stat = conn.createStatement();
		String sentence = " SELECT cifd, AVG(stockage) final_avg FROM distribution GROUP BY cifd "
				+ "HAVING AVG(stockage) >= ALL (SELECT AVG (stockage) avg_stockage FROM distribution GROUP by cifd)";
		ResultSet rs = stat.executeQuery(sentence);

		System.out.println("-------EXERCISE 1_2-------");
		while (rs.next()) {
			System.out.println(rs.getString("cifd"));
		}
		// IN THIS ORDER THE CLOSES!!!!
		rs.close();
		stat.close();
		conn.close();
	}

	/*
	 * 2. Develop a Java method that shows the results of query 6 from lab session
	 * SQL2, so that the search color is inputted by the user. (6) Names of car
	 * makers that have sold cars with a color that is inputted by the user.
	 */
	public static void exercise2() throws SQLException {
		Connection conn = getConnection();
		String sentence = " SELECT namecm FROM Carmakers cm, Cmcars cmc, Sales s WHERE cm.cifcm=cmc.cifcm AND cmc.codecar=s.codecar AND s.color=?";

		PreparedStatement stat = conn.prepareStatement(sentence);
		System.out.println("Write a color: ");
		String str = ReadString();
		stat.setString(1, str);
		ResultSet rs = stat.executeQuery();

		System.out.println("-------EXERCISE 2-------");
		while (rs.next()) {
			System.out.println(rs.getString("namecm"));
		}
		// IN THIS ORDER THE CLOSES!!!!
		rs.close();
		stat.close();
		conn.close();
	}

	/*
	 * 3. Develop a Java method to run query 27 from lab session SQL2, so that the
	 * limits for the number of cars are inputted by the user. (27) CIFD of dealers
	 * with a stock between two values inputted by the user inclusive.
	 */
	public static void exercise3() throws SQLException {
		Connection conn = getConnection();
		String sentence = " SELECT cifd FROM distribution GROUP BY cifd HAVING SUM(Stockage)>=? AND SUM(Stockage)<=? ";

		PreparedStatement stat = conn.prepareStatement(sentence);
		System.out.println("Write the min stockage: ");
		int min = ReadInt();
		System.out.println("Write the max stockage: ");
		int max = ReadInt();
		stat.setInt(1, min);
		stat.setInt(2, max);
		ResultSet rs = stat.executeQuery();

		System.out.println("-------EXERCISE 3-------");
		while (rs.next()) {
			System.out.println(rs.getString("cifd"));
		}
		// IN THIS ORDER THE CLOSES!!!!
		rs.close();
		stat.close();
		conn.close();
	}

	/*
	 * 4. Develop a Java method to run query 24 from lab session SQL2, so that the
	 * city of the dealer and the color are inputted by the user. (24) Names of the
	 * customers that have NOT bought cars with a user-defined color at dealers
	 * located in a user-defined city.
	 */
	public static void exercise4() throws SQLException {
		Connection conn = getConnection();
		String sentence = " SELECT name FROM sales s, dealers d, customers c WHERE c.dni=s.dni AND s.color=? AND d.cifd=s.cifd AND d.cityd=? ";

		PreparedStatement stat = conn.prepareStatement(sentence);
		System.out.println("Write a color: ");
		String color = ReadString();
		stat.setString(1, color);
		System.out.println("Write a city: ");
		String city = ReadString();
		stat.setString(2, city);
		ResultSet rs = stat.executeQuery();

		System.out.println("-------EXERCISE 4-------");
		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
		// IN THIS ORDER THE CLOSES!!!!
		rs.close();
		stat.close();
		conn.close();
	}

	/*
	 * 5. Develop a Java method that, using the suitable SQL sentence: 5.1 Creates
	 * cars into the CARS table, taking the data from the user.
	 */
	public static void exercise5_1() throws SQLException {
		Connection conn = getConnection();
		String sentence = " INSERT INTO Cars (codecar, namec, model) VALUES (?,?,?)";

		PreparedStatement stat = conn.prepareStatement(sentence);

		System.out.println("Write a codecar: ");
		String codecar = ReadString();
		stat.setString(1, codecar);

		System.out.println("Write a namec: ");
		String namec = ReadString();
		stat.setString(2, namec);

		System.out.println("Write a model: ");
		String model = ReadString();
		stat.setString(3, model);

		int rs = stat.executeUpdate();

		System.out.println("-------EXERCISE 5.1-------");
		System.out.println("Number of records created: " + rs);
		// IN THIS ORDER THE CLOSES!!!!
		stat.close();
		conn.close();
	}

	/*
	 * 5.2. Deletes a specific car. The code for the car to delete is defined by the
	 * user.
	 */
	public static void exercise5_2() throws SQLException {
		Connection conn = getConnection();
		String sentence = " DELETE FROM Cars WHERE codecar=? ";

		PreparedStatement stat = conn.prepareStatement(sentence);

		System.out.println("Write a codecar: ");
		String codecar = ReadString();
		stat.setString(1, codecar);

		int rs = stat.executeUpdate();

		System.out.println("-------EXERCISE 5.2-------");
		System.out.println("Number of records eliminated: " + rs);
		// IN THIS ORDER THE CLOSES!!!!
		stat.close();
		conn.close();
	}

	/*
	 * 5.3. Updates the name and model for a specific car. The code for the car to
	 * update is defined by the user
	 */
	public static void exercise5_3() throws SQLException {
		Connection conn = getConnection();
		String sentence = " UPDATE Cars SET namec='hola',model='caracola' WHERE codecar=? ";

		PreparedStatement stat = conn.prepareStatement(sentence);

		System.out.println("Write a codecar: ");
		String codecar = ReadString();
		stat.setString(1, codecar);

		int rs = stat.executeUpdate();

		System.out.println("-------EXERCISE 5.3-------");
		System.out.println("Number of records updated: " + rs);
		// IN THIS ORDER THE CLOSES!!!!
		stat.close();
		conn.close();
	}

	/*
	 * 6. Invoke the exercise 10 function and procedure from lab session PL1 from a
	 * Java application. (10) Develop a procedure and a function that take a dealer
	 * id and return the number of sales that were made in that dealer. 6.1.
	 * Function
	 */
	public static void exercise6_1() throws SQLException {
		// JDBC_EX6_FUNCTION

		Connection conn = getConnection();

		CallableStatement cs = conn.prepareCall("{? = call JDBC_EX6_FUNCTION(?)}");

		System.out.println("Write a id of a dealer: ");
		int dealer = ReadInt();
		cs.setInt(2, dealer);

		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.execute();
		int sales = cs.getInt(1);

		System.out.println("-------EXERCISE 6 FUNCTION-------");
		System.out.println("Total sales: " + sales);
		// IN THIS ORDER THE CLOSES!!!!
		cs.close();
		conn.close();
	}

	/*
	 * 6.2. Procedure
	 */
	public static void exercise6_2() throws SQLException {
		// JDBC_EX6_PROCEDURE

		Connection conn = getConnection();

		CallableStatement cs = conn.prepareCall("{call JDBC_EX6_PROCEDURE(?,?)}");

		System.out.println("Write a id of a dealer: ");
		int dealer = ReadInt();
		cs.setInt(1, dealer);

		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.execute();
		int sales = cs.getInt(2);

		System.out.println("-------EXERCISE 6 PROCEDURE-------");
		System.out.println("Total sales: " + sales);
		// IN THIS ORDER THE CLOSES!!!!
		cs.close();
		conn.close();
	}

	/*
	 * 7. Invoke the exercise 11 function and procedure from lab session PL1 from a
	 * Java application. (11) Develop a procedure and a function that take a city
	 * and return the number of customers in that city. 7.1. Function
	 */
	public static void exercise7_1() throws SQLException {
		// JDBC_EX7_FUNCTION

		Connection conn = getConnection();

		CallableStatement cs = conn.prepareCall("{? = call JDBC_EX7_FUNCTION(?)}");

		System.out.println("Write a city: ");
		String city = ReadString();
		cs.setString(2, city);

		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.execute();
		int total = cs.getInt(1);

		System.out.println("-------EXERCISE 7 FUNCTION-------");
		System.out.println("Total customers: " + total);
		// IN THIS ORDER THE CLOSES!!!!
		cs.close();
		conn.close();
	}

	/*
	 * 7.2. Procedure
	 */
	public static void exercise7_2() throws SQLException {
		// JDBC_EX7_PROCEDURE

		Connection conn = getConnection();

		CallableStatement cs = conn.prepareCall("{call JDBC_EX7_PROCEDURE(?,?)}");

		System.out.println("Write a city: ");
		String city = ReadString();
		cs.setString(1, city);

		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.execute();
		int total = cs.getInt(2);

		System.out.println("-------EXERCISE 7 PROCEDURE-------");
		System.out.println("Total customers: " + total);
		// IN THIS ORDER THE CLOSES!!!!
		cs.close();
		conn.close();
	}

	/*
	 * 8. Develop a Java method that displays the cars that have been bought by each
	 * customer. Besides, it must display the number of cars that each customer has
	 * bought and the number of dealers where each customer has bought. Customers
	 * that have bought no cars should not be shown in the report.
	 */
	public static void exercise8() throws SQLException {
		Connection conn = getConnection();

		Statement stat = conn.createStatement();
		String sentence = " SELECT c.dni, name,surname, COUNT(codecar) as numcars, COUNT(cifd) as numdeal FROM sales s, customers c WHERE s.dni=c.dni GROUP BY (c.dni,name,surname)";
		ResultSet customers = stat.executeQuery(sentence);

		sentence = " SELECT c.codecar, namec, model, color FROM Cars c, Sales s WHERE c.codecar=s.codecar AND s.dni=? ";
		PreparedStatement pStat = conn.prepareStatement(sentence);

		System.out.println("-------EXERCISE 8-------");
		while (customers.next()) {
			System.out.println("- Customer: " + customers.getString("name") + " " + customers.getString("surname") + " "
					+ customers.getString("numcars") + " " + customers.getString("numdeal"));
			pStat.setString(1, customers.getString("dni"));
			ResultSet cars = pStat.executeQuery();
			while (cars.next()) {
				System.out.println("---> Car: " + cars.getString("codecar") + " " + cars.getString("namec") + " "
						+ cars.getString("model") + " " + cars.getString("color"));
			}
			cars.close();
		}
		// IN THIS ORDER THE CLOSES!!!!
		customers.close();
		stat.close();
		conn.close();
	}

	@SuppressWarnings("resource")
	private static String ReadString() {
		return new Scanner(System.in).nextLine();
	}

	@SuppressWarnings("resource")
	private static int ReadInt() {
		return new Scanner(System.in).nextInt();
	}
}
