package com.catalyst.android.birdapp.utilities;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utilities {

	private final static String DATE_TIME_FORMAT_ENGLISH = "MM/dd/yyyy' 'hh:mm a";
	private final static String DATE_TIME_FORMAT_GERMAN = "dd/MM/yyyy' 'HH:mm";
	private final static String DATE_FORMAT_ENGLISH = "MM/dd/yyyy";
	private final static String DATE_FORMAT_GERMAN = "dd/MM/yyyy";
	private final static String TIME_FORMAT_ENGLISH = "hh:mm a";
	private final static String TIME_FORMAT_GERMAN = "HH:mm";
	private String countryCode;
	private SimpleDateFormat formatter;

	// Suppressing due to setting the localized pattern below
	@SuppressLint("SimpleDateFormat")
	public Utilities() {
		this.formatter = new SimpleDateFormat();
	}

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
		countryCode = localeCode();
		if (countryCode.equals("DE")) {
			formatter.applyPattern(DATE_FORMAT_GERMAN);
		} else {
			formatter.applyPattern(DATE_FORMAT_ENGLISH);
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
		countryCode = localeCode();
		if (countryCode.equals("DE")) {
			formatter.applyPattern(TIME_FORMAT_GERMAN);
		} else {
			formatter.applyPattern(TIME_FORMAT_ENGLISH);
		}
		return formatter.format(millis);
	}

	/**
	 * Gets the locale that the device has been set to
	 * 
	 * @return two digit country code
	 */
	public String localeCode() {
		Locale locale = Locale.getDefault();
		String country = locale.getCountry();
		return country;
	}

	// TODO Scheduled for removal
	public Date getDateObject(String dateString) {
		String countryCode = localeCode();
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
