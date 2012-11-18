package loclock.client;

import com.google.api.gwt.client.GoogleApiRequestTransport;
import com.google.api.gwt.client.OAuth2Login;
import com.google.api.gwt.services.plus.shared.Plus;
import com.google.api.gwt.services.plus.shared.Plus.ActivitiesContext.ListRequest.Collection;
import com.google.api.gwt.services.plus.shared.Plus.PlusAuthScope;
import com.google.api.gwt.services.plus.shared.model.Activity;
import com.google.api.gwt.services.plus.shared.model.ActivityFeed;
import com.google.api.gwt.services.plus.shared.model.Person;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.requestfactory.shared.Receiver;

 

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.TabSet;

public class MainServices extends TabSet{
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);
	private final UserLocationServiceAsync locationService = GWT.create(UserLocationService.class);
	private HLayout rootLayout;
	private LoginService loginService;
	
	private static final Plus plus = GWT.create(Plus.class);
	private static final String CLIENT_ID = "280564165047.apps.googleusercontent.com";
	private static final String API_KEY = "AIzaSyAgtpPYGuQ60KpiPRbwcFcR7tSylxuD1XI";
	private static final String APPLICATION_NAME = "PlusSample/1.0";
	
	private MapService mapService;
	
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
		HLayout mapPanel=new HLayout();
		
		
		String mapWidth=(rootLayout.getRight()-rootLayout.getLeft())/2+"px";
		String mapHeight=(rootLayout.getBottom()-rootLayout.getTop())+"px";
		mapService=new MapService(mapWidth,mapHeight);
		
		//mapPanel.setSize("50%", "100%");
		//mapPanel.setAlign(Alignment.RIGHT);
		//mapPanel.setAlign(VerticalAlignment.BOTTOM);
		//mapPanel.setMemberOverlap(200);
		
		mapPanel.addMember(mapService.toWidget());	
		
		mapService.bindTo(mapPanel);
		//mapPanel.addMember(mapService.getMapOverlayPanel(),10);
		
		rootLayout.addMember(mapPanel);		
		rootLayout.redraw();
	}
	public MapService getMapService()
	{
		return mapService;
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
					System.out.println("bbbb "+MainServices.account);
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
			
			plus.initialize(new SimpleEventBus(), new GoogleApiRequestTransport(APPLICATION_NAME, API_KEY));
			 final IButton b = new IButton("Authenticate to get public activities");

			 b.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
				
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
					// TODO Auto-generated method stub
					login();
					Window.alert("After login!");
				}
			});
			
			 loginLayout.addMember(b);
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
			addService(new FriendService(account.getEmailAddress())); //@@ TODO stub for usrname
			addService(new TimeTableService());
			addService(new NotificationTabService());
			addService(new SettingTabService());
			rootLayout.addMember(MainServices.this);
			
			rootLayout.draw();
		}
		
		private void login() 
		{
		    OAuth2Login.get().authorize(CLIENT_ID, PlusAuthScope.PLUS_ME, new Callback<Void, Exception>() {
		      @Override
		      public void onSuccess(Void v) {
		        getMe();
		      }

		      @Override
		      public void onFailure(Exception e) {
		        println(e.getMessage());
		      }
		    });
		  }
		
		private void getMe() {
		    plus.people().get("me").to(new Receiver<Person>() {
		      @Override
		      public void onSuccess(Person person) {
		        println("Hello " + person.getDisplayName());
		        Window.alert("Hello, this is your name: " + person.getDisplayName());
		        getMyActivities();
		      }
		    }).fire();
		  }
		
		private void getMyActivities() {			
		    plus.activities().list("me", Collection.PUBLIC).to(new Receiver<ActivityFeed>() {
		      @Override
		      public void onSuccess(ActivityFeed feed) {
		        println("===== PUBLIC ACTIVITIES =====");
		        if (feed.getItems() == null || feed.getItems().isEmpty()) {
		          println("You have no public activities");
		        } else {
		          for (Activity a : feed.getItems()) {
		            println(a.getTitle());
		          }
		        }
		      }
		    }).fire();
		  }
		
		private void println(String msg) {
			Window.alert(msg);
			//loginLayout.addMember(new Label(msg));
			//rootLayout.draw();
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

