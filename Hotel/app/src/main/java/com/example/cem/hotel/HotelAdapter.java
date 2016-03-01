package com.example.cem.hotel;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyMainViewHolder> {


    private List<Hotel> hotel;

    public HotelAdapter(List<Hotel> hotel) {
        this.hotel = hotel;
    }

    public HotelAdapter(){
        
    }

    public class MyMainViewHolder extends RecyclerView.ViewHolder {
        TextView groupNameTextView;
        CardView cv;
//        LinearLayout linearLayout;

        public MyMainViewHolder(View itemView) {
            super(itemView);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.mainLayout);
            cv = (CardView)itemView.findViewById(R.id.cv);
            groupNameTextView = (TextView) itemView.findViewById(R.id.content);
//            groupNotesTextView = (TextView) itemView.findViewById(R.id.group_notes_TextView);
//            groupImgFull = (ImageView) itemView.findViewById(R.id.group_Img_Full);
        }

    }

    public void deleteAll(){
        hotel.clear();
        notifyDataSetChanged();
//        notifyItemRemoved(position);
    }



    @Override
    public MyMainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false);
        MyMainViewHolder holder = new MyMainViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyMainViewHolder holder, final int position) {
        holder.groupNameTextView.setText("Name: "+ hotel.get(position).getHotelName() +"\nAddress: " + hotel.get(position).getHotelAddress());
    }



    @Override
    public int getItemCount() {
        return hotel.size();
    }

}