package baskara.tespostjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String STRING_JSON = "{\"university\":{\"studentName\":\"Thomas\",\"id\":\"233242\"}}";
    final String TAG = "POST";
    private Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSend = (Button)findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);
    }

    private void postJson(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;

                try {
                    url = new URL("http://192.168.11.5:8080/test");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    String ret = "";
                    try {
                        connection = (HttpURLConnection) url.openConnection();

                        // set parameter to http request
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        connection.setDoOutput(true);

                        connection.connect();

                        // set json data to send data
                        OutputStream outputStream = connection.getOutputStream();
                        PrintStream ps = new PrintStream(connection.getOutputStream());
                        ps.print(STRING_JSON);
                        Log.d(TAG, STRING_JSON);
                        ps.close();
                        outputStream.close();

                        // parse response data
                        String str = inputStreamToString(connection.getInputStream());
                        Log.d(TAG, str);
                        System.out.print(str);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (connection != null) {
                            //7.コネクションを閉じる。
                            connection.disconnect();
                        }
                    }

                } catch (Exception ex) {
                    System.out.println(ex);
                }

            }
        }).start();
    }
    // InputStream -> String
    static String inputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        try {
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSend){
            postJson();
            Log.d("tes", "buttonclear");
        }
    }
}