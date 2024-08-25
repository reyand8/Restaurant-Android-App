package com.example.ecommerce_android_app.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ecommerce_android_app.R;
import com.example.ecommerce_android_app.activities.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        LinearLayout logout = root.findViewById(R.id.logOut);

        auth = FirebaseAuth.getInstance();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        return root;
    }
}
