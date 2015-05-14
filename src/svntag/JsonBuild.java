package svntag;

import org.json.JSONObject;

public class JsonBuild {
	public static String build(String username,String pwd,String svnurl,String language,String tagname)
	{
		JSONObject jsonobj=new JSONObject();
		jsonobj.put("username", username);
		jsonobj.put("userpwd", pwd);
		jsonobj.put("svnurl", svnurl);
		jsonobj.put("language",language);
		jsonobj.put("tagname", tagname);
		return jsonobj.toString()+"\n";
		//System.out.println(jsonobj.get("username"));
	}
}
