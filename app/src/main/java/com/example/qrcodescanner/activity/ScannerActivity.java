package com.example.qrcodescanner.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qrcodescanner.R;
import com.example.qrcodescanner.retrofit.ApiManager;
import com.example.qrcodescanner.retrofit.Message;
import com.example.qrcodescanner.retrofit.Ticket;
import com.example.qrcodescanner.retrofit.Token;
import com.google.zxing.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ScannerActivity extends AppCompatActivity {
    public String event_id;
    private CodeScanner mCodeScanner;
    SharedPreferences mSettings;
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    ProgressDialog dialog;
    private void openInternetDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle(R.string.internet).setMessage("Для проверки необходим интернет");

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
    public void openInvalidLoginDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
         //R.string.login_invalid R.string.login_message

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
    public void openTicketDialog(String ticketInfo) {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                this);
        quitDialog.setTitle("Проверка билета").setMessage(ticketInfo);

        quitDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Подождите...");
        mSettings = getSharedPreferences("mysettings", Context.MODE_PRIVATE);
        Ticket ticket = new Ticket();
        ticket.setEvent_id(mSettings.getString("EventId", ""));
        ticket.setName(mSettings.getString("EventName",""));
        ApiManager apiManager = ApiManager.getInstance();
        setContentView(R.layout.activity_scanner);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isNetworkAvailable(getApplicationContext())){
                            ticket.setQr(result.getText());
                            String token = mSettings.getString("token", "");
                            Log.d("ffffff", token);
                            apiManager.checkQr(ticket, token,new Callback<Message>(){
                                @Override
                                public void onResponse(Call<Message> call, Response<Message> response) {
                                    Message ms = response.body();
                                    Log.d("msmsmsms", ms.getMs());
                                    Log.d("ffffff", token);
                                    if (response.isSuccessful() /*&& ms != null*/) {
                                        Log.d("HttpCodeID","Result " + result.getText() + " name "+ ticket.getName());
                                        dialog.dismiss();
                                        if(ms.getStatus().equals("none"))
                                            openTicketDialog("Билета нет в базе данных");
                                        if(ms.getStatus().equals("activ"))
                                            openTicketDialog("Билет уже активирован, категория билета - " + ms.getMs());
                                        if(ms.getStatus().equals("inactiv"))
                                            openTicketDialog("Проверка успешно пройдена, категория билета - "+ ms.getMs());
                                        if(ms.getStatus().equals("time_w"))
                                            openTicketDialog("Мероприятие не началось");
                                        if(ms.getStatus().equals("time_e"))
                                            openTicketDialog("Мероприятие закончилось");
                                    } else {
                                        dialog.dismiss();
                                        openInvalidLoginDialog();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Message> call, Throwable t) {
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
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}