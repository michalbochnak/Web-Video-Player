//
// Michal Bochnak, Netid: mbochn2
// CS 478 - Project #02, Web Video Player
// UIC, March 2, 2018
// Professor: Ugo Buy
//
// ListAdapter.java
//


package com.webvideoplayer.cs478_project02_webvideoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;


//
// Allows for displaying two lines of text for each item that
// is included in the ListView
//
public class ListAdapter extends BaseAdapter {

    ArrayList<HashMap<String,String>> items;
    Context context;

    public ListAdapter(Context cntx, ArrayList<HashMap<String,String>> data) {
        items = data;
        this.context = cntx;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public HashMap<String, String> getItem(int index) {
        return items.get(index);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.two_lines, parent, false);
        ((TextView)convertView.findViewById(R.id.songTextView)).setText(items.get(pos).get("title"));
        ((TextView)convertView.findViewById(R.id.artistTextView)).setText(items.get(pos).get("artist"));
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
