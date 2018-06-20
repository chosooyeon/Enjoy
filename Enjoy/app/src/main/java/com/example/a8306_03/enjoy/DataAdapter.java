package com.example.a8306_03.enjoy;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> implements Filterable {
    private ArrayList<Url> mArrayList;
    private ArrayList<Url> mFilteredList;

    public DataAdapter(ArrayList<Url> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_url, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, int i) {

        viewHolder.url_image.setText(mFilteredList.get(i).getPicture());
        viewHolder.title_text.setText(mFilteredList.get(i).getTitle());
        viewHolder.url_text.setText(mFilteredList.get(i).getUrl());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Url> filteredList = new ArrayList<>();

                    for (Url androidVersion : mArrayList) {

                        if (androidVersion.getUrl().toLowerCase().contains(charString) || androidVersion.getPicture().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Url>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView url_image,title_text,url_text;
        public ViewHolder(View view) {
            super(view);

            url_image = (TextView)view.findViewById(R.id.url_image);
            title_text = (TextView)view.findViewById(R.id.title_text);
            url_text = (TextView)view.findViewById(R.id.url_text);

        }
    }

}