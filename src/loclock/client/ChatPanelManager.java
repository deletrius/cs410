package loclock.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.TabPanel;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class ChatPanelManager extends TabSet{
	private List<ChatPanel> chats;
	
	public ChatPanelManager()
	{
		super();
		this.setSize("50%", "100%");
		chats=new ArrayList<ChatPanel>();
	}

	public void openChat(String from, String to)
	{
		int i=findChat(to);
		if (i!=-1)
			this.selectTab(i);
		else
		{
			ChatPanel chat=new ChatPanel(from, to);
			chats.add(chat);
//			Tab chat1=new Tab();
//			chat1
			this.addTab(chat);
//			this.addTab(chat1);
			//this.add(chat, to);
			this.selectTab(chats.size()-1);
		}
	}
	
	public void closeChat(String to)
	{
		int i=findChat(to);
		if (i!=-1)
			this.removeTab(i);

	}
	
	private int findChat(String to)
	{
		for(int i=0;i<chats.size();i++)
		{
			if (chats.get(i).getToUserName().equals(to))
				return i;
		}
		return -1;
	}
}
