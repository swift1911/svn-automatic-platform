package svntag;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.python.icu.impl.coll.Collation;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNEditModeReader;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCommitPacket;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc2.SvnMerge;


public class SVNUtil {
	public SVNClientManager svnClientManager;
	public ISVNOptions options;
	public String resURL;
	public void setUpSVNClient(String path,String userName,String passwd) throws SVNException
	{ 
		SVNRepositoryFactoryImpl.setup(); 
		File defaultConfigDir = new File(path);
		ISVNOptions options = SVNWCUtil.createDefaultOptions(defaultConfigDir,true); 
		svnClientManager = SVNClientManager.newInstance( (DefaultSVNOptions) options, userName, passwd); 
		resURL=path;
	}
	public void maketag(String path1,String path2,String tag)
	{
    	SVNCopyClient copyClient=svnClientManager.getCopyClient();
    	copyClient.setIgnoreExternals(false); 
    	try 
    	{ 
    		
    		SVNURL SourceUrl=SVNURL.parseURIEncoded(path1); 
    		SVNURL destUrl=SVNURL.parseURIEncoded(path2); 
    		SVNCopySource[] copySources = new SVNCopySource[1]; 
    		copySources[0] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, SourceUrl); 
    		if(isURLExist(destUrl, "","")==false)
    		{
    			copyClient.doCopy(copySources, destUrl, false, true, true, "make tags :"+path2, null); 
    			System.out.println("tag has been copied");
    		}
    		else
    		{
    			System.out.println("tag already existed");
    		}
    	} 
    	catch (SVNException e) 
    	{ 
    		// TODO Auto-generated catch block e.printStackTrace(); }}
    		e.printStackTrace();	
    	}
	}
	public void Javabuild(String path)
	{
		File buildFile=new File(resURL+"/"+path+"/build.xml");
        //创建一个ANT项目
        Project p=new Project();
        //创建一个默认的监听器,监听项目构建过程中的日志操作
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
        p.addBuildListener(consoleLogger);
        try{
                 p.fireBuildStarted();
                 //初始化该项目
                 p.init();
                 ProjectHelper helper=ProjectHelper.getProjectHelper();
                 //解析项目的构建文件
                 helper.parse(p,buildFile);
                 //执行项目的某一个目标
                 p.executeTarget(p.getDefaultTarget());
                 p.fireBuildFinished(null);
        }catch(BuildException be){
                 p.fireBuildFinished(be);
        }
	}
	public void uploadMoel(String dirPath,String modelName)
	{ 
		File impDir = new File(dirPath); 
		SVNCommitClient commitClient = svnClientManager.getCommitClient();
		commitClient.setIgnoreExternals(false); 
		try 
		{ 
			SVNURL repositoryOptUrl=SVNURL.parseURIEncoded(resURL+modelName); 
			commitClient.doImport(impDir, repositoryOptUrl, "import operation!", null, true, true, SVNDepth.INFINITY); 
		} 
		catch (SVNException e) 
		{
			e.printStackTrace();
		}
	}
	public void deleteModel(String deleteModelName){
		 SVNCommitClient commitClient=svnClientManager.getCommitClient(); 
		 commitClient.setIgnoreExternals(false); 
		 try 
		 { 
			 SVNURL repositoryOptUrl=SVNURL.parseURIEncoded(resURL+deleteModelName); 
			 SVNURL deleteUrls[]=new SVNURL[1]; 
			 deleteUrls[0]=repositoryOptUrl; 
			 commitClient.doDelete(deleteUrls, "delete model named: "+deleteModelName); 
			 System.out.println(resURL+"/"+deleteModelName+" has deleted");
		 } 
		 catch (SVNException e) 
		 {
			 e.printStackTrace();
			 // TODO Auto-generated catch block e.printStackTrace(); } }
		 }
	}
	public void downloadModel(String downloadModelName,String dirPath)
	{ 
		File outDir=new File(dirPath+"/"+downloadModelName); 
		outDir.mkdirs();//创建目录 
		SVNUpdateClient updateClient=svnClientManager.getUpdateClient(); 
		updateClient.setIgnoreExternals(false);
		try 
		{ 
			SVNURL repositoryOptUrl=SVNURL.parseURIEncoded(resURL+downloadModelName); 
			updateClient.doExport(repositoryOptUrl, outDir, SVNRevision.HEAD, SVNRevision.HEAD, "downloadModel",true,true); 
		} 
		catch (SVNException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isURLExist(SVNURL url,String username,String password){
		try {
			SVNRepository svnRepository = SVNRepositoryFactory.create(url);
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
			svnRepository.setAuthenticationManager(authManager);
			SVNNodeKind nodeKind = svnRepository.checkPath("", -1);
			return nodeKind == SVNNodeKind.NONE ? false : true; 
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String ret="";
	
	public String showdiff(String path1,String path2) throws SVNException
	{
		SVNURL url1=SVNURL.parseURIEncoded(path1);
		//File url1=new File(path1);
		//SVNURL url2=SVNURL.parseURIEncoded(path2);
		SVNURL url2=SVNURL.parseURIEncoded(path2);
		SVNDiffClient svnDiffClient=svnClientManager.getDiffClient();
		Collection c=new ArrayList<String>();
		StringBuilder s=new StringBuilder();
		OutputStream out=new OutputStream() {
			
			@Override
			public void write(int paramInt) throws IOException {
				// TODO Auto-generated method stub
				s.append((char)paramInt);
				//System.out.print((char)paramInt);
			}
		};
		svnDiffClient.doDiff(url1, SVNRevision.HEAD, url2, SVNRevision.HEAD,SVNDepth.INFINITY,true,out);
		return s.toString();
		//svnDiffClient.doDiff("", -1, "",-1, true, true,"");
	}
	
	public void Merge(String path1,String path2) throws SVNException
	{
		SVNURL url1=SVNURL.parseURIEncoded(path1);
		//SVNURL url2=SVNURL.parseURIEncoded(path2);
		SVNURL url2=SVNURL.parseURIEncoded(path2);
		File dstpath=new File("f:\\netwc\\tags\\version1.6");
		dstpath.mkdirs();
		SVNDiffClient svnDiffClient=svnClientManager.getDiffClient();
		svnDiffClient.doMerge(url1,SVNRevision.HEAD, url2, SVNRevision.HEAD,dstpath,SVNDepth.INFINITY,true,true,true,true);
		
		
	}
	 public void moveModel(String path1,String path2)
	 { 
		 SVNCopyClient copyClient=svnClientManager.getCopyClient();
	    	copyClient.setIgnoreExternals(false); 
	    	try 
	    	{ 
	    		SVNURL SourceUrl=SVNURL.parseURIEncoded(path1); 
	    		SVNURL destUrl=SVNURL.parseURIEncoded(path2); 
	    		SVNCopySource[] copySources = new SVNCopySource[1]; 
	    		copySources[0] = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, SourceUrl); 
	    		if(isURLExist(destUrl, "","")==false)
	    		{
	    			copyClient.doCopy(copySources, destUrl, true, true, true, "move from"+path1+"to"+path2, null); 
	    			JOptionPane.showMessageDialog(null,"tag成功修改" );
	    			System.out.println("tag has been moved");
	    		}
	    		else
	    		{
	    			JOptionPane.showMessageDialog(null,"tag已经存在" );
	    			System.out.println("tag already existed");
	    		}
	    	} 
	    	catch (SVNException e) 
	    	{ 
	    		// TODO Auto-generated catch block e.printStackTrace(); }}
	    		JOptionPane.showMessageDialog(null, "更改tag失败，可能是没有权限");
	    		e.printStackTrace();	
	    	}
	 }
	
}
