package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class homePageTest {
    @Rule
    public ActivityTestRule<homePage> myHomePageTestRule = new ActivityTestRule<homePage>(homePage.class);
    private homePage myHomePage = null;

    @Before
    public void setUp() throws Exception {
        myHomePage = myHomePageTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myHomePage.findViewById(R.id.lvCurrency);
        View view2 = myHomePage.findViewById(R.id.etSearch);
        assertNotNull(view1);
        assertNotNull(view2);
    }
    @After
    public void tearDown() throws Exception {
        myHomePage = null;
    }
}