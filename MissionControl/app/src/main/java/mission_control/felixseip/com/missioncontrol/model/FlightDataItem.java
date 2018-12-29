package mission_control.felixseip.com.missioncontrol.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class FlightDataItem implements Serializable {

    enum FlightDataType{
        EngineTemperature,
        Altitude,
        AtmosphericPressure,
        WindSpeed,
        Angle,
        Velocity
    }

    private String _id;

    private FlightDataType _dataType;

    private float _value;

    private Timestamp _collectionTime;

    public FlightDataItem(){
        //Default constructor
    }

    public FlightDataItem(final FlightDataType type, final float value, final Timestamp collectionTime){
        _id = UUID.randomUUID().toString();
        _dataType = type;
        _value = value;
        _collectionTime = collectionTime;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public FlightDataType getDataType() {
        return _dataType;
    }

    public void setDataType(FlightDataType dataType) {
        _dataType = dataType;
    }

    public float getValue() {
        return _value;
    }

    public void setValue(float value) {
        _value = value;
    }

    public Timestamp getCollectionTime(){
        return _collectionTime;
    }

    public void setCollectionTime(Timestamp collectionTime){
        _collectionTime = collectionTime;
    }

}
