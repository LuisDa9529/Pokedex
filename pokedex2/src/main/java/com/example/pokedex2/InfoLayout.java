package com.example.pokedex2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InfoLayout extends AppCompatActivity {

    Context context;
    int pokeIndex;
    String pokeName;
    String pokeGenera;
    List<String> pokeTypes = new ArrayList<>();
    int pokeHeight;
    int pokeWeight;
    String pokeDesc;

    private RequestQueue mQueue;
    private RequestQueue mQueue2;
    private String url;
    private String url2;

    ImageView imageView;
    TextView nameView;
    TextView generaView;
    TextView typeView;
    TextView heightView;
    TextView weightView;
    TextView descView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_layout);

        imageView = findViewById(R.id.imagePoke);
        nameView = findViewById(R.id.nameText);
        generaView = findViewById(R.id.generaText);
        typeView = findViewById(R.id.typeText);
        heightView = findViewById(R.id.heightText);
        weightView = findViewById(R.id.weightText);
        descView = findViewById(R.id.descriptionText);

        pokeName = getIntent().getStringExtra("POKEMON");

        url = "https://pokeapi.co/api/v2/pokemon/"+pokeName;
        url2 = "https://pokeapi.co/api/v2/pokemon-species/"+pokeName;
        mQueue = Volley.newRequestQueue(this);
        mQueue2 = Volley.newRequestQueue(this);

        jsonParser(url, url2);



    }

    private void jsonParser(String url, String url2){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            pokeIndex = response.getInt("id");
                            pokeHeight = response.getInt("height");
                            pokeWeight = response.getInt("weight");
                            JSONArray array = response.getJSONArray("types");
                            JSONObject obj, type;
                            for (int i=0;i<array.length();i++){
                                obj = array.getJSONObject(i);
                                type = obj.getJSONObject("type");
                                pokeTypes.add(type.getString("name"));
                            }

                            Log.d("TAG", "No problem reaching API: "+pokeIndex+" "+pokeHeight+" "+pokeWeight);
                        }catch(JSONException e){
                            Log.d("TAG", e.getMessage());
                            e.printStackTrace();
                        }
                        Picasso.with(context)
                                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+pokeIndex+".png")
                                .resize(90,90)
                                .into(imageView);
                        nameView.setText("# "+pokeIndex+" "+pokeName);
                        heightView.append(String.valueOf(pokeHeight));
                        weightView.append(String.valueOf(pokeWeight));
                        for (int i=0;i<pokeTypes.size();i++){
                            typeView.append("  ");
                            typeView.append(pokeTypes.get(i));
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }
        });

        JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        try{
                            JSONArray array = response.getJSONArray("genera");
                            JSONObject form = array.getJSONObject(2);
                            pokeGenera = form.getString("genus");
                            array = response.getJSONArray("flavor_text_entries");
                            String txt;
                            for(int i=0;i<array.length();i++) {
                                form = array.getJSONObject(i);
                                JSONObject obj = form.getJSONObject("language");
                                txt = obj.getString("name");
                                if(txt.contentEquals("en")){
                                       pokeDesc = form.getString("flavor_text");
                                        Log.d("TAGDES", pokeDesc);
                                       break;
                                }
                            }

                        }catch(JSONException e){
                            Log.d("TAG", e.getMessage());
                            e.printStackTrace();
                        }
                        generaView.setText(pokeGenera);
                        descView.setText(pokeDesc);
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        mQueue2.add(request2);


    }
}
