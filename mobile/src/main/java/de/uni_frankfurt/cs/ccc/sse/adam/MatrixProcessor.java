package de.uni_frankfurt.cs.ccc.sse.adam;

import org.opencv.core.Mat;

/**
 * Created by idstein on 17/01/16.
 */
public interface MatrixProcessor {

    Mat process(Mat input);

    void visualize(Mat input);
}
