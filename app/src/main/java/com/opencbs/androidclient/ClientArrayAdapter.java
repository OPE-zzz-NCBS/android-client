package com.opencbs.androidclient;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opencbs.androidclient.models.Client;

import java.util.ArrayList;

public class ClientArrayAdapter extends ProgressAdapter<Client> {

    private ArrayList<Client> clients;
    private Context context;
    private String searchString = "";

    public ClientArrayAdapter(Context context, ArrayList<Client> clients) {
        super(context, clients);
        this.clients = clients;
        this.context = context;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_client_row, parent, false);
        TextView nameTextView = (TextView) rowView.findViewById(R.id.client_name_text_view);
        TextView typeTextView = (TextView) rowView.findViewById(R.id.client_type_text_view);
        Client client = clients.get(position);
        if (searchString.isEmpty()) {
            nameTextView.setText(client.name);
        } else {
            int start = client.name.toLowerCase().indexOf(searchString.toLowerCase());
            if (start > -1) {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(client.name);
                int end = start + searchString.length();
                builder.setSpan(new BackgroundColorSpan(Color.parseColor("#ffee55")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                nameTextView.setText(builder);
            } else {
                nameTextView.setText(client.name);
            }
        }
        if (!client.isSynced()) {
            nameTextView.setTypeface(nameTextView.getTypeface(), Typeface.BOLD);
        }

        typeTextView.setText(client.type);
        return rowView;
    }
}
