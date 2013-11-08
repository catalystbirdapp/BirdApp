/**
 * 
 */
package com.catalyst.android.birdapp.utilities;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.test.InstrumentationTestCase;

/**
 * @author hyoung
 *
 */
public class CameraUtilityTest extends InstrumentationTestCase{

	
	private CameraUtilities cUtils = new CameraUtilities();
	private Camera camera;
	private Parameters params;
	private static final String SETTING_480 = "480";
	private static final String SETTING_640 = "640";
	private static final String SETTING_640_X_480 = "640 X 480";
	private static final String SETTLING_480_X_640 = "480 X 640";
	private static final String ZOOM_1 = "1.0x";
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		camera = Mockito.mock(Camera.class);
		params = Mockito.mock(Parameters.class);
	}
	
	
	public void testGetResolutionAndPreviewSize(){
		String resolution = SETTLING_480_X_640;
		String preview = SETTING_640_X_480;
		String[] resolutionAndPreview = cUtils.getResolutionAndPreviewSize(resolution, preview);
		
		assertEquals(resolutionAndPreview[0], SETTING_480);
		assertEquals(resolutionAndPreview[1], SETTING_640);
		assertEquals(resolutionAndPreview[2], SETTING_640);
		assertEquals(resolutionAndPreview[3], SETTING_480);
	}
	
	
	public void testGetSupportedWhiteBalanceSettings(){
		List<String> whiteBalance = new ArrayList<String>();
		ArrayList<String> supportedWhiteBalance = new ArrayList<String>();
		supportedWhiteBalance.add("auto");
		when(camera.getParameters()).thenReturn(params);
		when(params.getSupportedWhiteBalance()).thenReturn(supportedWhiteBalance);
		whiteBalance = cUtils.getSupportedWhiteBalanceSettings(params);
		
		assertEquals(whiteBalance.get(0), "Auto");
		
	}
	
	public void testGetSupportedCameraResolution(){
		List<String> resolutions = new ArrayList<String>();
		ArrayList<Size> supportedCameraResolution = new ArrayList<Size>();
		Size size = camera.new Size(480, 640);
		supportedCameraResolution.add(size);
		when(camera.getParameters()).thenReturn(params);
		when(params.getSupportedPictureSizes()).thenReturn(supportedCameraResolution);
		resolutions = cUtils.getSupportedCameraResolution(params);
		assertEquals(resolutions.get(0), SETTING_640_X_480);
	}
	
	public void testGetSupportedPreviewSizes(){
		List<String> previewSizes = new ArrayList<String>();
		ArrayList<Size> supportedPreviewSizes = new ArrayList<Size>();
		Size size = camera.new Size(480, 640);
		supportedPreviewSizes.add(size);
		when(camera.getParameters()).thenReturn(params);
		when(params.getSupportedPreviewSizes()).thenReturn(supportedPreviewSizes);
		previewSizes = cUtils.getSupportedPreviewSize(params);
		assertEquals(previewSizes.get(0), SETTING_640_X_480);
	}
	
	public void testGetSupportedCameraZoom(){
		List<String> zoomLevel = new ArrayList<String>();
		when(camera.getParameters()).thenReturn(params);
		when(params.getMaxZoom()).thenReturn(59);
		zoomLevel = cUtils.getSupportedCameraZoom(params);
		assertEquals(zoomLevel.get(0), ZOOM_1);
	}
	
	public void testGetCameraZoom(){
		int zoom = 0;
		when(camera.getParameters()).thenReturn(params);
		when(params.isZoomSupported()).thenReturn(true);
		when(params.getMaxZoom()).thenReturn(59);
		zoom = cUtils.getCameraZoom(params, ZOOM_1);
		assertEquals(10, zoom);
		
	}
	

}
