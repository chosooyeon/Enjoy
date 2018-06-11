package com.example.a8306_03.enjoy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterUrl extends BaseAdapter {
    private List<Url> mData;
    private Map<String,Integer> mUrlImageMap;

    public AdapterUrl(List<Url> data){
        this.mData = data;
        //사진이라는 picutre데이터가 들어갔을 때 나올 이미지 설정
        mUrlImageMap = new HashMap<>();
        mUrlImageMap.put("사진",R.drawable.ic_launcher_background);
    }
    @Override
    public int getCount() { //아이템 갯수
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_url, viewGroup, false);

            ImageView urlImage = view.findViewById(R.id.url_image);
            TextView titleText = view.findViewById(R.id.title_text);
            TextView urlText = view.findViewById(R.id.url_text);

            holder.urlImage = urlImage;
            holder.titeText = titleText;
            holder.urlText = urlText;

            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        Url url = mData.get(i);
        holder.titeText.setText(url.getTitle());
        holder.urlText.setText(url.getUrl());
        holder.urlImage.setImageResource(mUrlImageMap.get(url.getPicture()));

        return view;
    }
    //holder를 통해 최적화
    static class ViewHolder{
        ImageView urlImage;
        TextView titeText;
        TextView urlText;
    }
}
