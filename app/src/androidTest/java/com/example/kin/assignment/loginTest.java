package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class loginTest {
    @Rule
    public ActivityTestRule<login> myLoginTestRule = new ActivityTestRule<login>(login.class);
    private login myLogin = null;

    @Before
    public void setUp() throws Exception {
        myLogin = myLoginTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myLogin.findViewById(R.id.etUserID);
        View view2 = myLogin.findViewById(R.id.etPassword);
        View view3 = myLogin.findViewById(R.id.btnLogin);
        View view4 = myLogin.findViewById(R.id.btnRegister);
        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
    }
    @After
    public void tearDown() throws Exception {
        myLogin = null;
    }
    @Test
    public void loginUser() {
        Boolean result = myLogin.loginUser("test","test");
        assertEquals(true,result);
    }
}