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
			//TODO determine most appropriate format for german date
			formatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
		}
		
		return formatter.format(date);
	}

}
