package com.opencbs.androidclient.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencbs.androidclient.R;
import com.opencbs.androidclient.event.BranchesLoadedEvent;
import com.opencbs.androidclient.event.LoadBranchesEvent;
import com.opencbs.androidclient.model.Branch;

import java.util.ArrayList;

public class BranchPickerActivity extends OkCancelActivity {

    private ArrayList<Branch> branches;
    private ArrayAdapter<Branch> adapter;
    private ListView listView;
    private int branchPickerId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_picker);

        branches = new ArrayList<Branch>();
        adapter = new ArrayAdapter<Branch>(this, android.R.layout.simple_list_item_single_choice, branches);
        listView = (ListView) findViewById(R.id.branch_list_view);
        listView.setAdapter(adapter);

        Intent intent = getIntent();
        branchPickerId = intent.getIntExtra("branchPickerId", 0);

        enqueueEvent(new LoadBranchesEvent());
    }

    public void onEvent(BranchesLoadedEvent event) {
        branches.clear();
        branches.addAll(event.branches);
        adapter.notifyDataSetChanged();

        Intent intent = getIntent();
        int id = intent.getIntExtra("branchId", 0);
        if (id > 0) {
            int position = -1;
            for (int j = 0; j < branches.size(); j++) {
                Branch branch = branches.get(j);
                if (branch.id == id) {
                    position = j;
                    break;
                }
            }
            if (position > -1) {
                listView.setSelection(position);
                listView.setItemChecked(position, true);
            }
        }
    }

    @Override
    protected void onOk() {
        int position = listView.getCheckedItemPosition();
        int id = 0;
        if (position != ListView.INVALID_POSITION) {
            Branch branch = branches.get(position);
            id = branch.id;
        }
        Intent intent = new Intent();
        intent.putExtra("branchPickerId", branchPickerId);
        intent.putExtra("branchId", id);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCancel() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
