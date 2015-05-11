package svntag;

import javax.swing.JOptionPane;

import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class AuthManager {
	
	public static SVNRepository setupandauth(String url,String name,String passwd,String path)
	{
		SVNClientManager clientManager=SVNClientManager.newInstance();
		ISVNOptions myOptions;
		DAVRepositoryFactory.setup();
		SVNRepositoryFactoryImpl.setup();
		FSRepositoryFactory.setup();
		SVNRepository repository=null;
		try
		{
			repository=SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( name , passwd );
			repository.setAuthenticationManager( authManager );
			SVNNodeKind nodeKind = repository.checkPath(path,-1);
			ISVNReporterBaton reporterBaton=new ReporterBaton();
			
			if (nodeKind == SVNNodeKind.NONE) 
			{
				//System.err.println("There is no entry at '" + url + "'.");
				return null;
			}
			else if (nodeKind == SVNNodeKind.DIR)
			{
			    //System.err.println("The entry at '" + url+"/"+path + "' is a directory while a file was expected.");
			}
			else
			{
				//System.err.println("it's a file");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "svn连接失败！请检查用户名密码地址");
		}
		return repository;
	}
}
