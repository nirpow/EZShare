package com.colman.ezshare.Search;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colman.ezshare.MainActivity;
import com.colman.ezshare.Models.User;
import com.colman.ezshare.R;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    SearchViewModel viewData;

    LiveData<List<User>> userListLiveData;

    private RecyclerView recyclerView;
    MainActivity activity;

    SearchAdapter adapter;
    List<User> usersList1;
    ImageView iconImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.rv_search);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        iconImageView = view. findViewById(R.id.icon_ppl_gruop);
        EditText searchEditText = view.findViewById(R.id.et_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<User> filteredList = new ArrayList<>();
                for (User user : usersList1
                ) {
                    if(user.getUserName().startsWith(s.toString())){
                        filteredList.add(user);
                        iconImageView.setVisibility(View.GONE);
                    }
                }
                updateDisplay(filteredList);
                if(s.toString().equals("")){
                    updateDisplay(new ArrayList<User>(){});
                    iconImageView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void updateDisplay(List<User>usersList) {
        Log.d(TAG, "updateDisplay: usersList = "+usersList);
        adapter = new SearchAdapter(usersList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        super.onAttach(context);
        viewData = ViewModelProviders.of(this).get(SearchViewModel.class);

        userListLiveData = viewData.getUsersList();
        userListLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> postList) {
                usersList1 = postList;
//                updateDisplay();
            }
        });
    }
}
