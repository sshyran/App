package crixec.app.imagefactory.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import crixec.app.imagefactory.core.ExceptionHandler;
import java.io.PrintWriter;

public class HttpUtils
{
	public static String doGet(final String url)
	{
		final StringBuilder str = new StringBuilder();
		Thread get = new Thread(new Runnable(){
				BufferedReader br;

				@Override
				public void run()
				{
					// TODO: Implement this method

					try
					{
						URLConnection coon = new URL(url).openConnection();
						coon.connect();
						br = new BufferedReader(new InputStreamReader(coon.getInputStream()));
						String line = br.readLine();
						if(line != null){
							str.append(line);
						}
						while ((line = br.readLine()) != null)
						{
							str.append("\n" + line);
						}
					}
					catch (Exception e)
					{
						ExceptionHandler.handle(e);
					}
					finally
					{
						try
						{
							br.close();
						}
						catch (IOException e)
						{
							ExceptionHandler.handle(e);
						}
					}
				}
			});
		try
		{
			get.start();
			get.join();
		}
		catch (Exception e)
		{
			ExceptionHandler.handle(e);
		}
		return str.toString();
	}
	public static String doPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
									"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
			ExceptionHandler.handle(e);
        }
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(Exception ex){
				ExceptionHandler.handle(ex);
            }
        }
        return result;
    }    
}
