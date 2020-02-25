package com.example.padamlight.ui.profile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.padamlight.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    @Bind(R.id.editTextDescription)
    EditText editTextDescription;
    @Bind(R.id.editTextMotivation)
    EditText editTextMotivation;


    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, root);

        profileViewModel.setProfileDescription().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                editTextDescription.setText(s);
            }
        });

        profileViewModel.setProfileMotivation().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                editTextMotivation.setText(s);
            }
        });
        return root;
    }
}