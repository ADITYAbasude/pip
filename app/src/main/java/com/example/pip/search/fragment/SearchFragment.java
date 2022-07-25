package com.example.pip.search.fragment;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pip.Adapters.SearchAdapter;
import com.example.pip.Models.UserModel;
import com.example.pip.R;
import com.example.pip.databinding.FragmentSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private SearchAdapter searchAdapter;
    private final ArrayList<UserModel> store_User_Model_Info = new ArrayList<>();
    private FragmentSearchBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int modeFlag = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (modeFlag == Configuration.UI_MODE_NIGHT_YES) {
            final Drawable searchTheme = ContextCompat.getDrawable(requireContext() , R.drawable.btn_with_roundborders);
            assert searchTheme != null;
            searchTheme.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.dimNight ), PorterDuff.Mode.SRC_ATOP);
            binding.searchBar.setBackground(searchTheme);
        }

        searchAdapter = new SearchAdapter(getContext(), store_User_Model_Info);
        binding.serachresult.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.serachresult.setAdapter(searchAdapter);

        fetchUserAccounts();

//        ----------------datasnapshot from firebase---------------
        takeUserNameFromFirebase();


    }


    private void takeUserNameFromFirebase() {
        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.progressBar.setVisibility(View.VISIBLE);
                fetchUserAccounts();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    void fetchUserAccounts() {
        ref.child("user").child("UserInfo").orderByChild("usName").startAt(binding.searchBar.getText().toString()).endAt("\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            store_User_Model_Info.clear();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                UserModel userModel = ds.getValue(UserModel.class);
                                store_User_Model_Info.add(userModel);
                                binding.progressBar.setVisibility(View.GONE);
                                binding.userNotFound.setVisibility(View.GONE);
                            }
                            searchAdapter.notifyDataSetChanged();
                        } else {
                            binding.userNotFound.setVisibility(View.VISIBLE);
                            binding.userNotFound.setVisibility(View.VISIBLE);
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}

