package loclock.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;

import loclock.client.CalendarService;
import loclock.client.ParserService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.smartgwt.client.widgets.calendar.CalendarEvent;


public class ParserServiceImpl extends RemoteServiceServlet implements ParserService{
	private String summary;
	private String until;
	
	private List<ArrayList<Object>> calendarAsList;
	private Date endDate = new Date();
	private Date classStartDate = new Date();
	private Date classEndDate = new Date();
	
	
	int eventId = 0;
	/**
	 * .ics file event builder.
	 * builds up event's attributes
	 * reset attributes once event is built
	 */
	public List<ArrayList<Object>> parse(String str, String userName){
		
		 
		String eventName=null;
		String description=null;
		String startDate=null;
		String startTime=null;
		String classEndTime=null;
		String courseFinish=null;
		Scanner scan = new Scanner(str);
		scan.useDelimiter(":");
		//DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		calendarAsList = new ArrayList<ArrayList<Object>>();
		while(scan.hasNextLine()){
			
			boolean one = false;
			
			String str1 = scan.nextLine();
			//get the date of when the term ends
			if(str1.contains("UNTIL")){
				one = true;
				System.out.println(str1);
				int index = str1.indexOf("UNTIL");
				courseFinish = str1.substring(index+6, index+14);
				String endTime = "235959";
				
				/**
				try{
					endDate=dateTimeFormat.parse(endDateStr+235959);
					
				}
				catch (ParseException e) {   
				    e.printStackTrace();   
				}   
				**/
				//System.out.println("endDate is:"+ endDate);
				
				
				
			}
			//get the eventName
			if(str1.contains("SUMMARY")){
				
				
				
				int index = str1.indexOf("SUMMARY");
				eventName = str1.substring(index+8);
				//System.out.println("eventName is:"+ eventName);
			}
			//get the location of the event (description)
			if(str1.contains("LOCATION")){
				
				
				int index = str1.indexOf("LOCATION");
				if(!str1.contains("America/Vancouver"))
				description = str1.substring(index+9);
				
				
				//System.out.println("description is:"+ description);
				
			}
		
			
			
			//get the date and time when the event starts
			
			
			if(str1.contains("DTSTART;")){
				
				
				int index = str1.indexOf("DTSTART;");
				startDate = str1.substring(index+31,39);
				//System.out.println("startDate is:"+ startDate);
				startTime = str1.substring(index+40);
		
				
			}
			
			//get the date and time when the event ends
			
			if(str1.contains("DTEND;")){
				
				int index = str1.indexOf("DTEND");
				classEndTime = str1.substring(index+38);
			//	System.out.println("endDate is:"+ classEndTime);
				
				
			}
			
				//System.out.println(eventName);
				//System.out.println(description);
				//System.out.println(startDate);
				//System.out.println(classEndTime);
				//System.out.println("");
				if(eventName!=null&&description!=null&&startDate!=null&&classEndTime!=null&&courseFinish!=null&&startTime!=null){
				//System.out.println("hahahaha");
					
								ArrayList<Object> calendarAttributes = new ArrayList<Object>();
								  calendarAttributes.add(eventName);
								  //System.out.println(eventName);
								  calendarAttributes.add(description);
								  //System.out.println(description);
								  calendarAttributes.add(startDate+startTime);
								  //System.out.println(startDate+startTime);
								  calendarAttributes.add(startDate+classEndTime);
								 // System.out.println(startDate+classEndTime);
								  calendarAttributes.add(courseFinish);
								  
								  calendarAsList.add(calendarAttributes);
								  	eventName=null;
								    description=null;
									startDate=null;
									startTime=null;
									classEndTime=null;
									courseFinish=null;
								  
								//  
							
				}
							
			
						//userName, eventName, description, startDate, endDate
		//	calEvent.add(new CalendarEvent(eventId, eventName,description,classStartDate,classEndDate));
			
			 
		}
		
		for(int i=0; i<calendarAsList.size();i++){
			System.out.println("ParserServiceImpl");
			System.out.println(calendarAsList.get(i).get(0));
			System.out.println(calendarAsList.get(i).get(1));
			System.out.println(calendarAsList.get(i).get(2));
			System.out.println(calendarAsList.get(i).get(3));
			System.out.println(calendarAsList.get(i).get(4));
			System.out.println("calendarAsList size is:"+calendarAsList.size());
		}
		
		return calendarAsList;
		
	}
	
}
