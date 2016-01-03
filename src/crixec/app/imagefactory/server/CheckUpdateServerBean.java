package crixec.app.imagefactory.server;

public class CheckUpdateServerBean
{
	public String BIND;
	public Data data = new Data();
	public Data getData(){
		return data;
	}
	public static class Data{
		public int version_code;
		public String version_name;
		public String apk_url;
		public String changelog_url;


		public void setVersionCode(int version_code)
		{
			this.version_code = version_code;
		}

		public int getVersioCode()
		{
			return version_code;
		}

		public void setVersionName(String version_name)
		{
			this.version_name = version_name;
		}

		public String getVersionName()
		{
			return version_name;
		}

		public void setApkUrl(String apk_url)
		{
			this.apk_url = apk_url;
		}

		public String getApkUrl()
		{
			return apk_url;
		}

		public void setChangelogUrl(String changelog_url)
		{
			this.changelog_url = changelog_url;
		}

		public String getChangelogUrl()
		{
			return changelog_url;
		}
	}
}	

