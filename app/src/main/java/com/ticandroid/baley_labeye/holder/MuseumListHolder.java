package com.ticandroid.baley_labeye.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticandroid.baley_labeye.R;

/**
 * Holder used to bind our data into our museum card layout.
 *
 * @author Baley
 * @author Labeye
 * @see androidx.recyclerview.widget.RecyclerView.ViewHolder
 */
public class MuseumListHolder extends RecyclerView.ViewHolder {

    /**
     * Title of the museum.
     **/
    private transient final TextView title;
    /**
     * Location of the museum.
     **/
    private transient final TextView location;
    /**
     * Phone number of the museum.
     **/
    private transient final TextView phoneNumber;

    /**
     * Card title element.
     **/
    private final static int CARD_TITLE = R.id.textTitle;
    /**
     * Card location element.
     **/
    private final static int CARD_LOCATION = R.id.textVisitedOn;
    /**
     * Card phone element.
     **/
    private final static int CARD_PHONE = R.id.textPhoneNumber;


    /**
     * Creates a museum holder for the museum list.
     *
     * @param itemView layout to inflate
     */
    public MuseumListHolder(@NonNull View itemView) {
        super(itemView);

        this.title = itemView.findViewById(CARD_TITLE);
        this.location = itemView.findViewById(CARD_LOCATION);
        this.phoneNumber = itemView.findViewById(CARD_PHONE);
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
     * Set the location in the layout element.
     *
     * @param location the location as a string to display
     */
    public void setTextInLocationView(String location) {
        this.location.setText(location);
    }

    /**
     * Set the phone number in the layout element.
     *
     * @param phoneNumber the phone number as a string to display
     */
    public void setTextInPhoneNumberView(String phoneNumber) {
        this.phoneNumber.setText(phoneNumber);
    }
}
