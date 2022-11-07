package com.lit.riseup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Achievement extends Fragment {

    View rootView;
    MaterialCardView Remaining, Collected, Collect;
    RecyclerView Recycler;
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    Fhome fragment = new Fhome();
    Bundle args = new Bundle();
    ArrayList<AchievementModal> card;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_achievement, container, false);

        Recycler = rootView.findViewById(R.id.recyclerView);



        card = new ArrayList<>();
        Bundle bundle = getArguments();

        try {
            setAchievementData(bundle.getString("Data",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private void setAchievementData(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            card.add(new AchievementModal(
                    obj.getString("Id"),
                    obj.getString("Achievement_Name"),
                    obj.getString("Description"),
                    obj.getString("Status")));
        }

        AchievementAdapter achievementAdapter = new AchievementAdapter(card, rootView.getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        Recycler.setLayoutManager(linearLayoutManager);

        Recycler.setAdapter(achievementAdapter);
    }
}