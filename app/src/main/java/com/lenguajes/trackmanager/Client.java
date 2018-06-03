package com.lenguajes.trackmanager;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    Client(String addr, int port, TextView textResponse) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
    }

    void sendToServer(String msg){
        out.println(msg);
    }

    @Override
    protected String doInBackground(Void... arg0) {
        try {
            socket = new Socket(dstAddress, dstPort);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                /*
                 * notice: inputStream.read() will block if no data return
                 */
                while (true) {
                    response = in.readLine();
                    textResponse.setText(response);
                }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
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
    protected void onPostExecute(String result) {
        textResponse.setText(response);
        super.onPostExecute(result);
    }

}
