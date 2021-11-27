package com.kuwathsala.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kuwathsala.music.models.SingleMusic;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView name;

        public ViewHolder(@NonNull View view) {
            super(view);
            containerView = view.findViewById(R.id.music_row);
            name = view.findViewById(R.id.row_music_name);
        }
    }

    private List<File> objects = new ArrayList<>();
    private List<String> items = new ArrayList<>();
    private Context context;
    ViewHolder alreadySelectedViewHolder;

    public MusicAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicAdapter.ViewHolder holder, final int position) {
        holder.name.setText(items.get(position));
        holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorGray));
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                NowPlay play = NowPlay.getInstance();
                SingleMusic music = SingleMusic.getInstance();
                music.setStart(true);
                play.pos = position;
                play.initMusicPlayer(position);

                holder.name.setTextColor(ContextCompat.getColor(context, R.color.colorLight));

                if (alreadySelectedViewHolder != null)
                    alreadySelectedViewHolder.name.setTextColor(ContextCompat.getColor(context, R.color.colorGray));

                alreadySelectedViewHolder = holder;

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

