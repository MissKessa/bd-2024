package db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQL3 {
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
		ex28();
	}

//	1. Retrieve a relation with two columns, one for the titles and one for the expected revenue for each
//	title. The revenue is calculated as expected sales times price of the title.
	public static void ex1() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title, expected_sales*price as revenue FROM Titles";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("title") + " " + rs.getFloat("revenue"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	2. Show the titles with expected sales between 200 and 5000 units (copies).
	public static void ex2() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title, expected_sales FROM Titles WHERE expected_sales >=200 AND expected_sales<=5000";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("title") + " " + rs.getFloat("expected_sales"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	3. Show the name, surname and telephone number for all the authors ordered first by name in
//	ascending order and then by surname in descending order.
	public static void ex3() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT au_name, au_surname, au_telephone FROM Authors ORDER BY au_name ASC, au_surname DESC";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(
					rs.getString("au_name") + " " + rs.getString("au_surname") + " " + rs.getString("au_telephone"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	4. Show the name and surname of the authors that do not have telephone number (i.e. it is null).
	public static void ex4() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT au_name, au_surname FROM Authors WHERE au_telephone IS NULL";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("au_name") + " " + rs.getString("au_surname"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	5. Show the name, surname and telephone number of all the authors, denoting “no telephone” for those
//	that do not have telephone number. Express the query using the function NVL.
	public static void ex5() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT au_name, au_surname, NVL(au_telephone, 'no telephone') as au_telephone FROM Authors";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(
					rs.getString("au_name") + " " + rs.getString("au_surname") + " " + rs.getString("au_telephone"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	6. Show the title id, title and expected sales for titles with type database (BD) or programming (PROG).
//	Order them descendingly by price. Express the query in two different ways.
	public static void ex6() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title_id, title, expected_sales FROM Titles WHERE type='BD' OR type='PROG' ORDER BY price DESC";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(
					rs.getString("title_id") + " " + rs.getString("title") + " " + rs.getFloat("expected_sales"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	7. Show the authors with a telephone number starting with ‘456’.
	public static void ex7() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT au_id, au_telephone FROM Authors WHERE au_telephone LIKE '456%'";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("au_id") + " " + rs.getString("au_telephone"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	8. Show the average price for titles in the relation TITLES. Show average price for titles with BD type.
//	Express the query in two different ways.
	public static void ex8() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT AVG(price) as avg FROM Titles";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getFloat("avg"));
		}
		sentence = "SELECT AVG(price) as avg FROM Titles WHERE type='BD'";
		rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getFloat("avg"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	9. Show the number of titles of each publisher. Number of titles belonging to each type, per publisher.
	public static void ex9() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT pub_id, COUNT(*) as total FROM Titles GROUP BY pub_id";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("pub_id") + " " + rs.getInt("total"));
		}
		System.out.println("-------------");
		sentence = "SELECT pub_id, type, COUNT(*) as total FROM Titles GROUP BY pub_id,type";
		rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("pub_id") + " " + rs.getString("type") + " " + rs.getInt("total"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	10. Show the number of copies in stock for each type of title. Ignore null values.
	public static void ex10() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT type, COUNT(*) as total FROM Titles GROUP BY type";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("type") + " " + rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	11. Show the average price for each type of title with a publishing date later than year 2000.
	public static void ex11() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT type, AVG(price) as avg FROM Titles GROUP BY type";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("type") + " " + rs.getFloat("avg"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	12. Show the number of copies in stock for each type of title, but only if it is greater than 1.
	public static void ex12() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT type, COUNT(*) as total FROM Titles GROUP BY type HAVING COUNT(*)>1";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("type") + " " + rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	13. Show the average price for each type of title, but only if it is greater than 35.
	public static void ex13() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT type, AVG(price) as total FROM Titles GROUP BY type HAVING AVG(price)>35";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("type") + " " + rs.getFloat("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	14. Show the average price for the titles of each publisher, but only if its identifier is greater than 2 and
//	the average price is greater than 60. The result should be ordered ascendingly by publisher id.
	public static void ex14() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT pub_id, AVG(price) as total FROM Titles WHERE pub_id > 2 GROUP BY pub_id HAVING AVG(price)>60 ORDER BY pub_id ASC";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("pub_id") + " " + rs.getFloat("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	15. Show the name, surname and editor_order for the title with ‘1’ as identifier.
	public static void ex15() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT ed_name, ed_surname, editor_order FROM Editors e, TitlesEditors te WHERE te.ed_id=e.ed_id AND title_id='1'";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(
					rs.getString("ed_name") + " " + rs.getString("ed_surname") + " " + rs.getString("editor_order"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	16. Show the names of the editors and publishers that are in the same city.
	public static void ex16() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT ed_name, pub_name FROM Editors e, Publishers p WHERE pub_city=ed_city";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("ed_name") + " " + rs.getString("pub_name"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	17. Show the titles of BD type books, with the names of the authors and author_order.
	public static void ex17() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title, au_name, author_order FROM Titles t, TitlesAuthors ta, Authors a WHERE t.type='BD' AND t.title_id=ta.title_id AND ta.au_id=a.au_id";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(
					rs.getString("title") + " " + rs.getString("au_name") + " " + rs.getString("author_order"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	18. Show the name and surname of editors with the name of his chief editor.
	public static void ex18() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT e1.ed_name as n1, e1.ed_surname as s1, e2.ed_name as n2 FROM Editors e1, Editors e2 WHERE e2.ed_id=e1.ed_chief";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("n1") + " " + rs.getString("s1") + " " + rs.getString("n2"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	19. Show the data from authors (au_id, au_name and au_surname) that have the same surname.
	public static void ex19() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT a1.au_id as i1, a2.au_id as i2, a1.au_surname as s FROM Authors a1, Authors a2 WHERE a1.au_id>a2.au_id AND a1.au_surname=a2.au_surname";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("i1") + " " + rs.getString("i2") + " " + rs.getString("s"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	20. Show the names of publishers that publish PROG titles. Express the query in two different ways.
	public static void ex20() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT pub_name FROM Publishers p, Titles t WHERE p.pub_id=t.pub_id AND t.type='PROG'";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("pub_name"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	21. Show the title and price of books having the same price as the cheapest book. Same with the most
//	expensive one.
	public static void ex21() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title, price FROM titles ORDER BY price ASC FETCH FIRST ROWS WITH TIES";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("title") + " " + rs.getFloat("price"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	22. Show the name and city of authors that live in the same city as ‘Abraham Silberschatz’.
	public static void ex22() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT a1.au_name as n1, a1.au_city as c1 FROM Authors a1, Authors a2 WHERE a1.au_city=a2.au_city AND a2.au_name='Abraham' AND a2.au_surname='Silberschatz'";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("n1") + " " + rs.getString("c1"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	23. Show the name and surname of authors that are both individual authors and co-authors.
	public static void ex23() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT au_name, au_surname FROM Authors a, TitlesAuthors ta, TitlesAuthors ta2  WHERE a.au_id=ta.au_id AND a.au_id=ta2.au_id AND ta.percentage_participation=1 AND ta2.percentage_participation<1";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("au_name") + " " + rs.getString("au_surname"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	24. Show the types of books that are in common for more than one publisher. Express the query in two different ways.
	public static void ex24() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT t1.type as t FROM Titles t1, Titles t2 WHERE t1.type=t2.type AND t1.title_id!=t2.title_id AND t1.pub_id!=t2.pub_id";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("t"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	25. Show the types of books with a maximum price at least a 10% more expensive than the average price of that type. 
	// And for 20%.
	public static void ex25() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT type, MAX(price) as m FROM Titles GROUP BY type HAVING MAX(price) > AVG(price)*1.1";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("type") + " " + rs.getFloat("m"));
		}
		System.out.println("-----------");
		sentence = "SELECT type, MAX(price) as m FROM Titles GROUP BY type HAVING MAX(price) > AVG(price)*1.2";
		rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("type") + " " + rs.getFloat("m"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	26. Show the books that have a greater pre-publishing than the greatest pre-publishing of ‘Prentice Hall’ publisher.
	public static void ex26() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title_id, title FROM Titles WHERE pre_publishing>(SELECT MAX(pre_publishing) FROM Titles t, Publishers p WHERE t.pub_id=p.pub_id AND p.pub_name='Prentice Hall')";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("title_id") + " " + rs.getString("title"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	27. Show the titles of the books published by a publisher established in a city starting with ‘B’.
	public static void ex27() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT title FROM Titles t, Publishers p WHERE t.pub_id=p.pub_id AND p.pub_city LIKE 'B%'";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("title"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	28. Show the names of publishers that do not publish BD type books. Express the query in two different ways.
	public static void ex28() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT pub_name FROM Publishers p WHERE p.pub_id NOT IN (SELECT pub_id FROM Titles WHERE type='BD')";
		ResultSet rs = stat.executeQuery(sentence);
		while (rs.next()) {
			System.out.println(rs.getString("pub_name"));
		}

		rs.close();
		stat.close();
		con.close();
	}
}
