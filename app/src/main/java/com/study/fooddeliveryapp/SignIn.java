package com.study.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.study.fooddeliveryapp.Models.User;

public class SignIn extends AppCompatActivity {

    private Button btn_sign_in;
    private EditText EditTextPassword, EditTextPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        EditTextPassword = findViewById(R.id.EditTextPassword);
        EditTextPhone = findViewById(R.id.EditTextPhone);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://food-delivery-app-61b40-default-rtdb.europe-west1.firebasedatabase.app/");
        final DatabaseReference table = database.getReference("User");

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(EditTextPhone.getText().toString()).exists()) {
                            User user = snapshot.child(EditTextPhone.getText().toString()).getValue(User.class);
                            if(user.getPassword().equals(EditTextPassword.getText().toString())) {
                                setDefaults("phone", EditTextPhone.getText().toString(), SignIn.this);
                                setDefaults("name", user.getName(), SignIn.this);

                                Toast.makeText(SignIn.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignIn.this, FoodPage.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignIn.this, "Log in failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignIn.this, "There's no such user", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SignIn.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }


}