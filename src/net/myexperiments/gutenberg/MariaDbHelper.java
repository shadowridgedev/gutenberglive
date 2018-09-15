package net.myexperiments.gutenberg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class MariaDbHelper {
	String con;
	String mysqlpassword;
	Connection connection;
	String username;
	String hostname;
	Statement stmt;

	boolean createconnection(Properties prop, String host, String sql, String table) {
		username = prop.getProperty("mysqluser");
		mysqlpassword = prop.getProperty("mysqlpassword");
		if (host == "")
			host = prop.getProperty("mysqlhost");
		if (sql == "")
			sql = prop.getProperty("createtable");
		hostname = host;
		try {
			// "jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");
			String con = "jdbc:mariadb://" + hostname + ":3306/gut?user=" + username + "&password="
					+ mysqlpassword;
			connection = DriverManager.getConnection(con);
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	void openConnection() {
		// Open a connection
		System.out.println("Connecting to database...");
		try {
			connection = DriverManager.getConnection(
					"jdbc:mariadb://" + hostname + ":3306/gut?user=" + username + "&password=" + mysqlpassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void closeconnection() throws SQLException {
		connection.close();

	}

	void StoreBooks(ArrayList<Book> books) {
		for (Book book : books) {
			InsertBook(book);
		}
	}

	public void InsertBook(Book book) {
		System.out.println("Writimg records into the table...");
		try {
			stmt = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String path = book.getPath();
		String author = book.getAuthor();
		String title = book.getTitle();
		String date = book.getDate();
		String file = book.getFilename();
		String sql = "INSERT  INTO gut ( Author, Title, Text, Date, Path, File) VALUES ( " + author + "`,`"
				+ title + "`," + "LOAD_FILE(`" + path + "`)," + date + "`,`" + path + "`,`" + file + "` )";
		// "INSERT INTO guttenberg ( Title) VALUES ( 'Unknown')"; // String sql
		// = "INSERT INTO `guttenberg` ( `Text`) VALUES ( LOAD_FILE( '" + Path + "'))";
		// "INSERT INTO `guttenberg` ( `Path`, `File`) VALUES ( " + "Test," + "twat" +
		// ")";

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) { // TODO
			e.printStackTrace();
		}
	}
}
