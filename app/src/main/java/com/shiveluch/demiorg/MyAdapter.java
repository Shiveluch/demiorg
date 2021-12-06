package com.shiveluch.demiorg;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import com.shiveluch.demiorg.aDemiOrgMain;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    private LayoutInflater lInflater;
    public ArrayList<MainList> items = new ArrayList<MainList>();
    Context ctx;
    ArrayList<MainList> objects;


    public MyAdapter(Context context, ArrayList<MainList> p) {
        super();
        ctx=context;
        objects=p;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < p.size(); i++) {
            ListItem listItem = new ListItem();
            listItem.caption = objects.get(i).addinfo;
            listItem.textCaption = objects.get(i).name;
            items.add(new MainList("", listItem.textCaption, listItem.caption));
            aDemiOrgMain.itemsForEditText.get(i).name="";
            aDemiOrgMain.itemsForEditText.get(i).addinfo="";
        }
        notifyDataSetChanged();
    }



    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.itemwithedittext, null);
            holder.textCaption = convertView.findViewById(R.id.itemtext);
            holder.caption = (EditText) convertView
                    .findViewById(R.id.ItemCaption);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Fill EditText with the value you have in data source
        holder.caption.setHint(items.get(position).addinfo);
        holder.caption.setId(position);
       // holder.caption.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        holder.textCaption.setText(items.get(position).name);



        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                { holder.caption.post(new Runnable() {
                    @Override
                    public void run() {
                                        System.out.println("Focus "+position);
                    }
                });
                }
                if (!hasFocus){
                    final int position = v.getId();
                    System.out.println("isposition: "+position);
                    final EditText Caption = (EditText) v;
                    aDemiOrgMain.itemsForEditText.get(position).addinfo=Caption.getText().toString();
                    System.out.println( "in Main:" +aDemiOrgMain.itemsForEditText.get(position).addinfo);
                }
            }
        });

        return convertView;
    }
}

class ViewHolder {
    EditText caption;
    TextView textCaption;
}

class ListItem {
    String caption;
    String textCaption;
}

