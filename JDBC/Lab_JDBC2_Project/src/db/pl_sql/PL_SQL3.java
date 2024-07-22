package db.pl_sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PL_SQL3 {
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
		ex8_function();
	}

//	2. Calculate Total Sales: Create a procedure that calculates the total sales for each publisher based
//	on the expected_sales from the Titles table. The procedure should take pub_id as
//	input and display the total sales for that publisher.
	public static void ex2() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT SUM(expected_sales*price) as total FROM Titles WHERE pub_id=?";
		PreparedStatement pstat = con.prepareStatement(sentence);

		System.out.println("Publisher:");
		String id = ReadString();
		pstat.setString(1, id);

		ResultSet rs = pstat.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getFloat("total"));
		}
		rs.close();
		pstat.close();
		con.close();
	}

//	3. Get Publisher Names List: Create a function that returns a comma-separated string of
//	publisher names for which a specific editor (ed_id) works. The function should
//	take ed_id as input and return the publisher names as a text string.
	public static void ex3_function() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{? = call PL3_EX3(?)}");

		cs.registerOutParameter(1, java.sql.Types.VARCHAR);
		System.out.println("Editor:");
		String ed_id = ReadString();
		cs.setString(2, ed_id);

		cs.execute();
		String result = cs.getString(1);
		System.out.println(result);

		cs.close();
		con.close();
	}

	public static void ex3_jdbc() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT pub_name FROM TitlesEditors te, Titles t, Publishers p WHERE te.ed_id=? AND t.title_id=te.title_id AND p.pub_id=t.pub_id";
		PreparedStatement pstat = con.prepareStatement(sentence);

		System.out.println("Editor:");
		String id = ReadString();
		pstat.setString(1, id);

		ResultSet rs = pstat.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("pub_name"));
		}
		rs.close();
		pstat.close();
		con.close();
	}

//	4. Update Title Price: Create a procedure that updates the price of a title based on
//	its title_id. The procedure should take title_id and the new price as input
//	parameters and update the price column in the Titles table.
	public static void ex4_procedure() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{call PL3_EX4(?,?)}");

		System.out.println("Title id:");
		String titleId = ReadString();
		cs.setString(1, titleId);

		System.out.println("New price:");
		int price = ReadInt();
		cs.setInt(2, price);

		cs.execute();

		cs.close();
		con.close();
	}

	public static void ex4_jdbc() throws SQLException {
		Connection con = getConnection();
		String sentence = "UPDATE Titles SET price=? WHERE title_id=?";
		PreparedStatement pstat = con.prepareStatement(sentence);

		System.out.println("New price:");
		int price = ReadInt();
		pstat.setInt(1, price);

		System.out.println("Title:");
		String title = ReadString();
		pstat.setString(2, title);

		int rs = pstat.executeUpdate();

		System.out.println(rs);

		pstat.close();
		con.close();
	}

//	5. Maintain Author Title Count: Create a trigger that automatically updates the total number of
//	titles for an author. This value will be stored in the new title_count field in
//	the authors table. Additionally, since there is already existing data, create a procedure that
//	updates the title_count value based on the data already present.

//	6. Insert New Author: Create a procedure that inserts a new author into the Authors table. The
//	procedure should use the author’s name, surname, phone_number, address, and city as input
//	parameters.
	public static void ex6() throws SQLException {
		Connection con = getConnection();
		String sentence = "INSERT INTO Authors (au_id,au_name,au_surname,au_telephone,au_address,au_city) VALUES (?,?,?,?,?,?)";
		PreparedStatement pstat = con.prepareStatement(sentence);

		System.out.println("Id:");
		String id = ReadString();
		pstat.setString(1, id);

		System.out.println("Name:");
		String name = ReadString();
		pstat.setString(2, name);

		System.out.println("Surname:");
		String surname = ReadString();
		pstat.setString(3, surname);

		System.out.println("Telephone:");
		String phone = ReadString();
		pstat.setString(4, phone);

		System.out.println("Address:");
		String address = ReadString();
		pstat.setString(5, address);

		System.out.println("City:");
		String city = ReadString();
		pstat.setString(6, city);

		int rs = pstat.executeUpdate();
		System.out.println(rs);

		pstat.close();
		con.close();
	}
//	7. Validate Title Data: Create a trigger that from now on verifies that the d_publishing of
//	titles is greater than January 1, 2024, and that the price is a positive number. It should
//	generate an error if any validation fails.

//	8. Calculate Title Antiquity: Create a function that calculates the antiquity of a title based on
//	its d_publishing date. The function should take title_id as input and return the title’s
//	antiquity in years.
	public static void ex8_function() throws SQLException {
		Connection con = getConnection();
		CallableStatement cs = con.prepareCall("{? = call PL3_EX8(?)}");

		cs.registerOutParameter(1, java.sql.Types.INTEGER);

		System.out.println("Title id:");
		String title_id = ReadString();
		cs.setString(2, title_id);

		cs.execute();
		int antiquity = cs.getInt(1);
		System.out.println(antiquity);

		cs.close();
		con.close();
	}

	public static void ex8_jdbc() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT d_publishing FROM Titles WHERE title_id=?";
		PreparedStatement pstat = con.prepareStatement(sentence);

		System.out.println("ID:");
		String title = ReadString();
		pstat.setString(1, title);

		ResultSet rs = pstat.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("d_publishing"));
		}

		rs.close();
		pstat.close();
		con.close();
	}

//	9. Ensure Price Uniqueness for Titles: Create a trigger that prevents duplicate prices for titles
//	in the Titles table from now on. The trigger should generate an error if the new price
//	already exists in the table.

