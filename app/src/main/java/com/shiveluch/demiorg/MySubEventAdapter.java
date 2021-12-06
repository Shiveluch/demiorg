package com.shiveluch.demiorg;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class MySubEventAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<dataFour> objects;

    protected ListView mListView;
    public MySubEventAdapter(Context context, ArrayList<dataFour> stalkers, Activity activity) {
        super();
        ctx = context;
        objects = stalkers;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = activity.findViewById(R.id.mysubeventslist);
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
            view = lInflater.inflate(R.layout.mysubeventsadapter, parent, false);
        }

        dataFour p = getMySubevent(position);
        ((TextView) view.findViewById(R.id.subeventname)).setText(p.name);
        ((TextView) view.findViewById(R.id.subeventinfo)).setText(p.info);
        ((TextView) view.findViewById(R.id.subeventid)).setText(p.id);
        ((TextView) view.findViewById(R.id.subeventapproved)).setText(p.additional);
        if (p.additional.equals("0"))
        {int color = Integer.parseInt("e50000", 16)+0xFF000000;
            ((TextView) view.findViewById(R.id.subeventapproved)).setText("Мероприятие не одобрено");
            ((TextView) view.findViewById(R.id.subeventapproved)).setTextColor(color);
       }

        if (p.name.length()>1)
        {
            ((TextView) view.findViewById(R.id.name)).setVisibility(View.VISIBLE);
        }

        return view;
    }

    dataFour getMySubevent(int position) {
        return ((dataFour) getItem(position));
    }

}
