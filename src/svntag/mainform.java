package svntag;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.io.SVNRepository;

public class mainform extends JFrame{

	public String resUrl;
	public String username;
	public String pwd;
	public static void main(String[] args) throws SVNException {
		// TODO Auto-generated method stub
		
		InputDialog i=new InputDialog();

	}
	public static void InitUI(String url,String username,String pwd) throws SVNException
	{
		final JFrame f=new MainWindow(url,username,pwd);

		f.setVisible(true);
		f.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		f.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				System.exit(0);
			}
		}
		);
	}
	
}

class InputDialog extends JFrame
{
	public InputDialog() 
    {
        JFrame f = new JFrame("svn设置");
        Container contentPane = f.getContentPane();
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,3));
        JLabel lable1=new JLabel("svn地址：");
        panel.add(lable1);
        
        JTextField url=new JTextField();
        panel.add(url);
        JLabel lable2=new JLabel("svn用户名：");
        panel.add(lable2);
        JTextField username=new JTextField();
        panel.add(username);
        JLabel lable3=new JLabel("svn密码：");
        panel.add(lable3);
        
        JTextField pwd=new JTextField();
        panel.add(pwd);
        JButton b = new JButton("确定");
        b.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				f.dispose();
				try {
					mainform.InitUI(url.getText(),username.getText(),pwd.getText());
				} catch (SVNException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null,"svn连接失败");
					//e.printStackTrace();
				}
			}
		});
        panel.add(b);
        b = new JButton("取消");
        //b.addActionListener(this);
        panel.add(b);
        
        JLabel label = new JLabel(" ",JLabel.CENTER);
        contentPane.add(label);
        contentPane.add(panel);
        setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
        
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }  
}
class MainWindow extends JFrame
{
	private JLabel label1;
    private JButton button1;
    private JTextArea text1;
    private JComboBox box1;
    private JComboBox box2;
    private JMenuBar menuBar;
    private JSlider slider;
    private JSpinner spinner;
    private JToolBar toolBar;
    private JButton maketag;
    private JOptionPane jOptionPane;
    private JButton modifytag;
    private JTree treeview;
    private JButton getchanglist;
    
    public MainWindow(String url,String username,String pwd) throws SVNException{
        super();   
        this.setSize(640,640);
        Container container=this.getContentPane();
        container.setLayout(null);
        //container.add(this.getSpinner(),null);
        //container.add(this.getToolBar(),null);
        box1=this.getTagBox(box1,url,username,pwd,20,20);
        box2=this.getTagBox(box2,url,username,pwd,200,20);
        container.add(box1);
        container.add(box2);
        //container.add(this.getTextField());
        container.add(this.getMaketagButton(url,username,pwd));
        container.add(this.modifytagButton(url, username, pwd));
        container.add(this.gettreeview(url, username, pwd));
        container.add(this.getchangelist(url, username, pwd));
        this.setTitle("自动化版本控制系统");//设置窗口标题
    }
    private JTree gettreeview(String url,String username,String pwd) throws SVNException{
    	if(treeview==null)
    	{
    		AuthManager authmanager=new AuthManager();
			SVNRepository svnRepository=authmanager.setupandauth(url,username,pwd,"");
    		ListDirectory listDirectory=new ListDirectory();
    		treeview=listDirectory.listallfiles(svnRepository, -1, "tags",new DefaultMutableTreeNode("tags"));
    		treeview.setBounds(100,100,500,500);
    	}
    	return treeview;
    }
    private JButton getchangelist(String url,String username,String pwd)
    {
    	if(getchanglist==null)
    	{
    		getchanglist=new JButton();
    		getchanglist.setBounds(400,20,80,20);
            getchanglist.setText("比较两个tag中不同的文件");
            getchanglist.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					SVNUtil util=new SVNUtil();
					try
					{
						long start=System.currentTimeMillis();
						util.setUpSVNClient(url, username, pwd);
						String ret=util.showdiff(url+"/tags/"+box1.getSelectedItem().toString(), url+"/tags/"+box2.getSelectedItem().toString());
						String regexp = "Index:.*";
						Pattern p = Pattern.compile(regexp);
						Matcher m = p.matcher(ret);
						ArrayList<String> changelist=new ArrayList<String>();
						String showmessege="";
						int count=0;
						String writepath="d:\\changelist_"+box1.getSelectedItem().toString()+"_"+box2.getSelectedItem().toString()+".txt";
						FileWriter fileWriter=new FileWriter(writepath);
						while(m.find()) {
							String addstring=m.group().substring(7);
							addstring+="\r\n";
							showmessege+=addstring;
							changelist.add(addstring);
							count++;
						}
						fileWriter.write(showmessege);
						fileWriter.flush();
						fileWriter.close();
						System.out.println(count+"files");
						System.out.println(System.currentTimeMillis()-start+"ms");
						JOptionPane.showMessageDialog(null, "输出已写入"+writepath);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			});
    	}
    	return getchanglist;
    }
    private JToolBar getToolBar(){
        if(toolBar==null){
            toolBar = new JToolBar();
            toolBar.setBounds(103,260,71,20);
            toolBar.setFloatable(true);
        }
        return toolBar;
    }
    private JButton modifytagButton(String url,String username,String pwd)
    {
    	if(modifytag==null)
    	{
    		modifytag=new JButton();
    		modifytag.setBounds(0,200,80,20);
    		modifytag.setText("更改tag");
    	}
    	modifytag.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				// TODO Auto-generated method stub
				SVNUtil util=new SVNUtil();
				try
				{
					util.setUpSVNClient(url, username, pwd);
					String newtag=JOptionPane.showInputDialog("请输入新的tag");
					if(newtag!=null && newtag!="")
					{
						util.moveModel(url+"/tags/"+box1.getSelectedItem().toString(), url+"/tags/"+newtag);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		});
    	return modifytag;
    }
    private JButton getMaketagButton(String url,String username,String pwd)
    {
    	if(maketag==null)
    	{
    		maketag=new JButton();
    		maketag.setBounds(0,100,80,20);
    		maketag.setText("生成tag");
    	}
    	maketag.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				SVNUtil util=new SVNUtil();
				try {
					util.setUpSVNClient(url, username, pwd);
					String tag =JOptionPane.showInputDialog("请输入标签（一个好记的东西）：");
					if(tag!=null && tag!="")
					{
						util.maketag(url+"/trunk",url+"/tags/"+tag, "");
						JOptionPane.showMessageDialog(null,"tag成功");
					}
				} catch (SVNException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
    	return maketag;
    }
    private JSpinner getSpinner(){
        if(spinner==null){
            spinner = new JSpinner();
            spinner.setBounds(103,220, 80,20);
            spinner.setValue(100);
        }
        return spinner;
    }
    private JTextArea getTextField()
    {
    	if(text1==null)
    	{
    		text1=new JTextArea();
    		text1.setBounds(0, 400, 300, 70);
    		text1.setAutoscrolls(true);
    	}
    	return text1;
    }
    private JComboBox getTagBox(JComboBox box,String url,String username,String pwd,int x,int y) throws SVNException{
        if(box==null){
            box = new JComboBox();
            box.setBounds(x,y,150,30);
            AuthManager authmanager=new AuthManager();
			SVNRepository svnRepository=authmanager.setupandauth(url,username,pwd,"");
			ListDirectory listDirectory=new ListDirectory();
			String [] s=listDirectory.listdirectory(svnRepository, -1, "tags");
			for(int i=0;i<s.length;i++)
			{
				box.addItem(s[i]);
			}
        }
        return box;
    }
}