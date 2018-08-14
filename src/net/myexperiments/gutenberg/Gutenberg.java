package net.myexperiments.gutenberg;

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
import org.mapdb.DB.HashMapMaker;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

public class Gutenberg {
	static Properties propfile;;
	SolrInputDocumentWriter writer;;
	static MariaDBHelper dbsql;
	static FindGuttenbergInfo info;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub		
		propfile = new Properties();
		InputStream in = ClassLoader.getSystemResourceAsStream("ward.properties");
		propfile.load(in);

		MySqlStorageHelper mysqlstore = new MySqlStorageHelper(propfile);

		String dbFile = propfile.getProperty("MapDBLocation");
		// Initialize a MapDB database
		File mdnew = new File(dbFile);
//		DB db = DBMaker.fileDB(mdnew).make();

		// Create a Map:
//		HashMapMaker<?, ?> myMap = db.hashMap(dbFile);
//		HTreeMap<String, Book> myonly = (HTreeMap<String, Book>) myMap.create();

		dbsql = new MariaDBHelper();
		dbsql.createconnection(propfile, "", "", "");

		ArrayList<Book> only = new ArrayList<Book>();
		ArrayList<Book> books = new ArrayList<Book>();
		int numberfiles = 0;

//		if (propfile.getProperty("loaddb") =="Yes") {
			try {
	

			String filetype = propfile.getProperty("filetype");
			numberfiles = Integer.parseInt(propfile.getProperty("numberfiles"));
			String createtable = propfile.getProperty("createtable");

			Path root = Paths.get(propfile.getProperty("GutenbergFileBase"));

			GuttenbergHelper helper = new GuttenbergHelper(propfile);
			numberfiles = helper.searchForFilesExt(root.toFile(), only, filetype, numberfiles, false);

			info = new FindGuttenbergInfo(root.toString());
			books = info.getinfo(only, filetype);

			mysqlstore.StoreBooks(books);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		}	
       int count =  dbsql.countcol("Title", "gutenberg.books");
       System.out.println("Count " +count);
	}

	static void showcapture(ArrayList<Book> books) throws SQLException {

		int count = 0, total = 0;
		dbsql.openConnection();
		for (Book thebook : books) {
			total++;
			if (!info.goodbook(thebook)) {
				count++;
				printbook(thebook);
				try {
					dbsql.InsertBook(thebook);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		dbsql.closeconnection();
		System.out.println("Done " + count + " " + total);
	}

	static void printbook(Book current) {
		System.out.println("Book " + current.getAuthor() + " " + current.getTitle() + " " + current.getDate() + " "
				+ current.getEtextNumber());
	}

	private static void writedoc() {
	}

	interface SolarInputDocumentWriter {
		// TODO Auto-generated method stub

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
