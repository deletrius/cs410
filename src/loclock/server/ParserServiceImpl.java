package loclock.server;

import java.util.ArrayList;
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
		while(scan.hasNextLine()){
			String str1 = scan.nextLine();
			//if(str1.contains("BEGIN:VEVENT"))
			//rawData.add(str1);
			if(str1.contains("UNTIL"))
				rawData.add(str1);
			
			if(str1.contains("SUMMARY"))
				rawData.add(str1);
			if(str1.contains("LOCATION"))
				rawData.add(str1);
			if(str1.contains("DESCRIPTION"))
				rawData.add(str1);
			if(str1.contains("DTSTART;"))
				rawData.add(str1);
			if(str1.contains("DTEND;"))
				rawData.add(str1);
		}
		for(int i=0; i< rawData.size();i++){
			
			System.out.println(rawData.get(i));
		}
		filteredData.add(rawData);
		return filteredData;
	}
}
