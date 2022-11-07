package com.lit.riseup;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fhome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fhome extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ProgressDialog progressDialog;
    JSONParser jsonParser = new JSONParser();
    Conn conn = new Conn();
    String WEB_URL = conn.getUrl();
    ArrayList<ListHomeCardData> card;
    View rootView;
    LinearLayout DataNotFound;
    RecyclerView RV;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fhome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fhome.
     */
    // TODO: Rename and change types and number of parameters
    public static Fhome newInstance(String param1, String param2) {
        Fhome fragment = new Fhome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        rootView = inflater.inflate(R.layout.fragment_fhome, container, false);
        RV = rootView.findViewById(R.id.recyclerView);
        DataNotFound = rootView.findViewById(R.id.dataNotFound);
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(getContext()));
        DataNotFound.setVisibility(View.GONE);

        card = new ArrayList<>();
        Bundle bundle = getArguments();
        try {
            setHomeCard(bundle.getString("Data",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }




    private void setHomeCard(String json) throws JSONException {
        if(json.trim().equals("")){
            RV.setVisibility(View.GONE);
            DataNotFound.setVisibility(View.VISIBLE);
            return;
        }
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            card.add(new ListHomeCardData(
                    obj.getString("Url"),
                    WEB_URL+obj.getString("Image_Url"),
                    obj.getString("Title"),
                    obj.getString("Description"),
                    WEB_URL+obj.getString("Channel_Logo"),
                    obj.getString("Channel_Name")+" • "+ obj.getString("Views")+" View • "+ obj.getString("Date_Time")));

        }

        ListHomeCardAdapter listHomeCardAdapter = new ListHomeCardAdapter(card, rootView.getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        RV.setLayoutManager(linearLayoutManager);

        RV.setAdapter(listHomeCardAdapter);
    }
}