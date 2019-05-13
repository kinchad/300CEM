package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class recommendTest {
    @Rule
    public ActivityTestRule<recommend> myRecommendTestRule = new ActivityTestRule<recommend>(recommend.class);
    private recommend myRecommend = null;
    @Before
    public void setUp() throws Exception {
        myRecommend = myRecommendTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myRecommend.findViewById(R.id.lvCurrency);
        assertNotNull(view1);
    }
    @After
    public void tearDown() throws Exception {
        myRecommend = null;
    }
}