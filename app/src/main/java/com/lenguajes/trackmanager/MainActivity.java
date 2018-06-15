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
    private Button buttonConnect, buttonClear, clrButton, startButton;
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
        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toStart = true;
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
