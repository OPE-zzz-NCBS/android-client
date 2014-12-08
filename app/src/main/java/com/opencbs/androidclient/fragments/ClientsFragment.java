package com.opencbs.androidclient.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.opencbs.androidclient.activities.PersonActivity;
import com.opencbs.androidclient.models.Client;
import com.opencbs.androidclient.ClientArrayAdapter;
import com.opencbs.androidclient.OnLoadMoreListener;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.events.CancelSearchEvent;
import com.opencbs.androidclient.events.ClientsLoadedEvent;
import com.opencbs.androidclient.events.LoadClientsEvent;
import com.opencbs.androidclient.events.NewPersonEvent;
import com.opencbs.androidclient.events.SearchEvent;

import java.util.ArrayList;

import javax.inject.Inject;

public class ClientsFragment extends FragmentWithBus implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, ListView.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressLayout;

    private ArrayList<Client> clients;
    private ClientArrayAdapter adapter;

    private int offset = 0;
    private final static int LIMIT = 25;

    private String query = "";

    @Inject
    public ClientsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients, container, false);

        clients = new ArrayList<Client>();
        adapter = new ClientArrayAdapter(getActivity(), clients);
        adapter.setOnLoadMoreListener(this);
        ListView listView = (ListView) view.findViewById(R.id.clients_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.clients_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setVisibility(View.GONE);

        progressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        postLoadClientsEvent();
    }

    public void onEventMainThread(ClientsLoadedEvent event) {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (offset == 0) {
            clients.clear();
        }

        clients.addAll(event.clients);
        adapter.setComplete(event.clients.size() < LIMIT);
        adapter.setLoading(false);
        adapter.setSearchString(query);
        adapter.notifyDataSetChanged();

        offset += LIMIT;
    }

    public void onEventMainThread(SearchEvent event) {
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        query = event.query;
        offset = 0;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                postLoadClientsEvent();
            }
        }, 100);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEventMainThread(CancelSearchEvent event) {
        if (query.isEmpty()) return;
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        query = "";
        offset = 0;
        postLoadClientsEvent();
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEvent(NewPersonEvent event) {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("id", 0);
        startActivity(intent);
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
