package com.catalyst.android.birdapp.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utilities {

	public long currentMillis() {
		long currentTime = System.currentTimeMillis();
		return currentTime;
	}

	public String formatDate(long millis){
		String countryCode = getLocaleCode();
		DateFormat formatter;   
		if (countryCode == "DE"){
			formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		}
		
		return formatter.format(millis);
	}

	public String formatTime(long millis){
		String countryCode = getLocaleCode();
		DateFormat formatter;
		if (countryCode == "DE"){
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
}
