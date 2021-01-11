package com.ticandroid.baley_labeye.utils;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.ticandroid.baley_labeye.R;

/**
 * Handle common errors in app
 *
 * @version 0.1
 * @author Labeye
 */
public final class ErrorHandler {

    /** Make class not instanciable **/
    private ErrorHandler(){}

    /**
     * Error generated if element not found
     * @param appCompatActivity current context
     */
    public static void NotFound(AppCompatActivity appCompatActivity){
        infoAndExit(
                appCompatActivity,
                appCompatActivity.getString(R.string.err_not_found)
        );
    }

    /**
     * Error generated if the process fails
     * @param appCompatActivity current context
     */
    public static void Failure(AppCompatActivity appCompatActivity){
        infoAndExit(
                appCompatActivity,
                appCompatActivity.getString(R.string.err_failure)
        );
    }

    /**
     * Display a message and exit the problematic context
     * @param appCompatActivity context where the issue appears
     * @param displayable text for the user
     */
    private static void infoAndExit(AppCompatActivity appCompatActivity, String displayable){
        Toast.makeText(appCompatActivity, displayable, Toast.LENGTH_LONG).show();
        appCompatActivity.finish();
    }

}
