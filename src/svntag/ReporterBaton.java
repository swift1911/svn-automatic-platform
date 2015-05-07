package svntag;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.ISVNReporter;
import org.tmatesoft.svn.core.io.ISVNReporterBaton;

public class ReporterBaton implements ISVNReporterBaton {
	public void report( ISVNReporter reporter ) throws SVNException {
		try
		{
			reporter.setPath("tags", null, -1, false);
			reporter.finishReport();
		}
		catch(SVNException e)
		{
			reporter.abortReport();
		}
		
	}
	
}