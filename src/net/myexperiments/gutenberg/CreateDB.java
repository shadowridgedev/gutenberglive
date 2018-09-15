package net.myexperiments.gutenberg;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.DB.HashMapMaker;

public class CreateDB {
	static Properties propfile;;
	SolrInputDocumentWriter writer;;
	static MariaDbHelper dbsql;
	static FindGuttenbergInfo info;
	ArrayList<Book> only = new ArrayList<Book>();
	ArrayList<Book> books = new ArrayList<Book>();
	int numberfiles = 0;
	
	public CreateDB	(Properties propfile )  {
		// TODO Auto-generated method stub

		try {
			GutenbergMySqlStorage mysqlstore = new GutenbergMySqlStorage(propfile.getProperty("mysqlhost"),
					propfile.getProperty("mysqluser"), propfile.getProperty("mysqlpassword"));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
/*		String dbFile = propfile.getProperty("DBLocation");
		// Initialize a MapDB database
		DB db = DBMaker.fileDB(new File(dbFile)).closeOnJvmShutdown().make();
		// Create a Map:
		HashMapMaker<?, ?> myMap = db.hashMap(dbFile);
		HTreeMap<String, String> myonly = (HTreeMap<String, String>) myMap.create();
		myonly.put("Dog", "cat");

		dbsql = new MariaDbHelper();
		dbsql.createconnection(propfile, "", "", "");
*/



		try {

			String filetype = propfile.getProperty("filetype");
			numberfiles = Integer.parseInt(propfile.getProperty("numberfiles"));
			String createtable = propfile.getProperty("createbook");
			Path root = Paths.get(propfile.getProperty("GutenbergFileBase"));

			GuttenbergHelper helper = new GuttenbergHelper(propfile);
			numberfiles = helper.searchForFilesExt(root.toFile(), only, filetype, numberfiles, false);

			info = new FindGuttenbergInfo(root.toString());
			books = info.getinfo(only, filetype);

			showcapture(books);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static void showcapture(ArrayList<Book> books) throws SQLException {

		int count = 0, total = 0;
		dbsql.openConnection();
		for (Book thebook : books) {
			total++;
			if (info.goodbook(thebook)) {
				count++;
				printbook(thebook);
				dbsql.InsertBook(thebook);
			}
		}
		dbsql.closeconnection();
		System.out.println("Done " + count + " " + total);
	}

	static void printbook(Book current) {
		System.out.println("Book " + current.getAuthor() + " " + current.getTitle() + " " + current.getDate() + " "
				+ current.getEtextNumber());
	}

	
	/*
	 * public void testMoreComplex() throws Exception { File file = tmp.newFile();
	 * DB db =
	 * DBMaker.fileDB(file).closeOnJvmShutdown().encryptionEnable("password").make()
	 * ; //a new collection ConcurrentNavigableMap<Integer, String> map =
	 * db.getTreeMap("collectionName"); map.put(1, "one"); map.put(2, "two"); //
	 * map.keySet() is now [1,2] assertThat(map).hasSize(2); //persist changes into
	 * disk db.commit(); map.put(3, "three"); assertThat(map).hasSize(3); //
	 * map.keySet() is now [1,2,3] //revert recent changes db.rollback(); //
	 * map.keySet() is now [1,2] assertThat(map).hasSize(2); db.close(); }
	 */

}