package crixec.app.imagefactory.server;
import org.json.JSONObject;
import org.json.JSONException;

public class CheckUpdateServerBean
{
	private String apkUrl;
	private String changeLogUrl;
	private int versionCode;
	private String versionName;
	public static String BIND;
	private CheckUpdateServerBean(){
		
	}
	public static CheckUpdateServerBean parseJson(String json) throws JSONException{
		CheckUpdateServerBean instance = new CheckUpdateServerBean();
		JSONObject obj = new JSONObject(json);
		JSONObject data = obj.getJSONObject("data");
		instance.setApkUrl(data.getString("apk_url"));
		instance.setChangeLogUrl(data.getString("changelog_url"));
		instance.setVersionCode(data.getInt("version_code"));
		instance.setVersionName(data.getString("version_name"));
		return instance;
	}
	public void setApkUrl(String apkUrl)
	{
		this.apkUrl = apkUrl;
	}

	public String getApkUrl()
	{
		return apkUrl;
	}

	public void setChangeLogUrl(String changeLogUrl)
	{
		this.changeLogUrl = changeLogUrl;
	}

	public String getChangeLogUrl()
	{
		return changeLogUrl;
	}

	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}

	public int getVersionCode()
	{
		return versionCode;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public String getVersionName()
	{
		return versionName;
	}
	
}	

