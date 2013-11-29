package commits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;


/* 
 * Helper class for for Git operations.
 * Moved from the CommitHistoryController class for general use.
 */
public class JGitHelper {
	
    public static Repository openRepository(String path) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File(path))
          .readEnvironment()
          .build();
        return repository;
	}
    
	// Walks through the commit tree of the target git repository
	//  that involves all files in the given sub-path. Returns a collection
	//  of the relevant commits.
	public static ArrayList<Commit> retrieveCommits(Repository repository, String subPath)
			throws Exception
	{
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
	
	public static void checkoutRepository(Repository repository, RevCommit start)
			throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException
	{
		Git git = new Git(repository);
		git.checkout().setStartPoint(start).setAllPaths(true).call();
	}
	
	public static void checkoutRepository(Repository repository, RevCommit start, String subPath)
				throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException
	{
		Git git = new Git(repository);
		git.checkout().setStartPoint(start).addPath(subPath).call();
	}
}
