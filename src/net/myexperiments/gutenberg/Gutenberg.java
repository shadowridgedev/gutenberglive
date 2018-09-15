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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentNavigableMap;

import org.apache.solr.common.util.NamedList;
import org.mapdb.DB;
import org.mapdb.DB.HashMapMaker;
import org.mapdb.DB.TreeMapMaker;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;

import de.citec.scie.ner.db.mapdb.MapDBDatabase;

public class Gutenberg {

	public static void main(String[] args)  {
		Properties propfile = new Properties();
		InputStream in = ClassLoader.getSystemResourceAsStream("ward.properties");
		try {
			propfile.load(in);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		
		if(propfile.getProperty("LoadDB").contentEquals("load")) {
		CreateDB DB = new CreateDB(propfile);
		}
	}

	private static void writedoc() {
	}

	interface SolarInputDocumentWriter {
		// TODO Auto-generated method stub

	}

}