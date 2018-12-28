package mission_control.felixseip.com.missioncontrol;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

interface RocketDataCallback {
    void onRocketDataReceived(String data);
}

public class RocketDataThread extends Thread {
    private BluetoothDevice _device;
    private RocketDataCallback _rocketDataCallback;
    private BluetoothSocket _bluetoothDeviceSocket;
    private InputStream _dataInputStream;
    private OutputStream _dataOutputStream;
    private boolean _stopThread = false;

    public RocketDataThread (BluetoothDevice device, RocketDataCallback rocketDataCallback) {
        _device = device;
        _rocketDataCallback = rocketDataCallback;

        try {
            _bluetoothDeviceSocket = getRocketSocket();
            _bluetoothDeviceSocket.connect();
            _dataInputStream = _bluetoothDeviceSocket.getInputStream();
            _dataOutputStream = _bluetoothDeviceSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        final byte delimiter = 10; //This is the ASCII code for a newline character

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

    private BluetoothSocket getRocketSocket() throws IOException {
        //return _device.createRfcommSocketToServiceRecord(UUID.randomUUID());
        return _device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
    }

}