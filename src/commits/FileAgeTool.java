package commits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;

public class FileAgeTool {
	private final ArrayList<Commit> commits;
	private int refDate;
	
	public FileAgeTool(ArrayList<Commit> commits) {
		Collections.sort(commits, new Comparator<Commit>() {
			public int compare(Commit c1, Commit c2) {
				// Sorts by commit date (descending)
				return c1.getDate() >= c2.getDate() ? -1 : 1; 
			}
		});
		this.commits = commits;
		refDate = commits.get(0).getDate();
	}
	
//	public void addCommit(Commit commit) {
//		commits.add(commit);
//	}
	
	public void setRefDate(int date) {
		refDate = date;
	}
	
	
	public int getAge(String file) {
		Iterator<Commit> iter = commits.iterator();
		while(iter.hasNext()) {
			Commit commit = iter.next();
			int date = commit.getDate();
			if (date <= refDate && commit.containsFile(file)) {
				return refDate - date;
			}
		}
		return -1;
	}
	
	public Commit getCommit(String file) {
		Iterator<Commit> iter = commits.iterator();
		while(iter.hasNext()) {
			Commit commit = iter.next();
			int date = commit.getDate();
			if (date <= refDate && commit.containsFile(file)) {
				return commit;
			}
		}
		return null;
	}
}
