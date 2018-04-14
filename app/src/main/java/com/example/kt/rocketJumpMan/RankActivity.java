package com.example.kt.rocketJumpMan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RankActivity extends AppCompatActivity {
    public static final String EXTRA_MY_SCORE = "my_score";

    private int mMyScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        Bundle extras = getIntent().getExtras();
        mMyScore = extras != null ? extras.getInt(EXTRA_MY_SCORE) : 0;

        Button button = (Button) findViewById(R.id.btnback);
        final TextView Score = (TextView) findViewById(R.id.highScore);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //startActivity(new Intent(RankActivity.this, UImenu.class));
                finish();
            }
        });

        final int score = getIntent().getIntExtra("SCORE", 0);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        final EditText input = new EditText(this);
        input.setHint("Name");
        alertDialog.setTitle("Ranking");
        alertDialog.setMessage("What is your name?");
        alertDialog.setView(input);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                if (input != null) {
//                    // http://php-toto37648674894205.codeanyapp.com/insertData.php?name=hihi&score=10
//                    String urlString1 = "http://php-toto37648674894205.codeanyapp.com/insertData.php?name="+input.getText().toString()+"&score="+score;
//                    HttpURLConnection connection = null;
//                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
//
//                    try {
//                        URL url = new URL(urlString1);
//                        connection = (HttpURLConnection) url.openConnection();
//
//                        connection.setReadTimeout(1500);
//                        connection.setConnectTimeout(1500);
//                        connection.setInstanceFollowRedirects(true);
//
//                        if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
//                            InputStream inputStream = connection.getInputStream();
//                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                            String tempStr;
//                            tempStr = bufferedReader.readLine();
//
//                            // Score.setText(tempStr);
//
//                            bufferedReader.close();
//                            inputStream.close();
//                        }
//                        String urlString = "http://php-toto37648674894205.codeanyapp.com/scoreboard.php";
//
//
//                        try {
//                            url = new URL(urlString);
//                            connection = (HttpURLConnection) url.openConnection();
//
//                            connection.setReadTimeout(1500);
//                            connection.setConnectTimeout(1500);
//                            connection.setInstanceFollowRedirects(true);
//
//                            if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
//                                InputStream inputStream = connection.getInputStream();
//                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                                String tempStr;
//                                tempStr = bufferedReader.readLine();
//                                // while( ( tempStr = bufferedReader.readLine() ) != null ) {
//                                //    stringBuffer.append( tempStr );
//                                //  }
//
//                                Score.setText(tempStr);
//
//                                bufferedReader.close();
//                                inputStream.close();
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        finally {
//                            if( connection != null ) {
//                                connection.disconnect();
//                            }
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    finally {
//                        if(connection != null) {
//                            connection.disconnect();
//                        }
//                    }
//                }
            }
        });
        alertDialog.show();
    }
}
