package commits;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import parser.FlowerObject;
import parser.ParseMethod;
import parser.XMLwriter;
import frameaggregator.FrameAggregator;

/* 
 * Main class for generating snapshots of the code base in XML format.
 * Exported data is subsequently fed to the Visualization class
 */
public class CommitHistoryController {
	
	// Number of snapshots to generate.
	private static final int countMax = 50;
	
	// Number of commits to revert per snapshot.
	private static final int increment = 1;
	
	// main function that eventually produces an XML file that collects 
	//  multiple snapshots of the target code-base.
	public static void main(String args[]) throws Exception
	{
		Repository repository = JGitHelper.openRepository("../jitsi/.git");
		System.out.println("Having repository: " + repository.getDirectory());
		
		String subPath = "src/net/java/sip/communicator/impl/protocol";
		
		ArrayList<Commit> commits = retrieveCommits(repository,subPath);
		FileAgeTool fat = new FileAgeTool(commits);
		
		generateFrames(repository, subPath, fat);
		
		// Collect the frames into one file.
		FrameAggregator aggregator = new FrameAggregator();
		for(int i = 0; i<countMax; i++){
			aggregator.aggregateToRoot("frames/frame" + i +".xml");
		}
		aggregator.endAggregate("results/result-merged.xml");
		
		repository.close();
		System.out.println("Done");
	}
	
	// Walks through the commit tree of the target git repository
	//  that involves all files in the given sub-path. Returns a collection
	//  of the relevant commits.
	private static ArrayList<Commit> retrieveCommits(Repository repository, String subPath)
			throws Exception
	{
        // for the result
        ArrayList<Commit> commits = new ArrayList<Commit>();
        
        Iterator<RevCommit> iter = new Git(repository).log()
	              .addPath(subPath)
	              .call()
	              .iterator();
        
		int count = 0;
		while(iter.hasNext()) {
			ArrayList<String> files = new ArrayList<String>();
			RevCommit rev = iter.next();
			
			int commitDate = rev.getCommitTime();
			String hash = rev.getName();
			String author = rev.getCommitterIdent().getName();
			
			// Walk the commit tree to determine affected files
	        RevWalk rw = new RevWalk(repository);
	        if (rev.getParentCount() == 0) {
		        // If the commit does not have a parent,
	        	//  all files are newly added
	        	TreeWalk tw = new TreeWalk(repository);
				tw.reset();
				tw.setRecursive(true);
				tw.addTree(rev.getTree());
				while (tw.next()) {
					String path = tw.getPathString();
					int start = path.lastIndexOf('/');
					int end = path.lastIndexOf(".java");
	    			if (start > 0 && end > 0) {
	    				path = path.substring(path.lastIndexOf('/')+1,end);
	    			}
	    			files.add(path);
				}
				tw.release();
	        } else {
		        // If the commit does have a parent,
	        	//  check diffs to determine file changes
	        	RevCommit parent = rw.parseCommit(rev.getParent(0).getId());
	        	DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
	        	
	        	df.setRepository(repository);
	        	df.setDiffComparator(RawTextComparator.DEFAULT);
	        	df.setDetectRenames(true);
	        	List<DiffEntry> diffs = df.scan(parent.getTree(), rev.getTree());
	        	for (DiffEntry diff : diffs) {
	        		// treat add modify and rename as having changed the file
	        		switch (diff.getChangeType()) {
	        		case ADD:
	        		case MODIFY:
	        		case RENAME:
	        			String path = diff.getNewPath();
	    				int start = path.lastIndexOf('/');
	    				int end = path.lastIndexOf(".java");
	        			if (start > 0 && end > 0) {
	        				path = path.substring(path.lastIndexOf('/')+1,end);
	        			}
	    				files.add(path);
	        			break;
	        		default:
	        			break;
	        		}
	        	}
	        }
	        commits.add(new Commit(commitDate,hash,author,files));
	        count++;
	        rw.dispose();
		}
		System.out.println("Had " + count + " commits overall on current branch");
		// Print out list of retrieved commits.
		for(Commit commit : commits) {
			commit.print();
		}
		return commits;
	}
	
	// iterate backwards through the most recent set of commits on the subPath,
	//  revert the code-base and generating snapshots at each step
	private static void generateFrames(Repository repository, String subPath, FileAgeTool fat)
			throws Exception
	{
		repository.getDirectory();
		Iterator<RevCommit> iter =
			new Git(repository).log()
				.addPath(subPath)
	            .call()
	            .iterator();
			
		int count = 0;
		Git git = new Git(repository);
		while(count < countMax && iter.hasNext()) {
			RevCommit rev = iter.next();
			if (count != 0) {
				for(int i = 1; i<increment; i++) {
					if (iter.hasNext()) {
						rev = iter.next();
					}
				}
			}
			git.checkout().setStartPoint(rev).addPath(subPath).call();
			System.out.println("Checked-out: " + rev);
			
			// frame date = date of last revision on the reverted git repo
			int commitDate = rev.getCommitTime();
			fat.setRefDate(commitDate);
			
			// Generating nodes for the frame
			File folder = new File("../jitsi/src/net/java/sip/communicator/impl/protocol");
			System.out.println("Parsing");
			ArrayList<FlowerObject> flowers = ParseMethod.parseFlowers(folder);
			for(FlowerObject flower : flowers) {
				String path = flower.getName();
				Commit commit = fat.getCommit(path);
				
				if (commit != null) {
					int age = commitDate - commit.getDate();
					flower.setAge(age);
					flower.setLastCommitter(commit.getAuthor());
				} else {
					flower.setAge(-1);
					flower.setLastCommitter("N/A");
				}
			}
			XMLwriter.GenerateXML(flowers,commitDate,countMax-count-1);
			System.out.println("Frame Written: " + (countMax-count-1));
			count++;
		}
	}
}
