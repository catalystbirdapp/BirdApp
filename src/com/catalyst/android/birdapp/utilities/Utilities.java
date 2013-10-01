package com.catalyst.android.birdapp.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.text.ParseException;

public class Utilities {
	
	private final static String DATE_TIME_FORMAT = "MM/dd/yyyy' 'HH:mm";

	public long currentMillis() {
		long currentTime = System.currentTimeMillis();
		return currentTime;
	}

	public String formatDate(long millis){
		String countryCode = getLocaleCode();
		DateFormat formatter;   
		if (countryCode.equals("DE")){
			formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		}
		
		return formatter.format(millis);
	}

	public String formatTime(long millis){
		String countryCode = getLocaleCode();
		DateFormat formatter;
		if (countryCode.equals("DE")){
			formatter = new SimpleDateFormat("HH:mm", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
		}
		
		return formatter.format(millis);
	}
	
	protected String getLocaleCode(){
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		return country;
	}
	
	
	public Date getDateObject(String dateString) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
		try {
			Date d = (Date)formatter.parse(dateString);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
