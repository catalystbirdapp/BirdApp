/**
 * 
 */
package com.catalyst.android.birdapp.utilities;

import java.util.ArrayList;
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
	
	
	protected void setUp() throws Exception {
		System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
		camera = Mockito.mock(Camera.class);
		params = Mockito.mock(Parameters.class);
	}
	
	
	public void testGetResolutionAndPreviewSize(){
		String resolution = "480 X 640";
		String preview = "640 X 480";
		String[] resolutionAndPreview = cUtils.getResolutionAndPreviewSize(resolution, preview);
		
		assertEquals(resolutionAndPreview[0], "480");
		assertEquals(resolutionAndPreview[1], "640");
		assertEquals(resolutionAndPreview[2], "640");
		assertEquals(resolutionAndPreview[3], "480");
	}
	
	
	public void testGetSupportedWhiteBalanceSettings(){
		ArrayList<String> whiteBalance = new ArrayList<String>();
		ArrayList<String> supportedWhiteBalance = new ArrayList<String>();
		supportedWhiteBalance.add("auto");
		when(camera.getParameters()).thenReturn(params);
		when(params.getSupportedWhiteBalance()).thenReturn(supportedWhiteBalance);
		whiteBalance = cUtils.getSupportedWhiteBalanceSettings(params);
		
		assertEquals(whiteBalance.get(0), "Auto");
		
	}
	
	public void testGetSupportedCameraResolution(){
		ArrayList<String> resolutions = new ArrayList<String>();
		ArrayList<Size> supportedCameraResolution = new ArrayList<Size>();
		Size size = camera.new Size(480, 640);
		supportedCameraResolution.add(size);
		when(camera.getParameters()).thenReturn(params);
		when(params.getSupportedPictureSizes()).thenReturn(supportedCameraResolution);
		resolutions = cUtils.getSupportedCameraResolution(params);
		assertEquals(resolutions.get(0), "640 X 480");
	}
	
	public void testGetSupportedPreviewSizes(){
		ArrayList<String> previewSizes = new ArrayList<String>();
		ArrayList<Size> supportedPreviewSizes = new ArrayList<Size>();
		Size size = camera.new Size(480, 640);
		supportedPreviewSizes.add(size);
		when(camera.getParameters()).thenReturn(params);
		when(params.getSupportedPreviewSizes()).thenReturn(supportedPreviewSizes);
		previewSizes = cUtils.getSupportedPreviewSize(params);
		assertEquals(previewSizes.get(0), "640 X 480");
	}
	
	public void testGetSupportedCameraZoom(){
		ArrayList<String> zoomLevel = new ArrayList<String>();
		when(camera.getParameters()).thenReturn(params);
		when(params.getMaxZoom()).thenReturn(59);
		zoomLevel = cUtils.getSupportedCameraZoom(params);
		assertEquals(zoomLevel.get(0), "1.0x");
	}
	
	public void testGetCameraZoom(){
		String savedZoom = "1.0x";
		int zoom = 0;
		when(camera.getParameters()).thenReturn(params);
		when(params.isZoomSupported()).thenReturn(true);
		when(params.getMaxZoom()).thenReturn(59);
		zoom = cUtils.getCameraZoom(params, savedZoom);
		assertEquals(10, zoom);
		
	}
	

}
