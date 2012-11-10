package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.InfoWindow;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.event.Event;
import com.google.gwt.maps.client.event.EventCallback;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

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

	private class UserMarker
	{
		Marker marker;
		InfoWindow infoWindow;
		String username;
		Date lastupdate=new Date();
		public UserMarker(final String username, LatLng latlng, Date date, Boolean panTo)
		{
			this.username=username;
			this.lastupdate=date;
			MarkerOptions mo = new MarkerOptions();
			mo.setMap(mapwidget.getMap());
			mo.setClickable(true);
			mo.setPosition(latlng);
			marker = new Marker(mo);		
			

			if (infoWindow==null)
				infoWindow = new InfoWindow();
			Event.addListener(marker, "click", new EventCallback() {
				@Override
				public void callback() {
					infoWindow.close();
					
					DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy MM dd HH:mm.ss");
					infoWindow.setContent("Your " + username + " location at: " + dtf.format(lastupdate));

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
			infoWindow.setContent("Your " + username + " location at: " + dtf.format(this.lastupdate));
			marker.setPosition(latlng);
			if (panTo)
				mapwidget.getMap().panTo(latlng);

		}

		public void show()
		{
			marker.setVisible(true);
		}
	}

	public MapService(String width,String height)
	{
		this.width=width;
		this.height=height;
		userMarkers=new HashMap<String,UserMarker>();

		buildMapUI();
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
				updateUserCurrentLocation(true);
				// Custom marker image code here
				// MarkerImage.Builder imageBuilder = new
				// MarkerImage.Builder("http://mingle2.com/images/new/sex_quiz/person_icon.png");
				// marker.setIcon(imageBuilder.build());
			}
		};

		refreshTimer.scheduleRepeating(5000);

	}

	public void updateUserCurrentLocation(final boolean panTo)
	{
		
		final Geolocation gps=Geolocation.getIfSupported();

		gps.getCurrentPosition(new Callback<Position, PositionError>() {
			@Override
			public void onFailure(PositionError reason) {
				Window.alert("Failed to get user current location.");
			}

			@Override
			public void onSuccess(Position result) {
				currentUserLat = result.getCoordinates().getLatitude();
				currentUserLng = result.getCoordinates().getLongitude();
				userLocationService.updateUserLatLng(MainServices.account.getEmailAddress(), Double.toString(result.getCoordinates().getLatitude()), Double.toString(result.getCoordinates().getLongitude()), new Date(),new AsyncCallback<Void>() {
					@Override
					public void onSuccess(Void result) {
						System.out.println("Success user update");
						showUserMarker(MainServices.account.getEmailAddress(),panTo);
					}

					@Override
					public void onFailure(Throwable caught) {
						System.out.println("User update failure.");
					}
				});

				
			}		
		});

	}

	public void showUserMarker(final String userName,final boolean panTo)
	{
		
		userLocationService.getUserLocation(userName, new AsyncCallback<ArrayList<String>>(){

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get user location for "+userName);
			}

			@Override
			public void onSuccess(ArrayList<String> result) {		
//				for(String i:result)
//					System.out.println(i);
				LatLng pos=new LatLng(Double.parseDouble(result.get(1)),Double.parseDouble(result.get(2)));
				Date date=new Date(result.get(3));
				System.out.println(date);
				if (!userMarkers.containsKey(userName))
				{
					System.out.println("put");
					userMarkers.put(userName, new UserMarker(userName,pos,date,panTo));
				}
				else
				{
					System.out.println("get");
					userMarkers.get(userName).updateUserMarker(pos, date,panTo);
				}
			}
		});
	}

	public MapWidget toWidget()
	{
		return mapwidget;
	}
}
