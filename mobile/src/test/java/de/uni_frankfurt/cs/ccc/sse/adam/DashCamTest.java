package de.uni_frankfurt.cs.ccc.sse.adam;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import de.uni_frankfurt.cs.ccc.sse.adam.test.OpenCVTestRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Simple test for sanity failures
 */
@RunWith(OpenCVTestRunner.class)
@Config(constants = BuildConfig.class)
public class DashCamTest {

    @Test
    public void startActivity() throws Exception {
        StreetViewActivity activity = Robolectric.setupActivity(StreetViewActivity.class);
        assertNotNull(activity);
    }
}
