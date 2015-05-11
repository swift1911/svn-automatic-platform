package svntag;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class unittest {
	public static void readxml() throws ParserConfigurationException, SAXException, IOException
	{
		File f=new File("config.xml"); 
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder=factory.newDocumentBuilder(); 
		Document doc = builder.parse(f);
		NodeList nl = doc.getElementsByTagName("svnconfig");
		System.out.println(doc.getElementsByTagName("url").item(0).getFirstChild().getNodeValue());
	}
	public static void main(String [] args)
	{
		try{
			Socket socket=new Socket("127.0.0.1",8001);
			OutputStream os=socket.getOutputStream();
			InputStream is=socket.getInputStream();
			os.write("java\n".getBytes());
			os.flush();

			int s;
			socket.setSoTimeout(3000);
			while((s=is.read())!=-1)
			{
				System.out.print(s);
			}
	        socket.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			//readxml();
	}
}
