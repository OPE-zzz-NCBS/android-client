package com.opencbs.androidclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opencbs.androidclient.model.Client;

import java.util.ArrayList;

public class ClientArrayAdapter extends ProgressAdapter<Client> {

    private ArrayList<Client> mClients;
    private Context mContext;

    public ClientArrayAdapter(Context context, ArrayList<Client> clients) {
        super(context, clients);
        mClients = clients;
        mContext = context;
    }

    @Override
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_client_row, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.client_name_text_view);
        TextView typeTextView = (TextView) rowView.findViewById(R.id.client_type_text_view);
        Client client = mClients.get(position);
        nameTextView.setText(client.name);
        typeTextView.setText(client.type);
        return rowView;
    }
}
