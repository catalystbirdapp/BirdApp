package com.catalyst.android.birdapp.test.utilities;

import java.util.Locale;

import org.junit.After;

import com.catalyst.android.birdapp.utilities.Utilities;

import android.test.InstrumentationTestCase;

public class UtilitiesTest extends InstrumentationTestCase {

	private static final long BOTTOM_VALUE = 1380582000000L;// 9/30/2013 16:00hrs
	private static final long TOP_VALUE = 4102401599000L;// 12/31/2099
	private static final long OTHER_VALUE = 1380538800000L;// 9/30/2013 04:00hrs
	Utilities utils = new Utilities();

	public void testCurrentMillisLiesWithinReasonableRange() {
		// Tests that returned millis lies between 9/30/2013 & 12/31/2099
		assertTrue(utils.currentMillis() > BOTTOM_VALUE
				|| utils.currentMillis() < TOP_VALUE);
	}

	public void testDateFormatDisplaysCorrectlyForGermanLocale() {
		Locale.setDefault(Locale.GERMANY);
		assertEquals("30/09/2013", utils.formatDate(BOTTOM_VALUE));
	}

	public void testDateFormatDisplaysCorrectlyForLocalesOtherThanGermany() {
		Locale.setDefault(Locale.CANADA);
		assertEquals("09/30/2013", utils.formatDate(BOTTOM_VALUE));
		Locale.setDefault(Locale.UK);
		assertEquals("09/30/2013", utils.formatDate(BOTTOM_VALUE));
		Locale.setDefault(Locale.US);
		assertEquals("09/30/2013", utils.formatDate(BOTTOM_VALUE));
	}

	public void testTimeFormatDisplaysCorrectlyForGermanLocale() {
		Locale.setDefault(Locale.GERMANY);
		assertEquals("16:00", utils.formatTime(BOTTOM_VALUE));
		assertEquals("04:00", utils.formatTime(OTHER_VALUE));
	}

	public void testTimeFormatDisplaysCorrectlyForForLocalesOtherThanGermany() {
		Locale.setDefault(Locale.CANADA);
		assertEquals("04:00 PM", utils.formatTime(BOTTOM_VALUE));
		assertEquals("04:00 AM", utils.formatTime(OTHER_VALUE));
		Locale.setDefault(Locale.UK);
		assertEquals("04:00 PM", utils.formatTime(BOTTOM_VALUE));
		assertEquals("04:00 AM", utils.formatTime(OTHER_VALUE));
		Locale.setDefault(Locale.US);
		assertEquals("04:00 PM", utils.formatTime(BOTTOM_VALUE));
		assertEquals("04:00 AM", utils.formatTime(OTHER_VALUE));
	}
	
	@After
	public void tearDown(){
		Locale.setDefault(Locale.US);
	}

}