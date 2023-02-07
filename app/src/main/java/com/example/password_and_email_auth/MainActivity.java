package com.example.password_and_email_auth;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    com.example.password_and_email_auth.databinding.ActivityMainBinding binding;

    private FirebaseAuth firebaseAuth;
    FirebaseFirestore fstore;
    String userID;
    String name, email, password, confirmpassword;

    boolean passwordvisible;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = com.example.password_and_email_auth.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        sharedPreferences=getSharedPreferences("data",MODE_PRIVATE);


        //titlebarcolorstart code
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.titlebar));
        //title bar color code end

        binding.password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int Right=2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX() >= binding.password.getCompoundDrawables()[Right].getBounds().width()) {

                        int selection=binding.password.getSelectionEnd();
                        if (passwordvisible){
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible=false;

                        }
                        else {
                            binding.password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_remove_red_eye_24,0);
                            binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordvisible=true;

                        }

                    }
                }
                return false;
            }
        });

        binding.confirmpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int Right=2;
                if (motionEvent.getAction()==motionEvent.ACTION_UP){
                    if (motionEvent.getRawX() >= binding.confirmpassword.getCompoundDrawables()[Right].getBounds().width()) {

                        int selection=binding.password.getSelectionEnd();
                        if (passwordvisible){
                            binding.confirmpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_visibility_off_24,0);
                            binding.confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordvisible=false;

                        }
                        else {
                            binding.confirmpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_remove_red_eye_24,0);
                            binding.confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
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
                startActivity(new Intent(getApplicationContext(), signin.class));
            }
        });

        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                binding.progressBar.setVisibility(View.VISIBLE);
                name = binding.name.getText().toString();
                email = binding.email.getText().toString();
                password = binding.password.getText().toString();
                confirmpassword = binding.confirmpassword.getText().toString();

                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString("username",name);
                editor.putString("Email",email);
                editor.apply();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "pleas enter uname", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "pleas enter email", Toast.LENGTH_SHORT).show();


                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "pleas enter password", Toast.LENGTH_SHORT).show();

                    return;
                }

                if (password.length() < 8) {

                    Toast.makeText(getApplicationContext(), "pleas enter length", Toast.LENGTH_SHORT).show();
                }
                if (confirmpassword.length() < 8) {

                    Toast.makeText(getApplicationContext(), "pleas enter length", Toast.LENGTH_SHORT).show();
                }

                if (!password.equals(confirmpassword)) {
                    Toast.makeText(getApplicationContext(), "password and confirm password not equal", Toast.LENGTH_SHORT).show();

                } else {


                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (task.isSuccessful()) {

                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "registerd succefully", Toast.LENGTH_SHORT).show();
                                userID = firebaseAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("admin").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("email", email);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess : user profile is created for" + userID);

                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), signin.class));


                            } else {
                                binding.progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Authentication Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }
        });

    }
}