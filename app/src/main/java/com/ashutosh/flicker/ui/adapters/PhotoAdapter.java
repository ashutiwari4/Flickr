package com.ashutosh.flicker.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashutosh.flicker.R;
import com.ashutosh.flicker.data.PhotoLoder;
import com.ashutosh.flicker.modals.PhotoModal;
import com.ashutosh.flicker.remote.WebUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ItemHolder> {

    private final Context context;
    private List<PhotoModal> itemsName;
    private OnItemClickListener onItemClickListener;
    private LayoutInflater layoutInflater;

    public PhotoAdapter(Context context, List<PhotoModal> itemsName) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.itemsName = itemsName;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public PhotoAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_photo, parent, false);
        return new ItemHolder(itemView, this);

    }

    @Override
    public void onBindViewHolder(PhotoAdapter.ItemHolder holder, int position) {
        holder.setItem(itemsName.get(position), context, position);
    }

    @Override
    public int getItemCount() {
        return itemsName.size();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public void remove(int location) {
        if (location >= itemsName.size())
            return;

        itemsName.remove(location);
        notifyItemRemoved(location);
    }

    public interface OnItemClickListener {
        void onItemClick(ItemHolder item, PhotoModal photoModal, int position) throws JSONException;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivItem;
        private PhotoAdapter parent;
        private PhotoModal movieData;

        public ItemHolder(View itemView, PhotoAdapter parent) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.parent = parent;
            ivItem = (ImageView) itemView;

        }

        public void setItem(PhotoModal itemObject, Context context, int position) {
            this.movieData = itemObject;
            Glide.with(context).load(itemObject.getPhotoUrl()).placeholder(R.drawable.placeholder).centerCrop().error(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivItem);
        }

        @Override
        public void onClick(View v) {
            final OnItemClickListener listener = parent.getOnItemClickListener();
            if (listener != null) {
                try {
                    listener.onItemClick(this, movieData, getPosition());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}