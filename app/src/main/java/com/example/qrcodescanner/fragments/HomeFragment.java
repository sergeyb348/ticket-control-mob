package com.example.qrcodescanner.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.qrcodescanner.adapters.EventAdapter;
import com.example.qrcodescanner.databinding.FragmentHomeBinding;
import com.example.qrcodescanner.db.DbHelper;
import com.example.qrcodescanner.retrofit.ApiManager;
import com.example.qrcodescanner.retrofit.Event;
import com.example.qrcodescanner.retrofit.EventList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    DbHelper databaseHelper;
    SQLiteDatabase db;
    public int i = 0;
    FragmentHomeBinding binding;
    SharedPreferences mSettings;



    public HomeFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DbHelper(getActivity().getApplicationContext());
        db = databaseHelper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ApiManager apiManager = ApiManager.getInstance();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mSettings =  getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        i = mSettings.getInt("levels",5);
        String token = mSettings.getString("token", "");
        ArrayList<Event> categories = new ArrayList<Event>();
        apiManager.getEvents(token, new Callback<EventList>() {
            @Override
            public void onResponse(Call<EventList> call, Response<EventList> response) {
                EventList eventList = response.body();
                Log.d("eventList", eventList.toString());

                final EventAdapter adapter = new EventAdapter(getContext(),eventList.getEvents());
                adapter.notifyDataSetChanged();

                binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
                binding.categoryList.setAdapter(adapter);

                if (response.isSuccessful() && eventList.getEvents() != null){
                    Log.d("eventList", eventList.toString());
                }
            }

            @Override
            public void onFailure(Call<EventList> call, Throwable t) {

            }
        });








        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}