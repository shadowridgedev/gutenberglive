package net.myexperiments.gutenberg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.text.StringEscapeUtils;
import java.sql.* ;  // for standard JDBC programs
import java.math.* ; // for BigDecimal and BigInteger support
public class MySqlStorageHelper {
	// static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
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

	public MySqlStorageHelper(Properties propfile) throws ClassNotFoundException {
		prop = propfile;
		root = propfile.getProperty("GutenbergFileBase");

		DBType = propfile.getProperty("mySQLDB");
		host = propfile.getProperty("mysqlhost");
		user = propfile.getProperty("mysqluser");
		password = propfile.getProperty("mysqlpassword");
		db = propfile.getProperty("db");
		cols = MakeArray(propfile.getProperty("fields"));
	}

	List<String> MakeArray(String namelist) {
		List<String> items = Arrays.asList(namelist.split(","));
		return items;

	}

	// Class.forName("com.mysql.jdbc.Driver");
	void openConnection() {
		// Open a connection
		System.out.println("Connecting to database...");

		try {

			PrintWriter log = new PrintWriter("Logfile");
			conn = DriverManager.getConnection(DBType + host + ":3306/", user, password);
			DriverManager.setLogWriter(log);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		} catch (SQLException e) { // TODO
			System.out.println("SQLException execute");
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