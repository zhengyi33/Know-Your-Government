package com.yizheng.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialViewHolder extends RecyclerView.ViewHolder {

    TextView titleText, nameText;

    public OfficialViewHolder(@NonNull View itemView) {
        super(itemView);

        titleText = itemView.findViewById(R.id.titleText);
        nameText = itemView.findViewById(R.id.nameText);
    }
}
