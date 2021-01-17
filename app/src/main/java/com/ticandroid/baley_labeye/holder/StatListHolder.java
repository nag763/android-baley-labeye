package com.ticandroid.baley_labeye.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticandroid.baley_labeye.R;

/**
 * @author baley labeye
 * stat list holder
 */
public class StatListHolder extends RecyclerView.ViewHolder {
    /**
     * title of the museum
     */
    private transient final TextView title;
    /**
     * note of the museum
     */
    private transient final TextView note;
    /**
     * distance of all users made for this museum
     */
    private transient final TextView distance;
    /**
     * card title
     */
    private final static int CARD_TITLE = R.id.lbl_title;
    /**
     * card note
     */
    private final static int CARD_NOTE = R.id.lbl_rating;
    /**
     * card distance
     */
    private final static int CARD_DISTANCE = R.id.lbl_distance;

    /**
     * stat list holder
     * @param itemView the item view
     */
    public StatListHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(CARD_TITLE);
        this.note = itemView.findViewById(CARD_NOTE);
        this.distance = itemView.findViewById(CARD_DISTANCE);
    }

    /**
     * method to set the title
     * @param title title of the museum
     */
    public void setTextInTitleView(String title) {
        this.title.setText(title);
    }

    /**
     * method to set the note
     * @param note note of the museum
     */
    public void setTextInNoteView(String note) {
        this.note.setText(String.format("%s %s %s", "Evaluation moyenne : ", note, "/5"));
    }

    /**
     * method to set the distance
     * @param distance distance parcoured
     */
    public void setTextInDistanceView(String distance) {
        this.distance.setText(String.format("%s %s %s", "Distance totale parcourue : ", distance, " km"));
    }
}
