package loclock.server;
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

	
	
	public void saveEvent(String userName, String eventName,String description, Date startDate, Date endDate) throws NotLoggedInException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		  try{
			 
				  pm.makePersistent(new Calendar(userName, eventName, description, startDate, endDate));
			  }
			
		  
		  finally{
			  pm.close();
		  }
	}
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
	/**
	public boolean checkFree(String userName, Date time){
		List<Calendar> calendarList = new ArrayList<Calendar>();
		boolean free = false;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Calendar.class);
		q.setFilter("userName ==u && startDate  >= s && endDate <= end");
		q.declareParameters("String u, java.util.Date s");
		try{
			calendarList = (List<Calendar>) q.execute(userName, time);
			if((calendarList.size())==0)
				free = true;
			else{
				free = false;
			}
		}
		finally{
			q.closeAll();
			pm.close();
		}
		return free;
	}
	**/
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
	
	public List<ArrayList<Object>> getEventByUserName(String userName){
		List<Calendar> calendarList = new ArrayList<Calendar>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		 Query q = pm.newQuery(Calendar.class);
		 q.setFilter("userName == u");
		 q.declareParameters("String u");
		try {
			 calendarList =  (List<Calendar>) q.execute(userName);
//		      for(Calendar cal : calendarList){
//		    	 Calendar calCopy = cal;
//		    	  calendarList.add(calCopy);
		    //}
		    
		
		List<ArrayList<Object>> calendarAsList = new ArrayList<ArrayList<Object>>();
		
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
		
		return calendarAsList;
		}
		finally {
	    	q.closeAll();
	      pm.close();
	    }
		
		
	}
	
}
