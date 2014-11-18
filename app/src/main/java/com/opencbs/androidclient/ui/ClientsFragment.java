package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.opencbs.androidclient.Client;
import com.opencbs.androidclient.ClientArrayAdapter;
import com.opencbs.androidclient.OnLoadMoreListener;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.CancelSearchEvent;
import com.opencbs.androidclient.event.ClientsLoadedEvent;
import com.opencbs.androidclient.event.LoadClientsEvent;
import com.opencbs.androidclient.event.SearchEvent;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

public class ClientsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, ListView.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressLayout;

    private ArrayList<Client> clients;
    private ClientArrayAdapter adapter;

    private int offset;
    private final static int LIMIT = 25;
    private int count = 0;
    private String query = "";
    private boolean wasPaused = false;

    @Inject
    EventBus bus;

    @Inject
    public ClientsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clients, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        wasPaused = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        bus.register(this);
        if (!wasPaused) {
            postLoadClientsEvent();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bus.unregister(this);
        wasPaused = true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        clients = new ArrayList<Client>();
        adapter = new ClientArrayAdapter(getActivity(), clients);
        adapter.setOnLoadMoreListener(this);
        offset = 0;
        ListView listView = (ListView) view.findViewById(R.id.clients_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

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
        adapter.setComplete(offset >= count);
        adapter.setLoading(false);
        adapter.notifyDataSetChanged();
    }

    public void onEvent(SearchEvent event) {
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        query = event.query;
        offset = 0;
        postLoadClientsEvent();
    }

    public void onEvent(CancelSearchEvent event) {
        if (query.isEmpty()) return;
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        query = "";
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Client client = adapter.getItem(position);
        if (client.type.equals("PERSON")) {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            intent.putExtra("id", client.id);
            startActivity(intent);
        }
    }
}
