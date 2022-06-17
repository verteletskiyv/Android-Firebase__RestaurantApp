package com.study.fooddeliveryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.study.fooddeliveryapp.Helpers.CartItemsAdapter;
import com.study.fooddeliveryapp.Helpers.JSONHelper;
import com.study.fooddeliveryapp.Models.Cart;
import com.study.fooddeliveryapp.Models.Order;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private Button btnMakeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnMakeOrder = findViewById(R.id.btnMakeOrder);
        listView = findViewById(R.id.shoppingCart);
        List<Cart> cartList = JSONHelper.importFromJSON(this);

        if (cartList != null) {
            CartItemsAdapter adapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
            listView.setAdapter(adapter);

            Toast.makeText(this, "Data has been restored", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://food-delivery-app-61b40-default-rtdb.europe-west1.firebasedatabase.app/");
        final DatabaseReference category_table = database.getReference("Order");


        btnMakeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category_table.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Cart> cartList = JSONHelper.importFromJSON(CartActivity.this);
                        if (cartList == null) {
                            Toast.makeText(CartActivity.this, "Failed to form a cart", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String activeUser = SignIn.getDefaults("phone", CartActivity.this);
                        Order order = new Order(JSONHelper.createJSONString(cartList), activeUser);

                        Long tsLong = System.currentTimeMillis() / 1000;

                        category_table.child(tsLong.toString()).setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                List<Cart> cartList = new ArrayList<>();
                                JSONHelper.exportToJSON(CartActivity.this, cartList);

                                CartItemsAdapter adapter = new CartItemsAdapter(CartActivity.this, R.layout.cart_item, cartList);
                                listView.setAdapter(adapter);

                                Toast.makeText(CartActivity.this, "The order has been confirmed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
}