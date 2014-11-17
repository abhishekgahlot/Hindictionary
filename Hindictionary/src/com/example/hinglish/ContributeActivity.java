package com.example.hinglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContributeActivity extends ActionBarActivity {

	EditText text;
	Button send;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contribute);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = 
		    new StrictMode.ThreadPolicy.Builder().permitAll().build();      
		        StrictMode.setThreadPolicy(policy);
		 }
		
		text=(EditText)findViewById(R.id.editText1);
		send=(Button) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(text.getText().toString().equals("")){
					Toast.makeText(ContributeActivity.this, "Please write the words and definitions above!", Toast.LENGTH_LONG).show();
					return;
				}
				//http://hunt.bugs3.com/submit.php
				try{
					String urlParameters = "data="+text.getText().toString();
					System.out.println(urlParameters);
					
					URL url = new URL("http://hunt.bugs3.com/hinglish/submit.php");
					URLConnection conn = url.openConnection();

					conn.setDoOutput(true);

					OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

					writer.write(urlParameters);
					writer.flush();

					String line;
					String all="";
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

					while ((line = reader.readLine()) != null) {
						System.out.println(line);
						all+=line;
					}
					writer.close();
					reader.close();   
					
					if(all.contains("信件已經發送成功。")){
						Toast.makeText(ContributeActivity.this, "Submitted Successfully. Thanks! Admin will view your work and upload it.", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(ContributeActivity.this, "Error. Submit again later please!", Toast.LENGTH_LONG).show();
					}
					
				}catch(IOException e){
					Toast.makeText(ContributeActivity.this, "Network Error. Check your connectivity please.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contribute, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
