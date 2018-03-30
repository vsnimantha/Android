package com.example.nimantha.virtualtranslator.TextOverlay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nimantha.virtualtranslator.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;


public class CameraView extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private BaseLoaderCallback mLoaderCallback  = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                {
                    Toast.makeText(getApplicationContext(),"OpenCV Loaded Sucessfully",Toast.LENGTH_LONG).show();
                    mOpencvCameraView.enableView();
                    break;
                }
                default:
                {
                    onManagerConnected(status);
                }
            }
        }
    };

    private JavaCameraView mOpencvCameraView;
    private Mat mRgba,mByte,mGray;
    private ImageView imageView;
    Scalar CONTOUR_COLOR;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Log.i("XXXX","Activity Loaded");

        mOpencvCameraView = (JavaCameraView) findViewById(R.id.camera_view);
        imageView = (ImageView) findViewById(R.id.imageView);
        mOpencvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpencvCameraView.setCvCameraViewListener((CameraBridgeViewBase.CvCameraViewListener2)this);
    }

    public void onResume(){
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this,mLoaderCallback);

    }

    public void onDestroy(){
        super.onDestroy();
        if(mOpencvCameraView!=null){
            mOpencvCameraView.disableView();
        }
    }



    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC3);
        mByte = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        CONTOUR_COLOR = new Scalar(255);
        MatOfKeyPoint keypoint = new MatOfKeyPoint();
        List<KeyPoint> listpoint = new ArrayList<KeyPoint>();
        KeyPoint kpoint = new KeyPoint();
        Mat mask = Mat.zeros(mGray.size(), CvType.CV_8UC1);

        int rectanx1;
        int rectany1;
        int rectanx2;
        int rectany2;

        Scalar zeos = new Scalar(0, 0, 0);
        List<MatOfPoint> contour1 = new ArrayList<MatOfPoint>();
        List<MatOfPoint> contour2 = new ArrayList<MatOfPoint>();
        Mat kernel = new Mat(1, 50, CvType.CV_8UC1, Scalar.all(255));
        Mat morbyte = new Mat();
        Mat hierarchy = new Mat();

        Rect rectan2 = new Rect();//
        Rect rectan3 = new Rect();//
        int imgsize = mRgba.height() * mRgba.width();

        FeatureDetector detector = FeatureDetector.create(FeatureDetector.MSER);
        detector.detect(mGray, keypoint);
        listpoint = keypoint.toList();
         for (int ind = 0; ind < listpoint.size(); ind++) {
            kpoint = listpoint.get(ind);
            rectanx1 = (int) (kpoint.pt.x - 0.5 * kpoint.size);
            rectany1 = (int) (kpoint.pt.y - 0.5 * kpoint.size);
            // rectanx2 = (int) (kpoint.pt.x + 0.5 * kpoint.size);
            // rectany2 = (int) (kpoint.pt.y + 0.5 * kpoint.size);
            rectanx2 = (int) (kpoint.size);
            rectany2 = (int) (kpoint.size);
            if (rectanx1 <= 0)
                rectanx1 = 1;
            if (rectany1 <= 0)
                rectany1 = 1;
            if ((rectanx1 + rectanx2) > mGray.width())
                rectanx2 = mGray.width() - rectanx1;
            if ((rectany1 + rectany2) > mGray.height())
                rectany2 = mGray.height() - rectany1;
            Rect rectant = new Rect(rectanx1, rectany1, rectanx2, rectany2);
            Mat roi = new Mat(mask, rectant);
            roi.setTo(CONTOUR_COLOR);
         }

        Imgproc.morphologyEx(mask, morbyte, Imgproc.MORPH_DILATE, kernel);
        Imgproc.findContours(morbyte, contour2, hierarchy,
                Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        for (int ind = 0; ind < contour2.size(); ind++) {
            rectan3 = Imgproc.boundingRect(contour2.get(ind));
            if (rectan3.area() > 0.5 * imgsize || rectan3.area() < 100 || rectan3.width / rectan3.height < 2) {
                Mat roi = new Mat(morbyte, rectan3);
                roi.setTo(zeos);
            } else
                Imgproc.rectangle(mRgba, rectan3.br(), rectan3.tl(),CONTOUR_COLOR);
        }

        return mRgba;




    }
}
