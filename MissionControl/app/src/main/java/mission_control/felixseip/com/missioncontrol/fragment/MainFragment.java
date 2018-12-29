package mission_control.felixseip.com.missioncontrol.fragment;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import me.aflak.bluetooth.Bluetooth;
import mission_control.felixseip.com.missioncontrol.MainActivity;
import mission_control.felixseip.com.missioncontrol.R;
import mission_control.felixseip.com.missioncontrol.model.Rocket;
import mission_control.felixseip.com.missioncontrol.list.RocketAdapter;

public class MainFragment extends Fragment {

    private ConstraintLayout _mainContentView;
    private Bluetooth _bluetoothController;
    private RocketAdapter _adapter;
    private Snackbar _discoverySnackbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _bluetoothController = ((MainActivity) getActivity()).getBluetoothController();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                searchForRockets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showScanningSnackbar() {
        _adapter.clear();
        _discoverySnackbar = Snackbar.make(_mainContentView, "Scanning for nearby rockets...", Snackbar.LENGTH_INDEFINITE);
        _discoverySnackbar.setAction("Stop", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _bluetoothController.stopScanning();
                Snackbar.make(_mainContentView, "Scan stopped...", Snackbar.LENGTH_SHORT).show();
                _discoverySnackbar.dismiss();
            }
        });

        _discoverySnackbar.show();
    }

    public void addAvailableDevice(BluetoothDevice device) {
        _adapter.add(new Rocket(device.getAddress(), device.getType() + "", device.getName(), "1.0", device));
    }

    private void init() {
        _mainContentView = getView().findViewById(R.id.main_content_view);

        ArrayList<Rocket> rockets = new ArrayList<>();
        /*TODO: Create list subitems to show the devices that have already paired with the app.
                2 items (paired and unpaired) with subitems.
                If an unpaired item is clicked, pair it and then connect.

                Alternative to this would be to perform a pair and connect operation on the device.
                Down side of this is the extra operation needed to connect to the device

                Currently, the device has to be paired through the settings of the android phone
        */

        _adapter = new RocketAdapter(getContext(), rockets);
        ListView listView = getView().findViewById(R.id.rocket_list);
        listView.setAdapter(_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Rocket rocket = _adapter.getItem(position);

                _discoverySnackbar.dismiss();

                //Stop searching for bluetooth devices
                try{
                    _bluetoothController.stopScanning();
                }catch (Exception ex){
                    ex.printStackTrace();
                }

                //Connect to the selected device
                Toast.makeText(getContext(), "Connecting to device: " + rocket.getBluetoothDevice().getName(), Toast.LENGTH_LONG).show();
                _bluetoothController.connectToAddress(rocket.getBluetoothDevice().getAddress());
            }
        });
    }

    private void searchForRockets() {
        _bluetoothController.startScanning();
    }
}
