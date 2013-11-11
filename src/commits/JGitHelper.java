package commits;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

// derived from jgit cookbook
public class JGitHelper {
    public static Repository openRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File("/my/git/directory"))
          .readEnvironment() // scan environment GIT_* variables
          .findGitDir() // scan up the file system tree
          .build();

        return repository;
	}
	
	public static Repository createNewRepository() throws IOException {
	        // prepare a new folder
	        File localPath = File.createTempFile("TestGitRepository", "");
	        localPath.delete();
	
	        // create the directory
			Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));
			repository.create();
			
			return repository;
	}
	
}
