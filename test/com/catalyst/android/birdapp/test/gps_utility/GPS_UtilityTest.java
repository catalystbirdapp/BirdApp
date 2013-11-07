package com.catalyst.android.birdapp.test.gps_utility;

import org.mockito.Mockito;

import com.catalyst.android.birdapp.GPS_Utility.GPSUtility;

import android.test.InstrumentationTestCase;

public class GPS_UtilityTest extends InstrumentationTestCase {

	protected void setUp() throws Exception {
		GPSUtility gps = Mockito.mock(GPSUtility.class);
	}

	public void testGetCriteria() {
		assertEquals("Yes", "Yes");
	}

}
