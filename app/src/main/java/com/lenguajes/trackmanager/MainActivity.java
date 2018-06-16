package com.lenguajes.trackmanager;

/*import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}*/

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

import java.io.IOException;

public class MainActivity extends Activity {

    private TextView response;
    private EditText editTextAddress, editTextPort;
    private Button buttonConnect, buttonClear, clrButton, startpointButton, sendButton;
    private GridLayout gridLayout;
    private ToggleButton test;
    private ToggleButton btnGrid[][];
    private Client myClient;
    private View connectForm;
    private View trackForm;
    private int startcoord[] = {-1,-1};

    private boolean toStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = findViewById(R.id.addressEditText);
        editTextPort = findViewById(R.id.portEditText);
        buttonConnect = findViewById(R.id.connectButton);
        buttonClear = findViewById(R.id.clearButton);
        response = findViewById(R.id.responseTextView);
        gridLayout = findViewById(R.id.gridlayout);
        editTextAddress.setText("192.168.100.3");
        editTextPort.setText("9090");

        connectForm = findViewById(R.id.connect_form);
        trackForm = findViewById(R.id.track_form);

        final int col = gridLayout.getColumnCount();
        final int row = gridLayout.getRowCount();

        buttonConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    myClient = new Client(editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()),
                            response);
                    connectForm.setVisibility(View.GONE);
                    trackForm.setVisibility(View.VISIBLE);
                    initGrid(col,row);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myClient.execute();
            }
        });

        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });
    }

    private void setStart(int i, int j) {
        if (btnGrid[i][j].isChecked()) {
            btnGrid[i][j].setChecked(false);
        } else {
            if (startcoord[0] != -1) {
                btnGrid[startcoord[0]][startcoord[1]].setBackground(btnGrid[i][j].getBackground());
            }
            btnGrid[i][j].setChecked(true);
            btnGrid[i][j].setBackgroundColor(Color.RED);
            startcoord[0] = i;
            startcoord[1] = j;
        }
    }

    private void clearMatrix(){
        for (ToggleButton btn[] : btnGrid){
            for (ToggleButton tbtn : btn){
                tbtn.setChecked(false);
            }
        }
        btnGrid[startcoord[0]][startcoord[1]].setBackground(btnGrid[startcoord[0]+1][0].getBackground());
    }

    private boolean checkAdjacent (int i, int j){
        System.out.println("Entra con " + i + " " + j);
        int n = 0;
        if (btnGrid[i+1][j].isChecked()) n++;
        if (btnGrid[i-1][j].isChecked()) n++;
        if (btnGrid[i][j+1].isChecked()) n++;
        if (btnGrid[i][j-1].isChecked()) n++;
        return n <= 2;
    }

    private int getDir(int i, int j, int prev){
        if (btnGrid[i-1][j].isChecked()) if (prev != 0) return 1;
        if (btnGrid[i+1][j].isChecked()) if (prev != 1) return 0;
        if (btnGrid[i][j+1].isChecked()) if (prev != 2) return 3;
        if (btnGrid[i][j-1].isChecked()) if (prev != 3) return 2;
        return -1;
    }

    private String parseMatrix(){
        StringBuilder toreturn = new StringBuilder();
        boolean done = false;
        if (startcoord[0] == -1)
            return "Error: No hay punto de inicio.";
        int i = startcoord[0];
        int j = startcoord[1];
        int prev = -1;

        while (!done){
            if (!checkAdjacent(i,j))
                return "Error: Las pistas pueden tener un máximo de dos adyacentes.";
            int dir = getDir(i,j,prev);
            System.out.println("Dir: " + dir);
            System.out.println("Prev: " + prev);
            toreturn.append(i).append("-").append(j).append("-");
            switch (dir){
                case 0: //Abajo
                    i++;
                    break;
                case 1: //Arriba
                    i--;
                    break;
                case 2: //Izquierda
                    j--;
                    break;
                case 3: //Derecha
                    j++;
                    break;
            }
            if (dir == -1){
                toreturn.append(prev);
                done = true;
            }
            if (i == startcoord[0] && j == startcoord[1] ){
                toreturn.append(dir);
                done = true;
            } else {
                prev = dir;
                toreturn.append(dir).append(":");
            }
        }
        return toreturn.toString();
    }

    private void initGrid(int col, int row){
        btnGrid = new ToggleButton[col][row];
        for (int i = 0; i < col; i++){
            for (int j = 0; j < row; j++){
                final int finalJ = j;
                final int finalI = i;
                btnGrid[i][j] = new ToggleButton(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(i),GridLayout.spec(j));
                params.width = 100;
                params.height = 100;
                gridLayout.addView(btnGrid[i][j], params);
                btnGrid[i][j].setText("");
                btnGrid[i][j].setTextOff("");
                btnGrid[i][j].setTextOn("");
                btnGrid[i][j].setBackgroundResource(R.drawable.select_button);
                //btnGrid[i][j].setLayoutParams(params);
                btnGrid[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            myClient.sendToServer(finalI + "-" + finalJ);
                            if (toStart){
                                setStart(finalI,finalJ);
                                toStart = false;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        }
        startpointButton = findViewById(R.id.startButton);
        startpointButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toStart = true;
            }
        });

        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(parseMatrix());
            }
        });

        clrButton = findViewById(R.id.clrButton);
        clrButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clearMatrix();
            }
        });
    }
}
