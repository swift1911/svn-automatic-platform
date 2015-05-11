package svntag;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.io.SVNRepository;

public class ListDirectory {
	public static String[] listdirectory(SVNRepository svnRepository,long revision,String path) throws SVNException
	{
		String ret="";
		Collection entry=svnRepository.getDir(path ,revision, null, (Collection)null);
		Iterator i=entry.iterator();		
		while(i.hasNext())
		{
			SVNDirEntry en=(SVNDirEntry)i.next();
			//System.out.println(en.getName()+" "+en.getDate()+"  revision:"+en.getRevision());
			ret=ret+en.getName()+",";
		}
		return ret.split(",");
	}
	static int count=0;
	static JTree treeview;
	public static JTree listallfiles(SVNRepository repository, long revision,String path,DefaultMutableTreeNode Node) throws SVNException {  
		if(count==0)
		{
			//DefaultMutableTreeNode top = new DefaultMutableTreeNode(path);
			treeview=new JTree(Node);
		}
		Collection entries = repository.getDir(path, revision , null , (Collection) null);  
	    Iterator iterator = entries.iterator();  
	    count++;
	    while (iterator.hasNext()) {  
	        SVNDirEntry entry = ( SVNDirEntry ) iterator.next(); 
	        if(entry.getKind()==SVNNodeKind.FILE)
	        {
	        System.out.println("/" + (path.equals("") ? "" : path + "/") + entry.getName() +   
	                           " ( author: '" + entry.getAuthor() + "'; revision: " + entry.getRevision() +   
	                           "; date: " + entry.getDate() + ")" +"size:"+entry.getSize()); 
	        Node.add(new DefaultMutableTreeNode(entry.getName()+" 最后编辑："+entry.getAuthor()));
	        
	        }
	        if (entry.getKind() == SVNNodeKind.DIR) 
	        {
	        	DefaultMutableTreeNode node=new DefaultMutableTreeNode(entry.getName()+" 最后编辑："+entry.getAuthor());
	        	Node.add(node);
	            listallfiles(repository, revision,(path.equals("")) ? entry.getName() : path + "/" + entry.getName(),node);
	        
	        }  
	    } 
	    return treeview;
	} 
}
