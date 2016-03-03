package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.PI;

public class LaneDetectProcessor implements MatrixProcessor {

    private static final Logger logger = Logger.getLogger(LaneDetectProcessor.class.getName());

    private static final double rho = 1, theta = PI / 180;

    private static final double CANNY_MIN_THRESHOLD = 1,//edge detector mininum hysteresis threshold
            CANNY_MAX_THRESHOLD = 50,//edge detector maximum hysteresis threshold
            HOUGH_MIN_LINE_LENGTH = 40, //remove lines shorter than this threshold
            HOUGH_MAX_LINE_GAP = 300; //join lines

    private static final int HOUGH_THRESHOLD = 40;     //line approval vote threshold
    List<Line> lines = new ArrayList<>();
    Mat lanes;

    @Override
    public Mat process(Mat input) {
        Mat grayscale = new Mat();
        Imgproc.cvtColor(input, grayscale, Imgproc.COLOR_BGR2GRAY);

        // Night mode
        Imgproc.equalizeHist(grayscale, grayscale);

        Imgproc.bilateralFilter(grayscale, input, 5, 10, 10);
        Imgproc.Canny(input, input, CANNY_MIN_THRESHOLD, CANNY_MAX_THRESHOLD);//, 3, false);
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

            int angle = (int) (Math.atan((y1 - y2) / (x2 - x1)) * 180 / Math.PI);
            if (Math.abs(angle) < 20 || Math.abs(angle) >= 85)
                continue;

            Line a = new Line();
            a.start = new Point(x1, y1);
            a.end = new Point(x2, y2);
            lines.add(a);

        }

        List<Point> vanishingPointCandidates = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            for (int j = i + 1; j < lines.size(); j++) {
                final Line a = lines.get(i), b = lines.get(j);
                final Point intersection = computeIntersect(a.start.x, a.start.y, a.end.x, a.end.y,
                        b.start.x, b.start.y, b.end.x, b.end.y);
                if (intersection != null)
                    vanishingPointCandidates.add(intersection);
            }
        }
        return input;
    }

    public void drawLanes(Mat input) {
        for (Line line : lines) {
            Imgproc.line(input, line.start, line.end, new Scalar(0, 128, 128), 3);
        }
    }

    Point computeIntersect(final double x1, final double y1, final double x2, final double y2,
                           final double x3, final double y3, final double x4, final double y4) {
        double d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (d == 0.0) { // Lines are parallel.
            return null;
        }

        return new Point(((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d,
                ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d);
    }

    class Line {
        Point start, end;
    }


}
