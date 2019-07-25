package com.example.pokedex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private Context context = this;
    private TextView textView1;
    private RequestQueue mQueue;
    ListView listView;

    public List<Integer> numbers = new ArrayList<>();
    public List<String> names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        textView1 = findViewById(R.id.textView1);
        mQueue = Volley.newRequestQueue(this);

        jsonParser();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);

//                Toast.makeText(MainActivity.this, String.valueOf(item), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getBaseContext(), InfoLayout.class);
                intent.putExtra("POKEMON", item);
                startActivity(intent);
            }

        });
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
                                numbers.add(i+1);
                                names.add(form1.getString("name"));
                            }
                            Log.d("TAG", "No problem reaching API: "+numbers.size()+" "+names.size());
                        }catch(JSONException e){
                            Log.d("TAG", e.getMessage());
                            e.printStackTrace();
                        }

                        MyAdapter adapter = new MyAdapter(context, numbers, names);
                        listView.setAdapter(adapter);
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

    public class MyAdapter extends ArrayAdapter {
        Context context;
        List<String> rName;
        List<Integer> rNumber;

        MyAdapter (Context c, List<Integer> number, List<String> name) {
            super(c, R.layout.row, R.id.textView1, name);
//            super(c, R.layout.row, R.id.textView1, name);
            this.context = c;
            this.rNumber = number;
            this.rName = name;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView pokeNumber = row.findViewById(R.id.textView1);
            TextView pokeName = row.findViewById(R.id.textView2);
            ImageView images = row.findViewById(R.id.image);

            // now set our resources on views
            Picasso.with(context)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + rNumber.get(position) + ".png")
                    .resize(90, 90)
                    .into(images);
            pokeNumber.append(String.valueOf(rNumber.get(position)));
            pokeName.setText(rName.get(position));

            return row;
        }
    }


}
