package com.study.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class SignUp extends AppCompatActivity {

    private Button btn_sign_up;
    private EditText EditTextPassword, EditTextPhone, EditTextName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btn_sign_up = findViewById(R.id.btn_sign_up);
        EditTextPassword = findViewById(R.id.EditTextPassword);
        EditTextPhone = findViewById(R.id.EditTextPhone);
        EditTextName = findViewById(R.id.EditTextName);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://food-delivery-app-61b40-default-rtdb.europe-west1.firebasedatabase.app/");
        final DatabaseReference table = database.getReference("User");

        btn_sign_up.setOnClickListener(view -> table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(EditTextPhone.getText().toString()).exists()) {
                    Toast.makeText(SignUp.this, "User already exist", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(EditTextName.getText().toString(), EditTextPassword.getText().toString());
                    table.child(EditTextPhone.getText().toString()).setValue(user);
                    Toast.makeText(SignUp.this, "Successfully registered", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        }));
    }
}