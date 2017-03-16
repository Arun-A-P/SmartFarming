package com.example.arun.smartfarming;

import android.graphics.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

public class ClassifierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Classify();
    }
    private Instances getInstances(Mat descriptors) {
        // the number of attributes to create the arff
        int numberAttributes = descriptors.cols();
        Log.d("instances for weka", "Number of attributes in the descriptor = {}" + numberAttributes);

        // loop in the number of attributes
        ArrayList<Attribute> attributes = new ArrayList<>();
        for (int att = 0; att < numberAttributes; att++) {
            attributes.add(new Attribute("Attribute" + att, att));
        }
        // Empty set of instances
        Instances instances = new Instances("ImageRelation", attributes, 0);
        // for each row of the descriptor, we will create a instance
        for (int row = 0; row < descriptors.rows(); row++) {
            double[] values = new double[instances.numAttributes()];
            for (int column = 0; column < descriptors.cols(); column++) {
                values[column] = descriptors.get(row, column)[0];
            }
            // add this instance to the list
            instances.add(new DenseInstance(row, values));
        }
        return instances;
    }
    private void Classify()
    {
        Mat temp = CameraActivity.rgbImg;
        try {
            //J48 c =  (J48)weka.core.SerializationHelper.read(getAssets().open("classifier.model"));;
            Classifier classifier = (Classifier) weka.core.SerializationHelper.read(getAssets().open("RFclassifier.model"));
            MatOfKeyPoint matOfKeyPoint = new MatOfKeyPoint();
           // FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
           // featureDetector.detect(temp, matOfKeyPoint);
          //  DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
          //  Mat descriptors = new Mat();
         //  descriptorExtractor.compute(temp, matOfKeyPoint, descriptors);
            Instances testCase = getInstances(temp);
            testCase.setClassIndex(testCase.numInstances() - 1);
            for(int i=0; i<testCase.numInstances(); i++) {
                double pred = classifier.classifyInstance(testCase.instance(i));
                Log.d("result line 1", Double.toString(testCase.instance(i).value(0)));
                Log.d("result line 2", testCase.classAttribute().value((int) testCase.instance(i).classValue()));
                Log.d("result line 3", testCase.classAttribute().value((int) pred));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }


    }
}

