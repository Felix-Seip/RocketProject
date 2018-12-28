package mission_control.felixseip.com.missioncontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.io.Serializable;
import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity implements Serializable {

    private Bluetooth _bluetoothController;
    private PermissionController _permissionController;
    private FragmentController _fragmentController;
    private MainFragment _mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _bluetoothController = new Bluetooth(getApplicationContext());
        _permissionController = new PermissionController(getApplicationContext(), this);
        _fragmentController = new FragmentController(getSupportFragmentManager());

        _mainFragment = new MainFragment();

        _fragmentController.switchFragment(_mainFragment, R.id.fragment_container, null);
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

    public Bluetooth getBluetoothController(){
        return _bluetoothController;
    }

    public FragmentController getFragmentController(){
        return _fragmentController;
    }
}
