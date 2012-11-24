package loclock.client;

import java.util.ArrayList;
import java.util.Date;
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
	private FriendService parent;
	Timer t=new Timer()
	{

		@Override
		public void run() {
			pullMessageFromServer();
			
		}};
	
	/**
	 * Class constructor of the chat panel manager
	 * 
	 * @param parent the parent panel that contains the chat panel manager
	 * @param fromUser message sender
	 */
	public ChatPanelManager(FriendService parent,String fromUser)
	{
		super();
		this.parent=parent;
		this.user=fromUser;
		this.setSize("100%", "50%");
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

	/**
	 * Keep pulling message from the server to get the message
	 * received.
	 */
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
						findChat(i[0]).updateConvoTextBox(i[2],new Date(Long.parseLong(i[3])).toString());
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



/**
 * Open a new chat panel.
 * 
 * @param from the message sender
 * @param to the  message receiver
 */
public void openChat(String from, String to)
{
	
	// TODO check for subscription
	MainServices.getInstance().selectTab(0);
	ChatPanel i=findChat(to);
	if (i!=null)
	{		
		this.selectTab(i);
	}
	else
	{
		parent.buildFriendList();
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

/**
 * Close the chat panel.
 * 
 * @param to the message receiver
 */
public void closeChat(String to)
{
	ChatPanel i=findChat(to);
	if (i!=null)
		this.removeTab(i);
}

/**
 * Find the chat panel
 * 
 * @param to the message receiver
 * @return the chat panel, null if not found
 */
public ChatPanel findChat(String to)
{
	for(int i=0;i<chats.size();i++)
	{
		if (chats.get(i).getToUserName().equals(to))
			return chats.get(i);
	}
	return null;
}
}
