package com.example.home.networking;

/**
 * Created by Himanshu on 16/03/2017.
 */

import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.PendingIntent.getActivity;
import static com.example.home.networking.R.id.progressBar1;
import static java.lang.Thread.sleep;

public class Front extends AppCompatActivity {

    ProgressBar spinner;
    String retry,timeout;
    TextView tv;
    String ss;
    EditText et,et1,et2,et3,ret,time;
    Button latency_c,n_check;
    int value = 0,cancel=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        setter();
        focus();
        spinner = (ProgressBar) findViewById(progressBar1);
        tv = (TextView) findViewById(R.id.output);
    }

    void setter()
    {
        et          = (EditText)findViewById(R.id.first);
        et1         = (EditText)findViewById(R.id.second);
        et2         = (EditText)findViewById(R.id.third);
        et3         = (EditText)findViewById(R.id.fourth);
        tv          = (TextView) findViewById(R.id.output);
        ret         = (EditText)findViewById(R.id.retry_et);
        time        = (EditText)findViewById(R.id.time_out_et);
        latency_c   = (Button)findViewById(R.id.check_button_latency);
        n_check     = (Button)findViewById(R.id.check_button);
    }

    public void check(View view)
    {
        new Thread() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.VISIBLE);
                    }
                });
            }
        }.start();

        if(cancel==0) {

            n_check.setContentDescription("CANCEL");
            latency_c.setContentDescription("CANCEL");
            retry       = ret.getText().toString();
            timeout     = time.getText().toString();
            if (et.getText().toString().length() != 0 || et1.getText().toString().length() != 0
                    || et2.getText().toString().length() != 0 || et3.getText().toString().length() != 0) {
                ss = et.getText().toString() + "." + et1.getText().toString() + "." + et2.getText().toString() + "."
                        + et3.getText().toString();
                value = 1;

                retry = ret.getText().toString();
                timeout = time.getText().toString();

                System.out.println("Upper retry" + retry);
                if (retry == null || retry.equals("") || retry.length() == 0 || retry.equals("0")) {
                    retry = "0.5";
                }
                if (timeout == null || timeout.equals("") || timeout.length() == 0 || timeout.equals("0")) {
                    timeout = "0.1";
                }
                thread tt = new thread(tv, ss, spinner, value, retry, timeout,cancel);
                Thread t = new Thread(tt);
                t.start();
            } else {
                new Thread() {
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setVisibility(View.VISIBLE);
                                spinner.setVisibility(View.INVISIBLE);
                                tv.setText("Some Ip address spaces are blank");
                            }
                        });
                    }
                }.start();
            }
        }
        else
        {
            new Thread() {
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.INVISIBLE);
                            tv.setText("Cancelled");
                            n_check.setContentDescription("CHECK");
                            latency_c.setContentDescription("WIFI LATENCY");
                        }
                    });
                }
            }.start();
            cancel=0;
        }
    }




    public void traceroute(View v) {
        new Thread() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.VISIBLE);
                        tv.setText(" ");
                    }
                });
            }
        }.start();

        if (et.getText().toString().length() != 0
                && et1.getText().toString().length() != 0
                && et2.getText().toString().length() != 0
                && et3.getText().toString().length() != 0)
        {
            ss = et.getText().toString() + "." + et1.getText().toString() + "." + et2.getText().toString() + "." + et3.getText().toString();
            threadtrace tt = new threadtrace(tv,ss,spinner);
            Thread t = new Thread(tt);
            t.start();
        }
        else
        {
            new Thread() {
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setVisibility(View.INVISIBLE);
                            tv.setText("Some Ip address spaces are blank");
                        }
                    });
                }
            }.start();
        }
    }

    boolean empty(EditText tv)
    {
        String string = tv.getText().toString().trim();
        if(string.isEmpty() || string.length()==0 || string.equals("") || string==null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    void focus()
    {
        EditText et = (EditText)findViewById(R.id.first);
        EditText et1 = (EditText)findViewById(R.id.second);
        EditText et2 = (EditText)findViewById(R.id.third);
        EditText et3 = (EditText)findViewById(R.id.fourth);


        et.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                EditText et = (EditText)findViewById(R.id.first);
                EditText et1 = (EditText)findViewById(R.id.second);
                EditText et2 = (EditText)findViewById(R.id.third);
                EditText et3 = (EditText)findViewById(R.id.fourth);
                if(!hasFocus && !empty(et))
                {
                    if(Integer.parseInt(et.getText().toString())<0)
                    {
                        et.setText("0");
                    }
                    if(Integer.parseInt(et.getText().toString())>255)
                    {
                        et.setText("255");
                    }
                }
            }
        });
        et1.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                EditText et = (EditText)findViewById(R.id.first);
                EditText et1 = (EditText)findViewById(R.id.second);
                EditText et2 = (EditText)findViewById(R.id.third);
                EditText et3 = (EditText)findViewById(R.id.fourth);
                if(!hasFocus && !empty(et1))
                {
                    if(Integer.parseInt(et1.getText().toString())<0)
                    {
                        et1.setText("0");
                    }
                    if(Integer.parseInt(et1.getText().toString())>255)
                    {
                        et1.setText("255");
                    }
                }
            }
        });
        et2.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                EditText et = (EditText)findViewById(R.id.first);
                EditText et1 = (EditText)findViewById(R.id.second);
                EditText et2 = (EditText)findViewById(R.id.third);
                EditText et3 = (EditText)findViewById(R.id.fourth);
                if(!hasFocus && !empty(et2))
                {
                    if(Integer.parseInt(et2.getText().toString())<0)
                    {
                        et2.setText("0");
                    }
                    if(Integer.parseInt(et2.getText().toString())>255)
                    {
                        et2.setText("255");
                    }
                }
            }
        });
        et3.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                EditText et = (EditText)findViewById(R.id.first);
                EditText et1 = (EditText)findViewById(R.id.second);
                EditText et2 = (EditText)findViewById(R.id.third);
                EditText et3 = (EditText)findViewById(R.id.fourth);
                if(!hasFocus && !empty(et3))
                {
                    if(Integer.parseInt(et3.getText().toString())<0)
                    {
                        et3.setText("0");
                    }
                    if(Integer.parseInt(et3.getText().toString())>255)
                    {
                        et3.setText("255");
                    }
                }
            }
        });
    }

    public void latency(View view)
    {

        if (cancel == 0) {
            new Thread() {
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            spinner.setVisibility(View.VISIBLE);
                            latency_c.setContentDescription("CANCEL");
                            n_check.setContentDescription("CANCEL");
                        }
                    });
                }
            }.start();
            final WifiManager manager = (WifiManager) super.getApplicationContext().getSystemService(WIFI_SERVICE);
            final DhcpInfo dhcp = manager.getDhcpInfo();
            String sss = Formatter.formatIpAddress(dhcp.gateway);
            value = 3;

            retry = ret.getText().toString();
            timeout = time.getText().toString();

            if (retry == null || retry.equals("") || retry.length() == 0 || retry.equals("0")) {
                retry = "0.5";
            }
            if (timeout == null || timeout.equals("") || timeout.length() == 0 || timeout.equals("0")) {
                timeout = "0.1";
            }
            thread tt = new thread(tv, sss, spinner, value, retry, timeout,cancel);
            Thread t = new Thread(tt);
            t.start();
        }
        else
        {
            new Thread() {
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.INVISIBLE);
                            tv.setText("Cancelled");
                            latency_c.setContentDescription("WIFI LATENCY");
                            n_check.setContentDescription("CHECK");
                            cancel=0;
                        }
                    });
                }
            }.start();
        }
    }

    public void udpLatency(View view) throws IOException {
        Server s = new Server();
        ServerSocket ss = new ServerSocket(5000);

    }
}


