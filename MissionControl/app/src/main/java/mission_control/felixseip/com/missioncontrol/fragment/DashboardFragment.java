package mission_control.felixseip.com.missioncontrol.fragment;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.aflak.bluetooth.Bluetooth;
import mission_control.felixseip.com.missioncontrol.MainActivity;
import mission_control.felixseip.com.missioncontrol.R;
import mission_control.felixseip.com.missioncontrol.RocketDataCallback;
import mission_control.felixseip.com.missioncontrol.RocketDataThread;

public class DashboardFragment extends Fragment {

    private Bluetooth _bluetoothController;
    private BluetoothDevice _bluetoothDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _bluetoothController = ((MainActivity) getActivity()).getBluetoothController();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Do something
        Button launchButton = getView().findViewById(R.id.start_launch_sequence);

        launchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _bluetoothController.send("^");
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dashboard, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_chart:
                //TODO: Show fragment with charts and graphs to analyze the data coming from the rocket
                //searchForRockets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startDataThread(Bluetooth controller){
        //BluetoothDevice device, RocketDataCallback rocketDataCallback
        RocketDataThread thread = new RocketDataThread(controller.getSocket(), new RocketDataCallback() {
            @Override
            public void onRocketDataReceived(String data) {
                Log.d("RocketThread", data);
            }
        });

        thread.start();
    }

    public void setBluetoothDevice(BluetoothDevice device) {
        _bluetoothDevice = device;
    }
}
