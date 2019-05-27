package br.com.psousa.controlesony;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnListar;
    Button btnRegistrar;
    Button btnPower;
    Button btnUp;
    Button btnLeft;
    Button btnRigth;
    Button btnDown;
    Button btnOK;
    Button btnReturn;
    Button btnHome;
    Button btnVolumeMais;
    Button btnVolumeMenos;


    private final String TAG = "remoteSony";
    WifiManager wm;

    private SimpleSsdpClient mSsdpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListar = findViewById(R.id.btn_listar);
        btnRegistrar = findViewById(R.id.btn_registrar);
        btnPower = findViewById(R.id.btn_power);
        btnUp = findViewById(R.id.btn_up);
        btnLeft = findViewById(R.id.btn_left);
        btnRigth = findViewById(R.id.btn_rigth);
        btnDown = findViewById(R.id.btn_down);
        btnOK = findViewById(R.id.btn_ok);
        btnReturn = findViewById(R.id.btn_return);
        btnHome = findViewById(R.id.btn_home);
        btnVolumeMenos = findViewById(R.id.btn_volume_menos);
        btnVolumeMais = findViewById(R.id.btn_volume_mais);


        mSsdpClient = new SimpleSsdpClient();
        wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarRegistro().execute(wm.getConnectionInfo().getMacAddress());
            }
        });

        btnPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAAVAw==");
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAB0Aw==");
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAA0Aw==");
            }
        });

        btnRigth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAAzAw==");
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAB1Aw==");
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAABlAw==");
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAABgAw==");
            }
        });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAgAAAJcAAAAjAw==");
            }
        });

        btnVolumeMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAASAw==");
            }
        });

        btnVolumeMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EnviarComando().execute("AAAAAQAAAAEAAAATAw==");
            }
        });


        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                mSsdpClient.search(new SimpleSsdpClient.SearchResultHandler() {

                    /*@Override
                    public void onDeviceFound(final ServerDevice device) {
                        // Called by non-UI thread.
                        Log.d(TAG, ">> Search device found: " + device.getFriendlyName());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListAdapter.addDevice(device);
                            }
                        });
                    }*/

                    @Override
                    public void onFinished() {
                        // Called by non-UI thread.
                        Log.d(TAG, ">> Search finished.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setProgressBarIndeterminateVisibility(false);
                                /*findViewById(R.id.button_search).setEnabled(true);
                                if (mActivityActive) {
                                    Toast.makeText(CameraRemoteSampleApp.this, //
                                            R.string.msg_device_search_finish, //
                                            Toast.LENGTH_SHORT).show(); //
                                }*/
                            }
                        });
                    }

                    @Override
                    public void onErrorFinished() {
                        // Called by non-UI thread.
                        Log.d(TAG, ">> Search Error finished.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setProgressBarIndeterminateVisibility(false);
                                /*findViewById(R.id.button_search).setEnabled(true);
                                if (mActivityActive) {
                                    Toast.makeText(CameraRemoteSampleApp.this, //
                                            R.string.msg_error_device_searching, //
                                            Toast.LENGTH_SHORT).show(); //
                                }*/
                            }
                        });
                    }
                });
            }
        });
    }

    class EnviarRegistro extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String resultado = "";

            resultado = NetworkUtils.Get("http://192.168.0.13:80/cers/api/register?name=SonyRemote&registrationType=new&deviceId=Sony:"+strings[0], strings[0]);

            return resultado;
        }


        @Override
        protected void onPostExecute(String resultado) {
            Toast.makeText(MainActivity.this, resultado, Toast.LENGTH_LONG).show();
            //listView.setAdapter(new ArrayAdapter<>());
        }
    }

    class EnviarComando extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... strings) {
            String resultado = "";

            resultado = NetworkUtils.sendComando("http://192.168.0.13:80/IRCC", strings[0]);

            return resultado;
        }


        @Override
        protected void onPostExecute(String resultado) {
            //Toast.makeText(MainActivity.this, resultado, Toast.LENGTH_LONG).show();
            //listView.setAdapter(new ArrayAdapter<>());
        }
    }
}
