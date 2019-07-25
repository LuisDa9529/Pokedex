package com.example.pokedex2;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> mNames;
    private Context context;

    public MyAdapter(Context c, List<String>  names) {
        this.mNames = names;
        this.context = c;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public ImageView imageView;
        private LinearLayout linearLayout;

        public ViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.nameView);
            imageView = v.findViewById(R.id.imageView);
            linearLayout = v.findViewById(R.id.row);

            linearLayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            String name = String.valueOf(textView.getText());
            Log.d("TAG", name);

            Intent intent = new Intent(view.getContext(), InfoLayout.class);
            intent.putExtra("POKEMON", name);
            view.getContext().startActivity(intent);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mNames.get(position));
        Picasso.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + (position+1) + ".png")
                .resize(90, 90)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }




}
