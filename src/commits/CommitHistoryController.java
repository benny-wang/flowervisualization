package commits;

import java.text.MessageFormat;
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
        
//        Iterable<RevCommit> logs = new Git(repository).log()
//                .all()
//                .call();
        
        Iterator<RevCommit> iter = new Git(repository).log()
	              .addPath("src/net/java/sip/communicator/impl/protocol")
	              .call()
	              .iterator();
        
		int count = 0;
		boolean first = true;
		long refDate = 0;
		
		while(iter.hasNext()) {
			RevCommit rev = iter.next();
//			Date commitDate = new Date(((long) rev.getCommitTime())*1000);
			int commitDate = rev.getCommitTime();
			if (first) {
				first = false;
				refDate = commitDate;
			}
			long age = refDate - commitDate;
	        System.out.printf("Commit: " + rev /*+ ", name: " + rev.getName() + ", id: " + rev.getId().getName()*/);
	        System.out.printf(" Date: %s Age: %s \n", new Date(((long) commitDate)*1000), age);
	        RevWalk rw = new RevWalk(repository);
	        if (rev.getParentCount() == 0) {
			  TreeWalk tw = new TreeWalk(repository);
			  tw.reset();
			  tw.setRecursive(true);
			  tw.addTree(rev.getTree());
			  while (tw.next()) {
        		System.out.println(MessageFormat.format("({0} {1} {2}", ChangeType.ADD, tw.getRawMode(0), tw.getPathString()));
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
	        		System.out.println(MessageFormat.format("({0} {1} {2}", diff.getChangeType().name(), diff.getNewMode().getBits(), diff.getNewPath()));
	        	}
	        }
	        
	        count++;
	        rw.dispose();
		    }
		System.out.println("Had " + count + " commits overall on current branch");
		
		iter = new Git(repository).log()
				.addPath("src/net/java/sip/communicator/impl/protocol")
	            .call()
	            .iterator();
		// Restore to original
//		Git git = new Git(repository);
//		git.reset().setMode(ResetType.HARD).call();
//		git.checkout().call();
		
		// Do the reverts
		count = 0;
		Git git = new Git(repository);
		while(count < 2 && iter.hasNext()) {
			RevCommit rev = iter.next();
			git.revert().include(rev).call();
			count++;
			System.out.println("Commit: " + rev + "Time: " + rev.getCommitTime() /*+ ", name: " + rev.getName() + ", id: " + rev.getId().getName()*/);
		}
		System.out.println("Done");
		repository.close();
		
//		RevWalk rw = new RevWalk(repository);
//		ObjectId head = repository.resolve(Constants.HEAD);
//		RevCommit commit = rw.parseCommit(head);
	}
	
	
}
