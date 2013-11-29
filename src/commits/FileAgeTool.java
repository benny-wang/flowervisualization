package commits;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;


/*
 * A tool that to help determine the age of the files in the
 *  repository at different revisions.
 */
public class FileAgeTool {
	
	// Set of commits to be finalized at object instantiation
	private final ArrayList<Commit> commits;
	
	// Reference date to determine the age of the files
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
	
	public void setRefDate(int date) {
		refDate = date;
	}
	
	// get just the age of the specified file
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

	// get the commit that corresponds to the file's last revision
	//  (capped by the reference date)
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
