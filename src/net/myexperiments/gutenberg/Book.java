package net.myexperiments.gutenberg;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;

import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

public class Book implements  Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5068990239464021287L;
	int idBook;
	String title;
	String author ;
	String date;
	String ReleaseDate;
	byte[] text ;
	String extra ;
	String filename ;
	String path;
	String EtextNumber ;
	String source ;
	String name ;
	boolean verified = false;
    public String getReleaseDate() {
		return ReleaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		ReleaseDate = releaseDate;
	}
	/**
	 * @return the idBook
	 */
	public int getIdBook() {
		return idBook;
	}
	/**
	 * @param idBook the idBook to set
	 */
	public void setIdBook(int idBook) {
		this.idBook = idBook;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		if (text != null)
		return text.toString();
		else return null;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text =  text.getBytes();
	}
	/**
	 * @return the extra
	 */
	public String getExtra() {
		return extra;
	}
	/**
	 * @param extra the extra to set
	 */
	public void setExtra(String extra) {
		this.extra = extra;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return the etextNumber
	 */
	public String getEtextNumber() {
		return EtextNumber;
	}
	/**
	 * @param etextNumber the etextNumber to set
	 */
	public void setEtextNumber(String etextNumber) {
		EtextNumber = etextNumber;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the verified
	 */
	public boolean isVerified() {
		return verified;
	}
	/**
	 * @param verified the verified to set
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	/**
	 * @return the parsed
	 */
	public boolean isParsed() {
		return parsed;
	}
	/**
	 * @param parsed the parsed to set
	 */
	public void setParsed(boolean parsed) {
		this.parsed = parsed;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	boolean parsed = false;
	public String language;

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book theBook= (Book) o;
/*
        if (title != null ? !title.equals(theBook.title) ) return false;
        if (author != null ? !author.equals(theBook.author) : theBook.author != null) return false;
        if (city != null ? !city.equals(theBook.city) : theBook.city != null) return false;
        if (name != null ? !name.equals(theBook.name) : theBook.name != null) return false;
        if (city != null ? !city.equals(theBook.city) : theBook.city != null) return false;
        if (name != null ? !name.equals(theBook.name) : theBook.name != null) return false;
        
    	String title;
    	String author ;
    	String date;
    	String ReleaseDate;
    	String text ;
    	String extra ;
    	String filename ;
    	String path;
    	String EtextNumber ;
    	String source ;
    	String name ;
    	boolean verified = false;
    	*/
        return true;
    }

	
    class CustomSerializer implements Serializer<Book>, Serializable{

        public void serialize(DataOutput out, Book value) throws IOException {
            out.writeUTF(value.getName());
            out.writeUTF(value.getAuthor());
        }

        public Book  deserialize(DataInput in, int available) throws IOException {
            return new Book();
        }

		@Override
		public void serialize(DataOutput2 out, Book value) throws IOException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Book deserialize(DataInput2 input, int available) throws IOException {
			// TODO Auto-generated method stub
			return null;
		}
    }


}
