package com.opencbs.androidclient;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ClientsFragment extends ListFragment {

    private ArrayList<Client> mClients;
    private ClientArrayAdapter mAdapter;
    private ClientService mClientService;

    private int mOffset;
    private final static int mLimit = 100;
    private int mCount = 0;
    private boolean mIncludeCount = true;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mClients = new ArrayList<Client>();
        mAdapter = new ClientArrayAdapter(getActivity(), mClients);
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        mOffset = 0;
        setListAdapter(mAdapter);

        RestAdapter restAdapter = Factory.getRestAdapter(getActivity());
        mClientService = restAdapter.create(ClientService.class);

        loadMore();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        String item = (String) getListAdapter().getItem(position);
//        Toast.makeText(getActivity(), item + " selected", Toast.LENGTH_LONG).show();
    }

    private void loadMore() {
        mAdapter.setLoading(true);
        mClientService.getClients(mOffset, mLimit, mIncludeCount, new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                if (mIncludeCount) {
                    mIncludeCount = false;
                    mCount = clientsResponse.count;
                }
                for (Client client : clientsResponse.items) {
                    mClients.add(client);
                }
                mOffset += mLimit;
                if (mOffset >= mCount) {
                    mAdapter.setComplete(true);
                }
                mAdapter.setLoading(false);
            }

            @Override
            public void failure(RetrofitError error) {
                mAdapter.setLoading(false);
            }
        });
    }
}
