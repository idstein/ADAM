package de.uni_frankfurt.cs.ccc.sse.adam;

import android.support.annotation.NonNull;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.Math.PI;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class LaneDetectProcessor implements MatrixProcessor {

    private static final double rho = 1, theta = PI / 180;

    List<Line> lines = new ArrayList<>();
    List<Point> vanishingPointCandidates = new ArrayList<>();
    Point vp_candidate = new Point();
    int previous_votes = 0;
    //Line left_lane, right_lane;

    static final Point computeIntersect(final Point l11, final Point l12, final Point l21, final Point l22) {
        return computeIntersect(l11.x, l11.y, l12.x, l12.y, l21.x, l21.y, l22.x, l22.y);
    }

    static final Point computeIntersect(final double x1, final double y1, final double x2, final double y2,
                                        final double x3, final double y3, final double x4, final double y4) {
        double d = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (d == 0.0) { // Lines are parallel.
            return null;
        }

        return new Point(((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d,
                ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d);
    }

    static final boolean computerLineCircleIntersect(double x1, double y1, double x2, double y2, double x3, double y3, float r) {
        return computerLineCircleIntersect(new Point(x1, y1), new Point(x2, y2), new Point(x3, y3), r);
    }

    static final boolean computerLineCircleIntersect(Point E, Point L, Point C, float r) {
        final Point d = new Point(L.x - E.x, L.y - E.y);
        final Point f = new Point(E.x - C.x, E.y - C.y);
        double a = d.dot(d);
        double b = 2 * f.dot(d);
        double c = f.dot(f) - r * r;

        double discriminant = b * b - 4 * a * c;
        return discriminant >= 0;
    }

    @Override
    public Mat process(Mat input) {
        final int width = input.cols();
        final Mat grayscale = new Mat();
        Imgproc.cvtColor(input, grayscale, Imgproc.COLOR_BGR2GRAY);
        // Night mode
        Imgproc.equalizeHist(grayscale, grayscale);
        final double mean = Core.mean(grayscale).val[0];
        Imgproc.Canny(grayscale, grayscale, 1.33 * mean, 1.66 * mean);
        final double sumEdges = Core.sumElems(grayscale).val[0] / 3 / 255;
        final Mat newLanes = new Mat();
        Imgproc.HoughLinesP(grayscale, newLanes, rho, theta,
                150, 20, grayscale.rows());
        lines.clear();
        //left_lane = null;
        //right_lane = null;
        final Rect rect = new Rect(new Point(0, vp_candidate.y * 1.2), input.size());
        for (int i = 0; i < newLanes.rows(); i++) {
            double[] vec = newLanes.get(i, 0);
            double x1 = vec[0],
                    y1 = vec[1],
                    x2 = vec[2],
                    y2 = vec[3];
            int angle = (int) (Math.atan((y1 - y2) / (x2 - x1)) * 180 / Math.PI);
            if (Math.abs(angle) < 20 || Math.abs(angle) >= 85)
                continue;
            final Line a = new Line(new Point(x1, y1),new Point(x2, y2));
            lines.add(a);
            /*if (!computerLineCircleIntersect(a.start, a.end, vp_candidate, 40))
                continue;*/

            /*if ((x2 < width / 2 && (left_lane == null || a.length() > left_lane.length()))) {
                left_lane = a.clone();
                Imgproc.clipLine(rect, left_lane.start, left_lane.end);
            }
            if ((x2 > width / 2 && (right_lane == null || a.length() > right_lane.length()))) {
                right_lane = a.clone();
                Imgproc.clipLine(rect, right_lane.start, right_lane.end);
            }*/
        }
        final Rect visibleWindow = new Rect(new Point(0, 0), input.size());
        //if (BuildConfig.DEBUG)
        vanishingPointCandidates.clear();
        int count = 0, x_avg = 0, y_avg = 0;
        for (int i = 0; i < lines.size(); i++) {
            final Line l1 = lines.get(i);
            for (int j = i + 1; j < lines.size(); j++) {
                final Line l2 = lines.get(j);
                final Point intersection = computeIntersect(l1.start, l1.end, l2.start, l2.end);
                if (intersection != null && intersection.inside(visibleWindow)) {
                    count++;
                    x_avg += intersection.x;
                    y_avg += intersection.y;
                    //if (BuildConfig.DEBUG)
                    vanishingPointCandidates.add(intersection);
                }
            }
        }

        if (count != 0) {
            vp_candidate.x = (vp_candidate.x * previous_votes + x_avg) / (previous_votes + count);
            vp_candidate.y = (vp_candidate.y * previous_votes + y_avg) / (previous_votes + count);

            /*final Set<Point> left = new HashSet<>();
            final Set<Point> right = new HashSet<>();
            for (Point p : vanishingPointCandidates) {
                if(p.y < vp_candidate.y)
                    continue;
                if (p.x < vp_candidate.x) {
                    left.add(p);
                } else {
                    right.add(p);
                }
            }

            left_lane = fitLineSegmentToPoints(left, rect);
            right_lane = fitLineSegmentToPoints(right, rect);*/
        }
        previous_votes = count;
        return grayscale;
    }

    @NonNull
    private Line fitLineSegmentToPoints(Set<Point> points, Rect rect) {
        if (points.size() < 3)
            return null;
        final Mat lineFunc = new Mat();
        Imgproc.fitLine(new MatOfPoint(points.toArray(new Point[0])), lineFunc, Imgproc.CV_DIST_L12, 0, 0.1, 0.1);
        double x0 = lineFunc.get(0, 0)[0], y0 = lineFunc.get(1, 0)[0], vx0 = lineFunc.get(2, 0)[0], vy0 = lineFunc.get(3, 0)[0];
        final double max_steps = Math.max(rect.width / vx0, rect.height / vy0);
        Line l = new Line(new Point(x0, y0),new Point(x0 + vx0 * max_steps, y0 + vy0 * max_steps));
        Imgproc.clipLine(rect, l.start, l.end);
        return l;
    }

    @Override
    public void visualize(Mat input) {
        drawLanes(input);
        drawVanishingPoints(input);
    }

    void drawLanes(Mat input) {
        final Rect rect = new Rect(new Point(0, vp_candidate.y * 1.2), input.size());
        //if (BuildConfig.DEBUG)
            for (Line line : lines) {
                if (!Imgproc.clipLine(rect, line.start, line.end))
                    continue;
                if (computerLineCircleIntersect(line.start, line.end, vp_candidate, 20))
                    Imgproc.line(input, line.start, line.end, BuildConfig.DEBUG ? new Scalar(60) : new Scalar(60, 0, 0), 3);
            }

        if (previous_votes == 0)
            return;

        /*if (left_lane != null) {
            Imgproc.line(input, left_lane.start, left_lane.end, BuildConfig.DEBUG ? new Scalar(200) : new Scalar(0, 128, 0), 3);
        }
        if (right_lane != null) {
            Imgproc.line(input, right_lane.start, right_lane.end, BuildConfig.DEBUG ? new Scalar(200) : new Scalar(0, 128, 0), 3);
        }*/
    }

    void drawVanishingPoints(Mat input) {
        if (BuildConfig.DEBUG)
            for (Point vp : vanishingPointCandidates) {
                Imgproc.drawMarker(input, vp, new Scalar(100, 0, 0));
            }
        if (!BuildConfig.DEBUG || previous_votes != 0)
            Imgproc.circle(input, vp_candidate, 20 / (previous_votes / 2 + 1), new Scalar(255));
    }

    class Line {
        final Point start, end;

        Line(Point a, Point b) {
            start = a;
            end = b;
        }

        double length() {
            return sqrt(pow(end.x - start.x, end.y - start.y));
        }

        public Line clone() {
            return new Line(start,end);
        }
    }
}
