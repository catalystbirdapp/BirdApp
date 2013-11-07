/**
 * 
 */
package com.catalyst.android.birdapp.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;

/**
 * @author hyoung
 *
 */
public class CameraUtilities {


	/**
	 * takes the resolution and preview size strings from the setting pages spinners
	 * and then splits them and stores them in a string array and returns the array. 
	 * 
	 * @param resolution
	 * @param previewSize
	 * @return
	 */
	
	public String[] getResolutionAndPreviewSize(String resolution, String previewSize){
		
		String[] resolutionParts = resolution.split(" X ");
		String resolutionHeight = resolutionParts[0];
		String resolutionWidth = resolutionParts[1];
		String[] previewParts = previewSize.split(" X ");
		String previewHeight = previewParts[0];
		String previewWidth = previewParts[1];
		String [] sizes = {resolutionHeight, resolutionWidth, previewHeight, previewWidth};
		
		return sizes;
	}
	
	/**
	 * gets the phone's specific white balance settings and sets them to an array to be populated in the
	 * white balance spinner
	 * @return 
	 */
	public List<String> getSupportedWhiteBalanceSettings(Parameters parameters){
		List<String> supportedWhiteBalance = new ArrayList<String>();
		List<String>whiteBalanceParams = parameters.getSupportedWhiteBalance();
		
		for(int i = 0; i<whiteBalanceParams.size(); i ++){
			String word = whiteBalanceParams.get(i);
			String output = word.substring(0,1).toUpperCase(Locale.US) + word.substring(1);
			for(int j = 0; j<output.length() - 2; j++){
				
				if(output.substring(j, j+1).equals("-")){
					output = output.substring(0, j+1) + output.substring(j+1, j+2).toUpperCase(Locale.US) + output.substring(j+2);
				}
			}
			supportedWhiteBalance.add(output);
		}
		
		return supportedWhiteBalance;
	}
	
	
	/**
	 * gets supported camera resolutions and puts them into an array
	 * @return 
	 */
	
	public List<String> getSupportedCameraResolution(Parameters parameters){
		List<Size> pictureSize = parameters.getSupportedPictureSizes();
		List<String> resolutions = new ArrayList<String>();
		
		if(pictureSize.size() != 0){
		for(int i = 0; i<pictureSize.size(); i++){
			int height = pictureSize.get(i).height;
			int width = pictureSize.get(i).width;
			String resolution = String.valueOf(height +" X "+ width);
			resolutions.add(resolution);
		}
		}
		return resolutions;
	}
	
	/**
	 * gets supported preview sizes and puts them in an array
	 * @return 
	 */
	public List<String> getSupportedPreviewSize(Parameters parameters){
		List<String>previewSizes =  new ArrayList<String>();
		List<Size> previewSizeParameters = parameters.getSupportedPreviewSizes();
	
		
		for(int i = 0; i<previewSizeParameters.size(); i++){
			int height = previewSizeParameters.get(i).height;
			int width = previewSizeParameters.get(i).width;
			String preview = String.valueOf(height +" X "+width);
			previewSizes.add(preview);
		}
		
		return previewSizes;
	}
	
	/**
	 * gets camera's supported camera zoom levels and max zoom
	 * Also adds .0x to the end of the zoom levels
	 * @return 
	 */
	public List<String> getSupportedCameraZoom(Parameters parameters){
		int maxZoom = parameters.getMaxZoom() + 1;
		int count = 0;
		ArrayList<String> zoomLevel = new ArrayList<String>();
		if(maxZoom > 0){
		for(int i = 10; i<=maxZoom; i=i+10){
			count++;
			String test = String.valueOf(count).concat(".0x");
			zoomLevel.add(test);
		}
		}else {
			
		}
		return zoomLevel;
	}
	
	public int getCameraZoom(Parameters parameters, String savedZoom){
		int zoomNumber = 0;
		
		if(parameters.isZoomSupported()){
			
			String zoomLevel = savedZoom.substring(0, 1);
			zoomNumber = (Integer.parseInt(zoomLevel) * 10);
			/*
			 * Too avoid having the app crash, if user selects the max
			 * zoom size from the drop down list, we set zoom to getMaxZoom.
			 */
			if(zoomNumber ==  (parameters.getMaxZoom() + 1)){
				zoomNumber = (parameters.getMaxZoom());
			
			}
		}
		return zoomNumber;
	}
	
}
