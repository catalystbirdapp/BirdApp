package com.catalyst.android.birdapp.utilities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {

	private final static String DATE_TIME_FORMAT_ENGLISH = "MM/dd/yyyy' 'HH:mm a";
	private final static String DATE_TIME_FORMAT_GERMAN = "dd/MM/yyyy' 'HH:mm";

	/**
	 * Returns the current time in milliseconds
	 * 
	 * @return
	 */
	public long currentMillis() {
		long currentTime = System.currentTimeMillis();
		return currentTime;
	}

	/**
	 * Formats the date for human readable display. Different depending on
	 * locale.
	 * 
	 * @param millis
	 * @return formatted date
	 */
	public String formatDate(long millis) {
		String countryCode = getLocaleCode();
		DateFormat formatter;
		if (countryCode.equals("DE")) {
			formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
		}

		return formatter.format(millis);
	}

	/**
	 * Formats the time for human readable display. Different depending on
	 * locale.
	 * 
	 * @param millis
	 * @return formatted time
	 */
	public String formatTime(long millis) {
		String countryCode = getLocaleCode();
		DateFormat formatter;
		if (countryCode.equals("DE")) {
			formatter = new SimpleDateFormat("HH:mm", Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
		}

		return formatter.format(millis);
	}

	/**
	 * Gets the locale that the device has been set to
	 * 
	 * @return two digit country code
	 */
	protected String getLocaleCode() {
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		return country;
	}

	public Date getDateObject(String dateString) {
		String countryCode = getLocaleCode();
		DateFormat formatter;
		if (countryCode.equals("DE")) {
			formatter = new SimpleDateFormat(DATE_TIME_FORMAT_GERMAN,
					Locale.GERMAN);
		} else {
			formatter = new SimpleDateFormat(DATE_TIME_FORMAT_ENGLISH,
					Locale.ENGLISH);
		}
		try {
			Date d = (Date) formatter.parse(dateString);
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

}
