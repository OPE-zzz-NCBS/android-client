package com.opencbs.androidclient.ui;

import android.app.Fragment;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.opencbs.androidclient.Client;
import com.opencbs.androidclient.ClientArrayAdapter;
import com.opencbs.androidclient.ClientService;
import com.opencbs.androidclient.ClientsResponse;
import com.opencbs.androidclient.Factory;
import com.opencbs.androidclient.OnLoadMoreListener;
import com.opencbs.androidclient.OnSearchListener;
import com.opencbs.androidclient.R;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClientsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, OnSearchListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout mProgressLayout;

    private ArrayList<Client> mClients;
    private ClientArrayAdapter mAdapter;
    private ClientService mClientService;

    private int mOffset;
    private final static int LIMIT = 25;
    private int mCount = 0;
    private boolean mIncludeCount = true;
    private String mQuery = "";

    @Inject
    public ClientsFragment() {}

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

        mProgressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);

        RestAdapter restAdapter = Factory.getRestAdapter(getActivity());
        mClientService = restAdapter.create(ClientService.class);

        load();
    }

    @Override
    public void onSearch(String query) {
        mQuery = query;
        load();
    }

    @Override
    public void onRefresh() {
        mOffset = 0;
        mIncludeCount = true;

        Callback<ClientsResponse> callback = new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                mIncludeCount = false;
                mCount = clientsResponse.count;
                mClients.clear();
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

            }
        };

        fireLoad(callback);
    }

    @Override
    public void onLoadMore() {
        mAdapter.setLoading(true);
        mAdapter.notifyDataSetChanged();

        Callback<ClientsResponse> callback = new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                Collections.addAll(mClients, clientsResponse.items);
                mOffset += LIMIT;
                if (mOffset >= mCount) {
                    mAdapter.setComplete(true);
                }
                mAdapter.setLoading(false);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                mAdapter.setLoading(false);
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
            }
        };

        fireLoad(callback);
    }

    private void load() {
        mSwipeRefreshLayout.setVisibility(View.GONE);
        mProgressLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setEnabled(false);
        mOffset = 0;
        mIncludeCount = true;

        Callback<ClientsResponse> callback = new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                mIncludeCount = false;
                mCount = clientsResponse.count;
                Collections.addAll(mClients, clientsResponse.items);
                mOffset += LIMIT;
                if (mOffset >= mCount) {
                    mAdapter.setComplete(true);
                }
                mAdapter.setLoading(false);
                mAdapter.notifyDataSetChanged();
                mProgressLayout.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setEnabled(true);
            }

            @Override
            public void failure(RetrofitError error) {
            }
        };

        fireLoad(callback);
    }

    private void fireLoad(Callback<ClientsResponse> callback) {
        if (mQuery.isEmpty()) {
            mClientService.getAll(mOffset, LIMIT, mIncludeCount, callback);
        } else {
            mClientService.search(mOffset, LIMIT, mIncludeCount, mQuery, callback);
        }
    }
}
