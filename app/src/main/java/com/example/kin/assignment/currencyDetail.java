/*
An activity to display the prediction results of each exchange currency pairs
All results are get from an custom API
 */

package com.example.kin.assignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class currencyDetail extends AppCompatActivity {
    private String userid;    //store login user id
    private String currencyName;    //store clicked currency name

    //initial all activity elements
    private TextView tvCurrencyName;
    private TextView tv7dayAlgorithm1;
    private TextView tv7dayBid1;
    private TextView tv7dayAsk1;
    private TextView tv1monthAlgorithm1;
    private TextView tv1monthBid1;
    private TextView tv1monthAsk1;
    private TextView tv1yearAlgorithm1;
    private TextView tv1yearBid1;
    private TextView tv1yearAsk1;
    private ImageButton btnFavour;

    //parameters  used in http request client
    private String responseString;
    private JSONArray jsonArray;
    private JSONObject jsonobject;

    private String remarks;     //initial for insert to SQLite
    public sqlData SD = null;   //initial SQLite database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_detail);

        SD = new sqlData(this);

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");   //indentify the login user
        currencyName = intent.getStringExtra("currencyName");   //get the clicked currency

        //define all elements
        btnFavour = findViewById(R.id.btnFavour);
        tv7dayAlgorithm1 = findViewById(R.id.tv7dayAlgorithm1);
        tv7dayBid1 = findViewById(R.id.tv7dayBid1);
        tv7dayAsk1 = findViewById(R.id.tv7dayAsk1);
        tv1monthAlgorithm1 = findViewById(R.id.tv1monthAlgorithm1);
        tv1monthBid1 = findViewById(R.id.tv1monthBid1);
        tv1monthAsk1 = findViewById(R.id.tv1monthAsk1);
        tv1yearAlgorithm1 = findViewById(R.id.tv1yearAlgorithm1);
        tv1yearBid1 = findViewById(R.id.tv1yearBid1);
        tv1yearAsk1 = findViewById(R.id.tv1yearAsk1);
        tvCurrencyName = findViewById(R.id.tvCurrencyName);
        tvCurrencyName.setText(currencyName);

        //make http requests with different parameters
        new httpClient().execute("7day");
        new httpClient().execute("1month");
        new httpClient().execute("1year");

        //add a currency pairs to favourite list
        btnFavour.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //create dialog box for user input remarks
                AlertDialog.Builder builder = new AlertDialog.Builder(currencyDetail.this);
                builder.setTitle("Add your remarks for this currency pairs:");
                final EditText input = new EditText(currencyDetail.this);   // Set up the input box
                builder.setView(input);

                // Set up the dialog buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {  //click OK
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        remarks = input.getText().toString();
                        add(userid,currencyName,remarks);       //add the remarks to SQLite
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //click Cancel
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }
    public class returnString{  //return object storing two strings
        public String responseString;
        public String params;
    }
    class httpClient extends AsyncTask<String, Void, returnString> {
        protected returnString doInBackground(String... params) {
            String urlString = "";
            //make http request URL depends on the params
            if(params[0].equals("7day")) {
                urlString = "http://10.112.160.105:7777/sevenDayPredict?name=" + currencyName + "&algorithm=LinearRegression";
            }else if(params[0].equals("1month")) {
                urlString = "http://10.112.160.105:7777/oneMonthPredict?name="+currencyName+"&algorithm=LinearRegression";
            }else{
                urlString = "http://10.112.160.105:7777/oneYearPredict?name="+currencyName+"&algorithm=LinearRegression";
            }
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);       // create URL & connection
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(1500);        // set request timeout
                connection.setConnectTimeout(1500);
                // simulate Chrome's user agent, mobile browser may have compatible problems
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36");
                connection.setInstanceFollowRedirects(true);    // set auto-redirect

                //return 200 -> request successful
                if( connection.getResponseCode() == HttpsURLConnection.HTTP_OK ){
                    InputStream inputStream = connection.getInputStream();      // read the website
                    BufferedReader bufferedReader  = new BufferedReader( new InputStreamReader(inputStream) );

                    String tempStr;
                    StringBuffer stringBuffer = new StringBuffer();

                    while( ( tempStr = bufferedReader.readLine() ) != null ) {
                        stringBuffer.append( tempStr );
                    }
                    bufferedReader.close();
                    inputStream.close();

                    // get the string from website
                    responseString = stringBuffer.toString();
                }
            } catch (IOException e) {
                Log.e("Connection fail:",e.toString());     //display exception in logcat
            }finally {
                if (connection != null) {                    connection.disconnect();                }
            }
            //create the return object
            returnString rs = new returnString();
            rs.responseString = responseString;
            rs.params = params[0];
            return rs;
        }
        protected void onPostExecute(returnString rs){      //return to UI thread
            super.onPostExecute(rs);
            try{
                jsonArray = new JSONArray(rs.responseString);

                //display the prediction results depends on the params
                if(rs.params.equals("7day")){
                    for(int i=0; i < jsonArray.length(); i++) {
                        jsonobject = jsonArray.getJSONObject(i);
                        String ask = jsonobject.getString("ask");
                        String bid = jsonobject.getString("bid");
                        String algorithm = jsonobject.getString("algorithm");

                        tv7dayAlgorithm1.setText("Algorithm : "+algorithm);
                        tv7dayBid1.setText("Bid : "+bid);
                        tv7dayAsk1.setText("Ask : "+ask);
                    }
                }else if(rs.params.equals("1month")) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonobject = jsonArray.getJSONObject(i);
                        String ask = jsonobject.getString("ask");
                        String bid = jsonobject.getString("bid");
                        String algorithm = jsonobject.getString("algorithm");

                        tv1monthAlgorithm1.setText("Algorithm : " + algorithm);
                        tv1monthBid1.setText("Bid : " + bid);
                        tv1monthAsk1.setText("Ask : " + ask);
                    }
                }else{
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonobject = jsonArray.getJSONObject(i);
                        String ask = jsonobject.getString("ask");
                        String bid = jsonobject.getString("bid");
                        String algorithm = jsonobject.getString("algorithm");

                        tv1yearAlgorithm1.setText("Algorithm : " + algorithm);
                        tv1yearBid1.setText("Bid : " + bid);
                        tv1yearAsk1.setText("Ask : " + ask);
                    }
                }
            }catch (Throwable t){
                Log.e("Error Log","catch error");   //display error log in logcat
            }
        }
    }
    private void add(String userid,String currencyName,String remarks) {    //insert remarks to table "favour"
        SQLiteDatabase db = SD.getWritableDatabase();
        db.execSQL("insert into favour ('userid','currencyName','remarks') values ('"+userid+"','"+currencyName+"','"+remarks+"')");
        db.close();
    }
}