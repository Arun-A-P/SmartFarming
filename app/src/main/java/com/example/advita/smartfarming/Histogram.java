package com.example.advita.smartfarming;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class Histogram extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        Log.d("Error", "");
        Hist();
    }

    protected void Hist(){
        Mat image = CameraActivity.tempImg;
        List<Mat> hsv_planes = new ArrayList<Mat>(3);
        Core.split(image, hsv_planes);

        MatOfInt histSize = new MatOfInt(256);

        final MatOfFloat histRange = new MatOfFloat(0f, 256f);

        boolean accumulate = false;

        Mat h_hist = new Mat();
        Mat s_hist = new Mat();
        Mat v_hist = new Mat();

        //error appear in the following sentences
        List<Mat> hplane = new ArrayList<Mat>();
        List<Mat> splane = new ArrayList<Mat>();
        List<Mat> vplane = new ArrayList<Mat>();

        hplane.add(hsv_planes.get(0));
        splane.add(hsv_planes.get(1));
        vplane.add(hsv_planes.get(2));

        Imgproc.calcHist(hplane, new MatOfInt(3), new Mat(), h_hist, histSize, histRange, accumulate);
        Imgproc.calcHist(splane, new MatOfInt(3), new Mat(), s_hist, histSize, histRange, accumulate);
        Imgproc.calcHist(vplane, new MatOfInt(3), new Mat(), v_hist, histSize, histRange, accumulate);

        int hist_w = 512;
        int hist_h = 600;
        long bin_w = Math.round((double) hist_w / 256);
        //bin_w = Math.round((double) (hist_w / 256));

        Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC1);
        Core.normalize(h_hist, h_hist, 3, histImage.rows(), Core.NORM_MINMAX);
        Core.normalize(s_hist, s_hist, 3, histImage.rows(), Core.NORM_MINMAX);
        Core.normalize(v_hist, v_hist, 3, histImage.rows(), Core.NORM_MINMAX);


        for (int i = 1; i < 256; i++) {
            Point p1 = new Point(bin_w * (i - 1), hist_h - Math.round(h_hist.get(i - 1, 0)[0]));
            Point p2 = new Point(bin_w * (i), hist_h - Math.round(h_hist.get(i, 0)[0]));
            Imgproc.line(histImage, p1, p2, new Scalar(255, 0, 0), 2, 8, 0);

            Point p3 = new Point(bin_w * (i - 1), hist_h - Math.round(s_hist.get(i - 1, 0)[0]));
            Point p4 = new Point(bin_w * (i), hist_h - Math.round(s_hist.get(i, 0)[0]));
            Imgproc.line(histImage, p3, p4, new Scalar(0, 255, 0), 2, 8, 0);

            Point p5 = new Point(bin_w * (i - 1), hist_h - Math.round(v_hist.get(i - 1, 0)[0]));
            Point p6 = new Point(bin_w * (i), hist_h - Math.round(v_hist.get(i, 0)[0]));
            Imgproc.line(histImage, p5, p6, new Scalar(0, 0, 255), 2, 8, 0);

        }
        ImageView imageView = (ImageView)findViewById(R.id.histogramView);
        Bitmap bmp = Bitmap.createBitmap(histImage.cols(), histImage.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(histImage, bmp);
        imageView.setImageBitmap(bmp);
    }
}