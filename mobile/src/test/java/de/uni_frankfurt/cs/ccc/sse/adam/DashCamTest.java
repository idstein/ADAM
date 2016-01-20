package de.uni_frankfurt.cs.ccc.sse.adam;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opencv.core.Mat;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Simple test for sanity failures
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class DashCamTest {

    @Test
    public void startActivity() throws Exception {
        StreetViewActivity activity = Robolectric.setupActivity(StreetViewActivity.class);
        assertNotNull(activity);
    }
}
