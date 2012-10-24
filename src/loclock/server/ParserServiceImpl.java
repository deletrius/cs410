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
	private ArrayList<String> rawData;
	private ArrayList<ArrayList<String>> filteredData=new ArrayList<ArrayList<String>>();
	private String until;
	private String eventName="";
	private String description="";
	private String startDate="";
	private String startTime="";
	private String classEndTime="";
	private List<ArrayList<Object>> calendarAsList = new ArrayList<ArrayList<Object>>();
	Date endDate = new Date();
	Date classStartDate = new Date();
	Date classEndDate = new Date();
	
	
	int eventId = 0;
	public List<ArrayList<Object>> parse(String str, String userName){
		
	//
		
		Scanner scan = new Scanner(str);
		scan.useDelimiter(":");
		rawData = new ArrayList<String>();
		//DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		
		while(scan.hasNextLine()){
			
			boolean one = false;
			
			String str1 = scan.nextLine();
			//get the date of when the term ends
			if(str1.contains("UNTIL")){
				one = true;
				int index = str1.indexOf("UNTIL");
				String endDateStr = str1.substring(index+6, index+14);
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
				
				rawData.add(str1);
				
			}
			//get the eventName
			if(str1.contains("SUMMARY")){
				
				
				rawData.add(str1);
				int index = str1.indexOf("SUMMARY");
				
				
				eventName = str1.substring(index+8);
			//	System.out.println("eventName is:"+ eventName);
			}
			//get the location of the event (description)
			if(str1.contains("LOCATION")){
				
				
				int index = str1.indexOf("LOCATION");
				description = str1.substring(index+9);
				
				
				//System.out.println("description is:"+ description);
				rawData.add(str1);
			}
		
			
			
			//get the date and time when the event starts
			
			
			if(str1.contains("DTSTART;")){
				
				
				int index = str1.indexOf("DTSTART;");
				startDate = str1.substring(index+31,39);
				//System.out.println("startDate is:"+ startDate);
				
				startTime = str1.substring(index+40);
			
				rawData.add(str1);
			}
			
			//get the date and time when the event ends
			
			if(str1.contains("DTEND;")){
				
				
				int index = str1.indexOf("DTEND");
				classEndTime = str1.substring(index+38);
			
				rawData.add(str1);
				
			}
			if(one){
				
								ArrayList<Object> calendarAttributes = new ArrayList<Object>();
								  calendarAttributes.add(eventName);
								  //System.out.println(calendarAttributes.get(0));
								  calendarAttributes.add(description);
								  //System.out.println(calendarAttributes.get(1));
								  calendarAttributes.add(startDate+startTime);
								 // System.out.println(calendarAttributes.get(2));
								  calendarAttributes.add(startDate+classEndTime);
								  
								  calendarAsList.add(calendarAttributes);
								//  
							
			
							}
			
						//userName, eventName, description, startDate, endDate
		//	calEvent.add(new CalendarEvent(eventId, eventName,description,classStartDate,classEndDate));
			
			 
		}
		/**
		for(int i=0; i<calendarAsList.size();i++){
			System.out.println(calendarAsList.get(i).get(0));
			System.out.println(calendarAsList.get(i).get(1));
			System.out.println(calendarAsList.get(i).get(2));
			System.out.println(calendarAsList.get(i).get(3));
			System.out.println("calendarAsList size is:"+calendarAsList.size());
		}
		**/
		return calendarAsList;
		
	}
	
}
