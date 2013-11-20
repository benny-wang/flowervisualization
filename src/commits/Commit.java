package commits;

import java.util.ArrayList;
import java.util.Date;

public class Commit {
	
	private int date; // seconds since epoch
	private String hash;
	private ArrayList<String> files;
	private String author;
	
	public Commit(int date, String hash, String author, ArrayList<String> changedFiles) {
		this.date = date;
		this.hash = hash;
		this.author = author;
		this.files = changedFiles;
	}
	
	public int getDate() {
		return date;
	}
	
	public String getHash() {
		return hash;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public ArrayList<String> getFiles() {
		return files;
	}
	
	public boolean containsFile(String file) {
		return files.contains(file);
	}
	
	public void print() {
		System.out.printf("Author: %s Commit: %s Date: %s\n", author, hash, new Date(((long) date)*1000));
		for(String file : files) {
			System.out.println("  -" + file);
		}
	}
}
