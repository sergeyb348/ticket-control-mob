package com.example.qrcodescanner.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qrcodescanner.R;
import com.example.qrcodescanner.activity.ScannerActivity;
import com.example.qrcodescanner.retrofit.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Event> eventModels;
    SharedPreferences mSettings;

    public EventAdapter(Context context, ArrayList<Event> eventModels) {
        this.context = context;
        this.eventModels = eventModels;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category,null);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final Event model = eventModels.get(position);

        mSettings = context.getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        holder.event.setText(model.getEvent_name());
        holder.dateStart.setText("Начало - "+ model.getEvent_start()); //.substring(0,16)
        holder.dateEnd.setText("Конец - "+ model.getEvent_end()); //.substring(0,16)
        Glide.with(context)
                .load(R.drawable.event)
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScannerActivity.class);
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString("EventName", model.getEvent_name());
                editor.putString("EventId", model.getEvent_id());
                editor.apply();
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return eventModels.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView event;
        TextView dateStart;
        TextView dateEnd;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            event = itemView.findViewById(R.id.category);
            dateStart = itemView.findViewById(R.id.timeStart);
            dateEnd = itemView.findViewById(R.id.timeEnd);
        }
    }
}
