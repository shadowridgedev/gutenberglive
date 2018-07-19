package net.myexperiments.gutenberg;

import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.solr.common.util.NamedList;
import org.mapdb.DBMaker;

import de.citec.scie.ner.db.mapdb.MapDBDatabase;

public class Gutenberg {
	static Properties propfile;;
	SolrInputDocumentWriter writer;;

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		propfile = new Properties();

		InputStream in = ClassLoader.getSystemResourceAsStream("ward.properties");
		propfile.load(in);
		GutenbergMySqlStorage mysqlstore = new GutenbergMySqlStorage(propfile.getProperty("mysqlhost"),propfile.getProperty("mysqluser"),propfile.getProperty("mysqlpassword"));
		
		ArrayList<Book> only = new 	ArrayList<Book> ();
		ArrayList<Book> books = new ArrayList<Book>();
		int numberfiles = 0;
		try {

			// String base = "Z:\\gut\\";
			// String base = "/media/sf_gutenberg/";

			// String base = args[0];

			// GuttenbergHelper helper = new
			// GuttenbergHelper("properties\\ward.properties");
			// int numfiles = Integer.parseInt(helper.getprop("numberfiles"));
			String filetype =propfile.getProperty("filetype");
			numberfiles = Integer.parseInt(propfile.getProperty("numberfiles"));
	        String createtable = propfile.getProperty("createtable");
	        
			Path root = Paths.get(propfile.getProperty("GutenbergFileBase"));
			GuttenbergHelper helper = new GuttenbergHelper(propfile);
			numberfiles = helper.searchForFilesExt(root.toFile(), only, filetype, numberfiles, true);

			// only = helper.searchForFilesExt(new File(helper.GuttenbergPath), only,
			// filetype, numfiles);

			FindGuttenbergInfo info = new FindGuttenbergInfo(root.toString());
			only = info.getinfo(only, filetype);
	
			showcapture(only);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	static void showcapture ( ArrayList<Book> books)
	{
		int count = 0, total = 0;
		for ( Book thebook : books) {
			total++;
			if ( thebook.filename != null)  {
				count++;
				printbook(thebook);
			}		
		}
		System.out.println( "Done "+ count + " " + total  );
	}
	
	static void printbook( Book current) {
		System.out.println("Book " + current.getAuthor()  + " " + current.getTitle() + " " + current.getDate( ) + " " + current.getEtextNumber( ));
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
