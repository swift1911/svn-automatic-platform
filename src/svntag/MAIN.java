package svntag;

import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.admin.SVNReporter;
import org.tmatesoft.svn.core.io.ISVNEditor;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class MAIN {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			AuthManager authmanager=new AuthManager();
			SVNRepository svnRepository=authmanager.setupandauth("file:///F:/net/", "", "", "");
			ListDirectory listAllFiles=new ListDirectory();	
			System.out.println(svnRepository.getLatestRevision());
			//listAllFiles.listdirectory(svnRepository, -1,"tags");			
			//listAllFiles.listallfiles(svnRepository,-1, "",null);
			//ReporterBaton reporterBaton=new ReporterBaton();
			Scanner s=new Scanner(System.in);
			String path="file:///f:/net/";
			SVNUtil util=new SVNUtil();
			util.setUpSVNClient(path, "","");
			//String tag=s.nextLine();
			//util.maketag(path+"/trunk", path+"tags/"+tag,"");
			//util.showdifff(path+"tags/version2.1", path+"tags/version1.9");
			
			String ret=util.showdiff(path+"tags/version2.1", path+"tags/version1.9");
			//System.out.println(ret);
			String regexp = "Index:.*";
			Pattern p = Pattern.compile(regexp);
			Matcher m = p.matcher(ret);
			ArrayList<String> changelist=new ArrayList<String>();
			while(m.find()) {
				String addstring=m.group().substring(7);
				changelist.add(addstring);
			    //System.out.println(m.group());
			}
			for(int i=0;i<changelist.size();i++)
			{
				System.out.println(changelist.get(i));
			}
			//util.Merge(path+"tags/version1.6", path+"tags/version1.7");
			//util.deleteModel("tags/version1.7");
			//util.downloadModel("tags/version1.7", "d:\\");
			//fetchchanges fet=new fetchchanges();
			//fet.sample();
			//util.Javabuild("tags/version1.7/svnkit");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
		
		
	
	 

}
