package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class converterTest {
    @Rule
    public ActivityTestRule<converter> myConverterDetailTestRule = new ActivityTestRule<converter>(converter.class);
    private converter myConverter = null;
    @Before
    public void setUp() throws Exception {
        myConverter = myConverterDetailTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myConverter.findViewById(R.id.baseSpinner);
        View view2 = myConverter.findViewById(R.id.targetSpinner);
        View view3 = myConverter.findViewById(R.id.etBase);
        View view4 = myConverter.findViewById(R.id.btnConvert);
        View view5 = myConverter.findViewById(R.id.tvTarget);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
    }

    @After
    public void tearDown() throws Exception {
        myConverter = null;
    }
}