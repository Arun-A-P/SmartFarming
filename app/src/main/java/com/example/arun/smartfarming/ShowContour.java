package com.example.arun.smartfarming;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class ShowContour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contour);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.next);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Histogram.class);
                startActivity(i);
            }
        });
        modifyImage();
        //graphCut();
    }

protected void graphCut(){
    Mat orig = CameraActivity.rgbImg.clone();
    Mat hierarchy = new Mat();
    List<MatOfPoint> contours = new ArrayList<>();
    Core.transpose(orig, orig);
    Core.flip(orig, orig, 1);
    Imgproc.cvtColor(orig, orig, Imgproc.COLOR_RGBA2RGB);
    Mat temp = orig.clone();
    Mat detectEdges = new Mat();
    Mat filter = new Mat(orig.width(), orig.height(), orig.type());
    Imgproc.bilateralFilter(orig, filter, 31, 31, 31 / 2);
    Imgproc.cvtColor(filter, filter, Imgproc.COLOR_BGR2GRAY);
    Imgproc.Canny(filter, detectEdges, 20, 60);
    Imgproc.threshold(detectEdges, detectEdges, 1, 255, Imgproc.THRESH_BINARY_INV);
    Imgproc.morphologyEx(detectEdges, detectEdges, Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2, 2)));
    Imgproc.findContours(detectEdges, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);

    double largestPerimeter = -1;
    int pos = 0;
    for( int i = 0; i< contours.size(); i++ ) // iterate through each contour.
    {
        MatOfPoint2f cont = new MatOfPoint2f(contours.get(i).toArray());
        double perimeter = Imgproc.arcLength(cont, true);

        if(perimeter > largestPerimeter)
        {
            largestPerimeter = perimeter;
            pos = i;
        }
    }
    MatOfInt hull = new MatOfInt();
    Imgproc.convexHull(contours.get(pos), hull);

    Point[] points = new Point[hull.rows()];

    for(int j=0; j < hull.rows(); j++){
        int index = (int)hull.get(j, 0)[0];
        points[j] = new Point(contours.get(pos).get(index, 0)[0], contours.get(pos).get(index, 0)[1]);
    }

    List<MatOfPoint> hullmop = new ArrayList<MatOfPoint>();
    MatOfPoint mop = new MatOfPoint();
    mop.fromArray(points);
    hullmop.add(mop);

    Imgproc.drawContours(temp, contours, -1, new Scalar(255, 255, 0));
    //Imgproc.drawContours(temp, hullmop, 0, new Scalar(255, 255, 0));

    //Imgproc.grabCut(temp, temp, );
    Bitmap bmp = Bitmap.createBitmap(temp.cols(), temp.rows(), Bitmap.Config.ARGB_8888);
    Utils.matToBitmap(temp, bmp);
    ImageView imageView = (ImageView) findViewById(R.id.imageView);
    imageView.setImageBitmap(bmp);
}

