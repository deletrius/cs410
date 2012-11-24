package loclock.server;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import loclock.client.CalendarService;
import loclock.client.NotLoggedInException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.smartgwt.client.widgets.calendar.CalendarEvent;

public class CalendarServiceImpl extends RemoteServiceServlet implements CalendarService{

	/* (non-Javadoc)
	 * @see loclock.client.CalendarService#saveEvent(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	public void saveEvent(String userName, String eventName,String description, Date startDate, Date endDate) throws NotLoggedInException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{

			pm.makePersistent(new Calendar(userName, eventName, description, startDate, endDate));
		}


		finally{
			pm.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.CalendarService#checkDuplicate(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	public String checkDuplicate(String userName, String eventName,String description, Date startDate, Date endDate){
		String duplicate ="1";
		String du ="0";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{

			List<ArrayList<Object>> list = new ArrayList<ArrayList<Object>>();
			list = getEventByUserName(userName);
			if(list.size()==0){
				duplicate = "0";
			}
			else if(list.size() !=0){
				duplicate = "0";
				for(int i=0; i< list.size();i++){


					if((list.get(i).get(1).toString().equals(eventName))){

						if((list.get(i).get(2).toString().equals(description))){

							if((list.get(i).get(3).toString().equals(startDate.toString()))){

								if((list.get(i).get(4).toString().equals(endDate.toString()))){

									duplicate = "1";
									if(duplicate.equals("1")){
										System.out.println("set du to 1");
										du = "1";
									}
								}
							}
						}
					}
				}
			}

		}
		finally{
			pm.close();

		}
		System.out.println("Duplicate is: "+ duplicate);
		return du;
	}
	
	/* (non-Javadoc)
	 * @see loclock.client.CalendarService#deleteEvent(java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	public void deleteEvent(String userName,String eventName,String description,Date startDate,Date endDate){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Calendar.class);

		q.setFilter("userName == u && eventName == e && description == d && startDate == s && endDate == end");
		q.declareParameters("String u, String e, String d, java.util.Date s, java.util.Date end");
		try{
			Object[] obj = {userName, eventName, description,startDate,endDate};
			List<Calendar> list = (List<Calendar>)q.executeWithArray(obj);
			pm.deletePersistent(list.get(0));
		}
		finally{
			q.closeAll();
			pm.close();
		}



	}

	/* (non-Javadoc)
	 * @see loclock.client.CalendarService#getEventByUserName(java.lang.String)
	 */
	public List<ArrayList<Object>> getEventByUserName(String userName){
		List<Calendar> calendarList = new ArrayList<Calendar>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Calendar.class);
		q.setFilter("userName == u");
		q.declareParameters("String u");
		List<ArrayList<Object>> calendarAsList;
		try {
			calendarList =  (List<Calendar>) q.execute(userName);
			calendarAsList = new ArrayList<ArrayList<Object>>();

			for (Calendar calendarObj : calendarList)
			{
				ArrayList<Object> calendarAttributes = new ArrayList<Object>();
				calendarAttributes.add(calendarObj.getUserName());
				calendarAttributes.add(calendarObj.getEventName());
				calendarAttributes.add(calendarObj.getDescription());
				calendarAttributes.add(calendarObj.getStartDate().toString());
				calendarAttributes.add(calendarObj.getEndDate().toString());

				calendarAsList.add(calendarAttributes);


			}

			
		}
		finally {
			q.closeAll();
			pm.close();
		}
		return calendarAsList;

	}

	/* (non-Javadoc)
	 * @see loclock.client.CalendarService#getCalendarEventsForTodayByUsername(java.lang.String)
	 */
	public List<ArrayList<Object>> getCalendarEventsForTodayByUsername(String userName){		
		Date date = new Date();
		int todayMonth = date.getMonth();
		int todayDay = date.getDay();
		int todayYear = date.getYear();

		System.out.println("Today's date is: " + todayMonth + " " + todayDay + " " + todayYear);

		List<Calendar> userCalendarList = new ArrayList<Calendar>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Calendar.class);
		q.setFilter("userName == u");
		q.declareParameters("String u");
		try {

			// get all the calendar objects of the user
			userCalendarList =  (List<Calendar>) q.execute(userName);

			List<ArrayList<Object>> calendarAsList = new ArrayList<ArrayList<Object>>();

			// loop through all the calendar objects
			for (Calendar calendarObj : userCalendarList)
			{
				int calendarObjectMonth = calendarObj.getStartDate().getMonth();
				int calendarObjectDay = calendarObj.getStartDate().getDay();
				int calendarObjectYear = calendarObj.getStartDate().getYear();

				// Check that the month, day and year of the date object in database matches today's month, day, year
				if (calendarObjectDay == todayDay && calendarObjectYear == todayYear && calendarObjectMonth == todayMonth)
				{
					System.out.println("Found date is: " + calendarObjectMonth + " " + calendarObjectDay + " " + calendarObjectYear);
					ArrayList<Object> calendarAttributes = new ArrayList<Object>();
					calendarAttributes.add(calendarObj.getUserName());
					calendarAttributes.add(calendarObj.getEventName());
					calendarAttributes.add(calendarObj.getDescription());

					// store as milliseconds so that we can convert it back in the client side
					calendarAttributes.add(Long.toString(calendarObj.getStartDate().getTime()));
					calendarAttributes.add(Long.toString(calendarObj.getEndDate().getTime()));

					calendarAsList.add(calendarAttributes);
				}
			}

			return calendarAsList;
		}
		finally {
			q.closeAll();
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see loclock.client.CalendarService#isWithinRange(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	public boolean isWithinRange(String hour, String amPm, Date start, Date end)
	{		
		Date seven;
		try {
			seven = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa").parse("2012-11-23 " + hour +":00:00 " + amPm);
			if (isWithinRange(seven, start, end))
			{
				System.out.println("The date: " + start.toString() + " " + end.toString() + " contains " + hour + " oclock");
				return true;
			} 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Check if the time input is out of the bound of the event.
	 * 
	 * @param testDate the time input
	 * @param startDate the start time of the event
	 * @param endDate the end time of the event
	 * @return
	 */
	private boolean isWithinRange(Date testDate, Date startDate, Date endDate)
	{
		return !(testDate.before(startDate) || testDate.after(endDate));
	}

}
