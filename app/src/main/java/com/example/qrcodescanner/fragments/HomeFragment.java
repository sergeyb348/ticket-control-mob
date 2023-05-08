package com.example.qrcodescanner.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.qrcodescanner.adapters.EventAdapter;
import com.example.qrcodescanner.databinding.FragmentHomeBinding;
import com.example.qrcodescanner.db.DbHelper;
import com.example.qrcodescanner.retrofit.Event;

import java.util.ArrayList;


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

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        mSettings =  getActivity().getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        i = mSettings.getInt("levels",5);

        ArrayList<Event> categories = new ArrayList<>();
        final EventAdapter adapter = new EventAdapter(getContext(), categories);
        for (Event cat : databaseHelper.getAllEvents()) {

            categories.add(cat);
        }


        adapter.notifyDataSetChanged();

        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
        binding.categoryList.setAdapter(adapter);





        // Inflate the layout for this fragment
        return binding.getRoot();
    }
}