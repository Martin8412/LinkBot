package dk.martin8412.linkbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class YoutubeHandler {
	/*
	 * We want to receive various data from Youtube.
	 * We have seen that Youtube stores these data in meta tags.
	 */
	public String[] getData(String videoId)
	{
		String[] res = {"", ""};
		HttpURLConnection connection = null;
		try {
			URL url = new URL("http://www.youtube.com/watch?v=" + videoId);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String inputline;
			while((inputline = br.readLine()) != null)
			{
				/*
				 * We check if the line contains some of the data we need.
				 */
				if(inputline.contains("<meta property=\"og:title\" content="))
				{
					String title = inputline.split("<meta property=\"og:title\" content=\"")[1];
					title = title.split("\">")[0];
					
					res[0] = title;
					System.out.println("Found title: " + title);
				}
				
				if(inputline.contains("<meta itemprop=\"duration\" content=\""))
				{
					String duration = inputline.split("<meta itemprop=\"duration\" content=\"")[1];
					duration = duration.split("\">")[0];
					
					res[1] = decodeDuration(duration);
					
					System.out.println("Found duration: " + decodeDuration(duration));
					
				}
			}
			
			return res;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		} finally {
			if(connection != null) {
				connection.disconnect();
			}
		}
			}
	
	/*
	 * Youtube stores the duration in some weird format.
	 * Therefore we transform into a more friendly format.
	 */
	public String decodeDuration(String duration)
	{
		String minutes = "0";
		String seconds = "0";
		String res = duration.replace("PT", "");
		minutes = res.split("M")[0];
		seconds = res.split("M")[1];
		seconds = seconds.split("S")[0];
		
		return minutes + " minutes and " + seconds + " seconds";
	}
}
