package loclock.client;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import com.google.gwt.user.client.ui.Label;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.gdata.client.impl.Map;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;


import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.ibm.icu.util.Calendar;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.IButton;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
//import com.smartgwt.client.widgets.drawing.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.TileRecord;
import com.smartgwt.client.widgets.tile.events.RecordClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordClickHandler;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.tile.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.tile.events.SelectionChangedEvent;
import com.smartgwt.client.widgets.tile.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class FriendService extends Service{
	public static ChatPanelManager chatManager;
	private VLayout friendsPanel;
	private DynamicForm requestForm;
	private String user;
	private String friendUserName;
	private UserLocationServiceAsync locationService = GWT.create(UserLocationService.class);
	private SubscriptionServiceAsync subscriptionService=GWT.create(SubscriptionService.class);
	private final CalendarServiceAsync calendarService = GWT.create(CalendarService.class);
	private TimeTableService timeTableService = GWT.create(TimeTableService.class);
	private static boolean free = true;
	private static String freeOrNot = "Yes";
	private TileGrid tileGrid;
	TextAreaItem searchBox = new TextAreaItem();
	ButtonItem searchButton = new ButtonItem("Search");
	IButton requestButton = new IButton("Add Friend");

	DynamicForm profileForm=new DynamicForm();
	//	private static final List<String> DAYS = Arrays.asList("Sunday", "Monday",
	//			"Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
	public FriendService(String user)
	{
		this.setTitle("Friends");
		this.setIcon("http://www.fabgb.com/images/friends_icon.jpg");
		this.user=user;
		friendsPanel=new VLayout();
		friendsPanel.setSize("100%", "100%");
		chatManager=new ChatPanelManager(this,user);

		buildFriendList();
		profileForm.setBorder("2px solid grey");		
		profileForm.setSize("100%", "20%");
		updateProfilePanel(user,"0",new Date().toString());
		friendsPanel.addMember(profileForm);
		friendsPanel.addMember(chatManager);
		buildRequest();
		this.setPane(friendsPanel);

	}
	public void checkInvitations()
	{
		subscriptionService.getInvitations(user,new AsyncCallback<List<String>> (){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(user+caught.getMessage());
			}

			@Override
			public void onSuccess(List<String> result) {
				for (final String i : result)
				{			
					SC.confirm(i+" wants to add you as a friend\nAccept?", new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								subscriptionService.acceptInvitation(user, i, new AsyncCallback<Void>(){

									@Override
									public void onFailure(Throwable caught) {
										Window.alert(caught.getMessage());

									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										Window.alert(i+" is added to your friend list!");
										chatManager.openChat(user,i);
										chatManager.findChat(i).sendMessage(i+" has accepted your friend request!");
										//chatManager.closeChat(i);

										checkInvitations();
										buildFriendList();
									}});
							} else {
								subscriptionService.rejectInvitation(user,i , new AsyncCallback<Void>(){

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										Window.alert(caught.getMessage());
									}

									@Override
									public void onSuccess(Void result) {
										// TODO Auto-generated method stub
										Window.alert("You have rejected friend request from "+i);
										checkInvitations();
									}});

							}
						}
					});
				}
			}});

	}

	public void buildFriendList(){
		if (tileGrid ==null)
		{
			tileGrid=new TileGrid();  
			tileGrid.setWidth("100%");  
			tileGrid.setHeight("70%");
			tileGrid.setTileHeight(150);
			tileGrid.setTileWidth(100);
			tileGrid.setCanReorderTiles(false);  
			tileGrid.setShowAllRecords(true); 
			tileGrid.setCanDrag(false);
			tileGrid.setSelectionType(SelectionStyle.SINGLE);
			tileGrid.setAnimateTileChange(true);

		}
		else
		{
			friendsPanel.removeMember(tileGrid);
			tileGrid=new TileGrid();  
			tileGrid.setWidth("100%");  
			tileGrid.setHeight("70%");
			tileGrid.setTileHeight(150);
			tileGrid.setTileWidth(100);
			tileGrid.setCanReorderTiles(false);  
			tileGrid.setShowAllRecords(true); 
			tileGrid.setCanDrag(false);
			tileGrid.setSelectionType(SelectionStyle.SINGLE);
			tileGrid.setAnimateTileChange(true);
		}
		//  Record rec = new StudentRecord("name",picture,"Profile");

		Timer invitationChecker=new Timer(){

			@Override
			public void run() {
				checkInvitations();					
			}};

			invitationChecker.scheduleRepeating(10000);
			invitationChecker.run();

			tileGrid.addRecordClickHandler(new RecordClickHandler(){
				@Override
				public void onRecordClick(RecordClickEvent event) {
					// TODO Auto-generated method stub
					free = true;
					freeOrNot = "Yes";
					final String profileName=event.getRecord().getAttribute("name").toString();
					checkSubscription(profileName);
					friendUserName = profileName;
					//Window.alert("test1");
					//timeTableService.buildGoogleCalendar(friendUserName);
					//Window.alert("test2");

					final Date time = new Date();


					calendarService.getEventByUserName(profileName, new AsyncCallback<List<ArrayList<Object>>>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(List<ArrayList<Object>> result) {
							for(int i =0; i< result.size();i++){
								if(new Date(result.get(i).get(3).toString()).before(time))
									if(new Date(result.get(i).get(4).toString()).after(time))
										free = false;
								freeOrNot = "No";
							}

						}});




					locationService.getUserLocation(profileName, new AsyncCallback<ArrayList<String>>(){

						@Override
						public void onFailure(Throwable caught) {
							Window.alert(caught.getMessage());
						}

						@Override
						public void onSuccess(ArrayList<String> result) {
							//Window.alert("Profile Lat: "+result);
							double lat1=Double.parseDouble(result.get(1));
							double lon1=Double.parseDouble(result.get(2));
							String lastUpdate=result.get(3);
							//Window.alert("Profile Lon: "+result);
							double lat2=MainServices.getInstance().getMapService().getUserLat();
							double lon2=MainServices.getInstance().getMapService().getUserLng();
							//Window.alert("User lat:"+lat2+"  User Long: "+lon2);
							int R = 6371; // km
							double dLat = Math.toRadians(lat2-lat1);
							double dLon = Math.toRadians(lon2-lon1);
							double lati1 = Math.toRadians(lat1);
							double lati2 = Math.toRadians(lat2);

							double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
									Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lati1) * Math.cos(lati2); 
							double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
							double d = R * c;

							//Window.alert(Double.toString(d));
							//LatLng profilePosition=new LatLng(lat,ln);

							updateProfilePanel(profileName,Double.toString((int)(d*1000)/1000.),new Date(lastUpdate).toString());

						}
					});
				}} );


			subscriptionService.getFriends(user, new AsyncCallback<List<String>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert(caught.getMessage());
				}

				@Override
				public void onSuccess(final List<String> result) {
					// TODO Auto-generated method stub
					final ArrayList<StudentRecord> friends=new ArrayList<StudentRecord>();

					subscriptionService.getFriendsImages(user, new AsyncCallback<List<String>>(){

						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Failed to retrieve pics");

						}

						@Override
						public void onSuccess(List<String> result2) {
							
							for (int i=0;i<result.size();i++)
							{
								
								//friends.add(new StudentRecord(i, "https://dotabuff.com/assets/heroes/drow-ranger-757bb2a5ae36ee4f138803062ac9a1d2.png","Profile"));
									System.out.println(result2.get(i));
										friends.add(new StudentRecord(result.get(i),result2.get(i)));
										MainServices.getInstance().getMapService().showUserMarker(result.get(i), false);
									
							}
							
							StudentRecord[] friendsRecord=new StudentRecord[friends.size()];
							for (int i=0;i<friends.size();i++)
							{
								friendsRecord[i]=friends.get(i);
							}
							Record[] record =friendsRecord; //new StudentRecord[]{new StudentRecord("ubc Student", "https://dotabuff.com/assets/heroes/drow-ranger-757bb2a5ae36ee4f138803062ac9a1d2.png","Profile")};
							tileGrid.setData(record);
						}});

				}});

			//Record[] record = new StudentRecord[]{new StudentRecord("ubc Student", "https://dotabuff.com/assets/heroes/drow-ranger-757bb2a5ae36ee4f138803062ac9a1d2.png","Profile")};
			//tileGrid.setData(record); 

			DetailViewerField pictureField = new DetailViewerField("picture"); 

			pictureField.setType("image");  
			pictureField.setImageWidth(100);  
			pictureField.setImageHeight(100);  
			//pictureField.setImageURLPrefix("war/images/");
			DetailViewerField nameField = new DetailViewerField("name");  
			tileGrid.setFields(pictureField, nameField);  
			tileGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler(){

				@Override
				public void onRecordDoubleClick(RecordDoubleClickEvent event) {

					String to=event.getRecord().getAttribute("name").toString();
					System.out.println("");
					System.out.println("user Name is: "+ to);
					System.out.println("");
					//TODO chatManager.openChat(user,to);
					chatManager.openChat(user,to);
				}});
			tileGrid.draw(); 

			friendsPanel.addMember(tileGrid,0);
	} 

	public void updateProfilePanel(final String name,String distance,String lastUpdate)
	{			

		profileForm.clearValues();
		StaticTextItem profileName=new StaticTextItem("ProfileName","Profile Name");
		profileName.setValue(name);

		StaticTextItem freeToMeet = new StaticTextItem("freeOrNot", "Free to meet up:");
		freeToMeet.setValue(freeOrNot);
		StaticTextItem profileDistance=new StaticTextItem("ProfileDistance","Distance To");
		profileDistance.setValue(distance+" km");
		StaticTextItem profileLastUpdate=new StaticTextItem("ProfileLastUpdate","Last Updated");
		profileLastUpdate.setValue(lastUpdate);
		ButtonItem showCalendar=new ButtonItem("showCalendar","Show User Calendar");
		showCalendar.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {	
				checkSubscription(name);

				// TODO Auto-generated method stub
				//Window.alert("1friendName is: "+ friendUserName);
				timeTableService.buildGoogleCalendarWithUserName(friendUserName);
				//Window.alert("2friendName is: "+ friendUserName);
				//calendarService.getEventByUserName(name, async)

			}});

		ButtonItem showMap=new ButtonItem("ShowMap","Indicate On Map");
		showMap.addClickHandler(new ClickHandler(){		

			@Override
			public void onClick(ClickEvent event) {
				checkSubscription(name);
				MainServices.getInstance().getMapService().showUserMarker(name, true);
			}});

		ButtonItem removeFriend=new ButtonItem("RemoveFriend","Remove Friend");
		removeFriend.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				checkSubscription(name);
				SC.confirm("Do you really want to remove: "+name+" ?", new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							subscriptionService.removeFriend(user, name, new AsyncCallback<Void>(){

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(Void result) {
									Window.alert("Friend "+name+" has been removed!");	
									buildFriendList();
								}});
						} 
					}
				});
			}});
		profileForm.setItems(profileName,freeToMeet,profileDistance,profileLastUpdate,showCalendar,showMap,removeFriend);		
		profileForm.setAutoHeight();
	}


	public void buildRequest(){
		requestForm=new DynamicForm();
		requestForm.setSize("100%", "20%");
		searchBox.setShowTitle(false);

		requestForm.setItems(searchBox, searchButton);
		//friendsPanel.addMember(searchButton);
		final Label label0 = new Label();
		final Label label = new Label();

		searchButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub

				String userName = searchBox.getValueAsString();
				searchBox.clearValue();
				locationService.getUserNameByID(userName, new AsyncCallback<String>(){

					public void onFailure(Throwable caught) {
						Window.alert(caught.getMessage());
					}

					public void onSuccess(final String result) {
						label0.setText("The Searching Result: " + result);
						if(result==null)
							Window.alert("User does not exist in ");
						else
						{
							final HLayout requestPanel = new HLayout();
							friendsPanel.addMember(requestPanel);
							requestPanel.addMember(label0);
							requestPanel.addMember(requestButton);
							requestButton.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

								@Override
								public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
									// TODO Auto-generated method stub
									SubscriptionServiceAsync requestService = GWT.create(SubscriptionService.class);
									System.out.println(MainServices.account.getEmailAddress());
									System.out.println(result);
									requestService.sendInvitation(MainServices.account.getEmailAddress(), result, new AsyncCallback<Void>(){

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub

											Window.alert(caught.getMessage());
										}

										@Override
										public void onSuccess(Void result) {
											// TODO Auto-generated method stub
											Window.alert("Invitation sent!");
										}


									});
								}
							});
							System.out.println(result);
							//label.setText(result);
						}
					}

				});
			}

		});

		friendsPanel.addMember(requestForm);
	}



	private void checkSubscription(String user2)
	{

		subscriptionService.areFriends(user, user2, new AsyncCallback<Boolean>(){
			@Override
			public void onFailure(Throwable caught) {
				//System.out.println(result);

			}

			@Override
			public void onSuccess(Boolean result) {
				if (!result)
					buildFriendList();			
			}});

	}

	//	private String getPersonProfilePic()
	//	{
	//		JsonFactory JSON_FACTORY = new GsonFactory();
	//		HttpTransport TRANSPORT = new NetHttpTransport();
	//		Plus.Builder builder=new Plus.Builder(TRANSPORT, JSON_FACTORY, null);
	//		Plus plus=builder.build();
	//		 try {
	//			Person profile = plus.people().get("me").execute();
	//			 System.out.println("image url: " + profile.getImage().getUrl());
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		
	////		Auth.authorize();
	//		return "";
	//		//Person mePerson = plus.people.get("me").execute();
	//	}
}
