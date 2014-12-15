package com.opencbs.androidclient.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.Settings;
import com.opencbs.androidclient.events.CacheInitializedEvent;
import com.opencbs.androidclient.events.CancelSearchEvent;
import com.opencbs.androidclient.events.LogoutEvent;
import com.opencbs.androidclient.events.LogoutSuccessEvent;
import com.opencbs.androidclient.events.NewPersonEvent;
import com.opencbs.androidclient.events.SearchEvent;
import com.opencbs.androidclient.fragments.ClientsFragment;
import com.opencbs.androidclient.fragments.DownloadFragment;
import com.opencbs.androidclient.fragments.JobsFragment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Provider;

public class MainActivity extends BaseActivity implements ListView.OnItemClickListener {
    @Inject
    Settings settings;

    @Inject
    Provider<ClientsFragment> clientsFragmentProvider;

    @Inject
    Provider<DownloadFragment> downloadFragmentProvider;

    @Inject
    Provider<JobsFragment> jobsFragmentProvider;

    private static final int MENU_CLIENTS = 10;
    private static final int MENU_JOBS = 15;
    private static final int MENU_DOWNLOAD = 20;
    private static final int MENU_LOGOUT = 100;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<String> drawerItems;
    private ArrayList<Integer> drawerIds;

    private interface FragmentActivator {
        void execute();
    }

    FragmentActivator fragmentActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, 0, 0);

        drawerItems = new ArrayList<>();
        drawerItems.add(getString(R.string.clients));
        drawerItems.add("Jobs");
        drawerItems.add(getString(R.string.logout));

        drawerIds = new ArrayList<>();
        drawerIds.add(MENU_CLIENTS);
        drawerIds.add(MENU_JOBS);
        drawerIds.add(MENU_LOGOUT);

        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_option_item, drawerItems));
        listView.setOnItemClickListener(this);

        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Settings.CacheState cacheState = settings.getCacheState();
        if (cacheState == Settings.CacheState.INITIALIZED) {
            fragmentActivator = getClientsFragmentActivator();
        } else {
            fragmentActivator = getDownloadFragmentActivator();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fragmentActivator != null) {
            fragmentActivator.execute();
            fragmentActivator = null;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEvent(CacheInitializedEvent event) {
        if (isVisible()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getClientsFragmentActivator().execute();
                }
            });
        } else {
            fragmentActivator = getClientsFragmentActivator();
        }
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
            getClientsFragmentActivator().execute();
        } else if (itemId == MENU_JOBS) {
            getJobsFragmentActivator().execute();
        } else if (itemId == MENU_DOWNLOAD) {
            getDownloadFragmentActivator().execute();
        } else if (itemId == MENU_LOGOUT) {
            logout();
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEvent(LogoutSuccessEvent event) {
        settings.setAccessToken("");
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
        styleSearchView(searchView);
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

    private void styleSearchView(SearchView searchView) {
        // It is pretty straightforward to have a light theme with dark actionbar
        // and light drop down menus. But the search view remains dark and we have to
        // customize it. This is not a trivial thing to do.
        // Check out these link for a better picture:
        // http://www.jayway.com/2014/06/02/android-theming-the-actionbar/
        // http://novoda.com/blog/styling-actionbar-searchview
        // http://nlopez.io/how-to-style-the-actionbar-searchview-programmatically/

        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(Color.WHITE);
        searchPlate.setHintTextColor(Color.LTGRAY);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(searchPlate, 0);
        } catch (Exception ignored) {
        }

        try {
            Class<?> clazz = Class.forName("android.widget.SearchView$SearchAutoComplete");
            Drawable searchIcon = getResources().getDrawable(R.drawable.ic_action_search);
            Method textSizeMethod = clazz.getMethod("getTextSize");
            Float rawTextSize = (Float) textSizeMethod.invoke(searchPlate);
            int textSize = (int) (rawTextSize * 1.25);
            searchIcon.setBounds(0, 0, textSize, textSize);

            SpannableStringBuilder stopHint = new SpannableStringBuilder("   ");
            stopHint.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            Method setHintMethod = clazz.getMethod("setHint", CharSequence.class);
            setHintMethod.invoke(searchPlate, stopHint);
        } catch (ClassNotFoundException ignored) {
        } catch (NoSuchMethodException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (InvocationTargetException ignored) {
        }

        int closeIconId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeIcon = (ImageView) searchView.findViewById(closeIconId);
        closeIcon.setImageResource(R.drawable.ic_action_cancel);
    }

    private void logout() {
        bus.post(new LogoutEvent());
    }

    private FragmentActivator getClientsFragmentActivator() {
        return new FragmentActivator() {
            @Override
            public void execute() {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, clientsFragmentProvider.get())
                        .commit();
                setTitle(getString(R.string.clients));
            }
        };
    }

    private FragmentActivator getDownloadFragmentActivator() {
        return new FragmentActivator() {
            @Override
            public void execute() {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, downloadFragmentProvider.get())
                        .commit();
                setTitle(getString(R.string.download));
            }
        };
    }

    private FragmentActivator getJobsFragmentActivator() {
        return new FragmentActivator() {
            @Override
            public void execute() {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_layout, jobsFragmentProvider.get())
                        .commit();
                setTitle("Jobs");
            }
        };
    }
}
