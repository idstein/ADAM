package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

import static java.lang.Math.PI;

public class LaneDetectProcessor implements MatrixProcessor {

    private static final Logger logger = Logger.getLogger(LaneDetectProcessor.class.getName());

    private static final double rho = 1, theta = PI/180;

    private static final double CANNY_MIN_THRESHOLD = 200,//edge detector mininum hysteresis threshold
            CANNY_MAX_THRESHOLD = 550,//edge detector maximum hysteresis threshold
            HOUGH_MIN_LINE_LENGTH = 25, //remove lines shorter than this threshold
            HOUGH_MAX_LINE_GAP = 200; //join lines

    private static final int HOUGH_THRESHOLD = 25;     //line approval vote threshold

    @Override
    public Mat process(Mat input) {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(input, grayscale, Imgproc.COLOR_BGR2GRAY);

        // Night mode
        Imgproc.equalizeHist(grayscale, grayscale);

        Imgproc.bilateralFilter(grayscale, input, 5, 10, 10);
        Imgproc.Canny(input, input, CANNY_MIN_THRESHOLD, CANNY_MAX_THRESHOLD, 3, false);
        Mat newLanes = new Mat();
        Imgproc.HoughLinesP(input, newLanes, rho, theta,
                HOUGH_THRESHOLD, HOUGH_MIN_LINE_LENGTH, HOUGH_MAX_LINE_GAP);
        lines.clear();
        for (int i = 0; i < newLanes.rows(); i++) {
            //double[] vec = lanes.get(0, i);
            double[] vec = newLanes.get(i, 0);
            double x1 = vec[0],
                    y1 = vec[1],
                    x2 = vec[2],
                    y2 = vec[3];

            int angle = (int)(Math.atan((y1-y2)/(x2-x1))*180/Math.PI);
            if(Math.abs(angle) <20)
                continue;

            Line a = new Line();
            a.start = new Point(x1, y1);
            a.end = new Point(x2, y2);
            lines.add(a);
        }

        return input;
    }

    class Line {
        Point start, end;
    }

    Queue<Line> lines = new LinkedList<>();

    Mat lanes;

    public void drawLanes(Mat input) {
        for (Line line : lines) {
            Imgproc.line(input, line.start, line.end, new Scalar(0, 128, 128), 3);
        }
    }

}
