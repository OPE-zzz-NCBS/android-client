package com.opencbs.androidclient;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClientsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, OnSearchListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ArrayList<Client> mClients;
    private ClientArrayAdapter mAdapter;
    private ClientService mClientService;

    private int mOffset;
    private final static int LIMIT = 25;
    private int mCount = 0;
    private boolean mIncludeCount = true;
    private String mQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mClients = new ArrayList<Client>();
        mAdapter = new ClientArrayAdapter(getActivity(), mClients);
        mAdapter.setOnLoadMoreListener(this);
        mOffset = 0;
        ListView listView = (ListView) view.findViewById(R.id.clients_list_view);
        listView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.clients_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        RestAdapter restAdapter = Factory.getRestAdapter(getActivity());
        mClientService = restAdapter.create(ClientService.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 100);
    }

    @Override
    public void onRefresh() {
        mOffset = 0;
        mIncludeCount = true;
        mClients.clear();
        mAdapter.setComplete(false);
        onLoadMore();
    }

    @Override
    public void onSearch(String query) {
        mQuery = query;
        onRefresh();
    }

    @Override
    public void onLoadMore() {
        mAdapter.setLoading(true);
        mAdapter.notifyDataSetChanged();
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        Callback<ClientsResponse> callback = new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                if (mIncludeCount) {
                    mIncludeCount = false;
                    mCount = clientsResponse.count;
                }

                Collections.addAll(mClients, clientsResponse.items);

                mOffset += LIMIT;
                if (mOffset >= mCount) {
                    mAdapter.setComplete(true);
                }
                mAdapter.setLoading(false);
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                mAdapter.setLoading(false);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
        };

        if (mQuery.isEmpty()) {
            mClientService.getAll(mOffset, LIMIT, mIncludeCount, callback);
        } else {
            mClientService.search(mOffset, LIMIT, mIncludeCount, mQuery, callback);
        }
    }
}
