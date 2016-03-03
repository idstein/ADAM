package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class HorizonDetectProcessor implements MatrixProcessor {

    Core.MinMaxLocResult maxLoc;

    @Override
    public Mat process(Mat input) {
        Mat mean_by_row = new Mat();
        Core.reduce(input, mean_by_row, 1, Core.REDUCE_AVG);
        Mat horizon_edges = new Mat();
        Imgproc.Canny(mean_by_row, horizon_edges, 1, 10);
        maxLoc = Core.minMaxLoc(horizon_edges);
        /*Mat mask = Mat.ones(input.size(), CvType.CV_8U);
        Imgproc.rectangle(mask, new Point(), new Point(input.cols(),maxLoc.maxLoc.y), new Scalar(0));
        Mat result = new Mat();
        input.copyTo(result,mask);*/
        //input.adjustROI((int) maxLoc.maxLoc.y,input.rows(),0,input.cols());
        return new Mat(input, new Range(/*(int) maxLoc.maxLoc.y*/input.rows()/2, input.rows()));
    }

    @Override
    public void visualize(Mat input) {
        Imgproc.line(input,new Point(0, maxLoc.maxLoc.y), new Point(input.rows(), maxLoc.maxLoc.y), new Scalar(255));
    }
}
