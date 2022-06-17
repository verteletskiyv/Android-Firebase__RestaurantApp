package com.study.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.study.fooddeliveryapp.Helpers.JSONHelper;
import com.study.fooddeliveryapp.Models.Cart;
import com.study.fooddeliveryapp.Models.Category;
import com.study.fooddeliveryapp.Models.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDetail extends AppCompatActivity {

    public static int ID = 0;

    private ImageView mainPhoto;
    private TextView FoodMainName, price, FoodFullName;
    private Button btnGoToCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mainPhoto = findViewById(R.id.mainPhoto);
        FoodMainName = findViewById(R.id.FoodMainName);
        price = findViewById(R.id.price);
        FoodFullName = findViewById(R.id.FoodFullName);
        btnGoToCart = findViewById(R.id.btn_go_to_cart);

        btnGoToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodDetail.this, CartActivity.class));
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance("https://food-delivery-app-61b40-default-rtdb.europe-west1.firebasedatabase.app/");
        final DatabaseReference table = database.getReference("Category");

        table.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Category category = snapshot.getValue(Category.class);

                FoodMainName.setText(category.getName());

                int id = getApplicationContext().getResources().getIdentifier("drawable/"
                        + category.getImage(), null, getApplicationContext().getPackageName());
                mainPhoto.setImageResource(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDetail.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });


        final DatabaseReference table_food = database.getReference("Food");

        table_food.child(String.valueOf(ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food_item = snapshot.getValue(Food.class);

                FoodFullName.setText(food_item.getFull_text());
                price.setText(food_item.getPrice() + " $");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FoodDetail.this, "No internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void btnAddToCart(View view) {
        List<Cart> cartList = JSONHelper.importFromJSON(this);
        if (cartList == null) {
            cartList = new ArrayList<>();
            cartList.add(new Cart(ID, 1));
        } else {
            boolean isFound = false;
            for (Cart el: cartList) {
                if (el.getCategoryID() == ID) {
                    el.setAmount(el.getAmount() + 1);
                    isFound = true;
                }
            }

            if (!isFound)
                cartList.add(new Cart(ID, 1));
        }

        boolean result = JSONHelper.exportToJSON(this, cartList);
        if (result) {
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
            Button btnCart = (Button) view;
            btnCart.setText("Added");
        } else {
            Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
        }
    }


}