package com.example.maneeshsagar.imload;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;



public class AdminImageAdapter extends RecyclerView.Adapter<AdminImageAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;
    String foldername;

    public AdminImageAdapter(Context context, List<Upload> uploads,String foldername) {
        this.uploads = uploads;
        this.context = context;
        this.foldername=foldername;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Upload upload = uploads.get(position);

        holder.textViewName.setText(upload.getName());

        Glide.with(context).load(upload.getUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public void removeItem(int position)
    {
        uploads.remove(position);
        notifyItemRemoved(position);
    }
    public void restoreItem(Upload upload,int position)
    {
         uploads.add(position,upload);
         notifyItemInserted(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textViewName;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent=new Intent(context,ImageDetail.class);
            intent.putExtra("key",uploads.get(getAdapterPosition()).getKey());
            intent.putExtra("url",uploads.get(getAdapterPosition()).getUrl());
            intent.putExtra("name",uploads.get(getAdapterPosition()).getName());
            intent.putExtra("foldername",foldername);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        }
    }
}
