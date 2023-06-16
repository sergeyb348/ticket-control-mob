package com.example.qrcodescanner.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.qrcodescanner.R;
import com.example.qrcodescanner.databinding.ActivityMainBinding;
import com.example.qrcodescanner.db.DbHelper;
import com.example.qrcodescanner.fragments.HomeFragment;
import com.example.qrcodescanner.fragments.ProfileFragment;
import com.example.qrcodescanner.fragments.StartupFragment;
import com.example.qrcodescanner.retrofit.ApiManager;
import com.example.qrcodescanner.retrofit.Event;
import com.example.qrcodescanner.retrofit.EventList;
import com.example.qrcodescanner.retrofit.User;

import java.util.List;

import me.ibrahimsn.lib.OnItemSelectedListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DbHelper databaseHelper;
    SQLiteDatabase db;
    SharedPreferences mSettings;
    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
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
    protected void onCreate(Bundle savedInstanceState) {
        mSettings = getSharedPreferences("mysettings", 0);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String login = mSettings.getString("username","");
        User user = new User();
        user.setEmail(login);
        databaseHelper = new DbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        databaseHelper.onCreate(db);
        setSupportActionBar(binding.toolbar);
        ApiManager apiManager = ApiManager.getInstance();

        if(isNetworkAvailable(this.getApplicationContext())){
            mSettings = getSharedPreferences("mysettings", 0);
            String token = mSettings.getString("token", "");
            apiManager.getEvents(token, new Callback<EventList>(){
                @Override
                public void onResponse(Call<EventList> call, Response<EventList> response) {
                    if (!response.isSuccessful()) {

                        Log.d("HttpCodeID", "Code: " + response.code());
                        Log.d("HttpCodeID", "Login: " + user.getEmail());
                        return;
                    }

                    List<Event> categories = response.body().getEvents();
                  /*  Log.d("HttpCodeID", "categories: " + categories.getEvents());*/
                    if(categories!=null){
                        databaseHelper.deleteAllEvents();
                        databaseHelper.addEvents(categories);
                        Log.d("HttpCodeID", "Обновились events");


                    }

                }

                @Override
                public void onFailure(Call<EventList> call, Throwable t) {
                    
                    Log.d("HttpCodeID","Error is " + t.getMessage());
                   
                }
            });}

        else{

            openInternetDialog();}



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, new StartupFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.content, new StartupFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.content, new HomeFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.content, new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}