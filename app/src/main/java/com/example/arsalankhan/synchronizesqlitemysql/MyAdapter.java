package com.example.arsalankhan.synchronizesqlitemysql;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arsalan khan on 8/14/2017.
 */

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    ArrayList<contract> arrayList = new ArrayList<>();

    public MyAdapter(ArrayList<contract> contractArrayList){
        arrayList = contractArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        contract contract = arrayList.get(position);
        holder.textViewName.setText(contract.getName());

        if(contract.getSyncState()== dbContract.SYNC_STATE_OK){
            holder.imageView_syncState.setImageResource(R.drawable.sync);
        }
        else{
            holder.imageView_syncState.setImageResource(R.drawable.unsync);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private ImageView imageView_syncState;
        public MyViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.tv_Name);
            imageView_syncState = itemView.findViewById(R.id.image_syncState);
        }
    }
}
