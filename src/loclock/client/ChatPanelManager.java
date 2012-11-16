package loclock.client;

import java.util.ArrayList;
import java.util.List;

import loclock.server.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TabPanel;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.CloseClickHandler;
import com.smartgwt.client.widgets.tab.events.TabCloseClickEvent;
import com.sun.java.swing.plaf.windows.WindowsBorders;

public class ChatPanelManager extends TabSet{
	private List<ChatPanel> chats;
	private MessageServiceAsync messageService = GWT.create(MessageService.class);
	private String user;
	Timer t=new Timer()
	{

		@Override
		public void run() {
			pullMessageFromServer();
			
		}};
	
	public ChatPanelManager(String fromUser)
	{
		super();
		this.user=fromUser;
		this.setSize("100%", "30%");
		chats=new ArrayList<ChatPanel>();
		this.addCloseClickHandler(new CloseClickHandler()
		{

			@Override
			public void onCloseClick(TabCloseClickEvent event) {
				chats.remove(getSelectedTabNumber());			
			}
		});

		
			
		pullMessageFromServer();
		

		
	}

	private void pullMessageFromServer()
	{
		try{
			messageService.retrieveMessage(user, 
					new AsyncCallback<List<String[]>>(){

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("Failed to retrieve message");
				}

				@Override
				public void onSuccess(List<String[]> result) {
					for (String[] i: result)
					{
						openChat(i[1],i[0]);
						findChat(i[0]).updateConvoTextBox(i[2],i[3]);
					}
					
//					Timer t=new Timer();
//					
//					pullMessageFromServer();
					t.schedule(2000);
			}
				});

		} catch (NotLoggedInException e) {
			e.printStackTrace();
		} 
	}



public void openChat(String from, String to)
{
	// TODO check for subscription
	MainServices.getInstance().selectTab(1);
	ChatPanel i=findChat(to);
	if (i!=null)
	{			
		this.selectTab(i);
	}
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
	ChatPanel i=findChat(to);
	if (i!=null)
		this.removeTab(i);
}

private ChatPanel findChat(String to)
{
	for(int i=0;i<chats.size();i++)
	{
		if (chats.get(i).getToUserName().equals(to))
			return chats.get(i);
	}
	return null;
}
}
