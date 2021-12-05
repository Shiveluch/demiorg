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

public class dataFourAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<dataFour> objects;

    protected ListView mListView;
    public dataFourAdapter(Context context, ArrayList<dataFour> stalkers, Activity activity) {
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
            view = lInflater.inflate(R.layout.commonfourfieldadapter, parent, false);
        }

        dataFour p = getdataFour(position);
        ((TextView) view.findViewById(R.id.name)).setText(p.name);
        ((TextView) view.findViewById(R.id.info)).setText(p.info);
        ((TextView) view.findViewById(R.id.id)).setText(p.id);
        ((TextView) view.findViewById(R.id.addinfo)).setText(p.additional);




        return view;
    }

    dataFour getdataFour(int position) {
        return ((dataFour) getItem(position));
    }

}
