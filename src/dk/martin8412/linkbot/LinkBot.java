package dk.martin8412.linkbot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.jibble.pircbot.Colors;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

public class LinkBot extends PircBot{
	private final String ircNick = "SetNick";
	private final String ircServer = "SetIRCServer";
	private final String ircChan = "SetIRCChan";

	public LinkBot()
	{
		/*
		 * We setup some basic information for the bot
		 * We have chosen to use unicode for the bot since Youtube supports this as well.
		 */
		this.setName(ircNick);
		try {
			this.setEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.connect(ircServer);
		} catch (NickAlreadyInUseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IrcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.joinChannel(ircChan);
	}
	
	/*
	 * We override the onMessage method to see the data in the IRC channel.
	 * @see org.jibble.pircbot.PircBot#onMessage(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void onMessage(String channel, String sender, String login, String hostname, String message)
	{
		if(message.contains("http://www.youtube.com/watch") || message.contains("https://www.youtube.com/watch"))
		{
			YoutubeHandler yh = new YoutubeHandler();
			int idxv = message.indexOf("v=")+2;
			String id = message.substring(idxv, idxv+11);
			System.out.println(id);
			String[] data = yh.getData(id);
			if(data[0] != "" && data[1] != "")
			{
				this.sendMessage(ircChan, Colors.RED + "Title of video: " + Colors.NORMAL + data[0] + Colors.RED + " duration: " + data[1]);
			}
		}
	}

}
