package com.example.zhanglian4.mybrowserexample;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListViewCustomAdapter extends BaseAdapter {

    private List<Websites> itemList;
    private LayoutInflater inflater;

    public ListViewCustomAdapter(Activity contaxt, List<Websites> itemList){

        super();
        this.itemList = itemList;
        this.inflater = (LayoutInflater)contaxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder{

        TextView myTextViewWebsite;
        TextView myTextViewUrl;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if(view == null){
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.data, null);
            viewHolder.myTextViewWebsite = (TextView)view.findViewById(R.id.myTextViewWebsite);
            viewHolder.myTextViewUrl = (TextView)view.findViewById(R.id.myTextViewUrl);
            view.setTag(viewHolder);

        }

        else{
            viewHolder = (ViewHolder)view.getTag();
            Websites websites = (Websites)itemList.get(i);
            viewHolder.myTextViewWebsite.setText(websites.getWebsite());
            viewHolder.myTextViewUrl.setText(websites.getUrl());

        }
        return view;
    }
}
