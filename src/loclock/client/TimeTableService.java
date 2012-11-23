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
import com.smartgwt.client.widgets.calendar.events.CalendarEventChangedEvent;
import com.smartgwt.client.widgets.calendar.events.CalendarEventClick;
import com.smartgwt.client.widgets.calendar.events.CalendarEventRemoved;
import com.smartgwt.client.widgets.calendar.events.DayBodyClickEvent;
import com.smartgwt.client.widgets.calendar.events.DayBodyClickHandler;
import com.smartgwt.client.widgets.calendar.events.EventAddedHandler;
import com.smartgwt.client.widgets.calendar.events.EventChangedHandler;
import com.smartgwt.client.widgets.calendar.events.EventClickHandler;
import com.smartgwt.client.widgets.calendar.events.EventRemovedHandler;


import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TimeTableService extends Service{

	private static final String CAL_PUBLIC_URL = "https://www.google.com/calendar/embed?src=phihl3hmbh48lq5ml3ek9cj19o%40group.calendar.google.com&ctz=Australia/Brisbane";
	private Frame googleCalendar = new Frame(CAL_PUBLIC_URL);
	private final CalendarServiceAsync calendarService = GWT.create(CalendarService.class);
	private SubscriptionServiceAsync sub = GWT.create(SubscriptionService.class);
	private Calendar calendar=null;
	private CalendarEvent[] events;
	private ArrayList<String> friendList = new ArrayList();
	private ButtonItem sendInvitationButton = new ButtonItem("SendInvitation");
	private NotificationServiceAsync notification = GWT.create(NotificationService.class);
	private CalendarEventClick event1;
	private ClickHandler clickHandler1;
	private VLayout calContainer;
	//private String uName = MainServices.account.getEmailAddress();
	private static volatile TimeTableService timeTableServiceInstance;
	private int count=0;
	private TimeTableService()
	{	
		super("Calendar", "http://i45.tinypic.com/2qsv5mu.png");
		calContainer = new VLayout();		
		calContainer.setSize("100%", "100%");
		this.setPane(calContainer);
		buildGoogleCalendar();

		//this.setPane(calContainer);
	}



	public static TimeTableService getInstance()
	{
		if (timeTableServiceInstance == null)
		{
			timeTableServiceInstance=new TimeTableService();

		}
		return timeTableServiceInstance;
	}

	public void buildGoogleCalendar(){
		
		final Calendar cal1 = new Calendar();

		cal1.setDisableWeekends(false);
		cal1.setSize("600px", "600px");

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
		//friends will be notified on event change

		cal1.addEventChangedHandler(new EventChangedHandler(){

			@Override
			public void onEventChanged(CalendarEventChangedEvent event) {
				//for(int i=0; i<friendList.size();i++){
				notification.addNotificationCalendar(MainServices.account.getEmailAddress(), MainServices.account.getEmailAddress(),
						"<u>I changed this event:</u> " + event.getEvent().getDescription(), event.getEvent().getName(),
						event.getEvent().getStartDate(), event.getEvent().getEndDate(), "modify", new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {
						// TODO Auto-generated method stub

					}});
				//}
			}});
		cal1.addEventClickHandler(new EventClickHandler(){


			public void onEventClick(CalendarEventClick event) {


				event1 = event;


				System.out.println("Event is: "+ event1.getEvent().getName().toString());





				//Window.alert("Clicked");

			}


		});
		//invitations of this event will be sent to all friends
		sendInvitationButton.addClickHandler(new ClickHandler(){


			public void onClick(ClickEvent event2) {

				//for(int i=0; i<friendList.size();i++){

				notification.addNotificationCalendar(MainServices.account.getEmailAddress(),
						"broadcast", event1.getEvent().getDescription(), event1.getEvent().getName(),
						event1.getEvent().getStartDate(), event1.getEvent().getEndDate(), "invite", new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(Void result) {

					}});
				//	}
			}});

		cal1.addDayBodyClickHandler(new DayBodyClickHandler(){

			@Override
			public void onDayBodyClick(DayBodyClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Date is: " + event.getDate());

			}


		});

		//event will be removed when clicked on remove on calendar. friends will be notified
		cal1.addEventRemovedHandler(new EventRemovedHandler() {

			@Override
			public void onEventRemoved(final CalendarEventRemoved event) {
				// TODO Auto-generated method stub
				calendarService.deleteEvent(MainServices.account.getEmailAddress(),
						event.getEvent().getName(), event.getEvent().getDescription(), event.getEvent().getStartDate(),
						event.getEvent().getEndDate(), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						notification.addNotificationCalendar(MainServices.account.getEmailAddress(), "broadcast",
								event.getEvent().getName(), "<u>I removed this event:</u> " + event.getEvent().getDescription(), event.getEvent().getStartDate(),
								event.getEvent().getEndDate(), "remove", new AsyncCallback<Void>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								System.out.println("user event deletion triggers notification");
							}});
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						//System.out.println("2222 Succeed");
					}
				});
			}
		});

		//event will be saved to datastore when added on Calendar UI
		cal1.addEventAddedHandler(new EventAddedHandler(){

			@Override
			public void onEventAdded(CalendarEventAdded event) {
				final CalendarEventAdded eventAdded = event;
				CalendarEvent cEvent = event.getEvent();
				calendarService.saveEvent(MainServices.account.getEmailAddress(),cEvent.getName(),cEvent.getDescription(),cEvent.getStartDate(),cEvent.getEndDate(), new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {


					}

					@Override
					public void onSuccess(Void result) {
						System.out.println("Event is saved successfully.");
						notification.addNotificationCalendar(MainServices.account.getEmailAddress(), "broadcast",
								eventAdded.getEvent().getName(), "<u>I added this new event:</u> " + eventAdded.getEvent().getDescription(), eventAdded.getEvent().getStartDate(),
								eventAdded.getEvent().getEndDate(), "add", new AsyncCallback<Void>() {

							@Override
							public void onSuccess(Void result) {
								// TODO Auto-generated method stub
								System.out.println("notification added due to added event");
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});



					}});
				System.out.println("event is: "+event.getEvent());
			}

		});


		cal1.setEventDialogFields(sendInvitationButton);
		cal1.setCanEditEvents(true);//CAL_PUBLIC_URL;
		if(MainServices.account != null)
		{
			//calendarService.getEventByUserName(MainServices.account.getEmailAddress(), 
			calendarService.getEventByUserName(MainServices.account.getEmailAddress(), 
					new AsyncCallback<List<ArrayList<Object>>>(){

				@Override
				public void onFailure(Throwable caught) {
					//							Window.alert("Failed to get User Calendar Events");
					System.out.println("Failed to get User Calendar Events");

				}
				//int eventId, String name, String description, java.util.Date startDate, java.util.Date endDate
				@Override
				public void onSuccess(List<ArrayList<Object>> result) {
					//for(int i =0; i<result.length;i++)
					ArrayList<CalendarEvent> calEvent=new ArrayList<CalendarEvent>();
					System.out.println("Result size: "+result.size());
					for(int i=0; i < result.size();i++){
						calEvent.add( new CalendarEvent(i,result.get(i).get(1).toString(), result.get(i).get(2).toString(),new Date(result.get(i).get(3).toString()),new Date(result.get(i).get(4).toString())));
					}
					events=new CalendarEvent[calEvent.size()];
					//CalendarEvent[] events=new CalendarEvent[calEvent.size()];
					
					
					for (int i=0; i<calEvent.size();i++)
					{
						events[i]=calEvent.get(i);
						System.out.println(events[i].getName());
					}
					cal1.setData(events);
					if (calendar!=null)
					{ 
						calContainer.removeMember(calendar);
						MainServices.getInstance().selectTab(1);
					}
					calendar = cal1;
					calContainer.addMember(calendar);
					System.out.println(count++);
				}
			});
		}

	}







	public void buildGoogleCalendarWithUserName(String userName){
		final Calendar cal2 = new Calendar();
		PopupPanel popUp = new PopupPanel();
		if(userName != null)
			//if(MainServices.account != null)
		{
			//calendarService.getEventByUserName(MainServices.account.getEmailAddress(), 
			calendarService.getEventByUserName(userName, 
					new AsyncCallback<List<ArrayList<Object>>>(){

				@Override
				public void onFailure(Throwable caught) {
					//							Window.alert("Failed to get User Calendar Events");
					System.out.println("Failed to get User Calendar Events");

				}
				//int eventId, String name, String description, java.util.Date startDate, java.util.Date endDate
				@Override
				public void onSuccess(List<ArrayList<Object>> result) {

					ArrayList<CalendarEvent> calEvent=new ArrayList<CalendarEvent>();

					for(int i=0; i < result.size();i++){
						calEvent.add( new CalendarEvent(i,result.get(i).get(1).toString(), result.get(i).get(2).toString(),new Date(result.get(i).get(3).toString()),new Date(result.get(i).get(4).toString())));

					}
					events=new CalendarEvent[calEvent.size()];

					for (int i=0; i<calEvent.size();i++)
					{
						events[i]=calEvent.get(i);
					}
					cal2.setData(events);

				}


			});
		}



		cal2.setSize("600px", "600px");

		cal2.setDisableWeekends(false);
		cal2.setCanEditEvents(false);
		calendar = cal2;
		calendar.draw();
		popUp.add(calendar);
		popUp.setGlassEnabled(true);
		popUp.setPixelSize(calendar.getWidth(), calendar.getHeight());
		popUp.setAnimationEnabled(true);
		popUp.setAutoHideEnabled(true);
		popUp.center();
		popUp.setVisible(true);
		popUp.show();


	}

public CalendarEvent[] getEvents()
{
	return events;
	}
}
