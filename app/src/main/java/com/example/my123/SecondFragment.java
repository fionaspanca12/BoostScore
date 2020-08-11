package com.example.my123;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my123.Api.ApiClient;
import com.example.my123.Api.ApiInterface;
import com.example.my123.Games.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SecondFragment extends Fragment {
    List<Game> matches_live =new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout,container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String BASE_URL = ((MainActivity)this.getActivity()).getBaseUrl()+"running/";
        ApiInterface apiInterface = ApiClient.getApiClient(BASE_URL).create(ApiInterface.class);
        Call<Game[]> call;
        String token = getString(R.string.token);
        call = apiInterface.getCsGo_matches(token);
        call.enqueue(new Callback<Game[]>() {
            @Override
            public void onResponse(Call<Game[]> call, Response<Game[]> response) {
                matches_live = Arrays.asList(response.body());
                Log.i("status", "if" );
                Log.i("Resita", " " );
                for (int i = 0; i< matches_live.size(); i++)
                {
                    Log.i("Res2", " " + matches_live.get(i).getLeague().getSlug());
                    try {
                        Log.i("Res2", " " + matches_live.get(i).getOpponents().get(0).getOpponent().getName() + " VS " + matches_live.get(i).getOpponents().get(1).getOpponent().getName());
                    }
                    catch (Exception e)
                    {
                        Log.i("Res2", " Null" );
                    }
                }
            }

            @Override
            public void onFailure(Call<Game[]> call, Throwable t) {
                Log.i("status", "off" );
            }
        });

        RecyclerViewAdapter itemsAdapter = new RecyclerViewAdapter(SecondFragment.this.getActivity(), matches_live, null);
        RecyclerView clv1 = (RecyclerView) view.findViewById(R.id.clist);
        clv1.setLayoutManager(new LinearLayoutManager(SecondFragment.this.getActivity()));
        clv1.setHasFixedSize(true);
        clv1.setAdapter(itemsAdapter);

    }
}
