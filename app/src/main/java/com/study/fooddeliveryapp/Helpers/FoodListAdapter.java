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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.study.fooddeliveryapp.FoodDetail;
import com.study.fooddeliveryapp.Models.Category;
import com.study.fooddeliveryapp.R;

import java.util.ArrayList;
import java.util.List;

public class FoodListAdapter extends ArrayAdapter<Category> {

    private LayoutInflater layoutInflater;
    private List<Category> categories;
    private ArrayList<String> allKeys;
    private int layoutListRow;
    private Context context;

    public FoodListAdapter(@NonNull Context context, int resource, @NonNull List<Category> objects, ArrayList<String> allKeys) {
        super(context, resource, objects);

        categories = objects;
        layoutListRow = resource;
        this.context = context;
        this.allKeys = allKeys;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = layoutInflater.inflate(layoutListRow, null);

        Category category = categories.get(position);

        if (category != null) {
            final TextView foodName = convertView.findViewById(R.id.FoodMainName);
            ImageView photo = convertView.findViewById(R.id.mainPhoto);

            if (foodName != null)
                foodName.setText(category.getName());

            if (photo != null){
                int id = getContext().getResources().getIdentifier("drawable/" + category.getImage(), null, getContext().getPackageName());
                photo.setImageResource(id);

                photo.setOnClickListener(v -> {
                    FoodDetail.ID = Integer.parseInt(allKeys.get(position));
                    context.startActivity(new Intent(context, FoodDetail.class));
                });
            }
        }
        return convertView;
    }
}
