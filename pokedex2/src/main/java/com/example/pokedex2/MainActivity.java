package com.example.pokedex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private RequestQueue mQueue;
    public List<String> pokeName = new ArrayList<>();
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (RecyclerView)findViewById(R.id.recyclerView);
        mQueue = Volley.newRequestQueue(this);

        view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        view.setLayoutManager(layoutManager);

        jsonParser();
    }

    private void jsonParser(){

        String url = "https://pokeapi.co/api/v2/pokemon/?limit=151";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            JSONArray jsonArray = response.getJSONArray("results");
                            JSONObject form1;
                            for(int i=0;i<jsonArray.length();i++){
                                form1 = jsonArray.getJSONObject(i);
                                pokeName.add(form1.getString("name"));
                            }
                            Log.d("TAGAPI", "No problem reaching API: "+pokeName.size());
                        }catch(JSONException e){
                            Log.d("TAG", e.getMessage());
                            e.printStackTrace();
                        }

                        MyAdapter myAdapter = new MyAdapter(context, pokeName);
                        view.setAdapter(myAdapter);
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);


//                        Log.d("TAG", String.valueOf(adapter.isEmpty())+numbers.size());
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

}
