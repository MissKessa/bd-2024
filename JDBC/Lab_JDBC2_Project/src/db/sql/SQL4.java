package db.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SQL4 {
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
		extraPLSQL();
	}

//	Create a stored procedure that displays the names of customers at a branch
//	(sorted alphabetically). The branch name is received as a parameter. Additionally, show the
//	loans and/or accounts that each customer has at that branch. First, display any loans (if they
//	exist), followed by all accounts (if they exist). The information should be presented as follows:
//
//	Customer: customer_name1
//	--- >Loan: lo_number amount
//	--- >Loan: lo_number amount
//	...
//	--- >Account: acc_number balance
//	--- >Account: acc_number balance
//	Customer: customer_name2
//	--- >Loan: lo_number amount
//	--- >Loan: lo_number amount
//	...
//	--- >Account: acc_number balance
//	--- >Account: acc_number balance
	public static void extraPLSQL() throws SQLException {
		Connection con = getConnection();
		String customers = "(SELECT cus_name FROM AccountHolder ah, Account a WHERE ah.acc_number=a.acc_number AND a.br_name=?) UNION "
				+ "(SELECT cus_name FROM Borrower b, Loan l WHERE b.lo_number=l.lo_number AND l.br_name=?)";
		PreparedStatement p1 = con.prepareStatement(customers);

		System.out.println("Branch:");
		String branch = ReadString();
		p1.setString(1, branch);
		p1.setString(2, branch);
		ResultSet rsCustomers = p1.executeQuery();

		String loans = "SELECT l.lo_number,amount FROM Loan l, Borrower b WHERE b.cus_name=? AND l.lo_number=b.lo_number AND l.br_name=?";
		PreparedStatement p2 = con.prepareStatement(loans);
		p2.setString(2, branch);
		ResultSet rsLoans;

		String accounts = "SELECT a.acc_number,balance FROM Account a, AccountHolder ah WHERE ah.cus_name=? AND a.acc_number=ah.acc_number AND a.br_name=?";
		PreparedStatement p3 = con.prepareStatement(accounts);
		p3.setString(2, branch);
		ResultSet rsAccounts;

		while (rsCustomers.next()) {
			System.out.println("Customer: " + rsCustomers.getString("cus_name"));
			p2.setString(1, rsCustomers.getString("cus_name"));
			p3.setString(1, rsCustomers.getString("cus_name"));

			rsLoans = p2.executeQuery();
			while (rsLoans.next()) {
				System.out.println("--- >Loan: " + rsLoans.getString("lo_number") + " " + rsLoans.getFloat("amount"));
			}
			rsLoans.close();

			rsAccounts = p3.executeQuery();
			while (rsAccounts.next()) {
				System.out.println(
						"--- >Account: " + rsAccounts.getString("acc_number") + " " + rsAccounts.getFloat("balance"));
			}
			rsAccounts.close();
		}

		rsCustomers.close();

		p1.close();
		p2.close();

		con.close();
	}

//	1. Names of customers with both accounts and loans at Perryridge branch.
	public static void ex1() throws SQLException {
		Connection con = getConnection();
		String sentence = "(SELECT cus_name FROM AccountHolder ah, Account a WHERE ah.acc_number=a.acc_number AND a.br_name='Perryridge')"
				+ " INTERSECT (SELECT cus_name FROM Borrower b, Loan l WHERE b.lo_number=l.lo_number AND l.br_name='Perryridge')";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cus_name"));
		}
		stat.close();
		con.close();
	}

//	2. Names of customers with an account but not a loan at Perryridge branch.
	public static void ex2() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT cus_name FROM AccountHolder ah, Account a WHERE ah.acc_number=a.acc_number AND a.br_name='Perryridge' AND cus_name NOT IN (SELECT cus_name FROM Borrower b, Loan l WHERE b.lo_number=l.lo_number AND l.br_name='Perryridge')";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cus_name"));
		}
		stat.close();
		con.close();
	}

//	3. Names of customers with accounts at a branch where Hayes has an account.
	public static void ex3() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT DISTINCT cus_name FROM AccountHolder ah, Account a WHERE ah.acc_number=a.acc_number AND a.br_name IN (SELECT br_name FROM AccountHolder ah, Account a WHERE ah.cus_name='Hayes' AND ah.acc_number=a.acc_number)";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cus_name"));
		}
		stat.close();
		con.close();
	}

//	4. Names of branches whose banking assets are greater than the assets of SOME branch in Brooklyn.
	public static void ex4() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT br_name FROM Branch WHERE asset>SOME(SELECT asset FROM Branch WHERE br_city='Brooklyn')";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("br_name"));
		}
		stat.close();
		con.close();
	}

//	5. Names of branches whose assets are greater than the assets of EVERY branch in Brooklyn.
	public static void ex5() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT br_name FROM Branch WHERE asset>ALL(SELECT asset FROM Branch WHERE br_city='Brooklyn')";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("br_name"));
		}
		stat.close();
		con.close();
	}

//	6. Names of customers at Perryridge branch, ordered by name.
	public static void ex6() throws SQLException {
		Connection con = getConnection();
		String sentence = "(SELECT cus_name FROM AccountHolder ah, Account a WHERE ah.acc_number=a.acc_number AND a.br_name='Perryridge')"
				+ "UNION (SELECT cus_name FROM Borrower b, Loan l WHERE b.lo_number=l.lo_number AND l.br_name='Perryridge')";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("cus_name"));
		}
		stat.close();
		con.close();
	}

//	7. Name of branch having largest average balance.
	public static void ex7() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT br_name, AVG(balance) FROM Account GROUP BY br_name ORDER BY AVG(balance) DESC FETCH FIRST 1 ROW WITH TIES";
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getString("br_name"));
		}
		stat.close();
		con.close();
	}

//	8. Average balance of all customers in Harrison having at least 2 accounts.
	public static void ex8() throws SQLException {
		Connection con = getConnection();
		String sentence = "SELECT c.cus_name,AVG(balance) as avg FROM Account a, AccountHolder ah, Customer c WHERE cus_city='Harrison' AND "
				+ "a.acc_number=ah.acc_number AND c.cus_name=ah.cus_name GROUP BY c.cus_name HAVING COUNT(*)>=2";

		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery(sentence);

		while (rs.next()) {
			System.out.println(rs.getFloat("avg"));
		}
		stat.close();
		con.close();
	}
}
