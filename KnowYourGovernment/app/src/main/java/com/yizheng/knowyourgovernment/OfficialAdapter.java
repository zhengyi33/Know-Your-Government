package com.yizheng.knowyourgovernment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private ArrayList<Official> officials;
    private MainActivity mainActivity;

    public OfficialAdapter(ArrayList<Official> officials, MainActivity mainActivity) {
        this.officials = officials;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_item, parent, false);
        itemView.setOnClickListener(mainActivity);

        return new OfficialViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {
        Official o = officials.get(position);
        holder.titleText.setText(o.getTitle());
        String name = o.getName();
        String party = o.getParty();
        String nameParty;
        if (party.equals("Republican")||party.equals("Democratic")) {
            nameParty = name + " (" + party + " Party)";
        }
        else {
            nameParty = name + " (" + party + ")";
        }
        holder.nameText.setText(nameParty);
    }

    @Override
    public int getItemCount() {
        return officials.size();
    }
}
