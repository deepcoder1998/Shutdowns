package com.example.newu.shutdowns;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebHistoryItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;

public class SubDivisionBulletin extends AppCompatActivity {

String url_3="http://192.168.43.22/CESC/Sub_Division_Bulletin.php";//?a=1&b=10";
   // @ColorInt public static final int CESCRED=;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_division_bulletin);
        Bundle bundle = getIntent().getExtras();
        String Subdivision = bundle.getString("SubDivision");
        String Division = bundle.getString("Division");
        Toast.makeText(getApplicationContext(),"Division"+Division+"SubDivision"+Subdivision,Toast.LENGTH_LONG).show();
        loadApiData(Division,Subdivision);

    }
    public void loadApiData(String d,String sd){

        String Division = d;
        String SubDivision = sd;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading data from Server");
        progressDialog.show();

        final StringRequest stringRequest= new StringRequest(Request.Method.GET,"http://192.168.43.22/CESC/Sub_Division_Bulletin.php?a="+Division+"&b="+SubDivision, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progressDialog.dismiss();

                    JSONObject jsonObject = new JSONObject(response);
                    String div=jsonObject.getString("Division");
                    String subDiv =jsonObject.getString("SubDivision");
                    JSONArray array = jsonObject.getJSONArray("listOfBulletin");

                    int len = array.length();

                    String Bull_Head[] = new String[len];

                    String details[] = new String[len];
                    String reason[] = new String[len];
                    String startTime[] = new String[len];
                    String endTime[] = new String[len];
                    String actionTime[]= new String[len];

                    for(int i=0;i<len;i++) {
                        JSONObject jo = array.getJSONObject(i);
                        Bull_Head[i]=jo.getString("BulletinHead");
                        details[i]=jo.getString("Bulletin_Details");
                        reason[i]=jo.getString("reason");
                        startTime[i]= jo.getString("start_time");
                        endTime[i]=jo.getString("end_time");
                        actionTime[i]= jo.getString("action_time");

//    Toast.makeText(getApplicationContext(),"Bull_head : "+Bull_Head[i]+"\n Detalis \n"+details[i],Toast.LENGTH_LONG).show();
                            }


                    CustomAdapter customAdapter = new CustomAdapter(div,subDiv,Bull_Head,details,reason,startTime,endTime,actionTime);
                    ListView listView =(ListView) findViewById(R.id.list_item_bulletin);
                    listView.setAdapter(customAdapter);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
error.printStackTrace();
Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();

            }
        });

        requestQueue.add(stringRequest);
    }

    class CustomAdapter extends BaseAdapter{
        String division;
        String subDivision;
        String[] bull_head;
        String[] details;
        String[] reason;
        String[] startTime;
        String[] endTime;
        String[] actionTime;

        public CustomAdapter(String d,String sd,String[] f,String[] i,String[] r,String[] s,String[] e,String[] a) {
            int len =i.length;
            bull_head= new String[len];
            details= new String[len];
            reason = new String[len];
            startTime = new String[len];
            endTime = new String[len];
            actionTime = new String[len];


            // Initializing the member variables
            for(int v =0;v<len;v++)
            {
                bull_head[v]=f[v];
                details[v]=i[v];
                reason[v]=r[v];
                startTime[v]=s[v];
           if(e[v].equals("null"))
           {
               endTime[v]="NO ACTION TAKEN";
          }
              else
           {
               endTime[v]= e[v];
           }

                actionTime[v]=a[v];


                }

            division=d;
            subDivision=sd;


        }

        @Override
        public int getCount() {
            // Here we need to return the size of the resource array
            return bull_head.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.bulletin,null);


            TextView tv= (TextView) view.findViewById(R.id.bulletin_heading);
            TextView tv2=(TextView) view.findViewById(R.id.bulletin_details);
            TextView tv3=(TextView)view.findViewById(R.id.bulletin_reason);
            TextView tv4 =(TextView) view.findViewById(R.id.bulletin_start_time);
            TextView tv5 = (TextView) view.findViewById(R.id.bulletin_end_time);
            TextView tv6 =(TextView) view.findViewById(R.id.bulletin_action_time);
            TextView tv7 =(TextView) view.findViewById(R.id.bulletin_division);
            TextView tv8 =(TextView) view.findViewById(R.id.bulletin_subdivision);


            tv.setText(bull_head[i]);
            tv2.setText(""+details[i]);
            tv3.setText(reason[i]);
            tv4.setText(startTime[i]);
            if(endTime[i].equals("NO ACTION TAKEN"))
            {
               tv5.setText(endTime[i]);
               tv5.setTextColor(WHITE);
               tv5.setBackgroundColor(RED);
            }
            else
            {
                tv5.setText(endTime[i]);
            }


            tv6.setText(actionTime[i]);
            tv7.setText(division);
            tv8.setText(subDivision);
            return view;
        }
    }
}
