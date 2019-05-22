package br.com.psousa.controlesony;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListar = findViewById(R.id.btn_listar);

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                new ConsultaIPS().execute(Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress()));
            }
        });
    }

    class ConsultaIPS extends AsyncTask<String, List<ConsultaIPS.Dispositivo>, List<ConsultaIPS.Dispositivo>> {


        @Override
        protected List<ConsultaIPS.Dispositivo> doInBackground(String... strings) {
            List<ConsultaIPS.Dispositivo> resultado = new ArrayList<ConsultaIPS.Dispositivo>();
            DatagramSocket socket = null;
            Dispositivo d = new Dispositivo();
            try {
                socket = new DatagramSocket();
                socket.setSoTimeout(15000);
                InetAddress address = InetAddress.getByName("239.255.255.250");

                // send request
                byte[] buf = new byte[4000];
                String pesquisa = "M-SEARCH * HTTP/1.1\\r\\nHOST:239.255.255.250:1900\\r\\nMAN:\\\"ssdp:discover\\\"\\r\\nST:ssdp:all\\r\\nMX:3\\r\\n\\r\\n";

                DatagramPacket packet =
                        new DatagramPacket(pesquisa.getBytes(), pesquisa.getBytes().length, address, 1900);
                socket.send(packet);


                // get response
                packet = new DatagramPacket(buf, buf.length);


                socket.receive(packet);
                String line = new String(packet.getData(), 0, packet.getLength());

                d.setAtivo(true);
                d.setNome(line);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(socket != null){
                    socket.close();
                    resultado.add(d);
                }
            }

            return resultado;
        }


        @Override
        protected void onPostExecute(List<ConsultaIPS.Dispositivo> resultado) {
            for (Dispositivo dip : resultado) {
                if (dip.isAtivo()) {
                    Toast.makeText(MainActivity.this, dip.nome, Toast.LENGTH_LONG).show();
                }
            }
            //listView.setAdapter(new ArrayAdapter<>());
        }

        public class Dispositivo {
            private String ip;
            private boolean ativo;
            private String nome;

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public boolean isAtivo() {
                return ativo;
            }

            public void setAtivo(boolean ativo) {
                this.ativo = ativo;
            }

            public String getNome() {
                return nome;
            }

            public void setNome(String nome) {
                this.nome = nome;
            }
        }
    }
}
