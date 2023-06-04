package com.example.yolijoli;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class coAdapter extends FirestoreRecyclerAdapter<coInfo, coAdapter.coViewHolder> {
    Context context;

    public coAdapter(@NonNull FirestoreRecyclerOptions<coInfo> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull coViewHolder holder, int position, @NonNull coInfo coinfo) {
        holder.coTitleTextView.setText(coinfo.coTitle);
        holder.coContentTextView.setText(coinfo.coContent);
        holder.timestTextView.setText(Utility.timestampToString(coinfo.timest));

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,coDetailsActivity.class);
            intent.putExtra("coTitle",coinfo.coTitle);
            intent.putExtra("coContent",coinfo.coContent);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);

            context.startActivity(intent);
        });



    }

    @NonNull
    @Override
    public coViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_co_item,parent,false);
        return new coViewHolder(view);
    }

    class coViewHolder extends RecyclerView.ViewHolder{
        TextView coTitleTextView,coContentTextView,timestTextView;

        Button chatBtn;

        public coViewHolder(@NonNull View itemView) {
            super(itemView);
            coTitleTextView = itemView.findViewById(R.id.co_title_text_view);
            coContentTextView = itemView.findViewById(R.id.co_content_text_view);
            timestTextView = itemView.findViewById(R.id.co_timestamp_text_view);

        }
    }
}
