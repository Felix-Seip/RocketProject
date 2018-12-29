package mission_control.felixseip.com.missioncontrol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

public class RocketDataThread extends Thread implements Serializable {
    private BluetoothDevice _device;
    private RocketDataCallback _rocketDataCallback;
    private BluetoothSocket _bluetoothDeviceSocket;
    private InputStream _dataInputStream;
    private OutputStream _dataOutputStream;
    private boolean _stopThread = false;

    public RocketDataThread (BluetoothSocket socket, RocketDataCallback rocketDataCallback) {
        _bluetoothDeviceSocket = socket;
        _rocketDataCallback = rocketDataCallback;

        try {
            _dataInputStream = _bluetoothDeviceSocket.getInputStream();
            _dataOutputStream = _bluetoothDeviceSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        int readBufferPosition = 0;
        byte[] readBuffer = new byte[1024];

        while(!isInterrupted())
        {
            try
            {
                if(_dataInputStream != null){
                    int bytesAvailable = _dataInputStream.available();
                    if(bytesAvailable > 0)
                    {
                        byte[] packetBytes = new byte[bytesAvailable];
                        _dataInputStream.read(packetBytes);
                        for(int i = 0; i < bytesAvailable; i++)
                        {
                            byte byteData = packetBytes[i];
                            if(readBufferPosition + 1 > readBuffer.length){
                                break;
                            }

                            readBuffer[readBufferPosition++] = byteData;
                        }
                        _rocketDataCallback.onRocketDataReceived(new String(readBuffer));
                    }
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                interrupt();
            }
            catch (NullPointerException ex){
                ex.printStackTrace();
                interrupt();
            }
        }
    }
}
