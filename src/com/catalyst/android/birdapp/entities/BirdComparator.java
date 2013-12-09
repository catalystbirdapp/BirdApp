package com.catalyst.android.birdapp.entities;

import java.util.Comparator;

public class BirdComparator implements Comparator<BirdSighting> {

	@Override
	public int compare(BirdSighting arg0, BirdSighting arg1) {
		return arg0.getCommonName().compareTo(arg1.getCommonName());
	}
	
}
