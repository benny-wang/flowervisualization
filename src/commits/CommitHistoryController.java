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

import parser.FlowerObject;
import parser.ParseMethod;

/* placeholder class for the commit history controller/iterator
  This class is supposed to iterate through a list of commits,
  and for each one, revert the codebase to version of when the
  commit was made, and feed it to the frame generator block.
*/
public class CommitHistoryController {

	public static void main(String args[]) throws Exception
	{
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
				files.add(tw.getPathString());
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
	        			files.add(diff.getNewPath());
	        			break;
	        		default:
	        			break;
	        		}
	        	}
	        }
	        commits.add(new Commit(commitDate,hash,files));
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
		count = 0;
		Git git = new Git(repository);
		while(count < 10 && iter.hasNext()) {
			RevCommit rev = iter.next();
			int commitDate = rev.getCommitTime();
			fat.setRefDate(commitDate);
//			git.revert().include(rev).call();
			
			// Flowers
			File folder = new File("C:\\Users\\Majin\\Documents\\GitHub\\jitsi\\src\\net\\java\\sip\\communicator\\impl\\protocol");
			ArrayList<FlowerObject> objs = ParseMethod.parseFlowers(folder);
			
			count++;
			System.out.println("Reverted: " + rev /*+ ", name: " + rev.getName() + ", id: " + rev.getId().getName()*/);
		}
		System.out.println("Done");
		repository.close();
		
//		RevWalk rw = new RevWalk(repository);
//		ObjectId head = repository.resolve(Constants.HEAD);
//		RevCommit commit = rw.parseCommit(head);
	}
	
	
}
