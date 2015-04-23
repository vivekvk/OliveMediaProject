package co.olivemedia.olivemediasampleproject;


import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity{

    String meetingTittle,meetingLocation,meetingTime;
    JSONArray jarray = null;

    ArrayList<HashMap<String, String>> meetingList;
    ListView lv;
    DatabaseHandler dbh;
    ProgressDialog pDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meetingList = new ArrayList<>();
        lv = getListView();
        new GetContacts().execute();

    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

            dbh = new DatabaseHandler(MainActivity.this);

        }


        @Override
        protected Void doInBackground(Void... arg0) {

            JsonParser jsonParser = new JsonParser();
            String json = jsonParser
                    .getJSONFromUrl("http://sandbox.hiho.olive.media/meetings");

            Log.e("Response: ", "> " + json);
            meetingList.clear();

            if (json != "") {

                try {
                    JSONObject jsonObj = new JSONObject(json);
                    jarray = jsonObj.getJSONArray("list");
                    int size = jarray.length();
                    dbh.EmptyRecords();
                    for (int i = 0; i < size; i++) {


                        JSONObject jObj = jarray.getJSONObject(i);
                        meetingTittle = jObj.getString("title");


                        meetingLocation = jObj.getString("location");

                        meetingTime = jObj.getString("time");

                        // tmp hashmap for single contact
                        HashMap<String, String> meetingdetailedList = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        meetingdetailedList.put("title", meetingTittle);
                        meetingdetailedList.put("location", meetingLocation);
                        meetingdetailedList.put("time", meetingTime);


                        meetingList.add(meetingdetailedList);


                        Meetingrecords records = new Meetingrecords();

                        records.setMeetingTitle(meetingTittle);

                        records.setMeetingLocation(meetingLocation);

                        records.setMeetingTime(meetingTime);


                        dbh.addRecords(records);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
                else {
                Cursor cHistory = null;
                meetingList.clear();
                cHistory = dbh.selectRecords();
                if (cHistory != null) {
                    if (cHistory.moveToFirst()) {

                        do {
                            meetingTittle  = cHistory.getString(cHistory
                                    .getColumnIndex("TITLE"));

                            meetingLocation = cHistory.getString(cHistory.getColumnIndex("LOCATION"));

                            meetingTime = cHistory.getString(cHistory
                                    .getColumnIndex("TIME"));

                            HashMap<String, String> meetingdetailedList = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            meetingdetailedList.put("title", meetingTittle);
                            meetingdetailedList.put("location", meetingLocation);
                            meetingdetailedList.put("time", meetingTime);


                            meetingList.add(meetingdetailedList);


                        } while (cHistory.moveToNext());

                    }
                    cHistory.close();
                }

                }


                return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing())
                pDialog.dismiss();

            ListAdapter adapter = new SimpleAdapter(MainActivity.this, meetingList,
                    R.layout.list_item, new String[] { "title", "location", "time" },
                    new int[] { R.id.title, R.id.location, R.id.time });
            lv.setAdapter(adapter);
            taskReload();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void taskReload(){

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                new GetContacts().execute();

            }
        }, 30000);
    }
}
