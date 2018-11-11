package tutorhelper.model.tuitiontiming;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tutorhelper.testutil.Assert;

public class TuitionTimingTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new TuitionTiming(null));
    }

    @Test
    public void constructor_invalidTuitionTiming_throwsIllegalArgumentException() {
        String invalidTiming = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new TuitionTiming(invalidTiming));
    }

    @Test
    public void isValidTiming() {
        // null tuition timing
        Assert.assertThrows(NullPointerException.class, () -> TuitionTiming.isValidTiming(null));

        // invalid tuition timing
        assertFalse(TuitionTiming.isValidTiming("")); // empty string
        assertFalse(TuitionTiming.isValidTiming("Monday")); // day only
        assertFalse(TuitionTiming.isValidTiming("MONDAY 12:00PM")); //day in caps
        assertFalse(TuitionTiming.isValidTiming("12:00pm")); // time only
        assertFalse(TuitionTiming.isValidTiming("12:00pm Monday")); // wrong format
        assertFalse(TuitionTiming.isValidTiming("Monday 13:00pm")); //invalid timing

        // valid tuition timing
        assertTrue(TuitionTiming.isValidTiming("Monday 12:00pm"));
        assertTrue(TuitionTiming.isValidTiming("Saturday 11:00am"));
        assertTrue(TuitionTiming.isValidTiming("Tuesday 5:00PM"));
        assertTrue(TuitionTiming.isValidTiming("Sunday 10:30AM"));
    }
}
