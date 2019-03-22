package com.example.wifishare;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button send;
    Button Receive;
    String work="send";
   ProgressBar progressBar ;
    WifiManager wm;
    String ip;
    ListView listView;
    InetAddress inetAddress;
    List<String> hostList = new ArrayList<String>();
    protected static String server_IP = "192.168.31.134";
    private static final int server_Port = 5555 ;

    public void getAllDevices(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        send= (Button) findViewById(R.id.button);
        Receive=(Button) findViewById(R.id.button2);
        wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // onServerSide();
                work="send";
                progressBar.setVisibility(View.VISIBLE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                Log.d("ip :",ip);
                ShareData task = new ShareData();
                task.execute();

            }
        });

        Receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // onClientSide();
                work="receive";
                progressBar.setVisibility(View.VISIBLE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                Log.d("ip :",ip);
                ShareData task = new ShareData();
                task.execute();

            }
        });
    }




   public class ShareData extends AsyncTask<String,String,String>{


        @Override
        protected String doInBackground(String... strings) {

            if(work=="send"){
                try {
                    Log.d("ajworkingornot","its working");
                    Boolean end = false;
                   // ServerSocket ss = new ServerSocket(5555);
                    ServerSocket ss = new ServerSocket(); // <-- create an unbound socket first
                    ss.setReuseAddress(true);
                    ss.bind(new InetSocketAddress(1253));
                   // Log.d("ajworking","is it working");
                    while(!end){
                        Log.d("ajworking","is it working");
                        //Server is waiting for client here, if needed
                        Socket s = ss.accept();
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        PrintWriter output = new PrintWriter(s.getOutputStream(),true); //Autoflush
                        String st = input.readLine();

                        Log.d("Tcp Example", "From client: "+st);
                        output.println("Its from that phone :)");
                        s.close();
                        if ( st.equals("Hello!"))
                        {
                            end = true;
                        }
                    }
                    ss.close();


                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if(work=="receive"){
                try {
                    Log.d("livinworking","is it working");
                    Socket s = new Socket("192.168.43.111",1253);
                    Log.d("livinworking or not","oh ye");
                    //outgoing stream redirect to socket
                    OutputStream out = s.getOutputStream();

                  //  PrintWriter output = new PrintWriter(out);
                    PrintWriter output =  new PrintWriter(out,true);
                    output.println("Hello!");
                    BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    //read line(s)
                    String st = input.readLine();
                    Log.d("Tcp Example", "From server: "+st);
                    //. . .
                    //Close connection
                    s.close();


                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }



            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }






}
