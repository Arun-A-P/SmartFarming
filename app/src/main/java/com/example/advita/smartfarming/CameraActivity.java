package com.example.advita.smartfarming;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.concurrent.Semaphore;

public class CameraActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
   // Camera camera = null;
    private BaseLoaderCallback callback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("Loader", "Successfully instantiated");
                    cameraView.enableView();
                }
                default:
                {
                    super.onManagerConnected(status);
                }
            }
        }
    };
    private JavaCameraView cameraView;
    public static Mat tempImg, rgbImg;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toast.makeText(this, "Camera", Toast.LENGTH_LONG).show();
        cameraView = (JavaCameraView)findViewById(R.id.CameraView);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        cameraView.setCvCameraViewListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finishActivity(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, callback);
    }

    public void onDestroy()
    {
        super.onDestroy();
        if(cameraView != null)
        {
            cameraView.disableView();
        }
    }
    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat orig = inputFrame.rgba();
        Imgproc.cvtColor(orig, orig, Imgproc.COLOR_RGBA2RGB);
        rgbImg = orig.clone();
        Imgproc.cvtColor(orig, orig, Imgproc.COLOR_BGR2HSV);
        Core.inRange(orig, new Scalar(53, 150, 0), new Scalar(90, 255, 255), orig);

      /*  Imgproc.erode(orig, orig, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
        Imgproc.dilate(orig, orig, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
       */ tempImg = orig.clone();
        return orig;
    }

    public void analyzeImage(View v){
        cameraView.disableView();
        Intent i = new Intent(this, ShowContour.class);
        startActivity(i);
    }
}

