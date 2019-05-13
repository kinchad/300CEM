package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

public class favourTest {
    @Rule
    public ActivityTestRule<favour> myFavourTestRule = new ActivityTestRule<favour>(favour.class);
    private favour myFavour = null;
    @Before
    public void setUp() throws Exception {
        myFavour = myFavourTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myFavour.findViewById(R.id.lvCurrency);
        assertNotNull(view1);
    }

    @After
    public void tearDown() throws Exception {
        myFavour = null;
    }
    @Test
    public void getAll(){    }
}