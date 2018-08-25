package net.myexperiments.gutenberg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;

public class FindGuttenbergInfo {

	private String root;
	int count = 0;

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

	private ArrayList<Book> getindexfileinfo(String filename, ArrayList<Book> books, String ext) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		Book current = new Book();
		String text ="";
		current.title = "Title";
		current.author = "Author";
		int Linecount = 0;
		Stream<String> s = br.lines();
		
		Iterator<String> i = s.iterator();
			
			while ( i.hasNext() ) {
			line = i.next();
			if (Linecount < 1000) {
				current.author = ckstring("Author:", line, current.author);
				current.title = ckstring("Title:", line, current.title);
				current.ReleaseDate = ckstring("Release Date:", line, current.ReleaseDate);
				current.language = ckstring("Language:", line, current.language);
				Linecount++;
			}
			text += line;

		}
		text = escape(text);
		count++;
		current.size = text.length();
		if (goodbook(current)) {
			System.out.println(count + " " + current.size +" Storing " + current.getAuthor() + " " + current.getTitle() + " "
					+ current.getReleaseDate() + " " + current.getEtextNumber());
		}
		File f = new File(filename);
		current.filename = f.getName();
		current.path = f.toPath().toString();

		current.source = "Gutenberg";
		current.text = text;
//		current.text = Files.readAllBytes(Paths.get(current.path));
		String EtextNumber = current.filename.replace("." + ext, "");
		current.EtextNumber = EtextNumber;
		current.idBook = count;
		current.verified = false;
//		System.out.println("Number " +current.EtextNumber + " Title " + current.title);
//		System.out.println("Adding " +current.getEtextNumber()   + " " + current.getTitle() + " "
//				+ current.getReleaseDate() + " " + current.getAuthor() + " " + current.path);
		books.add(current);
		current = null;
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

	Boolean chbookforNull(Book book) {

		if (ckfield(book.author))
			return true;
		if (ckfield(book.name))
			return true;
		if (ckfield(book.language))
			return true;
		if (ckfield(book.title))
			return true;
		if (ckfield(book.date))
			return true;
		if (ckfield(book.ReleaseDate))
			return true;
		return false;
	}

	boolean ckfield(String value) {
		return (value != null);
	}

	String ckstring(String test, String line, String CurrentValue) {
		if (line.contains(test)) {
			line = line.replace(test, "");
			if (line != "") {
//				System.out.println("Checking " + test + "  Value " + line);
				return StringEscapeUtils.escapeJava(line.trim());

			}
		}
		return CurrentValue;
	}

	boolean goodbook(Book book) {
		if (ckfield(book.author) && (ckfield(book.title)))
			return true;
		return false;
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

	String escape(String string) {
		return StringEscapeUtils.escapeJava(string).replace("'", "");
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
