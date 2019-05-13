package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class registerTest{
    @Rule
    public ActivityTestRule<register> myRegisterTestRule = new ActivityTestRule<register>(register.class);
    private register myRegister = null;

    @Before
    public void setUp() throws Exception {
        myRegister = myRegisterTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myRegister.findViewById(R.id.btnRegister);
        View view2 = myRegister.findViewById(R.id.etUserID);
        View view3 = myRegister.findViewById(R.id.etPassword);
        View view4 = myRegister.findViewById(R.id.etUserName);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
    }
    @After
    public void tearDown() throws Exception {
        myRegister = null;
    }
}