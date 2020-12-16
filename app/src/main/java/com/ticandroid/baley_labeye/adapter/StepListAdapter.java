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
 */
public class StepListAdapter extends RecyclerView.Adapter<StepListHolder> {

    /** List of elements to display. **/
    private transient final StepBean[] stepBeans;

    /**
     *
     * @param stepBeans steps to be displayed in the adapter.
     */
    public StepListAdapter(StepBean[] stepBeans) {
        this.stepBeans = stepBeans;
    }

    @NonNull
    @Override
    public StepListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list_instruction, parent, false);
        Log.d(this.getClass().toString(), "view holder created");
        return new StepListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepListHolder holder, int position) {
        final StepBean currentStep = stepBeans[position];
        // TODO : PASS TO CLASS
        final String STREET_NAME = String.format("Etape %s : %s", ++position, currentStep.getRoadName());
        SimpleDateFormat formatter = new SimpleDateFormat(
                "HH' heures 'mm' minutes 'ss' secondes'", Locale.FRANCE
        );
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String DISTANCE_DURATION = String.format("Dans %s - à %s kms", formatter.format(new Date((long) currentStep.getDuration() * 1000)), currentStep.getDistance());
        final String INSTRUCTION = currentStep.getInstruction();

        holder.setTextInTvStreetName(STREET_NAME);
        holder.setTextInTvDistanceDuration(DISTANCE_DURATION);
        holder.setTextInTvInstruction(INSTRUCTION);
    }

    @Override
    public int getItemCount() {
        return stepBeans.length;
    }
}
