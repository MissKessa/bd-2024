package db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQL2 {
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
		ex32();
	}

//	1. Tuples from the CARMAKERS relation having ‘barcelona’ in the city attribute.
	public static void ex1() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT * FROM CarMakers WHERE citycm='barcelona'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifcm") + " - " + rs.getString("namecm") + " - " + rs.getString("citycm"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	2. Tuples from the CUSTOMERS relation for ‘madrid’ customers with ‘garcia’ surname. The
//	same for customers having either one or the other condition.
	public static void ex2() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT * FROM Customers WHERE city='madrid' AND surname='garcia'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("dni") + " - " + rs.getString("name") + " - " + rs.getString("surname")
					+ " - " + rs.getString("city"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	3. Get a relation having the values of the surname and city attributes from the CUSTOMERS
//	relation.
	public static void ex3() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT surname,city FROM Customers";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("surname") + " - " + rs.getString("city"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	4. Get a relation showing the surnames of the CUSTOMERS from ‘madrid’.
	public static void ex4() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT surname FROM Customers WHERE city='madrid'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("surname"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	5. Names of car makers having ‘gtd’ models.
	public static void ex5() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT namecm FROM Carmakers cm, CmCars cc, Cars c WHERE cm.cifcm=cc.cifcm AND cc.codecar=c.codecar AND c.model='gtd'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("namecm"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	6. Names of car makers that have sold red cars.
	public static void ex6() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT namecm FROM Carmakers cm, CmCars cc, Sales s WHERE cm.cifcm=cc.cifcm AND cc.codecar=s.codecar AND s.color='red'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("namecm"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	7. Names of cars having the same models as the car named ‘cordoba’.
	public static void ex7() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT c1.namec FROM Cars c1, Cars c2 WHERE c1.model=c2.model AND c2.namec='cordoba'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("namec"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	8. Names of cars NOT having a ‘gtd’ model.
	public static void ex8() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT namec FROM Cars WHERE namec NOT IN (SELECT namec FROM Cars WHERE model='gtd')";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("namec"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	9. Pairs of values: CIFC from the CARMAKERS relation, and DNI from the CUSTOMERS
//	relation belonging to the same city. The same for the ones not belonging to the same city.
	public static void ex9() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT cifcm, dni FROM Carmakers, Customers WHERE citycm=city";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifcm") + " - " + rs.getString("dni"));
		}

		System.out.println("-------------------");
		sentence = "SELECT cifcm, dni FROM Carmakers, Customers WHERE citycm!=city";
		rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifcm") + " - " + rs.getString("dni"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	10. Codecar values for the cars stocked in any dealer in ‘barcelona’.
	public static void ex10() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT codecar FROM Distribution dis, Dealers de WHERE dis.cifd=de.cifd AND de.cityd='barcelona'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("codecar"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	11. Codecar values for cars bought by a ‘madrid’ customer in a ‘madrid’ dealer.
	public static void ex11() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT codecar FROM Sales s, Customers c, Dealers d WHERE s.cifd=d.cifd AND c.dni=s.dni AND c.city='madrid' AND d.cityd='madrid'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("codecar"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	12. Codecar values for cars sold in a dealer in the same city as the buying customer.
	public static void ex12() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT codecar FROM Sales s, Customers c, Dealers d WHERE s.cifd=d.cifd AND c.dni=s.dni AND c.city=d.cityd";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("codecar"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	13. Pairs of CARMAKERS (names) from the same city.
	public static void ex13() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT c1.namecm as n1,c2.namecm as n2 FROM Carmakers c1, Carmakers c2 WHERE c1.citycm=c2.citycm AND c1.cifcm!=c2.cifcm";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("n1") + " - " + rs.getString("n2"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	14. DNI of the customers that have bought a car in a ‘madrid’ dealer.
	public static void ex14() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT dni FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.cityd='madrid'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("dni"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	15. Colors of cars sold by the ‘acar’ dealer.
	public static void ex15() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT color FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.named='acar'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("color"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	16. Codecar of the cars sold by any ‘madrid’ dealer.
	public static void ex16() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT codecar FROM Sales s, Dealers d WHERE s.cifd=d.cifd AND d.cityd='madrid'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("codecar"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	17. Names of customers that have bought any car in the ‘dcar’ dealer.
	public static void ex17() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT name FROM Customers c,Sales s, Dealers d WHERE s.cifd=d.cifd AND s.dni=c.dni AND d.named='dcar'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	18. Name and surname of the customers that have bought a ‘gti’ model with ‘white’ color.
	public static void ex18() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT name,surname FROM Customers cus,Sales s, Cars c WHERE s.dni=cus.dni AND s.codecar=c.codecar AND c.model='gti' AND s.color='white'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name") + " - " + rs.getString("surname"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	19. Name and surname of customers that have bought a car in a ‘madrid’ dealer that has ‘gti’ cars.
	public static void ex19() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT name,surname FROM Customers cus,Sales s, Dealers d WHERE s.dni=cus.dni AND d.cifd=s.cifd AND d.cityd='madrid' AND d.cifd IN "
				+ "(SELECT cifd FROM Distribution dis, Cars c WHERE c.codecar=dis.codecar AND c.model='gti')";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name") + " - " + rs.getString("surname"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	20. Name and surname of customers that have bought (at least) a ‘white’ and a ‘red’ car.
	public static void ex20() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT name, surname FROM Customers WHERE dni IN "
				+ "(SELECT dni FROM Sales WHERE color='white') AND dni IN "
				+ "(SELECT dni FROM Sales WHERE color='red')";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name") + " - " + rs.getString("surname"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	21. DNI of customers that have only bought cars at the dealer with CIFD ‘1’.
	public static void ex21() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT dni FROM Customers WHERE dni NOT IN (SELECT dni FROM Sales WHERE cifd!='1')";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("dni"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	22. Names of the customers that have NOT bought ‘red’ cars at ‘madrid’ dealers.
	public static void ex22() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT name FROM Customers WHERE dni NOT IN (SELECT dni FROM Sales s, Dealers d WHERE s.color='red' AND d.cifd=s.cifd AND d.cityd='madrid')";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	23. For each dealer (cifd), show the total amount of cars stocked.
	public static void ex23() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT cifd, SUM(stockage) as total FROM Distribution GROUP BY cifd";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifd") + " - " + rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	24. Show the cifd of dealers with an average stockage of more than 10 units (show that average as well).
	public static void ex24() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT cifd, AVG(stockage) as avg FROM Distribution GROUP BY cifd HAVING AVG(stockage)>10";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifd") + " - " + rs.getFloat("avg"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	25. CIFD of dealers with a stock between 10 and 18 units inclusive.
	public static void ex25() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT cifd, SUM(stockage) as total FROM Distribution GROUP BY cifd HAVING SUM(stockage)>=10 AND SUM(stockage)<=18";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifd") + " - " + rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	26. Total amount of car makers. Total amount of cities with car makers.
	public static void ex26() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT COUNT(*) as total FROM Carmakers";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("total"));
		}

		sentence = "SELECT COUNT(DISTINCT citycm) as total FROM Carmakers";
		rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getInt("total"));
		}
		rs.close();
		stat.close();
		con.close();
	}

//	27. Name and surname of customers that have bought a car in a ‘madrid’ dealer, and have a name starting with j.
	public static void ex27() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT name,surname FROM Customers c, Sales s, Dealers d WHERE c.dni=s.dni AND s.cifd=d.cifd AND d.cityd='madrid' AND c.name LIKE 'j%'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name") + " - " + rs.getString("surname"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	28. List customers ordered by name (ascending).
	public static void ex28() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT dni,name,surname,city FROM Customers ORDER BY name ASC";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	29. List customers that have bought a car in the same dealer as customer with dni ‘2’ (excluding the customer with dni ‘2’)
	// Same with dni ‘1’.
	public static void ex29() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT DISTINCT name, surname FROM Sales s1,Customers c, Sales s2 WHERE s1.dni=c.dni AND s1.dni!=s2.dni AND s1.cifd=s2.cifd AND s2.dni='2'";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("name") + " - " + rs.getString("surname"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	30. Return a list with the dealers which have a total number of car units in stock greater than the global unit 
	// average of all dealers together.
	public static void ex30() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT cifd FROM Distribution GROUP BY cifd HAVING SUM(stockage) > ((SELECT  AVG(total) FROM (SELECT SUM(stockage)total FROM  distribution GROUP BY cifd)))";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifd"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	31. Dealer having the best average stockage of all dealers; that is, dealer having an average stockage greater 
//  than the average stockage of each one of the remaining dealers.
	public static void ex31() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT cifd, AVG(stockage) as avg FROM Distribution GROUP BY cifd ORDER BY avg DESC FETCH FIRST 1 ROW WITH TIES";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifd"));
		}

		rs.close();
		stat.close();
		con.close();
	}

//	32. List the two customers that have bought more cars in total, ordered by the number of cars bought.
//	List the sales of cars ordered by color. We want to remove the first one and obtain the next two allowing for ties 
	// (and without allowing them).
	public static void ex32() throws SQLException {
		Connection con = getConnection();
		Statement stat = con.createStatement();
		String sentence = "SELECT dni, COUNT(dni) as bought FROM Sales GROUP BY dni ORDER BY bought DESC FETCH FIRST 2 ROW WITH TIES";
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("dni"));
		}

		sentence = "SELECT * FROM Sales ORDER BY color DESC OFFSET 1 ROWS FETCH FIRST 2 ROWS WITH TIES";
		rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cifd") + "-" + rs.getString("dni") + "-" + rs.getString("codecar") + "-"
					+ rs.getString("color"));
		}
		rs.close();
		stat.close();
		con.close();
	}
//	33. Create a view from query 34. Using such view, list for each of the customers who have bought
//	more cars in total, the code of the cars bought, the cifd of the dealers where they were bought,
//	and the color.
}
