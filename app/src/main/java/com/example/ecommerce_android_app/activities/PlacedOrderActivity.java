package com.example.ecommerce_android_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.ecommerce_android_app.R;
import com.example.ecommerce_android_app.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlacedOrderActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        List<CartModel> list = (ArrayList<CartModel>)
                getIntent().getSerializableExtra("itemList");

        if (list != null && list.size() > 0){
            for (CartModel model : list){
                final HashMap<String,Object> cartMap = new HashMap<>();
                cartMap.put("productName",model.getProductName());
                cartMap.put("productPrice",model.getProductPrice());
                cartMap.put("currentDate",model.getCurrentDate());
                cartMap.put("currentTime",model.getCurrentTime());
                cartMap.put("totalQuantity",model.getTotalQuantity());
                cartMap.put("totalPrice",model.getTotalPrice());

                firestore.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("MyOrder").add(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                Toast.makeText(PlacedOrderActivity.this,
                                        "Your Order Has Been Placed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }
}