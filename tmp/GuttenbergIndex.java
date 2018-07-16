package guttenberg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.mockito.internal.verification.Only;

public class GuttenbergIndex {

	public GuttenbergIndex() {
		// TODO Auto-generated constructor stub
	}

	public List<String> GetGuttbergIndex(String path) {
		// read file into stream, try-with-resources
		List<String> Only;
		Only = new ArrayList<String>();
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println("File " + listOfFiles[i].getName());
				if (listOfFiles[i].toString().contains("GUTINDEX")) {
					Only.add(listOfFiles[i].toString());
					System.out.println("File with G " + listOfFiles[i].getName());
				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}

		}
		return Only;
	}



	public List<String> getFiles(String path) {
		List<String> Only;
		Only = new ArrayList<String>();
		try (Stream<String> stream = Files.lines(Paths.get(path + "\\GUTINDEX.ALL"))) {

			Iterator<String> i = Files.lines(Paths.get(path)).iterator();
			for (i = stream.iterator(); i.hasNext();) {

				{
					Only.add(i.next());

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Only;
	}

}
