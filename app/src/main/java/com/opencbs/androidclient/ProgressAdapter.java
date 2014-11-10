package com.opencbs.androidclient;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/*
 * The implementation is based on the example below:
 * https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews
 */
public abstract class ProgressAdapter<T> extends BaseAdapter {
    protected ArrayList<T> mData;
    protected Context mActivity;

    protected boolean mComplete = false;
    protected boolean mLoading = false;

    protected OnLoadMoreListener mOnLoadMoreListener;

    public static final int VIEW_TYPE_MORE = 0;
    public static final int VIEW_TYPE_DATA = 1;

    public ProgressAdapter(Context activity, ArrayList<T> data) {
        mActivity = activity;
        mData = data;
    }

    public void setComplete(boolean complete) {
        mComplete = complete;
        notifyDataSetChanged();
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) == VIEW_TYPE_DATA;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mComplete ? mData.size() : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position >= mData.size()) ? VIEW_TYPE_MORE : VIEW_TYPE_DATA;
    }

    @Override
    public T getItem(int position) {
        return (getItemViewType(position) == VIEW_TYPE_DATA) ? mData.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return (getItemViewType(position) == VIEW_TYPE_DATA) ? position : -1;
    }

    public abstract View getDataRow(int position, View convertView, ViewGroup parent);

    public View getFooterView(int position, View convertView, ViewGroup parent) {
        if (mLoading) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.layout_progress, parent, false);
        } else {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_more, parent, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadMoreListener == null) return;
                    mOnLoadMoreListener.onLoadMore();
                }
            });
            return view;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_MORE) {
            return getFooterView(position, convertView, parent);
        }

        return getDataRow(position, convertView, parent);
    }
}
