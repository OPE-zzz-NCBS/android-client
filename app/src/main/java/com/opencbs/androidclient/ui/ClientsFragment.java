package com.opencbs.androidclient.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.opencbs.androidclient.Client;
import com.opencbs.androidclient.ClientArrayAdapter;
import com.opencbs.androidclient.OnLoadMoreListener;
import com.opencbs.androidclient.OnSearchListener;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.ClientsLoadedEvent;
import com.opencbs.androidclient.event.LoadClientsEvent;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class ClientsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, OnSearchListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressLayout;

    private ArrayList<Client> clients;
    private ClientArrayAdapter adapter;

    private int offset;
    private final static int LIMIT = 25;
    private int count = 0;
    private String query = "";

    @Inject
    EventBus bus;

    @Inject
    public ClientsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        bus.register(this);
        postLoadClientsEvent();
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        clients = new ArrayList<Client>();
        adapter = new ClientArrayAdapter(getActivity(), clients);
        adapter.setOnLoadMoreListener(this);
        offset = 0;
        ListView listView = (ListView) view.findViewById(R.id.clients_list_view);
        listView.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.clients_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setVisibility(View.GONE);

        progressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);
    }

    public void onEvent(ClientsLoadedEvent event) {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (offset == 0) {
            clients.clear();
            count = event.count;
        }
        Collections.addAll(clients, event.clients);
        offset += LIMIT;
        if (offset >= count) {
            adapter.setComplete(true);
        }
        adapter.setLoading(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearch(String query) {
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        this.query = query;
        offset = 0;
        postLoadClientsEvent();
    }

    @Override
    public void onRefresh() {
        offset = 0;
        postLoadClientsEvent();
    }

    @Override
    public void onLoadMore() {
        adapter.setLoading(true);
        adapter.notifyDataSetChanged();
        postLoadClientsEvent();
    }

    private void postLoadClientsEvent() {
        LoadClientsEvent event = new LoadClientsEvent();
        event.includeCount = offset == 0;
        event.offset = offset;
        event.limit = LIMIT;
        event.query = query;
        bus.post(event);
    }
}
