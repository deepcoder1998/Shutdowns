package com.example.newu.shutdowns;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

public class SubDivisions extends AppCompatActivity {
TextView tv;
String url_2 = "http://192.168.43.22/CESC/Area_Sub_division.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_divisions);
        tv =(TextView) findViewById(R.id.divisionss);


        Bundle bundle = getIntent().getExtras();
        String division = bundle.getString("Division");
        tv.setText(""+division);

        String i = extractStringV(division);
        load_SubDivision_Data(i);




    }
String extractStringV(String str)
    {
        String sendStr;
        sendStr=str.substring(11);
        return sendStr;
    }
void load_SubDivision_Data(String i){

    final RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String divisionNumber = i;

        String url_2_final = url_2+"?a="+divisionNumber;
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Loading Data from server");
    progressDialog.show();
    final StringRequest stringRequest_2 = new StringRequest(Request.Method.GET, url_2_final, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
        progressDialog.dismiss();
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("subDivisionList");
            int len = array.length();
            final String subDiv_list[] =new String[len];
            final String acc[] = new String[len];
            final String not_acc[] = new String[len];

            for(int i=0;i<array.length();i++){
            JSONObject jsonObject1= array.getJSONObject(i);
            acc[i]=jsonObject1.getString("count_not_null");
            not_acc[i]=jsonObject1.getString("count_null");
            subDiv_list[i] = "Sub Division : "+jsonObject1.getString("Sub_Division");
           }
           /* ListAdapter listAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, subDiv_list);
            ListView divListView = (ListView) findViewById(R.id.sub_division_list);
            divListView.setAdapter(listAdapter);
            requestQueue.stop();
*/


            CustomAdapter customAdapter = new CustomAdapter(subDiv_list,not_acc,acc);
            ListView listView =(ListView) findViewById(R.id.sub_division_list);
            listView.setAdapter(customAdapter);



/*            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

*/



           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String subdivision = String.valueOf(adapterView.getItemAtPosition(i));
                    //Toast.makeText(SubDivisions.this,subdivision, Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(SubDivisions.this,SubDivisionBulletin.class);
                    //subdivision=subdivision.substring(21);
                    intent.putExtra("SubDivision",subDiv_list[i].substring(15));
                    intent.putExtra("Division",divisionNumber);
                    startActivity(intent);


                }
            });

      }
        catch (JSONException e)
        {
            e.printStackTrace();
        }



        }

        }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"Error in connecting to server",Toast.LENGTH_LONG).show();
        requestQueue.stop();
        }
    });

//    RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest_2);
        }
    class CustomAdapter extends BaseAdapter {
        String[] division;
        String[] not_accomplished;
        String[] accomplished;

        public CustomAdapter(String[] d,String[] na,String[] ac) {
            int len =d.length;
            division= new String[len];
            not_accomplished = new String[len];
            accomplished = new String[len];
            for(int v =0;v<len;v++)
            {
                division[v]=d[v];
                not_accomplished[v]= na[v];
                accomplished[v]=ac[v];
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
            view = getLayoutInflater().inflate(R.layout.subdivision,null);
            TextView tv= (TextView) view.findViewById(R.id.subdivision_for_SubDivisions_activity);
            tv.setText(""+division[i]);
            TextView tv1 =(TextView) view.findViewById(R.id.not_acc);
            TextView tv2 =(TextView) view.findViewById(R.id.acc);
            tv1.setText(""+not_accomplished[i]);
            tv2.setText(""+accomplished[i]);
            return view;
        }
    }
}

