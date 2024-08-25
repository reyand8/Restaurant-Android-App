package com.example.ecommerce_android_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecommerce_android_app.R;
import com.example.ecommerce_android_app.models.RecommendedModel;
import com.example.ecommerce_android_app.models.ViewAllModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {
    int totalQuantity = 1;
    int totalPrice = 0;
    TextView quantity;
    ImageView detailedImg;
    TextView price,rating,description;
    ImageView addItem,removeItem;
    Toolbar toolbar;

    Button addToCart;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    ViewAllModel viewAllModel = null;
    RecommendedModel recommendedModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel = (ViewAllModel) object;
        }
        if (object instanceof RecommendedModel){
            recommendedModel = (RecommendedModel) object;
        }
        quantity = findViewById(R.id.quantity);
        detailedImg = findViewById(R.id.detailedImg);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);

        price = findViewById(R.id.detailed_price);
        rating = findViewById(R.id.detailed_rating);
        description = findViewById(R.id.detailed_dec);

        if (viewAllModel != null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            rating.setText(viewAllModel.getRating());
            description.setText(viewAllModel.getDescription());
            price.setText("Price: $"+viewAllModel.getPrice());
            totalPrice = Integer.parseInt(viewAllModel.getPrice()) * totalQuantity;
        }
        if (recommendedModel != null){
            Glide.with(getApplicationContext()).load(recommendedModel.getImg_url()).into(detailedImg);
            rating.setText(recommendedModel.getRating());
            description.setText(recommendedModel.getDescription());
            price.setText("Price: $"+recommendedModel.getPrice());
            totalPrice = Integer.parseInt(recommendedModel.getPrice()) * totalQuantity;
        }

        addToCart = findViewById(R.id.add_to_cart);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addedToCart();
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity < 20){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    if (viewAllModel != null) {
                        totalPrice =
                                Integer.parseInt(viewAllModel.getPrice()) * totalQuantity;
                    } else if (recommendedModel != null) {
                        totalPrice =
                                Integer.parseInt(recommendedModel.getPrice()) * totalQuantity;
                    }
                }
            }
        });

        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalQuantity > 1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                    if (viewAllModel != null) {
                        totalPrice = Integer.parseInt(viewAllModel.getPrice()) * totalQuantity;
                    } else if (recommendedModel != null) {
                        totalPrice = Integer.parseInt(recommendedModel.getPrice()) * totalQuantity;
                    }
                }
            }
        });
    }

    private void addedToCart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        if (viewAllModel != null) {
            cartMap.put("productName",viewAllModel.getName());
        } else if (recommendedModel != null) {
            cartMap.put("productName",recommendedModel.getName());
        }
        cartMap.put("productPrice",price.getText());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}