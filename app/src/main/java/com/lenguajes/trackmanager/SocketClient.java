package com.lenguajes.trackmanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient extends Thread {
    private Socket mSocket;
    private static final String TAG = "socket";
    private String mIP = "192.168.1.123";
    private int mPort = 27010;

    public SocketClient(String ip, int port) {
        mIP = ip;
        mPort = port;
        start();
    }

    public SocketClient() {
        start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
        try {
            mSocket = new Socket();
            mSocket.connect(new InetSocketAddress(mIP, mPort), 10000); // hard-code server address
            BufferedOutputStream outputStream = new BufferedOutputStream(mSocket.getOutputStream());
            BufferedInputStream inputStream = new BufferedInputStream(mSocket.getInputStream());

            String greetingMsg = "login_hand";

            byte[] buff = new byte[256];
            int len = 0;
            String msg = null;
            outputStream.write(greetingMsg.getBytes());
            outputStream.flush();


            while ((len = inputStream.read(buff)) != -1) {
                msg = new String(buff, 0, len);

                String[] data = msg.split(" ");

                float roll;
                float pitch;
                float yaw;

                float flexP;
                float flexR;
                float flexM;
                float flexI;
                float flexT;

                try{

                    yaw = Float.parseFloat(data[0]);
                    pitch = Float.parseFloat(data[1]);
                    roll = Float.parseFloat(data[2])-90;

                    flexT = (Float.parseFloat(data[6]) - 404f) * 100 / 196f;
                    flexI = (Float.parseFloat(data[7]) - 420f) * 100 / 311f;
                    flexM = (Float.parseFloat(data[8]) - 355f) * 100 / 332f;
                    flexR = (Float.parseFloat(data[9]) - 375f) * 100 / 252f;
                    flexP = (Float.parseFloat(data[10]) - 379f) * 100 / 287f;

                    System.out.println(msg);
                }catch (Exception e){
                    continue;
                }

                System.out.println(flexI+" - "+
                flexM+" - "+
                flexP+" - "+
                flexR+" - "+
                flexT);

            }

            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
        }
        finally {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void close() {
        if (mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}