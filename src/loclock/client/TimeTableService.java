package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.widgets.calendar.Calendar;
import com.smartgwt.client.widgets.calendar.CalendarEvent;
import com.smartgwt.client.widgets.calendar.events.CalendarEventAdded;
import com.smartgwt.client.widgets.calendar.events.CalendarEventClick;
import com.smartgwt.client.widgets.calendar.events.DayBodyClickEvent;
import com.smartgwt.client.widgets.calendar.events.DayBodyClickHandler;
import com.smartgwt.client.widgets.calendar.events.EventAddedHandler;
import com.smartgwt.client.widgets.calendar.events.EventClickHandler;


import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class TimeTableService extends Service{
	
	private static final String CAL_PUBLIC_URL = "https://www.google.com/calendar/embed?src=phihl3hmbh48lq5ml3ek9cj19o%40group.calendar.google.com&ctz=Australia/Brisbane";
	private Frame googleCalendar = new Frame(CAL_PUBLIC_URL);
	private final CalendarServiceAsync calendarService = GWT.create(CalendarService.class);
	private SubscriptionServiceAsync sub = GWT.create(SubscriptionService.class);
	public static Calendar calendar;
	public static CalendarEvent[] events;
	private ArrayList<String> friendList = new ArrayList();
	private ButtonItem sendInvitationButton = new ButtonItem("SendInvitation");
	private NotificationServiceAsync notification = GWT.create(NotificationService.class);
	private CalendarEventClick event1;
	private ClickHandler clickHandler1;
	//private String uName = MainServices.account.getEmailAddress();
	public TimeTableService()
	{	
		super();
		
		this.setTitle("Calendar");
		
		buildGoogleCalendar();
		this.setPane(calendar);
	}
	
	
	
	
	
	public void buildGoogleCalendar(){
		calendar = new Calendar();
		
		
		
		if(MainServices.account != null)
		{
		//calendarService.getEventByUserName(MainServices.account.getEmailAddress(), 
			calendarService.getEventByUserName(MainServices.account.getEmailAddress(), 
				new AsyncCallback<List<ArrayList<Object>>>(){
			
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Failed to get User Calendar Events");
							
						}
			//int eventId, String name, String description, java.util.Date startDate, java.util.Date endDate
						@Override
						public void onSuccess(List<ArrayList<Object>> result) {
							//for(int i =0; i<result.length;i++)
							ArrayList<CalendarEvent> calEvent=new ArrayList<CalendarEvent>();
							
							for(int i=0; i < result.size();i++){
								calEvent.add( new CalendarEvent(i,result.get(i).get(1).toString(), result.get(i).get(2).toString(),new Date(result.get(i).get(3).toString()),new Date(result.get(i).get(4).toString())));
							}
							events=new CalendarEvent[calEvent.size()];
							//CalendarEvent[] events=new CalendarEvent[calEvent.size()];
							for (int i=0; i<calEvent.size();i++)
							{
								events[i]=calEvent.get(i);
							}
							calendar.setData(events);
							
						}
						
						
					});
	}
			

	
		calendar.setSize("600px", "600px");
		
		
		sub.getFriends(MainServices.account.getEmailAddress(), new  AsyncCallback<List<String>>(){

			
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			
			public void onSuccess(List<String> result) {
				// TODO Auto-generated method stub
				
				for(int i=0; i<result.size();i++){
				System.out.println(result.get(i));
				friendList.add(result.get(i));
				}
			}});
			
		calendar.addEventClickHandler(new EventClickHandler(){
			
			
			public void onEventClick(CalendarEventClick event) {
				
				
				event1 = event;
				
				
				System.out.println("Event is: "+ event1.getEvent().getName().toString());
				
				
				
			
				
				//Window.alert("Clicked");
				
			}
			
			
		});
		
		sendInvitationButton.addClickHandler(new ClickHandler(){
			
			
			public void onClick(ClickEvent event2) {
				
				for(int i=0; i<friendList.size();i++){
					
					notification.addNotificationCalendar(MainServices.account.getEmailAddress(),
							friendList.get(i),event1.getEvent().getDescription() , event1.getEvent().getName(),
							event1.getEvent().getStartDate(), event1.getEvent().getEndDate(), new AsyncCallback<Void>(){

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									
								}

								@Override
								public void onSuccess(Void result) {
									
								}});
				}
			}});
		
		calendar.addDayBodyClickHandler(new DayBodyClickHandler(){

			@Override
			public void onDayBodyClick(DayBodyClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Date is: " + event.getDate());

			}


		});
		calendar.addEventAddedHandler(new EventAddedHandler(){
				
			@Override
			public void onEventAdded(CalendarEventAdded event) {
				CalendarEvent cEvent = event.getEvent();
				calendarService.saveEvent(MainServices.account.getEmailAddress(),cEvent.getName(),cEvent.getDescription(),cEvent.getStartDate(),cEvent.getEndDate(), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Go home");

					}

					@Override
					public void onSuccess(Void result) {
						//Window.alert("Event Saved");
						calendarService.getEventByUserName(MainServices.account.getEmailAddress(), new AsyncCallback<List<ArrayList<Object>>>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(List<ArrayList<Object>> result) {
								// TODO Auto-generated method stub
								Window.alert(result.get(0).get(0).toString()+" "+result.get(0).get(1).toString()+" "+result.get(0).get(2).toString()+ " "+result.get(0).get(3).toString());
							}});

					}});
				System.out.println("event is: "+event.getEvent());

			}

		});
		
		calendar.setEventDialogFields(sendInvitationButton);
		googleCalendar.setWidth("600px");
		googleCalendar.setHeight("600px");
		calendar.setCanEditEvents(true);//CAL_PUBLIC_URL;
		
		calendar.draw();
	}
	
	
	
	
	
	
	
	
	
	
	public void buildGoogleCalendarWithUserName(String userName){
		calendar = new Calendar();
		PopupPanel popUp = new PopupPanel();
		if(userName != null)
		//if(MainServices.account != null)
		{
		//calendarService.getEventByUserName(MainServices.account.getEmailAddress(), 
			calendarService.getEventByUserName(userName, 
				new AsyncCallback<List<ArrayList<Object>>>(){
			
						@Override
						public void onFailure(Throwable caught) {
							Window.alert("Failed to get User Calendar Events");
							
						}
			//int eventId, String name, String description, java.util.Date startDate, java.util.Date endDate
						@Override
						public void onSuccess(List<ArrayList<Object>> result) {
							//Window.alert("WOW, TimeTable Service is up!!!!");
							//for(int i =0; i<result.length;i++)
							ArrayList<CalendarEvent> calEvent=new ArrayList<CalendarEvent>();
							
							for(int i=0; i < result.size();i++){
								calEvent.add( new CalendarEvent(i,result.get(i).get(1).toString(), result.get(i).get(2).toString(),new Date(result.get(i).get(3).toString()),new Date(result.get(i).get(4).toString())));
							//System.out.println(i+result.get(i).get(2).toString()+ result.get(i).get(3).toString()+new Date(result.get(i).get(4).toString())+new Date(result.get(i).get(5).toString()));
							//System.out.println("1:"+ i);
							//System.out.println("");
							//System.out.println(result.get(i).get(1).toString());
							//System.out.println(result.get(i).get(2).toString());
							//System.out.println(result.get(i).get(3).toString());
							//System.out.println("");
							}
							events=new CalendarEvent[calEvent.size()];
							//CalendarEvent[] events=new CalendarEvent[calEvent.size()];
							for (int i=0; i<calEvent.size();i++)
							{
								events[i]=calEvent.get(i);
							}
							calendar.setData(events);
							
						}
						
						
					});
	}
			

	
		calendar.setSize("600px", "600px");
		/**
		calendar.addDayBodyClickHandler(new DayBodyClickHandler(){

			@Override
			public void onDayBodyClick(DayBodyClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Date is: " + event.getDate());

			}


		});
		**/
		calendar.draw();
		calendar.setCanEditEvents(false);
		popUp.add(calendar);
		popUp.setGlassEnabled(true);
		popUp.setPixelSize(calendar.getWidth(), calendar.getHeight());
		popUp.setAnimationEnabled(true);
		popUp.setAutoHideEnabled(true);
		popUp.center();
		popUp.setVisible(true);
		popUp.show();
		

	}
	

}
