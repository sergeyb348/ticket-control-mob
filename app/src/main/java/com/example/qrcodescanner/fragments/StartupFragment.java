package com.example.qrcodescanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.qrcodescanner.R;
import com.example.qrcodescanner.activity.ScannerActivity;
import com.example.qrcodescanner.databinding.FragmentStartupBinding;


public class StartupFragment extends Fragment {

    public StartupFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentStartupBinding binding;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startup,
                container, false);

        binding = FragmentStartupBinding.inflate(inflater, container, false);
        /*Button buttonChangePassword = (Button) view.findViewById(R.id.event);
        buttonChangePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                startActivity(new Intent(getContext(), ScannerActivity.class));

            }

        });*/
        // Inflate the layout for this fragment
        return view;
    }


}