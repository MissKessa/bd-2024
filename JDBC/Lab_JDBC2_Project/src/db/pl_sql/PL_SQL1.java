package db.pl_sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PL_SQL1 {
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
		ex11_function();
	}

//	2. Code a stored procedure that prints “Hello World”.
//	3. Code a stored procedure that takes a name as a parameter, and then prints “Hello” + name.
//	4. Code a stored procedure that shows the maximum value for the stockage attribute in table distribution.
//	5. Code a stored procedure that, given a specific dealer, shows the total number of cars stocked.
	public static void ex5() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT SUM(stockage) as sum FROM Distribution WHERE cifd=?";
		PreparedStatement stat = con.prepareStatement(sentence);

		System.out.println("Dealer: ");
		String dealer = ReadString();
		stat.setString(1, dealer);

		ResultSet rs = stat.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getFloat("sum"));
		}

		rs.close();
		stat.close();
		con.close();
	}
//	6. Create a table with these attributes: total number of sales, total number of cars, total number
//	of car makers, total number of customers, total number of dealers. 
//  Create a stored procedure that queries the database to compute each of those values, stores them in variables, and then
//	inserts them into the table. Check that the insertion has been performed correctly.

//	7. Create a new table CustomersLog using dni as primary key. Develop a stored procedure that
//	copies the contents from the Customers table into that table. Run the procedure twice.

//	8. Develop a stored procedure that prints the objects created by the user.

//	9. Create a new table Purchases with the following information: dni, name, surname, number of
//	cars bought (per customer). Develop a PL/SQL procedure that uses a read cursor to insert the
//	information into the new table.

//	10. Develop a stored procedure that takes a dealer id, and then returns the number of sales that were
//	made in that dealer. Develop a function with the same functionality.
	public static void ex10_procedure() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{call PL1_EX10(?,?)}");

		System.out.println("Dealer:");
		int id = ReadInt();
		cs.setInt(1, id);
		cs.registerOutParameter(2, java.sql.Types.INTEGER);
		cs.execute();
		int sales = cs.getInt(2);
		System.out.println(sales);
		cs.close();
		con.close();
	}

	public static void ex10_function() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{? = call PL1_EX10_FUNCTION(?)}");
		cs.registerOutParameter(1, java.sql.Types.INTEGER);

		System.out.println("Dealer:");
		String dealer = ReadString();
		cs.setString(2, dealer);

		cs.execute();
		int sales = cs.getInt(1);
		System.out.println(sales);

		cs.close();
		con.close();
	}

	public static void ex10_jdbc() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT COUNT(*) as total FROM Sales WHERE cifd=?";
		PreparedStatement stat = con.prepareStatement(sentence);

		System.out.println("Dealer: ");
		String dealer = ReadString();
		stat.setString(1, dealer);
		ResultSet rs = stat.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	11. Develop a PL/SQL function that takes a city and then returns the number of customers in that
//	city. Develop a stored procedure with the same functionality.
	public static void ex11_function() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{? = call PL1_EX11(?)}");
		cs.registerOutParameter(1, java.sql.Types.INTEGER);

		System.out.println("City:");
		String city = ReadString();
		cs.setString(2, city);

		cs.execute();
		int customers = cs.getInt(1);
		System.out.println(customers);

		cs.close();
		con.close();
	}

	public static void ex11_jdbc() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT COUNT(*) as total FROM Customers WHERE city=?";
		PreparedStatement stat = con.prepareStatement(sentence);

		System.out.println("City: ");
		String city = ReadString();
		stat.setString(1, city);
		ResultSet rs = stat.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	12. Develop a stored procedure called ListCarsByCustomer. The procedure should generate a
//	report with the cars that have been bought by each customer with this template:
//	- Customer: name1 surname1 numcars1 numdeal1
//	---> Car: codecar1 namec1 model1 color1
//	---> Car: codecar2 namec2 model2 color2
//	---> . . .
//	- Customer: name2 surname2 numcars2 numdeal2
//	---> Car: codecar1 namec1 model1 color1
//	---> Car: codecar2 namec2 model2 color2
//	---> . . .
//	- . . .
//	Customers that have bought no cars should not be shown in the report.
	public static void ex12() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();

		String customers = "SELECT c.dni as d, name,surname,COUNT(codecar) as numcars, COUNT(DISTINCT cifd) as numdeal FROM Sales s, Customers c WHERE c.dni=s.dni GROUP BY c.dni,name, surname";
		String cars = "SELECT s.codecar as c, namec, model, color FROM Sales s,Cars c WHERE s.codecar=c.codecar AND s.dni=?";

		PreparedStatement pstat = con.prepareStatement(cars);
		ResultSet rs = stat.executeQuery(customers);
		ResultSet rs2;

		while (rs.next()) {
			System.out.println("- Customer: " + rs.getString("name") + " " + rs.getString("surname") + " "
					+ rs.getString("numcars") + " " + rs.getString("numdeal"));

			pstat.setString(1, rs.getString("d"));
			rs2 = pstat.executeQuery();
			while (rs2.next()) {
				System.out.println("---> Car: " + rs2.getString("c") + " " + rs2.getString("namec") + " "
						+ rs2.getString("model") + " " + rs2.getString("color"));
			}
			rs2.close();
		}
		rs.close();

		pstat.close();
		stat.close();
		con.close();
	}

//	13. Develop a stored procedure ListCarsOneCustomer that does the same as the previous
//	one, but just for one customer. The dni of that customer should be passed as an in parameter of the procedure.
	public static void ex13() throws SQLException {
		Connection con = getConnection();

		String customers = "SELECT c.dni as d, name,surname,COUNT(codecar) as numcars, COUNT(DISTINCT cifd) as numdeal FROM Sales s, Customers c WHERE c.dni=? AND c.dni=s.dni GROUP BY c.dni,name, surname";
		String cars = "SELECT s.codecar as c, namec, model, color FROM Sales s,Cars c WHERE s.codecar=c.codecar AND s.dni=?";

		PreparedStatement stat = con.prepareStatement(customers);
		PreparedStatement pstat = con.prepareStatement(cars);
		System.out.println("DNI: ");
		String dni = ReadString();
		stat.setString(1, dni);

		ResultSet rs = stat.executeQuery();
		ResultSet rs2;

		while (rs.next()) {
			System.out.println("- Customer: " + rs.getString("name") + " " + rs.getString("surname") + " "
					+ rs.getString("numcars") + " " + rs.getString("numdeal"));

			pstat.setString(1, rs.getString("d"));
			rs2 = pstat.executeQuery();
			while (rs2.next()) {
				System.out.println("---> Car: " + rs2.getString("c") + " " + rs2.getString("namec") + " "
						+ rs2.getString("model") + " " + rs2.getString("color"));
			}
			rs2.close();
		}
		rs.close();

		pstat.close();
		stat.close();
		con.close();
	}
//	14. Create a labPL1 package including all procedures and functions defined before.
}
