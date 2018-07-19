package net.myexperiments.gutenberg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class MariaDbHelper {

	boolean createconnection(Properties prop, String host, String sql, String table) {
		String username = prop.getProperty("mysqluser");
		String mysqlpassword = prop.getProperty("mysqlpassword");
		if (host=="")  host = prop.getProperty("mysqlhost");
		if (sql=="")  sql = prop.getProperty("createtable");
		if (table == "") table = prop.getProperty("table");
		Connection connection;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"+ host + ":3306/" +table, username, mysqlpassword );
			Statement stmt = connection.createStatement();
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

}
