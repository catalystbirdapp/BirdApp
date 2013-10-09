package com.catalyst.android.birdapp.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormValidationUtilities {

	private List<String> missingFields = new ArrayList<String>();
	private static final List<String> FormFields = Arrays.asList("Bird Name",
			"Scientific Name", "Notes");

	public List<String> validateBirdFormFields(List<String> fieldValues) {
		for (int i = 0; i < fieldValues.size(); i++) {
			String current = fieldValues.get(i);
			if (current.equals("")) {
				missingFields.add(FormFields.get(i));
			}
		}
		return missingFields;
	}
}
