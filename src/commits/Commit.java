package commits;

import java.util.ArrayList;
import java.util.Date;

public class Commit {
	
	private int date; // seconds since epoch
	private String hash;
	private ArrayList<String> files;
	
	public Commit(int date, String hash, ArrayList<String> changedFiles) {
		this.date = date;
		this.hash = hash;
		this.files = changedFiles;
	}
	
	public int getDate() {
		return date;
	}
	
	public String getHash() {
		return hash;
	}
	
	public ArrayList<String> getFiles() {
		return files;
	}
	
	public boolean containsFile(String file) {
		return files.contains(file);
	}
	
	public void print() {
		System.out.printf("Commit: %s Date: %s\n", hash, new Date(((long) date)*1000));
		for(String file : files) {
			System.out.println("  -" + file);
		}
	}
}
