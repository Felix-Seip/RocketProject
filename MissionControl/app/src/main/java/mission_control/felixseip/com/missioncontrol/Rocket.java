package mission_control.felixseip.com.missioncontrol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.Serializable;
import java.util.List;

public class Rocket implements Serializable {

    private String _id;
    private String _type;
    private String _name;
    private String _revision;
    private boolean _reachable;
    private List<FlightDataItem> _rocketData;
    private BluetoothDevice _device;

    public Rocket(String id, String type, String name, String revision, BluetoothDevice device){
        _id = id;
        _type = type;
        _name = name;
        _revision = revision;
        _device = device;
    }

    public String getId(){
        return _id;
    }

    public String getType(){
        return _type;
    }

    public String getName(){
        return _name;
    }

    public String getRevision(){
        return _revision;
    }

    public boolean isRocketReachable(){
        return _reachable;
    }

    public BluetoothDevice getBluetoothDevice(){
        return _device;
    }
}
