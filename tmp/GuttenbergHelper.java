package guttenberg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class GuttenbergHelper {
	int GuttenbergFiles = 0;
	int NotGuttenbergFiles = 0;
	int count = 0;
	private ArrayList<String> removetext = new ArrayList<String>();

	// ArrayList<String> checklist = new ArrayList<String> ();

	GuttenbergHelper(String prop) throws IOException {

		String GuttenbergPath = prop + "\\Guttenberg1\\";
		String NotGuttenbergPath = prop + "\\NotGuttenberg\\";
		String RemoveText = prop + "\\RemoveText\\";
		String CleanBook = prop + "\\CleanBook\\";
		String[] paths = new File(RemoveText).list();
		if (paths != null) {
			for (String remove : paths) {
				Path path = Paths.get(RemoveText + remove);

				removetext.add(new String(Files.readAllBytes(path)));
			}
		}
	}

	boolean isGuttenberg(File current) throws FileNotFoundException {
		boolean Guttenberg = false;
		if (current.isFile() && current.exists() && current.getName().endsWith("txt")
				&& !current.getName().endsWith("zip") && lookslikeGuttenberg(current)) {
			GuttenbergFiles++;
			Guttenberg = true;
			return Guttenberg;
		}
		NotGuttenbergFiles++;
		return Guttenberg;

	}

	boolean lookslikeGuttenberg(File current) throws FileNotFoundException {
		Scanner scanner = null;

		try {
			scanner = new Scanner(current);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		// now read the file line by line...

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.contains(" Project Gutenberg ") || line.contains("Project Gutenberg's")
					|| !line.contains("Preliminary Matter.")
							&& !line.contains("The Project Gutenberg Etext of Human Genome Project")) {
				// System.out.println("ho hum, i found it on line " + lineNum);
				scanner.close();
				GuttenbergFiles++;
				return true;
			}
		}
		// System.out.println("Not Guttenberg1 file is " + current.toString() +
		// "
		// Size is " + current.length() + " Not Guttenberg1 " +
		// NotGuttenbergFiles++ + " GuttenbergFiles " + GuttenbergFiles);
		/*
		 * scanner.close(); scanner = new Scanner(current);
		 * 
		 * for (int x = 0; x < 3; x++) { while (scanner.hasNextLine()) {
		 * System.out.println(scanner.nextLine()); } }
		 */
		scanner.close();
		return false;
	}

	HashMap<String, String> GetBookMetadata(String text) {

		HashMap<String, String> items = new HashMap<String, String>();
		int index;
		String[] lines = text.split(System.getProperty("line.separator"));

		for (String line : lines) {
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

	Book RemoveText(Book book) throws IOException {
		Path p = Paths.get(book.getPath());
		String text = book.getText();
		Iterator<String> itt = removetext.listIterator();
		String replaced;
		while (itt.hasNext()) {
			replaced = itt.next();
			text.replace(replaced, "%^%^");

		}
		book.setText(text);
		return book;
	}

	public int searchForFilesExt(File root, ArrayList<File> only, String ext, int max) throws Exception {
		// TODO Auto-generated method stub
		if (count > max)
			return count;

		if (root == null || only == null)
			return 0; // just for safety
		// || !root.getPath().toString().contains("old"))
		if (root.isDirectory()) {
			// System.out.println(root.toString());
			for (File file : root.listFiles()) {
				if (file != null) {
					searchForFilesExt(file, only, ext, max);
				}
			}
		} else if (root.isFile() && root.getName().endsWith(ext)) {
			count++;
			System.out.println(count + "    " + root.getName());
			only.add(root);
		}
		return count;
	}

	/*
	 * void ProcessFiles(ArrayList<File> only, int max) { while ( i++ != max) {
	 * 
	 * for (File current : only) { String result = ("File " + current.getName()
	 * + " "); if (isGuttenberg(current)) { result += (" is Guttenbberg"); Path
	 * local = Paths.get(GuttenbergPath + current.getName());
	 * Files.copy(current.toPath(), local, REPLACE_EXISTING); Book book = new
	 * Book(); book.setPath(local.toString()); book.setText(new
	 * String(Files.readAllBytes(local))); book.setName(current.getName());
	 * metadata = GetBookMetadata(book.text);
	 * 
	 * // add own metadata metadata.put("extra", "Things");
	 * 
	 * System.out.println(Arrays.asList(metadata)); // method 1 book =
	 * addMetadata(book, metadata);
	 * 
	 * book = helper.RemoveText(book); book.setPath(CleanBook + book.getName());
	 * Files.write(Paths.get(book.getPath()), book.getText().getBytes());
	 * 
	 * result += current.getPath() + "    " + current.getName();
	 * storage.InsertBook(book);
	 * 
	 * } else { Path local = Paths.get(NotGuttenbergPath + current.getName());
	 * Files.copy(current.toPath(), local, REPLACE_EXISTING); result +=
	 * " is not Guttenberg"; } System.out.println(result);
	 * 
	 * } /* int problem = count - (helper.GuttenbergFiles +
	 * helper.NotGuttenbergFiles); if (problem != 0)
	 * System.out.println("Problem " + problem);
	 * System.out.println("Final count Guttenberg Files" +
	 * helper.GuttenbergFiles + " Not Guttenberg Files " +
	 * helper.NotGuttenbergFiles); }
	 */
}

