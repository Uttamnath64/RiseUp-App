package com.lit.riseup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Joined extends Fragment {

    View rootView;
    RecyclerView Recycler;
    Intent intent;
    Bundle bundle;
    ArrayList<JoinedModal> card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_joined, container, false);
        Recycler = rootView.findViewById(R.id.recyclerView);

        bundle = getArguments();
        card = new ArrayList<>();

        try {
            setListCardData(bundle.getString("Data",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void setListCardData(String data) throws JSONException {
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            card.add(new JoinedModal(
                    jsonObject.getString("Channel_Url"),
                    jsonObject.getString("Channel_Name"),
                    jsonObject.getString("Channel_Joiner"),
                    jsonObject.getString("Channel_Image"),
                    jsonObject.getString("Article_Url"),
                    jsonObject.getString("Article_Image")
            ));

        }
        JoinedAdapter joinedAdapter = new JoinedAdapter(card,rootView.getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        Recycler.setLayoutManager(linearLayoutManager);

        Recycler.setAdapter(joinedAdapter);
    }

}