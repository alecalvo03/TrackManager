package com.lenguajes.trackmanager;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<Void,String,String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    Client(String addr, int port, TextView textResponse) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        System.out.println("va a crear socket con " + dstAddress + " en port: " + dstPort);
        try {
            socket = new Socket(dstAddress, dstPort);
        }catch (Exception e){
            e.printStackTrace();
        }
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    void sendToServer(String msg){
        out.println(msg);
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            while (true) {
                response = in.readLine();
                System.out.println("Response de server: "+response);
                publishProgress(response);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    @Override
    protected void onProgressUpdate(String... result) {
        textResponse.setText(response);
        super.onProgressUpdate(result);
    }
}
