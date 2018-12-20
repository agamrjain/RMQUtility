package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatterObject {

	public static void main(String[] args) {
		  String dateString = "Sat Nov 10 01:06:16 GMT+05:30 2018";
		  SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
	      try {
			Date sDate = originalFormat.parse(dateString);
			System.out.println(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		 
		  
		  
		  SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
	      Date date = new Date();
	      String date1 = dateFormatter.format(date);
	      System.out.println(date1);
	
	}
}
