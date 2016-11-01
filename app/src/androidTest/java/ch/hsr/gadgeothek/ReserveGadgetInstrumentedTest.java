package ch.hsr.gadgeothek;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Date;

import ch.hsr.gadgeothek.ui.LoginActivity;
import ch.hsr.gadgeothek.util.RecyclerViewMatcher;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by jmat on 01.11.16.
 */
@RunWith(AndroidJUnit4.class)
public class ReserveGadgetInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void testReserveGadget() throws Exception {

        //perform login
        onView(withId(R.id.emailAddressEditText)).perform(typeText("test@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.keepMeLoggedInSwitch)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());

        //perform reservation
        onView(withRecyclerView(R.id.gadgetRecyclerView).atPositionOnView(0, R.id.gadget_detail_reserve_button)).perform(click());

        //confirm reservation
        onView(withText("Confirm Reservation")).check(matches(isDisplayed()));
        onView(withId(android.R.id.button1)).perform(click());

        //switch tab
        onView(withText("Reservations")).perform(click());

        //check reservation
        Context context = getInstrumentation().getContext();
        DateFormat dateFormatter = android.text.format.DateFormat.getDateFormat(context);
        String reservationString = String.format("Has been reserved at %s", dateFormatter.format(new Date()));
        onView(withId(R.id.gadget_detail_reserved)).check(matches(withText(reservationString)));

        //delete reservation
        onView(withId(R.id.gadget_detail_del_reserve_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        //we'll have to wait 550ms, because after the reservation was deleted, we'll wait 500ms before reloading the data
        Thread.sleep(550);

        //logout
        onView(withId(R.id.logoutMenu)).perform(click());
    }

}
