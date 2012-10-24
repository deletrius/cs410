package loclock.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;

 

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

public class MainServices extends TabSet{
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	private final LocationServiceAsync locationService = GWT.create(LocationService.class);
	private HLayout rootLayout;
	private LoginService loginService;
	
	
	public static Account account = null;
	private static volatile MainServices mainServicesInstance;
	private MainServices()
	{
		rootLayout=new HLayout(5);
		rootLayout.setSize("100%", "100%");
		
		// Check if the user is logged in
		loginService=new LoginService();	
		
	}
	
	public static MainServices getInstance()
	{
		if (mainServicesInstance == null)
		{
			mainServicesInstance=new MainServices();
		}
			return mainServicesInstance;
	}
	
	public void addService(Service service)
	{
		this.addTab(service);
		if (!rootLayout.contains(this))
			rootLayout.addMember(this);
		rootLayout.redraw();
	}
	
	private void addMapService()
	{
		String mapWidth=(rootLayout.getRight()-rootLayout.getLeft())/2+"px";
		String mapHeight=(rootLayout.getBottom()-rootLayout.getTop())+"px";
		MapService mapService=new MapService(mapWidth,mapHeight);
		rootLayout.addMember(mapService.toWidget());
		rootLayout.redraw();
	}
	
	public Account  getAccount()
	{
		return account;
	}
	
	
	private class LoginService
	{
		private AccountServiceAsync accountService = GWT.create(AccountService.class);
		private VLayout loginLayout;
		private Label registerLabel = new Label("Register");
		private Anchor signInLink = new Anchor("Sign In");
		private Anchor signOutLink = new Anchor("Sign Out");
		private Anchor registerLink = new Anchor("Register");
		private Anchor googleLink = new Anchor("Google");
		private Label loginLabel = new Label("Please sign in to your Google Account to access the LocLockr application.");

		public LoginService()
		{
			accountService.login(GWT.getHostPageBaseURL(), new AsyncCallback<Account>(){
				public void onFailure(Throwable caught) {
					Window.alert("Error Message: "+caught);
				}
				public void onSuccess(Account result) {
					account = result;
					if(account.isLoggedIn()){
						System.out.println("is logged in");
						loadLoggedInScreen();

					}
					else{
						System.out.println("setLoginScreen");
						setLoginScreen();
					}
				}
			});
		}
		protected void setLoginScreen() {
			loginLayout=new VLayout(15);
			loginLayout.setSize("300px", "100px");
			signInLink.setHref(account.getLoginUrl());
			registerLink.setHref("https://www.google.com/accounts/NewAccount");
			
			loginLayout.setDefaultLayoutAlign(Alignment.CENTER);
			
			
			loginLayout.addMember(loginLabel);
			loginLayout.addMember(signInLink);
			loginLayout.addMember(registerLabel);
			loginLayout.addMember(registerLink);
			
			rootLayout.setAlign(Alignment.CENTER);
			rootLayout.addMember(loginLayout);
			rootLayout.draw();
		}
		protected void loadLoggedInScreen() {
			
			System.out.println("OK");
			//rootLayout.destroy();
			rootLayout=new HLayout(5);
			rootLayout.setSize("100%", "100%");
			//TabPanel tabPanel=new TabPanel();
			TabSet tabSet = new TabSet();
			tabSet.setAutoHeight();
			tabSet.setAutoWidth();
			//tabSet.setSize("50%", "100%");
			System.out.println("Good");
			
			
			addUser(account.getEmailAddress());
			addMapService();
			addService(new FileUploadService());
			addService(new FriendService("Raymond")); //@@ TODO stub for usrname
			addService(new TimeTableService());
			addService(new NotificationTabService());
			addService(new SettingTabService());
			rootLayout.addMember(MainServices.this);
			rootLayout.draw();

			
		}
		
		private void addUser(final String username)
		{
			locationService.addUser(username, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(Void result) {
					// TODO Auto-generated method stub
					loadUsers();
				}
			});
		}
		private void loadUsers()
		{
			locationService.getUsers(new AsyncCallback<String[]>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(String[] result) { 
					// For testing purpose only
					//displayUsers(result);
				}
			});
		}
		private void displayUsers(String[] users)
		{
			String stringOfUsers = "";

			for(String username : users)
			{
				stringOfUsers = stringOfUsers + username + " ";
			}

			Window.alert(stringOfUsers);
		}
		
	}
}

