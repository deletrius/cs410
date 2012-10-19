package loclock.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
@RemoteServiceRelativePath("upload")
public class Loclock implements EntryPoint {



	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private MainServices mainServices;
	
	
	public void onModuleLoad() {
		mainServices=MainServices.getInstance();		
		mainServices.addService(new FileUploadService());
		mainServices.addService(new FriendService("Raymond")); //@@ TODO stub for usrname
		mainServices.addService(new TimeTableService());
		mainServices.addService(new NotificationTabService());
		mainServices.addService(new SettingTabService());

/*
	VerticalPanel statsPanel = new VerticalPanel();
	tabSet.add(statsPanel, "Stats", false);
	statsPanel.setSize("5cm", "3cm");

	VerticalPanel settingPanel = new VerticalPanel();
	tabSet.add(settingPanel, "Setting", false);
	settingPanel.setSize("5cm", "3cm");
*/

	}
	

}
