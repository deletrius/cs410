package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Geolocation.PositionOptions;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.ElementProvider;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.InfoWindow;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.Size;
import com.google.gwt.maps.client.directions.DirectionsCallback;
import com.google.gwt.maps.client.directions.DirectionsRenderer;
import com.google.gwt.maps.client.directions.DirectionsRendererOptions;
import com.google.gwt.maps.client.directions.DirectionsRequest;
import com.google.gwt.maps.client.directions.DirectionsService;
import com.google.gwt.maps.client.directions.DirectionsTravelMode;
import com.google.gwt.maps.client.directions.HasDirectionsRendererOptions;
import com.google.gwt.maps.client.directions.HasDirectionsRequest;
import com.google.gwt.maps.client.directions.HasDirectionsResult;
import com.google.gwt.maps.client.directions.HasDirectionsTravelMode;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.EventCallback;
import com.google.gwt.maps.client.geocoder.GeocoderRequest;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerImage;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class MapService {

	private MapWidget mapwidget;
	private String width;
	private String height;
	private HashMap<String,UserMarker> userMarkers;
	//	private InfoWindow userMarkerInfoWindow;
	private boolean firstTime = true;
	private final UserLocationServiceAsync userLocationService = GWT.create(UserLocationService.class);
	private AccountServiceAsync accountService = GWT.create(AccountService.class);
	private double currentUserLat = -1;
	private double currentUserLng = -1;
	private MarkerOptions friendsMo;
	private Marker friendMarker;
	private InfoWindow iw;
	private String email;
	private VLayout interactionPanel=new VLayout(5);
	private VLayout directionPanel=new VLayout(5);
	private VerticalPanel directionDetailPanel=new VerticalPanel();
	private String selectedUserName;
	private TimeTableService timeTableService = GWT.create(TimeTableService.class);
	public static enum TYPE{FRIEND, ME, STRANGER}

	private class UserMarker
	{

		Marker marker;
		InfoWindow infoWindow;
		String username;
		Date lastupdate=new Date();
		TYPE type;
		private String icon;
		public UserMarker(final String username, LatLng latlng, Date date, Boolean panTo, TYPE type,String iconUrl)
		{
			this.username=username;
			this.lastupdate=date;
			this.icon=iconUrl;
			this.type=type;
			MarkerOptions mo = new MarkerOptions();
			mo.setMap(mapwidget.getMap());
			mo.setClickable(true);
			mo.setPosition(latlng);
			if (type.equals(TYPE.ME))
				mo.setIcon(new MarkerImage.Builder("http://google-maps-icons.googlecode.com/files/lingerie.png").build());
			else if(type.equals(TYPE.FRIEND))
			{
				MarkerImage.Builder iconBuilder=new MarkerImage.Builder(iconUrl);
				iconBuilder.setScaledSize(new Size(30,30));

				mo.setIcon(iconBuilder.build());
			}
			else if(type.equals(TYPE.STRANGER))
			{
				mo.setIcon(new MarkerImage.Builder("http://google-maps-icons.googlecode.com/files/beach-certified.png").build());
			}
			marker = new Marker(mo);		


			if (infoWindow==null)
				infoWindow = new InfoWindow();

			Event.addListener(infoWindow,"closeclick",new EventCallback() {
				@Override
				public void callback() {
					infoWindow.close();
					interactionPanelMoveOut();
				}});

			Event.addListener(marker, "click", new EventCallback() {
				@Override
				public void callback() {
					selectedUserName=username;
					Set<String> keys=userMarkers.keySet();
					for (String i:keys)
					{
						UserMarker um=userMarkers.get(i);
						um.infoWindow.close();
					}

					//					infoWindow.close();
					interactionPanelMoveIn();
					DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy MM dd HH:mm.ss");

					infoWindow.setContent( Canvas.imgHTML(icon,50,50)+"<br><b>" + username + "</b><br><br>Last updated: " + "<i>" + dtf.format(lastupdate) + "</i>");

					infoWindow.bindTo("", marker);
					infoWindow.open(mapwidget.getMap(), marker);

				}
			});

			show();

			if (panTo)
			{
				mapwidget.getMap().setZoom(16);
				mapwidget.getMap().panTo(latlng);
			}
		}

		public void updateUserMarker(LatLng latlng,Date lastupdate,boolean panTo)
		{
			this.lastupdate=lastupdate;
			DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy MM dd HH:mm.ss");
			infoWindow.setContent(Canvas.imgHTML(icon,50,50)+"<br><b>" + username + "</b><br><br>Last updated: " + "<i>" + dtf.format(this.lastupdate) + "</i>");
			//			infoWindow.setContent("Your " + username + " location at: " + dtf.format(this.lastupdate));
			marker.setPosition(latlng);
			if (panTo)
				mapwidget.getMap().panTo(latlng);

		}

		public void show()
		{
			marker.setVisible(true);
		}
		
		public void destroy()
		{
			marker.setVisible(false);
			marker.unbindAll();
			infoWindow.close();
			infoWindow.unbindAll();
			//this.destroy();
		}
		
		public TYPE getType()
		{
			return type;
		}
	}

	public MapService(String width,String height)
	{
		this.width=width;
		this.height=height;
		userMarkers=new HashMap<String,UserMarker>();

		buildMapUI();
		initMapOverlayPanel();
	}

	public void removePublicUserMarkers(){

		Set<String> keyset=userMarkers.keySet();
		String[] keys=new String[keyset.size()];
		for (int i=0;i<keyset.size();i++)
			keys[i]=keyset.toArray()[i].toString();
		for (String i:keys)
		{
			
			UserMarker um=userMarkers.get(i);
			if (um.getType().equals(TYPE.STRANGER))
			{
				userMarkers.remove(i);
				um.destroy();
			}
		}
	}
	
	public void removeFriendUserMarkers(){
		
		Set<String> keyset=userMarkers.keySet();
		String[] keys=new String[keyset.size()];
		for (int i=0;i<keyset.size();i++)
			keys[i]=keyset.toArray()[i].toString();
		for (String i:keys)
		{
			
			UserMarker um=userMarkers.get(i);
			if (um.getType().equals(TYPE.FRIEND))
			{
				userMarkers.remove(i);
				um.destroy();
			}
		}
	}
	
	public double getUserLat()
	{
		return currentUserLat;
	}
	public double getUserLng()
	{
		return currentUserLng;
	}
	private void buildMapUI()
	{
		final MapOptions options = new MapOptions();

		// Zoom level. Required
		options.setZoom(3);
		// Open a map centered on Cawker City, KS USA. Required
		options.setCenter(new LatLng(39.509, -98.434));

		final PositionOptions po=new PositionOptions();		

		po.setHighAccuracyEnabled(true);

		// Map type. Required.
		options.setMapTypeId(new MapTypeId().getRoadmap());

		// Enable maps drag feature. Disabled by default.
		options.setDraggable(true);
		// Enable and add default navigation control. Disabled by default.
		options.setNavigationControl(true);
		// Enable and add map type control. Disabled by default.
		options.setMapTypeControl(true);
		options.setZoom(14);
		options.setNavigationControl(true);
		options.setScrollwheel(true);


		mapwidget = new MapWidget(options);


		mapwidget.setSize(width,height);


		updateUserCurrentLocation(true);
		Timer refreshTimer = new Timer() {
			public void run() {
				updateUserCurrentLocation(false);
				// Custom marker image code here
				// MarkerImage.Builder imageBuilder = new
				// MarkerImage.Builder("http://mingle2.com/images/new/sex_quiz/person_icon.png");
				// marker.setIcon(imageBuilder.build());
			}
		};

		refreshTimer.scheduleRepeating(5000);

	}

	private VLayout initMapOverlayPanel()
	{

		interactionPanel.setSize("100px","100px");
		directionPanel.setSize("300px","100%");
		//directionDetailPanel.setSize("100%", "100%");
		//directionDetailPanel.setBorderWidth(20);

		//panel.setBackgroundColor("WHITE");
		IButton chatBtn=new IButton("Chat");
		chatBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {

				FriendService.chatManager.openChat(MainServices.account.getEmailAddress(), selectedUserName);

			}});

		IButton refreshFriendsBtn=new IButton("Refresh Friends");
		refreshFriendsBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				removeFriendUserMarkers();
				FriendService.refreshFriendMarkers();

			}});

		
		IButton addFriendBtn=new IButton("Add Friend");
		addFriendBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {

				SubscriptionServiceAsync requestService = GWT.create(SubscriptionService.class);
				requestService.sendInvitation(MainServices.account.getEmailAddress(), selectedUserName, new AsyncCallback<Void>(){

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

		IButton directionBtn=new IButton("Direction");

		//HasDirectionsRendererOptions options = new DirectionsRendererOptions();		
		final DirectionsRenderer directionsDisplay = new DirectionsRenderer();//options);
		directionsDisplay.setMap(mapwidget.getMap());

		directionsDisplay.setPanel(new ElementProvider(directionDetailPanel.getElement()));

		final DirectionsRequest dr=new DirectionsRequest();		
		DirectionsTravelMode dt=new DirectionsTravelMode();		
		dr.setTravelMode(dt.Driving());

		final DirectionsService ds=new DirectionsService();


		directionBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {		
				dr.setOriginLatLng(new LatLng(currentUserLat,currentUserLng));
				System.out.println(new LatLng(currentUserLat,currentUserLng));
				dr.setDestinationLatLng(userMarkers.get(selectedUserName).marker.getPosition());
				System.out.println(userMarkers.get(selectedUserName).marker.getPosition());

				ds.route(dr, new DirectionsCallback(){

					@Override
					public void callback(HasDirectionsResult response,
							String status) {

						// TODO Auto-generated method stub
						directionsDisplay.setDirections(response);						
						directionPanelMoveIn();
					}});
			}
		}
				);

		IButton calendarBtn=new IButton("Calendar");
		calendarBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				timeTableService.buildGoogleCalendarWithUserName(selectedUserName);

			}});
		interactionPanel.addMember(chatBtn);
		interactionPanel.addMember(directionBtn);
		interactionPanel.addMember(calendarBtn);
		interactionPanel.addMember(refreshFriendsBtn);
		interactionPanel.addMember(addFriendBtn);
		interactionPanel.setTop(Integer.parseInt(height.substring(0,height.length()-2))-200);
		interactionPanel.setLeft(-120);

		directionPanel.setTop(100);
		directionPanel.setLeft(-320);
		IButton directionCloseBtn=new IButton("Close");
		directionCloseBtn.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				directionPanelMoveOut();

			}});
		directionPanel.addMember(directionCloseBtn);
		directionPanel.addMember(directionDetailPanel);

		directionPanel.setBackgroundColor("WHITE");
		//directionPanel.animateMove(10, Integer.parseInt(height.substring(0,height.length()-2))-200);

		//panel.setShowEdges(true);
		return interactionPanel;
		}

		private void interactionPanelMoveIn()
		{
			interactionPanel.animateMove(10, Integer.parseInt(height.substring(0,height.length()-2))-200);
		}

		private void interactionPanelMoveOut()
		{
			interactionPanel.animateMove(-120, Integer.parseInt(height.substring(0,height.length()-2))-200);
		}

		private void directionPanelMoveIn()
		{
			directionPanel.animateMove(Integer.parseInt(width.substring(0,width.length()-2)), 0);
		}

		private void directionPanelMoveOut()
		{
			directionPanel.animateMove(-320, 100);
		}

		public void bindTo(Layout layout)
		{
			//layout.addMember(directionPanel);
			directionPanel.setParentElement(layout);
			interactionPanel.setParentElement(layout);
		}

		public void updateUserCurrentLocation(final boolean panTo)
		{

			final Geolocation gps=Geolocation.getIfSupported();

			gps.getCurrentPosition(new Callback<Position, PositionError>() {
				@Override
				public void onFailure(PositionError reason) {
					//				Window.alert("Failed to get user current location.");
					System.out.println("Failed to get user current location.");
				}

				@Override
				public void onSuccess(Position result) {
					currentUserLat = result.getCoordinates().getLatitude();
					currentUserLng = result.getCoordinates().getLongitude();
					userLocationService.updateUserLatLng(MainServices.account.getEmailAddress(), Double.toString(result.getCoordinates().getLatitude()), Double.toString(result.getCoordinates().getLongitude()), new Date(),new AsyncCallback<Void>() {
						@Override
						public void onSuccess(Void result) {
							//System.out.println("Success user update");
							showUserMarker(MainServices.account.getEmailAddress(),panTo,TYPE.ME,MainServices.getInstance().currentUserDisplayPicUrl);
						}

						@Override
						public void onFailure(Throwable caught) {
							System.out.println("User update failure.");
						}
					});


				}		
			});

		}

		public void showUserMarker(final String userName,final boolean panTo, final TYPE type, final String iconUrl)
		{

			userLocationService.getUserLocation(userName, new AsyncCallback<ArrayList<String>>(){

				@Override
				public void onFailure(Throwable caught) {
					//				Window.alert("Failed to get user location for "+userName);\
					System.out.println("Failed to get user location for "+userName);
				}

				@Override
				public void onSuccess(ArrayList<String> result) {	
					if (result.get(5).compareTo("only me")==0)
					{
						//Window.alert("private");
						return;
					}
					//				for(String i:result)
					//					System.out.println(i);
					LatLng pos=new LatLng(Double.parseDouble(result.get(1)),Double.parseDouble(result.get(2)));
					Date date=new Date(result.get(3));
					//System.out.println(date);
					if (!userMarkers.containsKey(userName))
					{
						//System.out.println("put");
						userMarkers.put(userName, new UserMarker(userName,pos,date,panTo,type,iconUrl));
					}
					else
					{
						//System.out.println("get");
						userMarkers.get(userName).updateUserMarker(pos, date,panTo);
					}
				}
			});
		}

		public void showPublicMarkers(int n)
		{
			this.removePublicUserMarkers();
			userLocationService.getNearNPublicUsers(MainServices.getInstance().account.getEmailAddress(), n, currentUserLat, currentUserLng, new AsyncCallback<ArrayList<String>>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(ArrayList<String> result) {
					// TODO Auto-generated method stub
					Window.alert("Found "+result.size()+" users near you.");
					for (String i:result)
					{
						showUserMarker(i, false, TYPE.STRANGER, "");
					}
				}});
		}

		public MapWidget toWidget()
		{
			return mapwidget;
		}
	}
