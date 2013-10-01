package com.catalyst.android.birdapp.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utilities {

	public long currentMillis() {
		long currentTime = System.currentTimeMillis();
		return currentTime;
	}

	public String formatMillis(long date){
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		DateFormat formatter;
		if (country == "DE"){
			formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH);
		}
		
		return formatter.format(date);
	}

}
