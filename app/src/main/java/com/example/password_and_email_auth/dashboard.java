package com.example.password_and_email_auth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.password_and_email_auth.databinding.ActivityDashboardBinding;

public class dashboard extends AppCompatActivity {
    ActivityDashboardBinding binding;
    SharedPreferences Preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertdailog = new AlertDialog.Builder(
                        dashboard.this);
                alertdailog.setTitle("LogOut");
                alertdailog.setMessage("Are You Sure Logout Application").setCancelable(false);
                alertdailog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putBoolean("flag",false);
                        editor.apply();
                        startActivity(new Intent(dashboard.this,signin.class));

                      /*  SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        finish();*/

                    }
                });
                alertdailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                alertdailog.show();


            }
        });


    }
}