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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {

    TextView response;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;
    GridLayout gridLayout;
    ToggleButton btnGrid[][];
    Client myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText) findViewById(R.id.addressEditText);
        editTextPort = (EditText) findViewById(R.id.portEditText);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonClear = (Button) findViewById(R.id.clearButton);
        response = (TextView) findViewById(R.id.responseTextView);
        gridLayout = findViewById(R.id.gridlayout);
        System.out.println("pusi");
        btnGrid = new ToggleButton[4][4];

        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                final int finalJ = j;
                final int finalI = i;
                btnGrid[i][j] = new ToggleButton(this);
                System.out.println("pusi");
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                        GridLayout.spec(j,1),GridLayout.spec(i));
                System.out.println("pusi");
                gridLayout.addView(btnGrid[i][j], i);
                System.out.println("yus");
                btnGrid[i][j].setLayoutParams(params);
                btnGrid[i][j].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            myClient.sendToServer(finalI + "-" + finalJ);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        }

        buttonConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                myClient = new Client(editTextAddress.getText()
                        .toString(), Integer.parseInt(editTextPort
                        .getText().toString()), response);
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
}
