package com.catalyst.android.birdapp.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidationUtilities {

	private Pattern pattern;
	private Matcher m;
	private boolean isCorrect;
	private List<String> missingFields = new ArrayList<String>();
	private static final List<String> FormFields = Arrays.asList("Bird Name",
			"Scientific Name", "Notes");

	/**
	 * Takes in a list of user defined field values, and returns a list of field
	 * titles which the user has not supplied input.
	 * 
	 * @param fieldValues
	 * @return missingFields
	 */
	public List<String> validateBirdFormFields(List<String> fieldValues) {
		for (int i = 0; i < fieldValues.size(); i++) {
			String current = fieldValues.get(i);
			if (current.equals("")) {
				missingFields.add(FormFields.get(i));
			}
		}
		return missingFields;
	}

	/**
	 * Compares given string against regex: All alphabetic characters, spaces,
	 * hyphens, and apostrophes.
	 * 
	 * @param value
	 * @return
	 */
	public boolean isFieldValueFormattedAlphaOnly(String value) {
		pattern = Pattern.compile("[a-zA-Z\\s'-]+");
		m = pattern.matcher(value);
		isCorrect = m.matches();
		return isCorrect;
	}

	/**
	 * Compares given string against regex: All alphabetic characters, numeric characters, spaces,
	 * hyphens, and apostrophes.
	 * 
	 * @param value
	 * @return
	 */
	public boolean isFieldValueFormattedAlphaNumeric(String value) {
		pattern = Pattern.compile("[a-zA-Z0-9\\s'-]+");
		m = pattern.matcher(value);
		isCorrect = m.matches();
		return isCorrect;
	}
}
