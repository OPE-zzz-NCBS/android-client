package com.opencbs.androidclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.opencbs.androidclient.model.Client;
import com.opencbs.androidclient.ClientArrayAdapter;
import com.opencbs.androidclient.OnLoadMoreListener;
import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.CancelSearchEvent;
import com.opencbs.androidclient.event.ClientsLoadedEvent;
import com.opencbs.androidclient.event.LoadClientsEvent;
import com.opencbs.androidclient.event.NewPersonEvent;
import com.opencbs.androidclient.event.SearchEvent;
import com.opencbs.androidclient.model.ClientRange;
import com.opencbs.androidclient.repo.ClientRepo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ClientsFragment extends FragmentWithBus  implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, ListView.OnItemClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout progressLayout;

    private ArrayList<Client> clients;
    private ClientArrayAdapter adapter;

    private int offset = 0;
    private final static int LIMIT = 25;

    private final static int BATCH_SIZE = 25;
    private String query = "";
    private ClientRange nextRange;

    @Inject
    ClientRepo clientRepo;

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

//        nextRange = new ClientRange(0, BATCH_SIZE - 1);

//        LoadClientsEvent event = new LoadClientsEvent();
//        event.query = query;
//        event.clientRange = nextRange;
//        enqueueEvent(event);
//
        fetchClients();
        return view;
    }

    private void fetchClients() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        List<Client> clients = clientRepo.getAll(offset, LIMIT);
        if (offset == 0) {
            this.clients.clear();
        }
        this.clients.addAll(clients);
        adapter.setComplete(clients.size() < LIMIT);
        adapter.setLoading(false);
        adapter.notifyDataSetChanged();

        offset += LIMIT;
    }

    public void onEvent(ClientsLoadedEvent event) {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (event.thisRange.from == 0) {
            clients.clear();
        }

        nextRange = event.nextRange;

        clients.addAll(event.clients);
        adapter.setComplete(event.nextRange == null);
        adapter.setLoading(false);
        adapter.notifyDataSetChanged();
    }

    public void onEvent(SearchEvent event) {
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        query = event.query;
        nextRange = new ClientRange(0, BATCH_SIZE - 1);
        postLoadClientsEvent();
    }

    public void onEvent(CancelSearchEvent event) {
        if (query.isEmpty()) return;
        swipeRefreshLayout.setVisibility(View.GONE);
        progressLayout.setVisibility(View.VISIBLE);
        query = "";
        nextRange = new ClientRange(0, BATCH_SIZE - 1);
        postLoadClientsEvent();
    }

    public void onEvent(NewPersonEvent event) {
        Intent intent = new Intent(getActivity(), PersonActivity.class);
        intent.putExtra("id", 0);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        offset = 0;
        fetchClients();
//        nextRange = new ClientRange(0, BATCH_SIZE - 1);
//        postLoadClientsEvent();
    }

    @Override
    public void onLoadMore() {
        adapter.setLoading(true);
        adapter.notifyDataSetChanged();
        fetchClients();
        //postLoadClientsEvent();
    }

    private void postLoadClientsEvent() {
        LoadClientsEvent event = new LoadClientsEvent();
        event.query = query;
        event.clientRange = nextRange;
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
