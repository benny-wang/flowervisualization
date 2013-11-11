package commits;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

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
        repository.close();
	}
}
