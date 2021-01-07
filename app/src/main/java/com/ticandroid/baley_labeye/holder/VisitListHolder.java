package com.ticandroid.baley_labeye.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticandroid.baley_labeye.R;

/**
 * Holder used to bind our data into our visit card layout.
 *
 * @author Baley
 * @author Labeye
 * @see androidx.recyclerview.widget.RecyclerView.ViewHolder
 */
public class VisitListHolder extends RecyclerView.ViewHolder {

    /**
     * Title of the museum.
     **/
    private transient final TextView title;
    /**
     * Date of the visit.
     **/
    private transient final TextView visitedOn;

    /**
     * Card title element.
     **/
    private final static int CARD_TITLE = R.id.textTitle;
    /**
     * Card visited on element.
     **/
    private final static int CARD_VISITED_ON = R.id.textVisitedOn;


    /**
     * Creates a visit holder for the museum list.
     *
     * @param itemView layout to inflate
     */
    public VisitListHolder(@NonNull View itemView) {
        super(itemView);

        this.title = itemView.findViewById(CARD_TITLE);
        this.visitedOn = itemView.findViewById(CARD_VISITED_ON);
    }

    /**
     * Set the title in the layout element.
     *
     * @param title the title as a string to display.
     **/
    public void setTextInTitleView(String title) {
        this.title.setText(title);
    }

    /**
     * Set the date in the layout element.
     *
     * @param visitedOn the date as a string to display
     */
    public void setTextInVisitedOnView(String visitedOn) {
        this.visitedOn.setText(visitedOn);
    }
}
