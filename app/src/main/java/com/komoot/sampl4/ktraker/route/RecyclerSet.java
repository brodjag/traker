package com.komoot.sampl4.ktraker.route;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.komoot.sampl4.ktraker.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by brodjag on 17.10.15.
 */
public class RecyclerSet {
    RouteActivity con;
    RecyclerViewAdapter adapter;
    RecyclerView myList;
    public RecyclerSet(RouteActivity c){
        con=c;
        LinearLayoutManager layoutManager = new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setStackFromEnd(true); layoutManager.setReverseLayout(true);

         myList = (RecyclerView) con.findViewById(R.id.recyclerView);
        myList.setLayoutManager(layoutManager);
        adapter=new RecyclerViewAdapter(con.db.getImages(""+con.routeID));
        myList.setAdapter(adapter);

    }

  public void addItem(String url){
        adapter.records.add(url);
        adapter.notifyItemInserted(adapter.records.size() - 1);
      myList.scrollToPosition(adapter.getItemCount()-1);
      //myList.scrollToPosition(0);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        public List<String> records;

        public RecyclerViewAdapter(List<String> records) {
            this.records = records;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            String url = records.get(i);
            try {
                Picasso.with(con).load(url).into(viewHolder.img);
            }catch (Exception e){}

        }

        @Override
        public int getItemCount() {
            return records.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
           // private TextView name;
            private ImageView img;

            public ViewHolder(View itemView) {
                super(itemView);
                //name = (TextView) itemView.findViewById(R.id.recyclerViewItemName);
                img = (ImageView) itemView.findViewById(R.id.ItemImg);
            }
        }
    }
}
