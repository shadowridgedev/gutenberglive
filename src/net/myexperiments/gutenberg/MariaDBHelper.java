package net.myexperiments.gutenberg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.text.StringEscapeUtils;

public class MariaDBHelper {
	String con;
	String mysqlpassword;
	Connection connection;
	String username;
	String hostname;
	String host;
	String user;
	String password;
	String db;
	List<String> cols;
	Connection conn = null;
	Statement stmt = null;
	String DBType = null;
	Properties prop = null;
	int count = 100;
	String root;
	int max;

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
			String con = "jdbc:mariadb://" + hostname + ":3306/gutenberg?user=" + username + "&password="
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

			PrintWriter log = new PrintWriter("Logfile");
			connection = DriverManager.getConnection(
					"jdbc:mariadb://" + hostname + ":3306/gutenberg?user=" + username + "&password=" + mysqlpassword);
			DriverManager.setLogWriter(log);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void closeconnection() throws SQLException {
		connection.close();

	}

	int countcol(String col, String table) {
		int count = 0;
		openConnection();

		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "SELECT COUNT(" + col + ") FROM " + table + ";";

		try {
			count = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			closeconnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
	}

	void StoreBooks(ArrayList<Book> books) throws IOException {
		openConnection();
		for (Book book : books) {
			try {
				InsertBook(book);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void InsertBook(Book book) throws IOException, SQLException {
//		System.out.println("Writing records into the table...");
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int idBook = book.getIdBook();
		String etextnumber = book.getEtextNumber();
		String path = book.getPath();

		String author = escape(book.getAuthor());
		String title = escape(book.getTitle());
		String date = book.getDate();
		String file = book.getFilename();
		String rawpath = escape(path);
		byte[] textbytes = Files.readAllBytes(Paths.get(path));
		String text = new String(textbytes);
		text = escape(text);
		String name = book.getName();
//		path = escape(path);
//		String sql = "INSERT  INTO gutenberg.books (  idBook, Title, Author, Date, File, Filename, Path, EtextNumber, Name) VALUES ("'" +count, title, author, date, file, filename, path, etextnumber, name )";
//		String sql = "INSERT  INTO guttenberg.books ( Author, Title, Date, Path, File, Text) VALUES (" + "'" + author +  "',"  + title + "',"  + "'," + date + "`," + path + "`,`" + file + LOAD_FILE(rawpath)" )";
		String sql = "INSERT  INTO gutenberg.books ( idBook, Author, Title, Date, Path, File) VALUES ( '" + etextnumber
				+ "','" + author + "','" + title + "','" + date + "','" + path + "','" + file + "' )";

		// String insertStr = "INSERT INTO users VALUES (+"'" +name +"', " +"'" +email
		// +"')";
		// String sql = "INSERT INTO guttenberg ( Title) VALUES ( 'Unknown')";
		// String sql = "INSERT INTO `guttenberg` ( `Text`) VALUES ( LOAD_FILE( '" +
		// Path + "'))"; //
		// String sql = "INSERT INTO `guttenberg` ( `Path`, `File`) VALUES ( " + "Test,"
		// + "twat" + ")";
		count++;

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) { // TODO
			System.out.println("SQLException execute");
			e.printStackTrace();
		}

		try {
			stmt.executeUpdate(
					"UPDATE gutenberg.books SET text= " + "'" + text + "' WHERE idbook = " + "'" + etextnumber + "'");
//			stmt.executeUpdate("UPDATE gutenberg.books SET text=LOAD_FILE(" + "'rawpath'" + ") WHERE idbook = " + "'etextnumber'");

		} catch (SQLException e) { // TODO
			System.out.println("SQLException execute");
			e.printStackTrace();
		}
		stmt.closeOnCompletion();
		System.out.println("Count " + count);
		book.text = null;
	}

	String escape(String string) {
		return StringEscapeUtils.escapeJava(string).replace("'", "");
	}
}
