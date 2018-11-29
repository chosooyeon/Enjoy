package com.example.sooyeon.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class UrlAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Memo> memos;

    public UrlAdapter(Context context, ArrayList<Memo> memo){
        mContext = context;
        memos = new ArrayList<>();
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        // 인플레이터로 뷰 가져옴
        convertView = mInflater.inflate(R.layout.list_view, null);

//        final LostBean bean = mList.get(position);
//        bean.setSelIdx(position);

        TextView txtTitle = convertView.findViewById(R.id.txt_title);
        CheckBox ckFavor = convertView.findViewById(R.id.btn_favorite);

        //txtTitle.setText(bean.getTitle());

        TextView txt_title = (TextView)convertView.findViewById(R.id.txt_title);
        txt_title.setText(memo.getTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BrowseActivity.class);
                intent.putExtra(URL, memo.getUrl());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
