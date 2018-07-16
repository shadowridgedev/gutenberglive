package guttenberg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindGuttenbergInfo {

	private String title;

	public FindGuttenbergInfo() {

		// TODO Auto-generated constructor stub
	}

	

	List<Book> getinfo(List<String> files) throws IOException {
		List<Book> books = new ArrayList<Book>();

		for (String filename : files) {
			getindexfileinfo(filename, books);
		}
		return books;

	}

	private List<Book> getindexfileinfo(String filename, List<Book> books) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = null;
		String index = null;
		while ((line = br.readLine()) != null) {
			line = removeBracket(line);
			if (line.contains("by")) {
				int len = line.length();
				int numsize = lastDigit(TrimLastChar(line));
				if (numsize > 0) {
					index = line.substring(len - (numsize + 1), len).trim();
					System.out.println("Book stored " + index + " line " + line);
					Book current = new Book();
					parsebook(line.substring(0, len - (numsize + 1)), current);
					current.EtextNumber = index;
					current.source = "Index";
					current.verified = false;
					current.parsed = false;
					books.add(current);
				}
			} else
				System.out.println("Book not stored  " + index + " line   " + line);

		}
		br.close();
		return books;
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
			if (word.equals("by"))
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
