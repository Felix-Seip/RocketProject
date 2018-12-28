package mission_control.felixseip.com.missioncontrol;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentController {

    public enum RocketFragment {
        RocketFinder,
        RocketControls,
        RocketMonitor
    }

    private FragmentManager _fragmentManager;

    public FragmentController(FragmentManager fragmentManager){
        _fragmentManager = fragmentManager;
    }

    public void switchFragment(Fragment fragment, int layoutId, String message){
        // Create new fragment and transaction

        FragmentTransaction transaction = _fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(layoutId, fragment);
        transaction.addToBackStack(message);

        // Commit the transaction
        transaction.commit();
    }

}
