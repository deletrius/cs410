package loclock.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import loclock.server.Calendar;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.calendar.CalendarEvent;

public interface CalendarServiceAsync {
	void saveEvent(String userName, String eventName,String description, Date startDate, Date endDate,AsyncCallback<Void> callback);
	//List<CalendarEvent> getEvent(String name,AsyncCallback<CalendarEvent> async);
	void getEventByUserName(String name,AsyncCallback<List<ArrayList<Object>>> async);
	void deleteEvent(String userName, String eventName, String description,
			Date startDate, Date endDate, AsyncCallback<Void> callback);
	//boolean checkFree(String userName, Date time)
	//void checkFree(String userName, Date time, AsyncCallback<Boolean> callback);
	void checkDuplicate(String userName, String eventName,String description, Date startDate, Date endDate,AsyncCallback<String> callback);
}
