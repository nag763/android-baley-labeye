package com.ticandroid.baley_labeye.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticandroid.baley_labeye.R;

public class StatListHolder extends RecyclerView.ViewHolder{
    private transient final TextView title;
    //private transient final TextView numberVisits;
    private transient final TextView note;
    private transient final TextView distance;

    private final static int CARD_TITLE = R.id.textTitle;
   // private final static int CARD_NUMBER_VISITS = R.id.textNumberVisits;
    private final static int CARD_NOTE = R.id.textNote;
    private final static int CARD_DISTANCE = R.id.textDistance;

    public StatListHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(CARD_TITLE);
        //this.numberVisits = itemView.findViewById(CARD_NUMBER_VISITS);
        this.note = itemView.findViewById(CARD_NOTE);
        this.distance = itemView.findViewById(CARD_DISTANCE);
    }

    public void setTextInTitleView(String title){
        this.title.setText(title);
    }
   // public void setTextInNumberVisitsView(String numberVisits) { this.numberVisits.setText(numberVisits);}
    public void setTextInNoteView(String note) { this.note.setText(note);}
    public void setTextInDistanceView(String distance) { this.distance.setText(distance);}
}
