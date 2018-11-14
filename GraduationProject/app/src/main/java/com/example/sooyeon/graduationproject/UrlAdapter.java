package com.example.sooyeon.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UrlAdapter extends BaseAdapter {

    private ArrayList<Memo> memos;

    public UrlAdapter(){
        memos = new ArrayList<>();
    }

    public boolean hasDuplicate(Memo memo){
        return memos.contains(memo);
    }

    public void addMemo(Memo memo){
        memos.add(memo);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return memos.size();
    }

    @Override
    public Object getItem(int position) {
        return memos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        final Memo memo = memos.get(position);
        final String URL = "url";

        //convertView가 없다면 item을 inflate한다
        if(convertView ==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view,parent,false);
            Log.i("Log............" , memo.getTitle());
        }

        TextView txt_title = (TextView)convertView.findViewById(R.id.txt_title);
        txt_title.setText(memo.getTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrowseActivity.class);
                intent.putExtra(URL, memo.getUrl());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
