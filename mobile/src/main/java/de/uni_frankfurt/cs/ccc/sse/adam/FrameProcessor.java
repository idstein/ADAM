package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class FrameProcessor implements MatrixProcessor {

    private static final Size ksize = new Size(3, 3);

    /**
     * @param frame grayscale image
     */
    public Mat process(Mat frame) {
        Mat in = frame.clone();
        Mat out = frame.clone();
        Imgproc.blur(frame, out, ksize);
        Imgproc.threshold(out, in, 150, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(in, out, new Mat());
        Imgproc.erode(out, in, new Mat());
        return in;
    }
}
