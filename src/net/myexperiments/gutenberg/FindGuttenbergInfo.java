package net.myexperiments.gutenberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FindGuttenbergInfo {

	private String root;

	public FindGuttenbergInfo(String base) {
		this.root = base;
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Book> getinfo(ArrayList<Book> only, String ext) throws IOException {
		ArrayList<Book> books = new ArrayList<Book>();

		for (Book thebook : only) {
			books = getindexfileinfo(thebook.path, books, ext);
		}
		return books;

	}

	private ArrayList<Book> getindexfileinfo(String filename,ArrayList<Book> books, String ext) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;

		Book current = new Book();
		while ((line = br.readLine()) != null) {
			current.author = ckstring("Author:", line);
			if (current.author != null )   
				System.out.println(current.getAuthor()  + " " + current.getTitle() + " " + current.getReleaseDate( ) + " " + current.getEtextNumber( ));

				
			current.title = ckstring("Title:", line);
			current.ReleaseDate = ckstring("Release Date:", line);
			current.language = ckstring("Language:", line);

		}
		File f = new File(filename);
		current.filename = f.getName();
		current.path = f.toPath().toString().replace(root, "");
		current.source = "Gutenberg";
		// current.text = readAllBytes(filename);
		String EtextNumber = current.filename.replace("." + ext, "");
		current.EtextNumber= EtextNumber;
		books.add(current);
	    System.out.println(current.filename + " " + current.getAuthor()  + " " + current.getTitle() + " " + current.getReleaseDate( ) + " " + current.getEtextNumber( ));

		br.close();
		return books;
	}

	private static String readAllBytes(String filePath) {
		String content = "";

		try {
			content = new String(Files.readAllBytes(Paths.get(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return content;
	}

	String ckstring(String test, String line) {
		if (line.contains(test)) {
			String value = line.replace(test, "");
			if (value != "") {
				System.out.println("Checking " + test +"  Value " + value );
			}
				
			return value;
		}
		return null;
	}

	String removeBracket(String line) {
		String result = getBrackted(line);
		if (result != "")
			line = line.replace(result, " ");
		return line;
	}

	private Book parsebook(String line, Book current) {
		String title = "";
		String author = "";
		boolean flag = true;

		String[] sentence = line.split(" ");
		for (String word : sentence) {
			if (word.equals("Title:"))
				flag = false;
			if (flag)
				title = title + " " + word;
			else
				author = author + " " + word;

		}
		title = title.replace(",", " ");
		author = author.replace("by", "");
		current.title = title.trim();
		current.author = author.trim();
		return current;
	}

	private String getBrackted(String line) {
		if (line.contains("[") && line.contains("]")) {
			int begin = line.indexOf('[');
			int end = line.indexOf(']');
			if ((begin >= 0) && (begin < end) && end <= line.length())
				return line.substring(begin, end) + "]";

		}
		return "";
	}

	String TrimLastChar(String s) {
		char c = s.charAt(s.length() - 1);
		if (s.charAt(s.length() - 1) == 'C') {
			return s.substring(0, s.length() - 1);
		}
		return s;

	}

	public int lastDigit(String s) {
		int count = 0;

		if (s != null && !s.isEmpty()) {
			String d = new StringBuilder(s).reverse().toString();
			for (char c : d.toCharArray()) {
				if (Character.isDigit(c)) {
					count++;
				} else
					break;
			}
		}

		return count;
	}

}
