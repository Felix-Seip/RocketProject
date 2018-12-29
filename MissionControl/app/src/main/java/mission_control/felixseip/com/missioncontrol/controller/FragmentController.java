package mission_control.felixseip.com.missioncontrol.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentController {

    public enum RocketFragment {
        RocketFinder,
        RocketControls,
        RocketMonitor
    }

    private static FragmentManager _fragmentManager;

    public static void switchFragment(Fragment fragment, int layoutId, String message){
        // Create new fragment and transaction

        FragmentTransaction transaction = _fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(message);

        // Commit the transaction
        transaction.commit();
    }

    public static void switchFragment(Fragment fragment, int layoutId, String message, Bundle extras){
        // Create new fragment and transaction

        FragmentTransaction transaction = _fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(message);

        // Commit the transaction
        transaction.commit();
    }

    public static void setFragmentManager(FragmentManager fragmentManager){
        _fragmentManager = fragmentManager;
    }

}