//	10. Check Author Existence: Create a function that checks if an author already exists in the
//	database based on their name and surname. The function should take the author’s name and
//	surname as input and return a boolean value indicating whether the author exists.
	public static void ex10() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT au_id FROM Authors WHERE au_name=? AND au_surname=?";
		PreparedStatement pstat = con.prepareStatement(sentence);

		System.out.println("Name:");
		String name = ReadString();
		pstat.setString(1, name);

		System.out.println("Surname:");
		String surname = ReadString();
		pstat.setString(2, surname);

		ResultSet rs = pstat.executeQuery();

		boolean exists = false;
		while (rs.next()) {
			exists = true;
		}

		System.out.println(exists);
		rs.close();
		pstat.close();
		con.close();
	}
//	11. Calculate Royalties (Author Rights): Create a function that calculates the total royalties for an
//	author based on the title’s price, expected_sales, and the author’s percentage_participation from the 
// TitlesAuthors table. The function should use the au_id as input and return
// the total royalties for the corresponding author.
//	12. Log Title Updates: Create a trigger that records any updates on the price or title in
//	the Titles table. The trigger should insert a row into the LOG_TITLES table containing
//	the title_id, old and new values of the updated columns, the date/time of the update, and
//	the name of the user who performed the operation.

//	13. Build Stored Procedure ListTitlesByPublisher: Create a stored procedure that
//	generates a listing displaying titles from a publisher along with their authors in the following
//	format:
//	-Publisher: publisherName1 totalTitles1
//	--->Title: titleName1 totalAuthors1
//	  --->Author: authorSurname1 authorName1
//	  --->Author: authorSurname2 authorName2
//	--->Title: titleName2 totalAuthors2
//	  --->Author: authorSurname1 authorName1
//	  --->Author: authorSurname2 authorName2
//	...
//	-Publisher: publisherName2 totalTitles2
//	--->...
//	-...
	public static void ex13() throws SQLException {
		Connection con = getConnection();
		String publishers = "SELECT p.pub_id as id, pub_name, COUNT(*) as total FROM Publishers p, Titles t WHERE p.pub_id=t.pub_id GROUP BY p.pub_id, pub_name";
		String titles = "SELECT t.title_id as id, title,COUNT(DISTINCT au_id) as total FROM TitlesAuthors ta, Titles t WHERE t.pub_id=? AND t.title_id=ta.title_id GROUP BY t.title_id,title";
		String authors = "SELECT a.au_id, au_surname, au_name FROM Authors a, TitlesAuthors ta WHERE ta.title_id=? AND ta.au_id=a.au_id";

		Statement pStat = con.createStatement();
		PreparedStatement tStat = con.prepareStatement(titles);
		PreparedStatement aStat = con.prepareStatement(authors);

		ResultSet p = pStat.executeQuery(publishers);
		ResultSet t;
		ResultSet a;
		while (p.next()) {
			System.out.println("-Publisher: " + p.getString("pub_name") + " " + p.getString("total"));
			tStat.setString(1, p.getString("id"));
			t = tStat.executeQuery();
			while (t.next()) {
				System.out.println("--->Title: " + t.getString("title") + " " + t.getString("total"));
				aStat.setString(1, t.getString("id"));
				a = aStat.executeQuery();

				while (a.next()) {
					System.out.println("  --->Author: " + a.getString("au_surname") + " " + a.getString("au_name"));
				}
				a.close();

			}
			t.close();
		}
		p.close();
		pStat.close();
		con.close();
	}

//	14. Create a stored procedure named ListTitlesByPriceRange based on the previous
//	procedure. This procedure should display only the titles within a specified price range. Include the
//	publication price in the Title information shown. The minimum and maximum prices will be two
//	input parameters.
	public static void ex14() throws SQLException {
		Connection con = getConnection();
		String publishers = "SELECT p.pub_id as id, pub_name, COUNT(*) as total FROM Publishers p, Titles t WHERE p.pub_id=t.pub_id GROUP BY p.pub_id, pub_name";
		String titles = "SELECT t.title_id as id, title,price,COUNT(DISTINCT au_id) as total FROM TitlesAuthors ta, Titles t WHERE t.pub_id=? AND t.title_id=ta.title_id AND price>=? AND price<=? GROUP BY t.title_id,title,price";
		String authors = "SELECT a.au_id, au_surname, au_name FROM Authors a, TitlesAuthors ta WHERE ta.title_id=? AND ta.au_id=a.au_id";

		Statement pStat = con.createStatement();
		PreparedStatement tStat = con.prepareStatement(titles);
		PreparedStatement aStat = con.prepareStatement(authors);

		System.out.println("Min price:");
		int min = ReadInt();
		System.out.println("Max price:");
		int max = ReadInt();

		ResultSet p = pStat.executeQuery(publishers);
		ResultSet t;
		ResultSet a;
		while (p.next()) {
			System.out.println("-Publisher: " + p.getString("pub_name") + " " + p.getString("total"));
			tStat.setString(1, p.getString("id"));
			tStat.setInt(2, min);
			tStat.setInt(3, max);
			t = tStat.executeQuery();
			while (t.next()) {
				System.out.println(
						"--->Title: " + t.getString("title") + " " + t.getFloat("price") + " " + t.getString("total"));
				aStat.setString(1, t.getString("id"));
				a = aStat.executeQuery();

				while (a.next()) {
					System.out.println("  --->Author: " + a.getString("au_surname") + " " + a.getString("au_name"));
				}
				a.close();

			}
			t.close();
		}
		p.close();
		pStat.close();
		con.close();
	}
}
