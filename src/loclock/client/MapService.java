package loclock.client;

import java.util.ArrayList;
import java.util.Date;
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
	private Marker currentLocation;
	private boolean firstTime = true;
	private final UserLocationServiceAsync locationService = GWT.create(UserLocationService.class);
	private AccountServiceAsync accountService = GWT.create(AccountService.class);
	private double currentUserLat = -1;
	private double currentUserLng = -1;
	private MarkerOptions friendsMo;
	private Marker friendMarker;
	private InfoWindow iw;
	private String email;
	
	public MapService(String width,String height)
	{
		this.width=width;
		this.height=height;
		
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
		final Geolocation gps=Geolocation.getIfSupported();


		System.out.println(Geolocation.getIfSupported());

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

		Timer refreshTimer = new Timer() {
			public void run() {
				gps.getCurrentPosition(new Callback<Position, PositionError>() {
					@Override
					public void onFailure(PositionError reason) {
						// TODO Auto-generated method stub
						System.out.println(reason.getLocalizedMessage());
						System.out.println(reason.getMessage());
						//Window.alert("Failed to get current location.");
					}

					@Override
					public void onSuccess(Position result) {
						//System.out.println("Lat: " + result.getCoordinates().getLatitude() + " Long: " + result.getCoordinates().getLongitude());
						if (currentUserLat == -1 || currentUserLng == -1)
						{
							currentUserLat = result.getCoordinates().getLatitude();
							currentUserLng = result.getCoordinates().getLongitude();
							// Update the current user's geolocation upon success
							//locationService.updateUserLatLng(MainServices.account.getEmailAddress(), Double.toString(88.0), Double.toString(88.0), new AsyncCallback<Void>() {
							locationService.updateUserLatLng(MainServices.account.getEmailAddress(), Double.toString(result.getCoordinates().getLatitude()), Double.toString(result.getCoordinates().getLongitude()), new AsyncCallback<Void>() {
								
								@Override
								public void onSuccess(Void result) {
									// TODO Auto-generated method stub
									System.out.println("First Success user update");
								}
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									System.out.println("First User update failure.");
								}
							});
							
//							locationService.updateUserLatLng(MainServices.account.getEmailAddress(), result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude(), new AsyncCallback<Void>() {
//								
//								@Override
//								public void onSuccess(Void result) {
//									// TODO Auto-generated method stub
//									System.out.println("First Success user update");
//								}
//								
//								@Override
//								public void onFailure(Throwable caught) {
//									// TODO Auto-generated method stub
//									System.out.println("First User update failure.");
//								}
//							});
						}
						else if (currentUserLat != result.getCoordinates().getLatitude() || currentUserLng != result.getCoordinates().getLongitude() )
						{
							currentUserLat = result.getCoordinates().getLatitude();
							currentUserLng = result.getCoordinates().getLongitude();
							// Update the current user's geolocation upon success
							//locationService.updateUserLatLng(MainServices.account.getEmailAddress(), Double.toString(88.0), Double.toString(88.0), new AsyncCallback<Void>() {
							locationService.updateUserLatLng(MainServices.account.getEmailAddress(), Double.toString(result.getCoordinates().getLatitude()), Double.toString(result.getCoordinates().getLongitude()), new AsyncCallback<Void>() {
								
								@Override
								public void onSuccess(Void result) {
									// TODO Auto-generated method stub
									//System.out.println("Diff Success user update");
								}
								
								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									//System.out.println("Diff User update failure.");
								}
							});
						}
						
						MarkerOptions mo = new MarkerOptions();
						mo.setMap(mapwidget.getMap());
						mo.setClickable(true);
						mo.setPosition(new LatLng(result.getCoordinates()
								.getLatitude(), result.getCoordinates()
								.getLongitude()));

						final Marker marker = new Marker(mo);
						Event.addListener(marker, "click", new EventCallback() {
							@Override
							public void callback() {
								InfoWindow iw = new InfoWindow();

								Date date = new Date();
								DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy MM dd HH:mm.ss");
								iw.setContent("Your " + MainServices.account.getEmailAddress() + " location at: " + dtf.format(date));
								
								iw.bindTo("", marker);
								iw.open(mapwidget.getMap(), marker);
							}
						});
						
						// Custom marker image code here
						// MarkerImage.Builder imageBuilder = new
						// MarkerImage.Builder("http://mingle2.com/images/new/sex_quiz/person_icon.png");
						// marker.setIcon(imageBuilder.build());
						
						if (currentLocation != null)
						{
							currentLocation.setVisible(false);
						}
						currentLocation = marker;
						currentLocation.setVisible(true);
						
						if (firstTime)
						{
							mapwidget.getMap().setZoom(16);
							mapwidget.getMap().panTo(
									new LatLng(result.getCoordinates()
											.getLatitude(), result.getCoordinates()
											.getLongitude()));
							firstTime = false;
						}
					}
				}, po);
			}
		};
		refreshTimer.scheduleRepeating(5000);
		
		locationService.getUsersAsArrayList( new AsyncCallback<List<ArrayList<Object>>>() {
		
		@Override
		public void onSuccess(List<ArrayList<Object>> result) {
			// TODO Auto-generated method stub
			System.out.println("The number of users in database is:" + result.size());
			//MarkerOptions friendsMo;
			//Marker friendMarker;
			for (ArrayList<Object> userAttributes : result)
			{
				email = (String) userAttributes.get(0);
				System.out.println("The user email is: " + email);
				// Do user parsing in server side instead of here
//				if (!userAttributes.get(1).equals("-1.0") && !userAttributes.get(2).equals("-1.0") && !email.equals(MainServices.account.getEmailAddress()))
//				{
				//if ()
				//{
					double lat = Double.parseDouble((String) userAttributes.get(1));
					System.out.println("The user lat is: " + lat);
					double lng = Double.parseDouble((String) userAttributes.get(2));
					System.out.println("The user lng is: " + lng);
					
					
					friendsMo = new MarkerOptions();
					friendsMo.setMap(mapwidget.getMap());
					friendsMo.setClickable(true);
					friendsMo.setPosition(new LatLng(lat, lng));

					friendMarker = new Marker(friendsMo);
					Event.addListener(friendMarker, "click", new EventCallback() {
						@Override
						public void callback() {
							iw = new InfoWindow();

							iw.setContent("Location for: " + email);
							
							//iw.bindTo("", friendMarker);
							iw.open(mapwidget.getMap(), friendMarker);
						}
					});
					
					friendMarker.setVisible(true);
				//}
//				}
//				else
//				{
//					System.out.println("User does not contain lat and lng, therefore skipped: " + email);
//				}
			}
		}
		
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}
	});
	}
	
	public MapWidget toWidget()
	{
		return mapwidget;
	}
}