class thread extends Front implements Runnable
{
    BufferedReader br;
    TextView tv;
    String s,ss,retry,timeout;
    StringBuilder sb;
    ProgressBar spinnerr;
    StringBuilder tracker;
    int value;
    int flag =0,cancel=0;

    @Override
    public View findViewById(@IdRes int id) {
        return super.findViewById(id);
    }

    public thread(TextView tvv,String vss,ProgressBar p,int v,String r,String t,int ct)
    {
        tv = tvv;
        ss = vss;
        spinnerr = p;
        value = v;
        retry = r;
        timeout = t;
        cancel = ct;
    }

    public void run()
    {
        try
        {
            sb = new StringBuilder();
            tracker = new StringBuilder();
            String command = getCommand();

            System.out.println(command+ss);

            java.lang.Process pp = Runtime.getRuntime().exec(command+ss);
            br = new BufferedReader(new InputStreamReader(pp.getInputStream()));

            while ((s = br.readLine()) != null) {
                if (s.length() > 0 && s.contains("avg")) {
                    break;
                }
                else if(!s.contains("PING"))
                {
                    tracker.append("\n" +s);
                }
            }

            if(s!=null)
            {
                System.out.println("Outside "+s+" "+s.length()+" Retry "+retry);
                Matcher match = Pattern.compile(".*( ?)([0-9]+(\\.[0-9]+))(/?)([0-9]+(\\.[0-9]+))(/?)([0-9]+(\\.[0-9]+))(/?)([0-9]+(\\.[0-9]+)).*").matcher(s);
                if (match.matches()) {
                    Double minm = Double.parseDouble(match.group(2));
                    Double avg = Double.parseDouble(match.group(5));
                    Double max = Double.parseDouble(match.group(8));

                    System.out.println("Inside  "+minm+" "+" "+avg+" "+" "+max);

                    if (value == 1) {
                        sb.append("IPADDRESS OPTION : " + ss + ":\n Avg. Latency :" + avg / 2 + "ms");
                        sb.append("\nMin. Latency :" + minm / 2 + "ms");
                        sb.append("\nMax. Latency :" + max / 2 + "ms");
                    }
                    else {
                        if(!ss.equals("0.0.0.0")) {
                            flag = 1;
                            sb.append("WIFI Latency : " + ss + ":\n Avg. Latency :" + avg / 2 + "ms");
                            sb.append("\nMin. Latency :" + minm / 2 + "ms");
                            sb.append("\nMax. Latency :" + max / 2 + "ms");
                        }
                    }
                }
            }
            else {
                if (value == 1)
                    sb.append("IPADDRESS OPTION :\n" + ss + ": No Connection/Invalid Address");
                else if(value == 3)
                {
                    if(!ss.equals("0.0.0.0")) {
                        flag = 1;
                    }
                }
                else
                    sb.append("Some input fields are not filled.");
            }
            //pp.destroy();
            System.out.println("end");

        } catch (IOException e)
        {
            e.printStackTrace();
        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (value == 1)
                    tv.setText(sb.toString()+tracker.toString()+"\n");
                else
                {
                    if(flag == 1) {
                        tv.setText(sb.toString()/*+"\n"+"Ip :"+ss*/+tracker.toString()+"\n" );
                    }
                    else
                    {
                        tv.setText("WIFI Disconnected");
                    }
                }
                try {
                    spinnerr.setVisibility(View.INVISIBLE);
                    tv.setVisibility(View.VISIBLE);
                }
                catch(Exception e)
                {
                    System.out.println(spinnerr+" Here thread "+tv);
                }
            }
        });

    }


    String getCommand()
    {
        String command;

        if(retry.equals("0.5"))
        {
            command = "/system/bin/ping -c 4 ";
        }
        else
        {
            command = "/system/bin/ping -c "+retry+" ";
        }
        if(timeout.equals("0.1"))
        {
        }
        else
        {
            command = command + "-w "+timeout +" ";
        }
        return command;
    }

}

