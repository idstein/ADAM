package de.uni_frankfurt.cs.ccc.sse.adam.test;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;

public class OpenCVTestRunner extends RobolectricGradleTestRunner {

    public OpenCVTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }
}
