package com.example.robotpicontroller;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    Button set_forward, set_backward, set_left, set_right;
    Button set_fccw, set_fcw, set_bcw, set_bccw;
    ToggleButton automation;
    TextInputEditText text_input;
    TextView view_connection_status, view_data_sent;
    Switch set_connection;

    public static String _server_ip;
    public static int _PORT;
    public static boolean connection = false;
    public static String[] command_list = new String[]{"F","B","L","R","I","Q","E","Z","C","A"};
    public static String command = "I";
    public static boolean data_output_stream_permission;

    Socket_Comm s = new Socket_Comm();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        set_forward = (Button) findViewById(R.id.button_forward);
        set_backward = (Button) findViewById(R.id.button_backward);
        set_left = (Button) findViewById(R.id.button_left);
        set_right = (Button) findViewById(R.id.button_right);
        set_fccw = (Button) findViewById(R.id.button_fccw45);
        set_fcw = (Button) findViewById(R.id.button_fcw45);
        set_bcw = (Button) findViewById(R.id.button_bcw45);
        set_bccw = (Button) findViewById(R.id.button_bccw45);
        text_input = (TextInputEditText) findViewById(R.id.input_text_field);
        automation = (ToggleButton) findViewById(R.id.toggleButton_auto);
        view_connection_status = (TextView) findViewById(R.id.textView_connection_status);
        view_data_sent = (TextView) findViewById(R.id.textView_data_sent);
        set_connection = (Switch) findViewById(R.id.switch_connect);

        automation.setEnabled(false);
        enableControllButton(false);
        getAddress();

        set_forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[0];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[1];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[2];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[3];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_fccw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[5];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_fcw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[6];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_bcw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[7];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_bccw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        data_output_stream_permission = true;
                        command = command_list[8];
                        return true;
                    case MotionEvent.ACTION_UP :
                        data_output_stream_permission = false;
                        command = command_list[4];
                        return true;
                }
                return false;
            }
        });

        set_connection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    getAddress();
                    connection = true;
                    s.execute();
                    automation.setEnabled(true);
                    enableControllButton(true);
                    text_input.setEnabled(false);
                } else {
                    connection = false;
                    automation.setEnabled(false);
                    enableControllButton(false);
                    text_input.setEnabled(true);
                }
            }
        });

        automation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    enableControllButton(false);
                    data_output_stream_permission = true;
                    command = command_list[9];
                } else {
                    enableControllButton(true);
                    data_output_stream_permission = false;
                }
            }
        });
    }

    public void getAddress() {
        String adress = text_input.getText().toString();
        String temp[] = adress.split(":");
        _server_ip = temp[0];
        _PORT = Integer.valueOf(temp[1]);
    }

    public void enableControllButton(boolean controll_button_status) {
            set_forward.setEnabled(controll_button_status);
            set_backward.setEnabled(controll_button_status);
            set_left.setEnabled(controll_button_status);
            set_right.setEnabled(controll_button_status);
            set_fccw.setEnabled(controll_button_status);
            set_fcw.setEnabled(controll_button_status);
            set_bcw.setEnabled(controll_button_status);
            set_bccw.setEnabled(controll_button_status);
    }
}

class Socket_Comm extends AsyncTask <Void, Void, Void> {

    Socket socket;
    DataOutputStream data_out;
    DataInputStream data_in;
    String data;
    String GET;

    @Override
    protected Void doInBackground(Void... params) {
        try {
            socket = new Socket(MainActivity._server_ip, MainActivity._PORT);
            data_out = new DataOutputStream(socket.getOutputStream());
            data_in = new DataInputStream(socket.getInputStream());
            //data_out.writeUTF("G");

            try {
                while (MainActivity.connection) {
                    data = "I";
                    if (MainActivity.data_output_stream_permission) {
                        data = MainActivity.command;
                    }
                    data_out.writeUTF(data);
                    data_out.flush();
                    TimeUnit.MILLISECONDS.sleep(50);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            data_out.close();
            data_in.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

/*
        set_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    data_output_stream_permission = true;
                    command = command_list[0];
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (set_forward.isPressed()==false) {
                        data_output_stream_permission = false;
                        command = command_list[4];
                    }
                }
            }
        });

        set_backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    data_output_stream_permission = true;
                    command = command_list[1];
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    data_output_stream_permission = false;
                    command = command_list[4];
                }
            }
        });

        set_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    data_output_stream_permission = true;
                    command = command_list[1];
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    data_output_stream_permission = false;
                    command = command_list[4];
                }
            }
        });

        set_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    data_output_stream_permission = true;
                    command = command_list[3];
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    data_output_stream_permission = false;
                    command = command_list[4];
                }
            }
        });
 */

 /*
class Socket_Comm {

    Socket socket;
    DataOutputStream data_out;
    DataInputStream data_in;
    boolean connection = false;
    String connected_string = "Connection Status : --";

    public void Socket_Comm() {
        System.out.print("Socket Object Created--");
    }

    public void establishConnection(String server_ip, int PORT) {
        try {
            socket = new Socket(server_ip, PORT);
            data_out = new DataOutputStream(socket.getOutputStream());
            data_in = new DataInputStream(socket.getInputStream());
            connection = true;
            connected_string = "Connection Status : True";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getConnectionStatus() {
        return connection;
    }

    public String getConnected_string() {
        return connected_string;
    }

    public void SendData(String data) {
        try {
            data_out.writeUTF(data);
            data_out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            data_out.close();
            data_in.close();
            socket.close();
            connection = false;
            connected_string = "Connection Status : False";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
*/