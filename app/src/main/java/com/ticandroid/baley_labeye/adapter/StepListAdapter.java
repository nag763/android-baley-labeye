package com.ticandroid.baley_labeye.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ticandroid.baley_labeye.R;
import com.ticandroid.baley_labeye.beans.StepBean;
import com.ticandroid.baley_labeye.holder.StepListHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Step list adapter used to display elegently our elements.
 *
 * @author Labeye
 */
public class StepListAdapter extends RecyclerView.Adapter<StepListHolder> {

    /**
     * List of elements to display.
     **/
    private transient final StepBean[] stepBeans;

    private transient static final SimpleDateFormat FORMATTER = new SimpleDateFormat(
            "HH' heures 'mm' minutes 'ss' secondes'", Locale.FRANCE
    );

    /**
     * @param stepBeans steps to be displayed in the adapter.
     */
    public StepListAdapter(StepBean... stepBeans) {
        this.stepBeans = stepBeans.clone();
    }

    @NonNull
    @Override
    public StepListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_view_list_instruction, parent, false);
        Log.d(this.getClass().toString(), "view holder created");

        FORMATTER.setTimeZone(TimeZone.getTimeZone("GMT"));

        return new StepListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListHolder holder, int position) {
        final StepBean currentStep = stepBeans[position];
        final String streetName = String.format("Etape %s : %s", position + 1, currentStep.getRoadName());

        final String distanceDuration = String.format("Dans %s - à %s kms",
                FORMATTER.format(
                        new Date((long) currentStep.getDuration() * 1000)
                ),
                currentStep.getDistance()
        );
        final String instruction = currentStep.getInstruction();

        holder.setTextInTvStreetName(streetName);
        holder.setTextInTvDistanceDuration(distanceDuration);
        holder.setTextInTvInstruction(instruction);
    }

    @Override
    public int getItemCount() {
        return stepBeans.length;
    }
}