package mission_control.felixseip.com.missioncontrol;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DiscoveryCallback;
import mission_control.felixseip.com.missioncontrol.list.RocketAdapter;

public class MainActivity extends AppCompatActivity implements Serializable {

    private ConstraintLayout _mainContentView;
    private Bluetooth _bluetoothController;
    private PermissionController _permissionController;
    private RocketAdapter _adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _bluetoothController = new Bluetooth(getApplicationContext());
        _mainContentView = findViewById(R.id.main_content_view);
        _permissionController = new PermissionController(getApplicationContext(), this);
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArrayList<Rocket> rockets = new ArrayList<>();
        /*TODO: Create list subitems to show the devices that have already paired with the app.
                2 items (paired and unpaired) with subitems.
                If an unpaired item is clicked, pair it and then connect.

                Alternative to this would be to perform a pair and connect operation on the device.
                Down side of this is the extra operation needed to connect to the device

                Currently, the device has to be paired through the settings of the android phone
        */

        _adapter = new RocketAdapter(this, rockets);
        ListView listView = (ListView) findViewById(R.id.rocket_list);
        listView.setAdapter(_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rocket rocket = _adapter.getItem(position);

                //Stop searching for bluetooth devices
                _bluetoothController.stopScanning();

                //Connect to the selected device
                _bluetoothController.connectToAddress(rocket.getBluetoothDevice().getAddress());

                Toast.makeText(getApplicationContext(), "Connected to device: " + rocket.getBluetoothDevice().getName(), Toast.LENGTH_LONG);

                //Start a new Intent so that the device can be controlled using the application
                Intent intent = new Intent();
                intent.putExtra("Rocket", rocket);
                intent.putExtra("PreviousActivity", getCallingActivity());
                intent.setClass();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if(_permissionController.checkAppPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
                searchForRockets();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void searchForRockets(){
        if(_bluetoothController.isEnabled()){
            _bluetoothController.startScanning();

            _bluetoothController.setDiscoveryCallback(new DiscoveryCallback() {
                @Override
                public void onDiscoveryStarted() {
                    _adapter.clear();
                    final Snackbar snackbar = Snackbar.make(_mainContentView, "Scanning for nearby rockets...", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Stop", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _bluetoothController.stopScanning();
                            Snackbar.make(_mainContentView, "Scan stopped...", Snackbar.LENGTH_SHORT).show();
                            snackbar.dismiss();
                        }
                    });

                    snackbar.show();
                }

                @Override
                public void onDiscoveryFinished() { }

                @Override
                public void onDeviceFound(final BluetoothDevice device) {
                    if(device != null){
                        if(device.getType() == 1){
                            _adapter.add(new Rocket(device.getAddress(), device.getType() + "",device.getName(),"1.0", device));
                        }
                    }
                }

                @Override
                public void onDevicePaired(BluetoothDevice device) {
                    _bluetoothController.stopScanning();
                }

                @Override public void onDeviceUnpaired(BluetoothDevice device) {
                    _bluetoothController.startScanning();
                }

                @Override public void onError(String message) {}
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        _permissionController.checkAppPermissions();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(_bluetoothController.isEnabled()){
            _bluetoothController.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(_bluetoothController.isConnected()){
            _bluetoothController.disconnect();
        }
    }

    public Bluetooth getBluetoothController(){
        return _bluetoothController;
    }
}
