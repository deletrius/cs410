package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.smartgwt.client.widgets.calendar.Calendar;
import com.smartgwt.client.widgets.calendar.CalendarEvent;
import com.smartgwt.client.widgets.calendar.events.CalendarEventAdded;
import com.smartgwt.client.widgets.calendar.events.DayBodyClickEvent;
import com.smartgwt.client.widgets.calendar.events.DayBodyClickHandler;
import com.smartgwt.client.widgets.calendar.events.EventAddedHandler;

public class TimeTableService extends Service{
	
	private static final String CAL_PUBLIC_URL = "https://www.google.com/calendar/embed?src=phihl3hmbh48lq5ml3ek9cj19o%40group.calendar.google.com&ctz=Australia/Brisbane";
	private Frame googleCalendar = new Frame(CAL_PUBLIC_URL);
	private final CalendarServiceAsync calendarService = GWT.create(CalendarService.class);
	public static Calendar calendar;
	
	public TimeTableService()
	{	
		super();
		
		this.setTitle("Calendar");
		
		buildGoogleCalendar();
		this.setPane(calendar);
	}
	
	
	public void buildGoogleCalendar(){
		calendar = new Calendar();
		//		calendar.setData(calendarService.getEvent("UserName", new AsyncCallback<CalendarEvent[]>(){
		//
		//			@Override
		//			public void onFailure(Throwable caught) {
		//				Window.alert("Failed to get User Calendar Events");
		//				
		//			}
		//
		//			@Override
		//			public void onSuccess(CalendarEvent[] result) {
		//				//for(int i =0; i<result.length;i++)
		//				calendar.setData(result);
		//				
		//			}
		//			
		//		}));
		//calendar.setData(calendarService.getEvent("UserName"));
		//calendar.setData(CalendarData.getRecords()); 
		System.out.println("aaaa "+MainServices.account);
		if(MainServices.account != null)
		{
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
							//System.out.println(i+result.get(i).get(2).toString()+ result.get(i).get(3).toString()+new Date(result.get(i).get(4).toString())+new Date(result.get(i).get(5).toString()));
							System.out.println("1:"+ i);
							System.out.println(result.get(i).get(1).toString());
							System.out.println(result.get(i).get(2).toString());
							System.out.println(result.get(i).get(3).toString());
							}
							CalendarEvent[] events=new CalendarEvent[calEvent.size()];
							for (int i=0; i<calEvent.size();i++)
							{
								events[i]=calEvent.get(i);
							}
							calendar.setData(events);
						}
						
						
					});
	}
			

	
		calendar.setSize("600px", "600px");
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
		calendar.draw();
		googleCalendar.setWidth("600px");
		googleCalendar.setHeight("600px");

	}
	

}
