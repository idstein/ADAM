package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class VehicleTrackingProcessor implements MatrixProcessor {

    final CascadeClassifier classifier;

    VehicleTrackingProcessor() {
        classifier = new CascadeClassifier(CascadeClassifier.class.getResource("/cars.xml").getFile());
        bbVehicles = new MatOfRect();
    }

    final MatOfRect bbVehicles;

    @Override
    public Mat process(Mat input) {
        classifier.detectMultiScale(input, bbVehicles);
        return input;
    }

    @Override
    public void visualize(Mat input) {
        for(Rect bb : bbVehicles.toArray()) {
            Imgproc.rectangle(input, bb.tl(), bb.br(), new Scalar(255, 0, 0));
        }
    }
}
