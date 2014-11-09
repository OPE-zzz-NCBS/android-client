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
    private final static int mLimit = 25;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadMore();
            }
        });

        mClients = new ArrayList<Client>();
        mAdapter = new ClientArrayAdapter(getActivity(), mClients);
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
        mClientService.getClients(mOffset, mLimit, new Callback<ClientsResponse>() {
            @Override
            public void success(ClientsResponse clientsResponse, Response response) {
                for (Client client : clientsResponse.items) {
                    mClients.add(client);
                }
                mOffset += mLimit;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }
}
