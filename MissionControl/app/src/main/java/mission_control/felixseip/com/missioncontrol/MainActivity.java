package mission_control.felixseip.com.missioncontrol;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.Serializable;
import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;
import mission_control.felixseip.com.missioncontrol.controller.FragmentController;
import mission_control.felixseip.com.missioncontrol.controller.PermissionController;
import mission_control.felixseip.com.missioncontrol.fragment.DashboardFragment;
import mission_control.felixseip.com.missioncontrol.fragment.MainFragment;

public class MainActivity extends AppCompatActivity implements Serializable {

    private Bluetooth _bluetoothController;
    private PermissionController _permissionController;
    private MainFragment _mainFragment;
    private DashboardFragment _dashboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _bluetoothController = new Bluetooth(getApplicationContext());
        _permissionController = new PermissionController(getApplicationContext(), this);
        FragmentController.setFragmentManager(getSupportFragmentManager());

        _mainFragment = new MainFragment();
        _dashboardFragment = new DashboardFragment();

        FragmentController.switchFragment(_mainFragment, R.id.fragment_container, null);

        setupBluetoothCallbacks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        _permissionController.checkAppPermissions();
        _bluetoothController.onStart();
        _bluetoothController.enable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(_bluetoothController.isEnabled()){
            _bluetoothController.onStop();
            _bluetoothController.disable();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(_bluetoothController.isConnected()){
            _bluetoothController.disconnect();
        }
    }

    private void setupBluetoothCallbacks(){
        _bluetoothController.setDeviceCallback(new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                FragmentController.switchFragment(_dashboardFragment, R.id.fragment_container, null);
                _dashboardFragment.setBluetoothDevice  (device);
                _dashboardFragment.startDataThread();
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {

            }

            @Override
            public void onMessage(String message) {

            }

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Failed to connect to device: " + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {

            }
        });

        _bluetoothController.setDiscoveryCallback(new DiscoveryCallback() {
            @Override
            public void onDiscoveryStarted() {
                _mainFragment.showScanningSnackbar();
            }

            @Override
            public void onDiscoveryFinished() {
            }

            @Override
            public void onDeviceFound(final BluetoothDevice device) {
                if (device != null) {
                    if (device.getType() == 1) {
                        _mainFragment.addAvailableDevice(device);
                    }
                }
            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {
                _bluetoothController.stopScanning();
            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {
                _bluetoothController.startScanning();
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    public Bluetooth getBluetoothController(){
        return _bluetoothController;
    }
}
