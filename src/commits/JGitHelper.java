package commits;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;


// derived from jgit cookbook
public class JGitHelper {
    public static Repository openRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File("../jitsi/.git"))
          .readEnvironment() // scan environment GIT_* variables
//          .findGitDir() // scan up the file system tree
          .build();

        return repository;
	}
		
}
