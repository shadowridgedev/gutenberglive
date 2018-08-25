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
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.text.StringEscapeUtils;

public class MariaDBHelper {
	String con;
	String mysqlpassword;
	Connection connection;
	String theusername;
	String thehostname;
	String thehost;
	String theuser;
	String thepassword;
	String thedb;
	String thetable;
	String theDate;
	String sql;
	String theText;
	int size;
	List<String> cols;
	Connection conn = null;
	Statement stmt = null;
	String DBType = null;
	Properties prop = null;
	int count = 100;
	String root;
	int max;

	boolean createconnection(Properties prop, String host, String sql, String table, String db) {
		theusername = prop.getProperty("mysqluser");
		thepassword = prop.getProperty("mysqlpassword");
		if (host == "")
			host = prop.getProperty("mysqlhost");
		if (sql == "")
			sql = prop.getProperty("createtable");
		if (table == "")
			table = prop.getProperty("table");
		if (db == "")
			db = prop.getProperty("db");
		if (host == "")
			host = prop.getProperty("mysqlhost");
		thehost = host;
		thetable = table;
		thedb = db;

		try {
			// "jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");
			String con = "jdbc:mariadb://" + thehost + ":3306/" + "?user=" + theusername + "&password=" + thepassword;
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
//		System.out.println("Connecting to database...");

		try {
			if (connection.isClosed()) {
				PrintWriter log = new PrintWriter("Logfile");
				sql = "jdbc:mariadb://" + thehost + ":3306/" + thedb + "?user=" + theusername + "&password="
						+ thepassword;
				connection = DriverManager.getConnection(sql);
				DriverManager.setLogWriter(log);
//			System.out.println("Writing records into the table...");

				stmt = connection.createStatement();
			}
		} catch (FileNotFoundException | SQLException e) {
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

		String sql = "SELECT COUNT(" + col + ") AS total FROM " + thetable + ";";

		try {
			java.sql.ResultSet rs = stmt.executeQuery(sql);
			if (rs != null) {
				rs.next();
				count = rs.getInt("total");
			}
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
		try {
			closeconnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	HashMap<String, String> GetBookMetadata(String text) {

		HashMap<String, String> items = new HashMap<String, String>();
		int index;
		String[] lines = new String[1000];
//		String[] lines = text.split(System.getProperty("line.separator"));
		lines = (String[]) text.split( "\n", 1000);
		
		for (String line : lines) {
			System.out.println(line);
			if ((index = line.lastIndexOf("Title:")) != -1) {
				items.put("Title", line.substring(index++).trim());
				continue;
			}
			if ((index = line.lastIndexOf("Author:")) != -1) {
				items.put("Author", line.substring(index++).trim());
				continue;
			}
			if ((index = line.lastIndexOf("of several works by")) != -1) {
				if (!items.containsKey("Author")) {
					items.put("Author", line.substring(index++).trim());
				}
				continue;
			}
			if ((index = line.lastIndexOf("Date:")) != -1) {
				items.put("Date", line.substring(index++).trim());
				continue;
			}
			if ((index = line.lastIndexOf("Release Date:")) != -1) {
				items.put("Date", line.substring(index++).trim());
				if ((index = line.lastIndexOf("[EBook")) != -1) {
					items.put("Ebook", line.substring(index++).trim());
				}
				continue;
			}
			if ((index = line.lastIndexOf("Etext")) != -1) {
				items.put("Ebook", line.substring(index).replace("]", "").trim());
				continue;
			}

			if ((index = line.lastIndexOf("The Project Gutenberg Etext of")) != -1) {
				if (!items.containsKey("Title")) {
					items.put("Title", line.substring(index++).trim());
				}
				continue;
			}

			if ((index = line.lastIndexOf("Translanted by")) != -1) {
				items.put("Translanted by", line.substring(index++).trim());
				continue;
			}

			if ((index = line.lastIndexOf("*** START OF THIS PROJECT GUTENBERG EBOOK")) != -1) {
				if (!items.containsKey("Title")) {
					items.put("Title", line.substring(index++).replace("***", "").trim());
				}
				continue;
			}

			if ((index = line.lastIndexOf("Language:")) != -1) {
				items.put("Language", line.substring(index++).trim());
				continue;
			}

		}
		return items;

	}

	Book addMetadata(Book book, HashMap<String, String> items) {
		book.author = items.get("Author");
		book.title = items.get("Title");
		return book;
	}

	public void InsertBook(Book book) throws IOException, SQLException {
//		System.out.println("Writing records into the table...");
//		if (count < 4000) {
//			GetBookMetadata(book.text);
//		}
		openConnection();
		int idBook = book.getIdBook();
		String etextnumber = book.getEtextNumber();
		String path = book.getPath();

		String author = escape(book.getAuthor());
		String title = escape(book.getTitle());
		String date = book.getDate();
		String file = book.getFilename();
		String rawpath = escape(path);
		size = book.size;
//  	    byte[] textbytes = Files.readAllBytes(Paths.get(path));
		String theText = book.text;
//		String text = new String(textbytes);
		theText = escape(theText);
		size = theText.length();
		String name = book.getName();
//		size = text.length();
//		path = escape(path);
//		String sql = "INSERT  INTO gutenberg.books (  idBook, Title, Author, Date, File, Filename, Path, EtextNumber, Name) VALUES ("'" +count, title, author, date, file, filename, path, etextnumber, name )";
//		String sql = "INSERT  INTO guttenberg.books ( Author, Title, Date, Path, File, Text) VALUES (" + "'" + author +  "',"  + title + "',"  + "'," + date + "`," + path + "`,`" + file + LOAD_FILE(rawpath)" )";
		String sql = "INSERT  INTO gut.books ( idBook, Author, Title, Date, Path, File, Size) VALUES ( '" + idBook
				+ "','" + author + "','" + title + "','" + date + "','" + path + "','" + file + "','" + size + "')";

		count++;

		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) { // TODO
			System.out.println("SQLException execute");
			e.printStackTrace();
		}

		try {
			stmt.executeUpdate(
					"UPDATE gut.books SET Text= " + "'" + theText + "' WHERE idbook = " + "'" + idBook + "'");
//			String textload = "UPDATE gut.books SET Text=LOAD_FILE(" + "'rawpath'" + ") WHERE idBook = " + "'idBook'";
//			stmt.executeUpdate(textload);

		} catch (SQLException e) { // TODO
			System.out.println("SQLException Update");
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
