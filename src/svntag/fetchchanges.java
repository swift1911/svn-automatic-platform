package svntag;

import java.io.FileInputStream;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNPropertyValue;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNEditModeReader;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNWorkspaceMediator;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.diff.SVNDeltaGenerator;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class fetchchanges {
	public class NullWorkspace implements ISVNWorkspaceMediator{
	    public SVNPropertyValue getWorkspaceProperty(String path,
							 String name) throws SVNException{
	      return SVNPropertyValue.create("");
	    }

		public void setWorkspaceProperty(String path,
                String name,
                SVNPropertyValue value) throws SVNException{}
	}
	
	public void sample(){
	    try{
	      AuthManager authManager=new AuthManager();
	      SVNRepository repository=authManager.setupandauth("file:///F:/net/", "", "", "");
	      ISVNEditor editor = repository.getCommitEditor("test", new NullWorkspace()); 
	      editor.targetRevision(-1);
	      editor.openRoot(-1);
	      //editor.openDir("tags", -1);
	      editor.openDir("tags/version1.5", -1);
	      editor.addFile("tags/version1.5/1.c", null, -1);
	      editor.applyTextDelta("tags/version1.5/1.c", null);
	      editor.textDeltaEnd("tags/version1.5/1.c");
	      SVNDeltaGenerator deltaGenerator = new SVNDeltaGenerator();
	      String checksum = deltaGenerator.sendDelta("tags/version1.5/1.c",new FileInputStream("./1.c"),editor,true);
	      editor.closeFile("tags/version1.5/1.c", checksum);
	      editor.openFile("tags/version1.5/1.c", -1);
	      editor.closeDir();
	      editor.closeEdit();
	      
	      
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  }
}
