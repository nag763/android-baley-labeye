package com.ticandroid.baley_labeye.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticandroid.baley_labeye.R;

/**
 * Holder of a step list to be used in a recylcer view.
 *
 * @author Baley
 * @author Labeye
 */
public class StepListHolder extends RecyclerView.ViewHolder {

    /**
     * Street name in TextView
     **/
    private transient final TextView tvStreetName;
    /**
     * Text view displaying the distance and duration
     **/
    private transient final TextView tvDistanceDuration;
    /**
     * Instruction to be followed
     **/
    private transient final TextView tvInstruction;

    /**
     * Matching text view element in card view
     **/
    private final static int CARD_STREET_NAME = R.id.textStreetName;
    /**
     * Matching text view element in card view
     **/
    private final static int CARD_DISTANCE_DURATION = R.id.textDistanceDuration;
    /**
     * Matching text view element in card view
     **/
    private final static int CARD_INSTRUCTION = R.id.textInstruction;

    /**
     * Constructor initiliasing the text view elements
     *
     * @param itemView current view where the holder is being called
     */
    public StepListHolder(@NonNull View itemView) {
        super(itemView);

        this.tvStreetName = itemView.findViewById(CARD_STREET_NAME);
        this.tvDistanceDuration = itemView.findViewById(CARD_DISTANCE_DURATION);
        this.tvInstruction = itemView.findViewById(CARD_INSTRUCTION);

    }

    /**
     * @param textInTvStreetName text to be displayhed in the street name tv
     */
    public void setTextInTvStreetName(String textInTvStreetName) {
        this.tvStreetName.setText(textInTvStreetName);
    }

    /**
     * @param textInTvDistanceDuration text to be displayed in the distance duration tv
     */
    public void setTextInTvDistanceDuration(String textInTvDistanceDuration) {
        this.tvDistanceDuration.setText(textInTvDistanceDuration);
    }

    /**
     * @param textInTvInstruction instruction to be displayed in the instruction tv
     */
    public void setTextInTvInstruction(String textInTvInstruction) {
        this.tvInstruction.setText(textInTvInstruction);
    }
}
