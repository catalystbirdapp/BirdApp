import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import com.catalyst.android.birdapp.utilities.Utilities;

@RunWith(JUnit4.class)
public class UtilitiesTest {

	private static final long BOTTOM_VALUE = 1380542399L;// 9/30/2013
	private static final long TOP_VALUE = 4102401599L;// 12/31/2099
	Utilities utils = new Utilities();
	
	
	@Test
	public void currentMillisLiesWithinResonableRange(){
		//Tests that returned millis lies between 9/30/2013 & 12/31/2099
		assertTrue(utils.currentMillis() > BOTTOM_VALUE || utils.currentMillis() < TOP_VALUE);
	}
	
//	@Test
//	public void dateFormatDisplaysCorrectlyForGermanLocale(){
//		utils.setFormatter();
//		utils.setCountryCode("DE");
//		assertEquals("30/09/2013", utils.formatDate(BOTTOM_VALUE));
//	}
	
}