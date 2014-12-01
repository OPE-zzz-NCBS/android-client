package com.opencbs.androidclient.ui;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.event.CancelSearchEvent;
import com.opencbs.androidclient.event.LogoutEvent;
import com.opencbs.androidclient.event.LogoutSuccessEvent;
import com.opencbs.androidclient.event.NewPersonEvent;
import com.opencbs.androidclient.event.SearchEvent;

import java.util.ArrayList;

import javax.inject.Inject;

public class MainActivity extends ActivityWithBus implements ListView.OnItemClickListener {

    private static final int MENU_CLIENTS = 10;
    private static final int MENU_DOWNLOAD = 20;
    private static final int MENU_LOGOUT = 100;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<String> drawerItems;
    private ArrayList<Integer> drawerIds;

    @Inject
    ClientsFragment clientsFragment;

    @Inject
    DownloadFragment downloadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 0, 0);

        drawerItems = new ArrayList<String>();
        drawerItems.add(getString(R.string.clients));
        drawerItems.add(getString(R.string.download));
        drawerItems.add(getString(R.string.logout));

        drawerIds = new ArrayList<Integer>();
        drawerIds.add(MENU_CLIENTS);
        drawerIds.add(MENU_DOWNLOAD);
        drawerIds.add(MENU_LOGOUT);

        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_option_item, drawerItems));
        listView.setOnItemClickListener(this);

        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        showClientsFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clients, menu);
        setupSearchMenuItem(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_new_person:
                bus.post(new NewPersonEvent());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        drawerLayout.closeDrawers();
        int itemId = drawerIds.get(position);
        if (itemId == MENU_CLIENTS) {
            showClientsFragment();
        } else if (itemId == MENU_DOWNLOAD) {
            showDownloadFragment();
        } else if (itemId == MENU_LOGOUT) {
            logout();
        }
    }

    public void onEvent(LogoutSuccessEvent event) {
        Settings.setAccessToken(this, "");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private void setupSearchMenuItem(Menu menu) {
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                SearchEvent event = new SearchEvent();
                event.query = query;
                bus.post(event);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                bus.post(new CancelSearchEvent());
                return true;
            }
        });
    }

    private void logout() {
        bus.post(new LogoutEvent());
    }

    private void showClientsFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, clientsFragment);
        transaction.commit();
        setTitle(getString(R.string.clients));
    }

    private void showDownloadFragment() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, downloadFragment);
        transaction.commit();
        setTitle(getString(R.string.download));
    }
}
