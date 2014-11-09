package com.opencbs.androidclient;

import android.app.Service;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * Implementation of the class was taken from here:
 * https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
 */
public abstract class ProgressAdapter<T> extends BaseAdapter {
    protected ArrayList<T> mData;
    protected Context mActivity;

    protected int mServerListSize = -1;

    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_ACTIVITY = 1;

    public ProgressAdapter(Context activity, ArrayList<T> data) {
        mActivity = activity;
        mData = data;
    }

    public void setServerListSize(int serverListSize) {
        mServerListSize = serverListSize;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == VIEW_TYPE_ACTIVITY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= mData.size()) ? VIEW_TYPE_LOADING : VIEW_TYPE_ACTIVITY;
    }

    @Override
    public T getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_ACTIVITY) ? position : -1;
    }

    public abstract View getDataRow(int position, View convertView, ViewGroup parent);

    public View getFooterView(int position, View convertView, ViewGroup parent) {
        if (position >= mServerListSize && mServerListSize > 0) {
            TextView lastRowTextView = new TextView(mActivity);
            lastRowTextView.setHint("Reached the last row.");
            lastRowTextView.setGravity(Gravity.CENTER);
            return lastRowTextView;
        }
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.layout_progress, parent, false);
        }

        return row;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            return getFooterView(position, convertView, parent);
        }

        View dataRow = convertView;
        dataRow = getDataRow(position, convertView, parent);
        return dataRow;
    }
}
