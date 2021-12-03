package com.shiveluch.demiorg;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<MainList> objects;

    protected ListView mListView;
    public MainListAdapter(Context context, ArrayList<MainList> stalkers, Activity activity) {
        super();
        ctx = context;
        objects = stalkers;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = activity.findViewById(R.id.maineventslist);
    }

    // ???-?? ?????????
    @Override
    public int getCount() {
        return objects.size();
    }

    // ??????? ?? ???????
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.mainadapter, parent, false);
        }

        MainList p = getMainList(position);
        ((TextView) view.findViewById(R.id.eventname)).setText(p.name);
        ((TextView) view.findViewById(R.id.eventtime)).setText(p.addinfo);
        ((TextView) view.findViewById(R.id.eventid)).setText(p.id);



        return view;
    }

    MainList getMainList(int position) {
        return ((MainList) getItem(position));
    }

}
