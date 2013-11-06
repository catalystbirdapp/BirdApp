/**
 * 
 */
package com.catalyst.android.birdapp.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.catalyst.android.birdapp.CameraActivity;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.util.Log;

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
	public ArrayList<String> getSupportedWhiteBalanceSettings(Parameters parameters, ArrayList<String> supportedWhiteBalance){
		//parameters = mCamera.getParameters();
		supportedWhiteBalance = (ArrayList<String>) parameters.getSupportedWhiteBalance();
		
		for(int i = 0; i<supportedWhiteBalance.size(); i ++){
			String word = supportedWhiteBalance.get(i);
			String output = word.substring(0,1).toUpperCase(Locale.US) + word.substring(1);
			for(int j = 0; j<output.length() - 2; j++){
				
				if(output.substring(j, j+1).equals("-")){
					output = output.substring(0, j+1) + output.substring(j+1, j+2).toUpperCase(Locale.US) + output.substring(j+2);
					
				}
			}
			supportedWhiteBalance.set(i, output);
		}
		
		return supportedWhiteBalance;
	}
	
	
	/**
	 * gets supported camera resolutions and puts them into an array
	 * @return 
	 */
	
	public ArrayList<String> getSupportedCameraResolution(Parameters parameters, ArrayList<String> resolutions){
		List<Size> pictureSize = parameters.getSupportedPictureSizes();
		resolutions = new ArrayList<String>();
		
		if(pictureSize.size() != 0){
		for(int i = 0; i<pictureSize.size(); i++){
			int height = pictureSize.get(i).height;
			Log.d("message20", String.valueOf(height));
			int width = pictureSize.get(i).width;
			String resolution = String.valueOf(height +" X "+ width);
			Log.d("message14", resolution);
			resolutions.add(resolution);
		}
		}else {
			resolutions.add("640 X 480");
			resolutions.add("1200 X 600");
		}
		
		return resolutions;
	}
	
	/**
	 * gets supported preview sizes and puts them in an array
	 * @return 
	 */
	public ArrayList<String> getSupportedPreviewSize(Parameters parameters, ArrayList<String>previewSizes){
		
		List<Size> previewSize = parameters.getSupportedPreviewSizes();
		Log.d("message13", String.valueOf(parameters.getSupportedPreviewSizes().size()));
		previewSizes = new ArrayList<String>();
		
		for(int i = 0; i<previewSize.size() - 1; i++){
			Log.d("message5", String.valueOf(previewSize.size()));
			int height = previewSize.get(i).height;
			int width = previewSize.get(i).width;
			String preview = String.valueOf(height +" X "+width);
			Log.d("message4", preview);
			previewSizes.add(preview);
		
		
		}
		
		return previewSizes;
	}
	
	/**
	 * gets camera's supported camera zoom levels and max zoom
	 * Also adds .0x to the end of the zoom levels
	 * @return 
	 */
	public ArrayList<String> getSupportedCameraZoom(Parameters parameters, ArrayList<String>zoomLevel){
		int maxZoom = parameters.getMaxZoom() + 1;
		int count = 0;
		Log.d("message12", String.valueOf(parameters.getMaxZoom()));
		if(maxZoom > 0){
		for(int i = 10; i<=maxZoom; i=i+10){
			count++;
			String test = String.valueOf(count).concat(".0x");
			zoomLevel.add(test);
		}
		}else {
		zoomLevel.add("1.0x");
		zoomLevel.add("2.0x");
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
