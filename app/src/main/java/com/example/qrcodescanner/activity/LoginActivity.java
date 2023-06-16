package com.example.qrcodescanner.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodescanner.R;
import com.example.qrcodescanner.databinding.ActivityLoginBinding;
import com.example.qrcodescanner.db.DbHelper;
import com.example.qrcodescanner.retrofit.ApiManager;
import com.example.qrcodescanner.retrofit.Token;
import com.example.qrcodescanner.retrofit.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LoginActivity extends AppCompatActivity {
    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.internet).setMessage(R.string.internet_message_login);
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
    public void openInvalidLoginDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.login_invalid).setMessage(R.string.login_message);
        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
    ActivityLoginBinding binding;
    ProgressDialog dialog;
    DbHelper databaseHelper;
    SQLiteDatabase db;
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DbHelper(getApplicationContext());
        db = databaseHelper.getWritableDatabase();
        mSettings = getSharedPreferences("mysettings", 0);
        boolean authorize = mSettings.getBoolean("authorize", false);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Подождите...");
        ApiManager apiManager = ApiManager.getInstance();
        if(authorize) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
                String login, pass;
                login = binding.loginBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                User user = new User();
                user.setEmail(login);
                user.setPassword(pass);
                if(isNetworkAvailable(getApplicationContext())){
                    apiManager.loginUser(user, new Callback<Token>(){
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            Token token = response.body();
                            if (response.isSuccessful() && token.getToken() != null) {
                                dialog.dismiss();
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putBoolean("authorize", true);
                                editor.putString("username", user.getEmail());
                                editor.putString("token", token.getToken());
                                editor.apply();
                                finish();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                dialog.dismiss();
                                openInvalidLoginDialog();
                            }
                        }
                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            openInvalidLoginDialog();
                            Log.d("HttpCodeID","Error is " + t.getMessage());
                            dialog.dismiss();
                        }
                    });}
                else{
                    dialog.dismiss();
                    openInternetDialog();}
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}