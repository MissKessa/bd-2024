package db.jdbc2;

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

	public static void main(String[] args) throws SQLException {
		// exercise2_b();
		// exercise3_a();
		exerciseExtraSQL4();
	}

	/*
	 * 2. Implement a Java application that: b. Shows a list that includes, for the
	 * cinemas of a given location, the total income obtained in each cinema as well
	 * as the one obtained for each film shown in it. Cinema 1 - Total income Code
	 * film 1 - Title 1 - Total_income_film1_in_cinema1 Code film 2 - Title 2 -
	 * Total_income_film2_in_cinema1 Cinema 2 - Total income Code film 1 - Title 1 -
	 * Total_income_film1_in_cinema1 Code film 2 - Title 2 -
	 * Total_income_film2_in_cinema1 Code film 3 - Title 3 -
	 * Total_income_film2_in_cinema2
	 */
	public static void exercise2_b() throws SQLException {
		Connection conn = getConnection();

		Statement stat = conn.createStatement();
		String sentence = " SELECT c.codcine, SUM(Precio) as income FROM Salas s, Cines c, Entradas e WHERE c.localidad=? AND c.codcine=s.codcine AND e.codsala=s.codsala GROUP BY c.codcine";
		PreparedStatement pStat = conn.prepareStatement(sentence);
		System.out.println("Write a city: ");
		String localidad = ReadString();
		pStat.setString(1, localidad);
		ResultSet cines = pStat.executeQuery();
		ResultSet films = null;

		sentence = "SELECT p.codpelicula, p.titulo, SUM(precio) as income FROM Peliculas p, Salas s, Entradas e WHERE s.codcine=? AND s.codsala=e.codsala AND p.codpelicula=e.codpelicula GROUP BY (p.codpelicula, p.titulo)";
		pStat = conn.prepareStatement(sentence);

		System.out.println("-------EXERCISE 2b-------");
		while (cines.next()) {
			System.out.println("Cinema " + cines.getString("codcine") + " - " + cines.getString("income"));

			pStat.setString(1, cines.getString("codcine"));
			films = pStat.executeQuery();

			while (films.next()) {
				System.out.println("\t" + films.getString("codpelicula") + " - " + films.getString("titulo") + " - "
						+ films.getString("income"));
			}

		}
		// IN THIS ORDER THE CLOSES!!!!
		films.close();
		cines.close();
		stat.close();
		conn.close();
	}

	/*
	 * 3. Implement a JAVA application that: a. Shows a list that includes, for each
	 * film, the following information: Film title 1 Cinema 1 Room - Session -
	 * Number of spectators Room - Session - Number of spectators Cinema 2 Room -
	 * Session - Number of spectators Room - Session - Number of spectators
	 */
	public static void exercise3_a() throws SQLException {
		Connection conn = getConnection();

		Statement stat = conn.createStatement();
		String sentence = " SELECT codpelicula, titulo FROM Peliculas ";
		ResultSet films = stat.executeQuery(sentence);

		sentence = " SELECT DISTINCT codcine FROM Salas s, Proyectan p WHERE p.codsala=s.codsala AND p.codpelicula=? ";
		PreparedStatement pStat = conn.prepareStatement(sentence);
		ResultSet cines = null;

		sentence = "SELECT p.codsala, p.sesion, SUM(p.entradasVendidas) as spectators FROM Salas s, Proyectan p WHERE p.codpelicula=? AND s.codsala=p.codsala AND s.codcine=? GROUP BY (p.codsala, p.sesion)";
		PreparedStatement pStat2 = conn.prepareStatement(sentence);
		ResultSet proyecciones = null;

		System.out.println("-------EXERCISE 3a-------");
		while (films.next()) {
			System.out.println(films.getString("titulo"));

			pStat.setString(1, films.getString("codpelicula"));
			cines = pStat.executeQuery();

			while (cines.next()) {
				System.out.println("\t" + cines.getString("codcine"));

				pStat2.setString(1, films.getString("codpelicula"));
				pStat2.setString(2, cines.getString("codcine"));
				proyecciones = pStat2.executeQuery();

				while (proyecciones.next()) {
					System.out.println("\t\t" + proyecciones.getString("codsala") + " - "
							+ proyecciones.getString("sesion") + " - " + proyecciones.getString("spectators"));
				}

			}
		}
		// IN THIS ORDER THE CLOSES!!!!
		proyecciones.close();
		cines.close();
		films.close();
		stat.close();
		conn.close();
	}

	/*
	 * (Based on query 6) Create a stored procedure that displays the names of
	 * customers at a branch (sorted alphabetically). The branch name is received as
	 * a parameter. Additionally, show the loans and/or accounts that each customer
	 * has at that branch. First, display any loans (if they exist), followed by all
	 * accounts (if they exist). The information should be presented as follows:
	 * Customer: customer_name1 --- >Loan: lo_number amount --- >Loan: lo_number
	 * amount ... --- >Account: acc_number balance --- >Account: acc_number balance
	 * Customer: customer_name2 --- >Loan: lo_number amount --- >Loan: lo_number
	 * amount ... --- >Account: acc_number balance --- >Account: acc_number balance
	 */
	public static void exerciseExtraSQL4() throws SQLException {

		Connection conn = getConnection();
		String sentence = " (SELECT cus_name FROM AccountHolder ah, Account a WHERE ah.acc_number=a.acc_number AND a.br_name=?) "
				+ "UNION (SELECT cus_name FROM Borrower b, Loan l WHERE b.lo_number=l.lo_number AND l.br_name=?)";
		PreparedStatement pStat = conn.prepareStatement(sentence);
		System.out.println("Write a branch: ");
		String branch = ReadString();
		pStat.setString(1, branch);
		pStat.setString(2, branch);
		ResultSet customers = pStat.executeQuery();

		sentence = "SELECT a.acc_number, balance FROM Account a, AccountHolder ah WHERE ah.cus_name=? AND ah.acc_number=a.acc_number AND a.br_name=?";
		pStat = conn.prepareStatement(sentence);
		pStat.setString(2, branch);
		ResultSet accounts = null;

		while (customers.next()) {
			System.out.println("Customer: " + customers.getString("cus_name"));

			pStat.setString(1, customers.getString("cus_name"));
			accounts = pStat.executeQuery();
			while (accounts.next()) {
				System.out.println(
						"--- >Account: " + accounts.getString("acc_number") + " " + accounts.getString("balance"));
			}
		}

		accounts.close();
		customers.close();
		pStat.close();
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
