package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.PI;
import static org.opencv.core.Core.REDUCE_AVG;

/**
 * Created by idstein on 17/01/16.
 */
public class LaneDetectProcessor implements  MatrixProcessor {

    private static final int element_shape=Imgproc.MORPH_RECT,an=1;
    private static final int thrs1=0,thrs2=4000;
    private static final double rho=1,theta= PI/180;

    private static final double CANNY_MIN_THRESHOLD=1,//edge detector mininum hysteresis threshold
    CANNY_MAX_THRESHOLD=100,//edge detector maximum hysteresis threshold
    HOUGH_MIN_LINE_LENGTH=50, //remove lines shorter than this threshold
    HOUGH_MAX_LINE_GAP=100; //join lines

    private static final int HOUGH_THRESHOLD=30;     //line approval vote threshold

    @Override
    public Mat process(Mat input) {
        Mat mean_by_row = new Mat();
        Core.reduce(input,mean_by_row,1,REDUCE_AVG);
        Mat horizon_edges = new Mat();
        Imgproc.Canny(mean_by_row,horizon_edges,1,10);
        Mat canny = new Mat();
        Imgproc.Canny(input,canny,CANNY_MIN_THRESHOLD,CANNY_MAX_THRESHOLD,3,false);
        Mat lines = new Mat();
        Imgproc.HoughLinesP(canny,lines,rho,theta,
                HOUGH_THRESHOLD,HOUGH_MIN_LINE_LENGTH,HOUGH_MAX_LINE_GAP);
        return lines;
    }
}
