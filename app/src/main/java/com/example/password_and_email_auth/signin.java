package com.example.password_and_email_auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.password_and_email_auth.databinding.ActivitySigninBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signin extends AppCompatActivity {

    ActivitySigninBinding binding;


    private FirebaseAuth firebaseAuth;
    String email,password;
    boolean passwordvisible;


    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //titlebarcolorstart code
        getWindow().setStatusBarColor(ContextCompat.getColor(signin.this, R.color.titlebar));
        //title bar color code end




        firebaseAuth=FirebaseAuth.getInstance();

/*
        SharedPreferences sharedPreferences = getSharedPreferences("PREFRENCE", MODE_PRIVATE);
        String Firsttime=sharedPreferences.getString("FirstTimeInstall","");



        if (Firsttime.equals("Yes")){
            startActivity(new Intent(signin.this,dashboard.class));

        }
        else {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();


        }*/








        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(signin.this,MainActivity.class));

            }
        });




        binding.edPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int Right=2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX() >= binding.edPassword.getCompoundDrawables()[Right].getBounds().width()) {

                        int selection=binding.edPassword.getSelectionEnd();
                        if (passwordvisible){
                            binding.edPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            binding.edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible=false;

                        }
                        else {
                            binding.edPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_remove_red_eye_24,0);
                            binding.edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible=true;

                        }

                    }
                }
                return false;
            }
        });








        binding.signin.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View view) {



                binding.progressBar.setVisibility(View.VISIBLE);
                email = binding.edEmail.getText().toString().trim();
                password = binding.edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signin.this, "please Enter Email", Toast.LENGTH_SHORT).show();                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signin.this, "Enter PAssword", Toast.LENGTH_SHORT).show();                    return;
                }

                if (password.length() < 8) {


                }


                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(signin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(signin.this, "succefuly signin", Toast.LENGTH_SHORT).show();


                            SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putBoolean("flag",true);
                            editor.apply();

                            startActivity(new Intent(getApplicationContext(), dashboard.class));



                        } else {
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(signin.this, "error !!!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                });
            }
        });

        binding.edForgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                EditText resetmail=new EditText(view.getContext());
                AlertDialog.Builder passwordresetdailog=new AlertDialog.Builder(view.getContext());
                passwordresetdailog.setTitle("Reset Password");
                passwordresetdailog.setMessage("Enter your Email To Received Reset Link");
                passwordresetdailog.setView(resetmail);

                passwordresetdailog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //extract the email and send reset link
                        String mail=resetmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(signin.this,"Reset link sent to your Email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(signin.this,"Error ! Reset Link is not sent"+e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

                passwordresetdailog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //close the Dailog
                    }
                });

                passwordresetdailog.create().show();
            }
        });    }

    public void onBackPressed() {
        AlertDialog.Builder alertdailog = new AlertDialog.Builder(
                signin.this);
        alertdailog.setTitle("Exit App");
        alertdailog.setMessage("Do You Want To Exit App").setCancelable(false);
        alertdailog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finishAffinity();

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
}