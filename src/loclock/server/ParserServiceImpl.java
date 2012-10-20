package loclock.server;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

import loclock.client.CalendarService;
import loclock.client.ParserService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class ParserServiceImpl extends RemoteServiceServlet implements ParserService{
	private String summary;
	private ArrayList<String> rawData;
	private ArrayList<ArrayList<String>> filteredData=new ArrayList<ArrayList<String>>();
	String until;
	public ArrayList<ArrayList<String>> parse(String str){
		
	//	System.out.println(str);
		
		
		Scanner scan = new Scanner(str);
		scan.useDelimiter(":");
		rawData = new ArrayList<String>();
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyymmdd/hhmmss");
		DateFormat dateFormat = new SimpleDateFormat("yyyymmdd");
		while(scan.hasNextLine()){
			Date endDate = new Date();
			Date classStartDate = new Date();
			String str1 = scan.nextLine();
			//if(str1.contains("BEGIN:VEVENT"))
			//rawData.add(str1);
			if(str1.contains("UNTIL")){
				int index = str1.indexOf("UNTIL");
				String endDateStr = str1.substring(index+6, index+14);
				String endTime = "235959";
				//System.out.println("parsed integer is: "+endDateStr);
				//System.out.println("parsed Date is: "+endDateStr.substring(6));
				//System.out.println("parsed Year is: "+endDateStr.substring(0, 4));
				//System.out.println("parsed Month is: "+endDateStr.substring(4, 6));
				
				//endDate.setTime(Long.parseLong(endTime));
				try{
					endDate=dateTimeFormat.parse(endDateStr+"/"+235959);
					
				}
				catch (ParseException e) {   
				    e.printStackTrace();   
				}   
				
				System.out.println("endDate is:"+ endDate);
				
				rawData.add(str1);
				
			}
			if(str1.contains("SUMMARY")){
				rawData.add(str1);
				int index = str1.indexOf("SUMMARY");
				
				
				String eventName = str1.substring(index+8);
				System.out.println("eventName is:"+ eventName);
			}
			if(str1.contains("LOCATION")){
				int index = str1.indexOf("LOCATION");
				String description = str1.substring(index+9);
				
				
				System.out.println("description is:"+ description);
				rawData.add(str1);
			}
			/**
			if(str1.contains("DESCRIPTION")){
				int index = str1.indexOf("DESCRIPTION");
				
				rawData.add(str1);
			}
			**/
			if(str1.contains("DTSTART;")){
				int index = str1.indexOf("DTSTART;");
				String startDate = str1.substring(index+31,39);
				System.out.println("startDate is:"+ startDate);
				
				String startTime = str1.substring(index+40);
				try{
					classStartDate=dateTimeFormat.parse(startDate+"/"+startTime);
	
					System.out.println("Start Date is:"+ classStartDate);
				}
				catch (ParseException e) {   
				    e.printStackTrace();   
				}   
				System.out.println("startTime is:"+ startTime);
				
				rawData.add(str1);
			}
			if(str1.contains("DTEND;")){
				int index = str1.indexOf("DTEND");
				String endTime = str1.substring(index+38);
				System.out.println("endTime is:"+ endTime);
				
				rawData.add(str1);
				System.out.println("");
				System.out.println("");
				System.out.println("");
				System.out.println("");
				
			}
		}
		for(int i=0; i< rawData.size();i++){
			
			System.out.println(rawData.get(i));
		}
		filteredData.add(rawData);
		return filteredData;
	}
}
