package com.example.kin.assignment;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class currencyDetailTest {
    @Rule
    public ActivityTestRule<currencyDetail> myCurrencyDetailTestRule = new ActivityTestRule<currencyDetail>(currencyDetail.class);
    private currencyDetail myCurrencyDetail = null;
    @Before
    public void setUp() throws Exception {
        myCurrencyDetail = myCurrencyDetailTestRule.getActivity();
    }
    @Test
    public void testLaunch(){
        View view1 = myCurrencyDetail.findViewById(R.id.btnFavour);
        View view2 = myCurrencyDetail.findViewById(R.id.tv7dayAlgorithm1);
        View view3 = myCurrencyDetail.findViewById(R.id.tv7dayBid1);
        View view4 = myCurrencyDetail.findViewById(R.id.tv7dayAsk1);
        View view5 = myCurrencyDetail.findViewById(R.id.tv1monthAlgorithm1);
        View view6 = myCurrencyDetail.findViewById(R.id.tv1monthBid1);
        View view7 = myCurrencyDetail.findViewById(R.id.tv1monthAsk1);
        View view8 = myCurrencyDetail.findViewById(R.id.tv1yearAlgorithm1);
        View view9 = myCurrencyDetail.findViewById(R.id.tv1yearBid1);
        View view10 = myCurrencyDetail.findViewById(R.id.tv1yearAsk1);
        View view11 = myCurrencyDetail.findViewById(R.id.tvCurrencyName);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
        assertNotNull(view4);
        assertNotNull(view5);
        assertNotNull(view6);
        assertNotNull(view7);
        assertNotNull(view8);
        assertNotNull(view9);
        assertNotNull(view10);
        assertNotNull(view11);
    }

    @After
    public void tearDown() throws Exception {
        myCurrencyDetail = null;
    }
}