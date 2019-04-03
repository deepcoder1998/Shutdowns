package com.example.newu.shutdowns;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nEW u on 5/12/2018.
 */

public class MyService extends Service {

    private static String TAG = MyService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;
    static int requestTime=0;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mythread  = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }

    public void readWebPage() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest stringRequest_2 = new StringRequest(Request.Method.GET, "http://192.168.43.22/CESC/test_api.php?request_number="+requestTime, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                 int uniqueId = 45612;
                //get it deleted if not required
                NotificationCompat.Builder notifications;
                notifications = new NotificationCompat.Builder(getApplicationContext());
                notifications.setAutoCancel(true);
                //delete

                Log.d(TAG, "Got Response");
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String count= jsonObject.getString("count");
                    String difference = jsonObject.getString("Difference");
                    int diff = Integer.parseInt(difference);
                    int cnt = Integer.parseInt(count);


                    Log.d(TAG, "Count as JSON value "+count);
                       Log.d(TAG, "Difference as JSON value "+difference);
                    Log.d(TAG, "Request Time sent as parameter  "+requestTime);

                    if(requestTime==0 )
                    {
                        Log.d(TAG, "requestTime "+requestTime);

                    }
                    else if(requestTime!=Integer.parseInt(count) &&  requestTime!=0 &&diff>0){
                        Log.d(TAG, "Got Response and Change in the Database detected ");
                        //Code to set Action

                        JSONArray array = jsonObject.getJSONArray("listOfBulletin");
                        int len = array.length();

                        String division[] = new String[len];
                        String subdivision[] = new String[len];

                        for(int i=0;i<len;i++) {
                            JSONObject jo = array.getJSONObject(i);
                            division[i]=jo.getString("area_divison");
       subdivision[i]= jo.getString("area_subDivision");

                        }



                        //Code to set Action

                        //Code to send a notification on database change detection


                        //Build the notification
for(int not=0;not<len;not++) {
    notifications.setSmallIcon(R.mipmap.ic_launcher_round);
    notifications.setTicker("New ShutDowns Detected ");
    notifications.setWhen(System.currentTimeMillis());
    notifications.setContentTitle("Division :" + division[not]);
    notifications.setContentText("Sub Division " + subdivision[not]);
    Intent intent = new Intent(getApplicationContext(), SubDivisionBulletin.class);
    intent.putExtra("SubDivision",subdivision[not]);
    intent.putExtra("Division",division[not]);

    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    notifications.setContentIntent(pendingIntent);

    //Builds notification and issues it

    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    notificationManager.notify(uniqueId, notifications.build());
uniqueId++;
                        }


                        //
                    }
                    else
                    {
                        Log.d(TAG, "Got Response Aleast ");
                    }

                    Log.d(TAG, "requestTime "+requestTime);
                requestTime=cnt;
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                // Work according to the response





                //

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }

        );
        requestQueue.add(stringRequest_2);
    }

    class MyThread extends Thread{
        static final long DELAY = 3000;
        @Override
        public void run(){
            while(isRunning){
                Log.d(TAG," Deepto Running");
                //Toast.makeText(getApplicationContext(),"Response",Toast.LENGTH_SHORT).show();
                try {
                    readWebPage();
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

}