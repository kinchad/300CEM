package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class settingsTest {
    @Rule
    public ActivityTestRule<settings> mySettingsTestRule = new ActivityTestRule<settings>(settings.class);
    private settings mySettings = null;
    @Before
    public void setUp() throws Exception {
        mySettings = mySettingsTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = mySettings.findViewById(R.id.etUserName);
        View view2 = mySettings.findViewById(R.id.etPassword);
        View view3 = mySettings.findViewById(R.id.btnUpdateAcc);
        View view4 = mySettings.findViewById(R.id.btnChi);
        View view5 = mySettings.findViewById(R.id.btnEng);
        View view6 = mySettings.findViewById(R.id.btnLogout);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
    }
    @After
    public void tearDown() throws Exception {
        mySettings = null;
    }
}