private void modifyImage() {
    Mat orig = CameraActivity.rgbImg.clone();
    Core.transpose(orig, orig);
    Core.flip(orig, orig, 1);
    Mat temp = orig.clone();
    Imgproc.cvtColor(orig, orig, Imgproc.COLOR_RGBA2RGB);
    Imgproc.cvtColor(orig, orig, Imgproc.COLOR_BGR2HSV);
    Core.inRange(orig, new Scalar(64, 0, 60), new Scalar(98, 255, 180), orig);

    Imgproc.dilate(orig, orig, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
    Imgproc.erode(orig, orig, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));

    Bitmap bmp = Bitmap.createBitmap(orig.cols(), orig.rows(), Bitmap.Config.ARGB_8888);
    List<MatOfPoint> contours = new ArrayList<>();
    Mat hierarchy = new Mat();

    Imgproc.findContours(orig, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
    MatOfPoint2f approxCurve = new MatOfPoint2f();
    double largest_area = -1;
    int largest_contour_index = 0;
    Rect bounding_rect = new Rect();

    for (int i = 0; i < contours.size(); i++) // iterate through each contour.
    {
        double a = Imgproc.contourArea(contours.get(i), false);  //  Find the area of contour
        bounding_rect = Imgproc.boundingRect(contours.get(i));
        //Imgproc.rectangle(temp, new Point(bounding_rect.x, bounding_rect.y), new Point(bounding_rect.x + bounding_rect.width, bounding_rect.y + bounding_rect.height), new Scalar(255, 0, 0, 255), 3);
        if (a > largest_area) {
            largest_area = a;
            largest_contour_index = i;                //Store the index of largest contour
            bounding_rect = Imgproc.boundingRect(contours.get(i)); // Find the bounding rectangle for biggest contour
        }
    }

    // Imgproc.rectangle(temp, new Point(bounding_rect.x, bounding_rect.y), new Point(bounding_rect.x + bounding_rect.width, bounding_rect.y + bounding_rect.height), new Scalar(255, 0, 0, 255), 3);

    MatOfInt hull = new MatOfInt();
    Imgproc.convexHull(contours.get(largest_contour_index), hull);

    Point[] points = new Point[hull.rows()];

    for (int j = 0; j < hull.rows(); j++) {
        int index = (int) hull.get(j, 0)[0];
        points[j] = new Point(contours.get(largest_contour_index).get(index, 0)[0], contours.get(largest_contour_index).get(index, 0)[1]);
    }

    List<MatOfPoint> hullmop = new ArrayList<MatOfPoint>();
    MatOfPoint mop = new MatOfPoint();
    mop.fromArray(points);
    hullmop.add(mop);

    bounding_rect = Imgproc.boundingRect(hullmop.get(0));
    Mat fgdModel = new Mat();
    fgdModel.setTo(new Scalar(255, 255, 255));
    Mat bgdModel = new Mat();
    bgdModel.setTo(new Scalar(255, 255, 255));
    Imgproc.cvtColor(temp, temp, Imgproc.COLOR_RGBA2RGB);
    Mat result = new Mat();
    Mat finImg = new Mat();
    Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3.0));


    Imgproc.grabCut(temp, result, bounding_rect, bgdModel, fgdModel, 1, 0);
    //Core.compare(result, source, result, Core.CMP_EQ);
    Imgproc.cvtColor(temp, temp, Imgproc.COLOR_RGB2RGBA);
    Core.convertScaleAbs(result, result, 100, 0);
    //Imgproc.cvtColor(result, result, Imgproc.COLOR_GRAY2RGB);
    //Mat cropImg = orig(bounding_rect);
    //Core.inRange(result, new Scalar(0, 0, 100), new Scalar(180, 255, 255), result);
    //Log.d("Mask", result.toString());
    for (int x = 0; x < result.rows(); x++) {
        for (int y = 0; y < result.cols(); y++) {
            double[] check = result.get(x, y);
            if(check[0] != 255){
                double[] data = {0, 0, 0, 0};
                temp.put(x, y, data);
               // Log.d("Value", Double.toString(check[0]));
            }
        }
    }
    Utils.matToBitmap(temp, bmp);
    ImageView imageView = (ImageView) findViewById(R.id.imageView);
    imageView.setImageBitmap(bmp);
}

    private static void convertToOpencvValues(Mat mask) {
        byte[] buffer = new byte[3];
        for (int x = 0; x < mask.rows(); x++) {
            for (int y = 0; y < mask.cols(); y++) {
                mask.get(x, y, buffer);
                int value = buffer[0];
                if (value >= 0 && value < 64) {
                    buffer[0] = Imgproc.GC_BGD; // for sure background
                } else if (value >= 64 && value < 128) {
                    buffer[0] = Imgproc.GC_PR_BGD; // probably background
                } else if (value >= 128 && value < 192) {
                    buffer[0] = Imgproc.GC_PR_FGD; // probably foreground
                } else {
                    buffer[0] = Imgproc.GC_FGD; // for sure foreground

                }
                mask.put(x, y, buffer);
            }
        }
    }

}