class threadtrace extends Front implements Runnable
{
    BufferedReader br;
    TextView tv;
    String s2,destination_addr,intermediate_addr;
    ProgressBar spinner;
    StringBuilder display;
    java.lang.Process p;
    int hops = 1,flag =0;
    threadtrace(TextView ttv,String ds,ProgressBar pb)
    {
        tv                  = ttv;
        destination_addr    = ds;
        spinner             = pb;
    }

    @Override
    public void run() {
        System.out.println("Inside run");
        display = new StringBuilder();

        if(destination_addr!="0.0.0.0") {
            do {
                try {
                    System.out.println("ping -t "+hops+" "+destination_addr);   //checker
                    p = Runtime.getRuntime().exec("ping -t "+hops+" "+destination_addr);
                    br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String s = "aa";
                    while ((s = br.readLine()) != null || intermediate_addr != destination_addr){
                        if (s.contains("From") || s.contains("from")) {
                            display.append(s);
                            break;
                        }
                    }
                    System.out.println("OUTSIDE LOOP " +hops);
                    p.destroy();
                    if(s!=null) {
                        Matcher match = Pattern.compile(".*( )(([0-9]+)(.)([0-9]+)(.)([0-9]+)(.)([0-9]+))(:).*").matcher(s);
                        if (match.matches()) {
                            s2 = match.group(2);
                            intermediate_addr   =   s2;
                            new Thread() {
                                public void run() {//updating ips

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String st = (String) tv.getText();
                                            tv.setText(st+s2+"\n");
                                        }
                                    });
                                }
                            }.start();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                hops++;
                System.out.println("Destination "+destination_addr+" Intermediate "+intermediate_addr+" hops "+hops);
                display.delete(0,display.capacity()-1);
            } while (!intermediate_addr.equals(destination_addr) && hops < 30);
        }
        else
        {
            flag =1;
        }

        new Thread() {
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setVisibility(View.INVISIBLE);
                        if(flag == 1)
                        {
                            tv.setText("WiFi Disconnected");
                        }
                        else if(hops>=30 || !intermediate_addr.equals(destination_addr))
                        {
                            tv.setText("Destination unrechable/Hop limit exceeded");
                        }
                    }
                });
            }
        }.start();
    }
}

class Server
{
    ServerSocket ss;
    Socket s;
    PrintWriter pw ;
    Server()
    {
        try {
            ss = new ServerSocket(1700);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start()
    {
        try {
            s = ss.accept();
            pw = new PrintWriter(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
