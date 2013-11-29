package commits;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
//import java.util.List;

import org.eclipse.jgit.api.Git;
//import org.eclipse.jgit.diff.DiffEntry;
//import org.eclipse.jgit.diff.DiffFormatter;
//import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
//import org.eclipse.jgit.revwalk.RevWalk;
//import org.eclipse.jgit.treewalk.TreeWalk;
//import org.eclipse.jgit.util.io.DisabledOutputStream;

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
		// repository expected to be in same directory as this project
		Repository repository = JGitHelper.openRepository("../jitsi/.git");
		System.out.println("Having repository: " + repository.getDirectory());
		
		String subPath = "src/net/java/sip/communicator/impl/protocol";
		
		ArrayList<Commit> commits = JGitHelper.retrieveCommits(repository,subPath);
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
		while(count < countMax && iter.hasNext()) {
			RevCommit rev = iter.next();
			if (count != 0) {
				for(int i = 1; i<increment; i++) {
					if (iter.hasNext()) {
						rev = iter.next();
					}
				}
			}
			// Checkout this version of the repository
			JGitHelper.checkoutRepository(repository, rev, subPath);
			System.out.println("Checked-out: " + rev);
			
			// frame date = date of last revision on the reverted git repo
			int commitDate = rev.getCommitTime();
			fat.setRefDate(commitDate);
			
			// Generating nodes for the frame
			File folder = new File(subPath);
			System.out.println("Parsing");
			
			// Parse source-code into nodes (flowers)
			ArrayList<FlowerObject> flowers = ParseMethod.parseFlowers(folder);
			// Get age of the nodes with the FileAgeTool
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
