package com.example.newu.shutdowns;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
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
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    String url_data ="http://192.168.43.22/CESC/areadivision.php";
    NotificationCompat.Builder notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/        setContentView(R.layout.activity_main);
        // fetching of data from the server

loadRecyclerViewData();
//        notifications = new NotificationCompat.Builder(this);
//        notifications.setAutoCancel(true);


       startService(new Intent(getBaseContext(),MyService.class));


//





      /*  String[] Divisions = {"1","2","3","4","5","6","7"};
        ListAdapter divAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Divisions);
        ListView divListView = (ListView) findViewById(R.id.list_item);
        divListView.setAdapter(divAdapter);

        divListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String division = String.valueOf(adapterView.getItemAtPosition(i));
                Toast.makeText(MainActivity.this,division,Toast.LENGTH_SHORT).show();

            }
        });
    */
    }



    private void loadRecyclerViewData() {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data....");
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("DivisionList");
                    int len = array.length();

                    final String divList[] = new String[len];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject1 = array.getJSONObject(i);
                        String div = jsonObject1.getString("Division");
                        divList[i] ="Division : "+div;
                    }

                    CustomAdapter customAdapter = new CustomAdapter(divList);
                    ListView listView =(ListView) findViewById(R.id.list_item);
                    listView.setAdapter(customAdapter);



                   listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            TextView tv =(TextView)findViewById(R.id.division_for_main_activity);
                          //  String division = String.valueOf(adapterView.getItemAtPosition(i).toString());
//                            Toast.makeText(MainActivity.this, division, Toast.LENGTH_SHORT).show();

                            //String division=tv.getText().toString();

                            Intent intent= new Intent(MainActivity.this,SubDivisions.class);
                            intent.putExtra("Division",divList[i]);
                            startActivity(intent);


                        }
                    });



                }

                catch (JSONException e){
                    e.printStackTrace();


                }



                requestQueue.stop();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
            requestQueue.stop();
            }
        });

        requestQueue.add(stringRequest);

    }

        class CustomAdapter extends BaseAdapter {
                    String[] division;

            public CustomAdapter(String[] d) {
                int len =d.length;
                division= new String[len];

                for(int v =0;v<len;v++)
                {
                    division[v]=d[v];
                    }

            }

            @Override
            public int getCount() {
                // Here we need to return the size of the resource array
                return division.length;
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
                view = getLayoutInflater().inflate(R.layout.divisions,null);
                TextView tv= (TextView) view.findViewById(R.id.division_for_main_activity);
                tv.setText(""+division[i]);
                return view;
            }
        }
}


