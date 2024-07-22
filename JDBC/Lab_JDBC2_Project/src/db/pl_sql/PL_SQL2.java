package db.pl_sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class PL_SQL2 {
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

	}

//	1. Add a new attribute DataCaps to the PURCHASES table that stores the name and surname
//	of customers in uppercase. Develop a trigger that keeps the value of that attribute updated.
//	2. Create a trigger on table SALES (in own schema). Each time a sale is recorded, the trigger
//	should increment in 1 the number of cars (quantity) in table PURCHASES.
//	3. Expand the above trigger so that when a customer deletes a sale, the number of cars in table
//	PURCHASESS is updated correspondingly.
//	4. Create a table CUSTOMERS_LOG with the following attributes: DniPrev, NamePrev,
//	SurnamePrev, CityPrev, DniCur, NameCur, SurnameCur, CityCur,
//	DateTime. Develop a trigger that manages this table logging every update done on the
//	CUSTOMERS table. DateTime stores the date and time in which the update was
//	performed.
//	5. Develop a trigger that decrements the number of cars stocked in a dealer whenever a car is
//	sold, but only in the case that the number of cars stocked in the dealer is greater than 1.
//	6. Develop a trigger so that table PURCHASES can be deleted only by the owner of the schema
//	between 11:00 and 13:00.
//	7. Develop a trigger that stores in a PURCHASES_LOG table the operation performed (insert,
//	delete, update), the date of the operation, and the user initiating the operation.
//	8. Yellow color has been discontinued, so yellow cars cannot be sold anymore.
//	9. No dealer can stock more than 40 cars.
//	10. Dealer 1 closed its doors yesterday. Therefore, this dealer cannot sell from now on.
//	11. We have detected a possible toxic substance in the paint used to paint red cars. Fortunately, no
//	cars were sold with that toxic before, but from now on, we want to log information about the
//	cars that are sold. The log should record the cifd and name of the dealer, the dni and name of
//	the customer, the code, name and model of the car, and the date and time of the sale.
//	12. Circulation of gray cars has been banned by the government. Therefore, gray cars that are sold
//	must be painted in another color: white if the car model is ‘gtd’, and black if not.

}
