package com.study.fooddeliveryapp.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.study.fooddeliveryapp.FoodDetail;
import com.study.fooddeliveryapp.Models.Cart;
import com.study.fooddeliveryapp.Models.Category;
import com.study.fooddeliveryapp.R;

import java.util.List;

public class CartItemsAdapter extends ArrayAdapter<Cart> {
    private LayoutInflater layoutInflater;
    private List<Cart> cartList;
    private int layoutListRow;



    public CartItemsAdapter(@NonNull Context context, int resource, @NonNull List<Cart> objects) {
        super(context, resource, objects);

        cartList = objects;
        layoutListRow = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(layoutListRow, null);

        Cart cart = cartList.get(position);

        if (cart != null) {
            final TextView productName = convertView.findViewById(R.id.productName);
            TextView amount = convertView.findViewById(R.id.amount);

            if (amount != null)
                amount.setText(String.valueOf(cart.getAmount()));

            if (productName != null) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://food-delivery-app-61b40-default-rtdb.europe-west1.firebasedatabase.app/");
                final DatabaseReference category_table = database.getReference("Category");

                category_table.child(String.valueOf(cart.getCategoryID())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Category category = snapshot.getValue(Category.class);
                        productName.setText(category.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
        return convertView;
    }
}
