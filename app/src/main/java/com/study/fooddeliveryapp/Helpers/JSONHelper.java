package com.study.fooddeliveryapp.Helpers;

import android.content.Context;

import com.google.gson.Gson;
import com.study.fooddeliveryapp.Models.Cart;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JSONHelper {

    private static final String FILE_NAME = "shopping_cart.json";

    public static String createJSONString(List<Cart> dataList) {
        Gson gson = new Gson();
        DataItems dataItems = new DataItems();
        dataItems.setCartList(dataList);
        return gson.toJson(dataItems);
    }

    public static boolean exportToJSON(Context context, List<Cart> dataList) {
        String jsonString = createJSONString(dataList);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fileOutputStream.write(jsonString.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    public static List<Cart> importFromJSON(Context context) {
        InputStreamReader streamReader = null;
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            streamReader = new InputStreamReader(fileInputStream);

            Gson gson = new Gson();
            DataItems dataItems = gson.fromJson(streamReader, DataItems.class);
            return dataItems.getCartList();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
        if (streamReader != null) {
            try {
                streamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }



    private static class DataItems {
        private List<Cart> cartList;

        private List<Cart> getCartList() {
            return this.cartList;
        }

        public void setCartList(List<Cart> cartList) {
            this.cartList = cartList;
        }
    }

}
