package commits;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;

import frameaggregator.FrameAggregator;

import parser.FlowerObject;
import parser.ParseMethod;
import parser.XMLwritter;
import parser.XMLwritter2;

/* placeholder class for the commit history controller/iterator
  This class is supposed to iterate through a list of commits,
  and for each one, revert the codebase to version of when the
  commit was made, and feed it to the frame generator block.
*/
public class CommitHistoryController {
	public static void main(String args[]) throws Exception
	{
		FrameAggregator aggregator = new FrameAggregator();
		Repository repository = JGitHelper.openRepository();
        System.out.println("Having repository: " + repository.getDirectory());
        
        Iterator<RevCommit> iter = new Git(repository).log()
	              .addPath("src/net/java/sip/communicator/impl/protocol")
	              .call()
	              .iterator();
        
        ArrayList<Commit> commits = new ArrayList<Commit>();
		int count = 0;
		while(iter.hasNext()) {
			ArrayList<String> files = new ArrayList<String>();
			RevCommit rev = iter.next();

			int commitDate = rev.getCommitTime();
			String hash = rev.getName();
			String author = rev.getCommitterIdent().getName();
			
//	        System.out.printf("Commit: " + rev /*+ ", name: " + rev.getName() + ", id: " + rev.getId().getName()*/);
//	        System.out.printf(" Date: %s\n", new Date(((long) commitDate)*1000));
	        RevWalk rw = new RevWalk(repository);
	        if (rev.getParentCount() == 0) {
			  TreeWalk tw = new TreeWalk(repository);
			  tw.reset();
			  tw.setRecursive(true);
			  tw.addTree(rev.getTree());
			  while (tw.next()) {
//        		System.out.println(MessageFormat.format("({0} {1} {2}", ChangeType.ADD, tw.getRawMode(0), tw.getPathString()));
				String path = tw.getPathString();
//				files.add(path);
				int start = path.lastIndexOf('/');
				int end = path.lastIndexOf(".java");
    			if (start > 0 && end > 0) {
    				path = path.substring(path.lastIndexOf('/')+1,end);
    			}
				files.add(path);
			  }
			  tw.release();
	        } else {
	        	RevCommit parent = rw.parseCommit(rev.getParent(0).getId());
	        	DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
	        	df.setRepository(repository);
	        	df.setDiffComparator(RawTextComparator.DEFAULT);
	        	df.setDetectRenames(true);
	        	List<DiffEntry> diffs = df.scan(parent.getTree(), rev.getTree());
	        	for (DiffEntry diff : diffs) {
//	        		System.out.println(MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(), diff.getNewMode().getBits(), diff.getNewPath()));
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
		FileAgeTool fat = new FileAgeTool(commits);
		
		for(Commit commit : commits) {
			commit.print();
		}
		
		iter = new Git(repository).log()
				.addPath("src/net/java/sip/communicator/impl/protocol")
	            .call()
	            .iterator();
		
		// Do the reverts
		int countMax = 50;
		int increment = 10;
		count = 0;
		Git git = new Git(repository);
		while(count < countMax && iter.hasNext()) {
			RevCommit rev = iter.next();
			if (count != 0) {
				for(int i = 1; i<increment; i++) {
					if (iter.hasNext()) {
						rev = iter.next();
//						fat.setRefDate(lastCommitDate);
//						git.revert().include(rev).call();
//						System.out.println("Reverted: " + rev /*+ ", name: " + rev.getName() + ", id: " + rev.getId().getName()*/);
					}
				}
			}
			git.checkout().setStartPoint(rev).addPath("src/net/java/sip/communicator/impl/protocol").call();
			System.out.println("Checked-out: " + rev /*+ ", name: " + rev.getName() + ", id: " + rev.getId().getName()*/);
			int commitDate = rev.getCommitTime();
			fat.setRefDate(commitDate);
			// Flowers
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
			XMLwritter2.GenerateXML(flowers,commitDate,countMax-count-1);
			System.out.println("Frame Written: " + (countMax-count-1));
//			for(FlowerObject flower : flowers) {
//				System.out.printf("Committer:  %s Age: %s \n", flower.getLastCommitter(), flower.getAge());
//			}
			count++;
		}
		
		for(int i = 0; i<countMax; i++){
			aggregator.aggregateToRoot("frames/frame" + i +".xml");
		}
		aggregator.endAggregate();
		System.out.println("Done");
		repository.close();
		
//		RevWalk rw = new RevWalk(repository);
//		ObjectId head = repository.resolve(Constants.HEAD);
//		RevCommit commit = rw.parseCommit(head);
	}
	
	
}
