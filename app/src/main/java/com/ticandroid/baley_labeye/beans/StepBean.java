package com.ticandroid.baley_labeye.beans;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Step bean to modelise our different steps in
 * our route.
 *
 * @author Baley
 * @author Labeye
 */
public class StepBean {

    /**
     * distance travelled since last step
     **/
    private double distance;
    /**
     * duration since last step
     **/
    private int duration;
    /**
     * what to do at this point
     **/
    private String instruction;
    /**
     * current road name
     **/
    private String roadName;

    /**
     * default constructor
     **/
    public StepBean() {
    }

    /**
     * Create a StepBean from a JsonObject
     *
     * @param jsonObject
     */
    public StepBean(JSONObject jsonObject) throws JSONException {
        this.distance = jsonObject.optDouble("distance", 0);
        this.duration = jsonObject.optInt("duration", 0);
        this.instruction = jsonObject.optString("instruction", "");
        this.roadName = jsonObject.optString("name", "");
    }

    /**
     * @return the distance since the last step
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return the time elapsed since the last step
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @return the action to do to go on next step
     */
    public String getInstruction() {
        return instruction;
    }


    /**
     * @return the current road name
     */
    public String getRoadName() {
        return roadName;
    }


    @Override
    public String toString() {
        return "Step{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", instruction='" + instruction + '\'' +
                ", name='" + roadName + '\'' +
                '}';
    }


}
