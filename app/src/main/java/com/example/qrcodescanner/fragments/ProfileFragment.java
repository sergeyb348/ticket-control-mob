package com.example.qrcodescanner.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.qrcodescanner.R;
import com.example.qrcodescanner.activity.LoginActivity;
import com.example.qrcodescanner.retrofit.ApiManager;
import com.example.qrcodescanner.retrofit.Event;
import com.example.qrcodescanner.retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    SharedPreferences mSettings;
    public ProfileFragment() {
        // Required empty public constructor
    }

    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                getContext());
        quitDialog.setTitle(R.string.internet).setMessage(R.string.internet_message);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        quitDialog.show();
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        mSettings = getContext().getSharedPreferences("mysettings", 0);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,
                container, false);
        EditText nameBox = (EditText)view.findViewById(R.id.nameBox);
        mSettings = getContext().getSharedPreferences("mysettings", 0);
        String username = mSettings.getString("username","");
        nameBox.setText(username);

        Button button = (Button) view.findViewById(R.id.exit);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putBoolean("authorize", false);
                editor.apply();
                button.setEnabled(false);
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });
        return view;
    }







}