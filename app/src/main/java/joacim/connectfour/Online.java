package joacim.connectfour;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import android.os.AsyncTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Online extends Activity {
    /** Called when the activity is first created. */
    Scanner scanner = new Scanner(System.in);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online);
        final EditText msg = (EditText) findViewById(R.id.etMsg);
        Button send = (Button) findViewById(R.id.bSend);
        final TextView convo = (TextView) findViewById(R.id.tvConvo);
        final TextView status = (TextView) findViewById(R.id.tvStatus);


        send.setOnClickListener(new View.OnClickListener() {
            String message = msg.getText().toString();

            @Override
            public void onClick(View v) {

                String serverMsg = "Bajs";
                try {
                    serverMsg = new Connect().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                status.setText("...");

                status.setText("Established connection..");


                convo.append(serverMsg + "\n");

                status.setText("Disconnected from server.");

                if (message != null) {
                    if (msg.getText().toString().trim() == "QUIT") {


                    }}
            }});
        }
        private class Connect extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    Socket s = new Socket("10.3.24.184", 8080);
                    PrintWriter outp = null;
                    BufferedReader inp = null;
                    outp = new PrintWriter(s.getOutputStream(), true);
                    inp = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    String msg= inp.readLine();
                    s.close();
                    return msg;
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onProgressUpdate(Void... values) {
            }
        }

    }

