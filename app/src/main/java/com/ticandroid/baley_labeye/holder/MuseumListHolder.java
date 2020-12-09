package com.ticandroid.baley_labeye.holder;

import com.ticandroid.baley_labeye.R;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MuseumListHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView location;
    private final TextView phoneNumber;

    private final static int CARD_TITLE = R.id.textTitle;
    private final static int CARD_LOCATION = R.id.textLocation;
    private final static int CARD_PHONE = R.id.textPhoneNumber;


    public MuseumListHolder(@NonNull View itemView) {
        super(itemView);

        this.title = itemView.findViewById(CARD_TITLE);
        this.location = itemView.findViewById(CARD_LOCATION);
        this.phoneNumber = itemView.findViewById(CARD_PHONE);
    }

    public void setTextInTitleView(String title){
        this.title.setText(title);
    }

    public void setTextInLocationView(String location){
        this.location.setText(location);
    }

    public void setPhoneNumber(String phoneNumber){
        this.title.setText(phoneNumber);
    }
}
