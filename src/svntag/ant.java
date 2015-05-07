package svntag;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
public class ant {
         public static void main(String[] args) {
                   File buildFile=new File(".//build.xml");
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
                            helper.parse(p, buildFile);
                            //执行项目的某一个目标
                            p.executeTarget(p.getDefaultTarget());
                            p.fireBuildFinished(null);
                   }catch(BuildException be){
                            p.fireBuildFinished(be);
                   }
         }

}