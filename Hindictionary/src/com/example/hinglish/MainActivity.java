package com.example.hinglish;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends ActionBarActivity {
	
	AutoCompleteTextView search;
	TextView result;
	Map<String,String> words;
	List<String> suggest;
	Button contribute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Hinglish Dictionary");
		
		search=(AutoCompleteTextView) findViewById(R.id.search);
		search.requestFocus();
		suggest=new ArrayList<String>();
		search.setThreshold(1); 
		words=new HashMap<String, String>();
		wordlistsetup();
		
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, suggest);
		System.out.println(suggest.size());
		adapter.setNotifyOnChange(true);
		search.setAdapter(adapter);
		
		
		search.setOnEditorActionListener(new OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	result.setText("");
		        	search.dismissDropDown();
		            search();
		            return true;
		        }
		        return false;
		    }
		});
		search.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			 public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			         result.setText("");
			         search();
			}
		});

		
		result=(TextView)findViewById(R.id.result);
		
		contribute=(Button)findViewById(R.id.contribute);
		contribute.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ContributeActivity.class);
				startActivity(intent);
			}
		});
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = 
		    new StrictMode.ThreadPolicy.Builder().permitAll().build();      
		        StrictMode.setThreadPolicy(policy);
		 }
		try{
			URL url = new URL("http://hunt.bugs3.com/hinglish/version.txt");
			URLConnection conn = url.openConnection();

			String line;
			String all="";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				all+=line;
			}
			reader.close();   
			
			if(!all.equals("2")){
				new AlertDialog.Builder(this)
			 	.setTitle("Hinglish Update Avaliable")
			 	.setMessage("Download Update now?")
			 	.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			 		public void onClick(DialogInterface dialog, int which) {
			 			String url = "http://hunt.bugs3.com/hinglish/Hinglish.apk";
			 			Intent i = new Intent(Intent.ACTION_VIEW);
			 			i.setData(Uri.parse(url));
			 			startActivity(i);
			 		}
			 	})
			 	.setNegativeButton("No", null)
			 	.show();
			}
			
		}catch(IOException e){
		}
		
	}
	private void search() {
		// TODO Auto-generated method stub
		String query=search.getText().toString();
		for (Map.Entry<String, String> entry : words.entrySet()) {
			if(query.toLowerCase().equals(entry.getKey().toLowerCase())){
				result.append("[HIN] "+query+"\n    ="+entry.getValue()+"\n\n");
			}else
			if(query.toLowerCase().equals(entry.getValue().toLowerCase())){
				result.append("[ENG] "+query+"\n    ="+entry.getKey()+"\n\n");
			}
		}
		
	}
	private void wordlistsetup(){
		 String tContents = "";

		    try {
		        InputStream stream = getAssets().open("words.txt");

		        int size = stream.available();
		        byte[] buffer = new byte[size];
		        stream.read(buffer);
		        stream.close();
		        tContents = new String(buffer);
		    } catch (IOException e) {
		        // Handle exceptions here
		    }
		    
		    String[] pairs = tContents.split("\n");
		   
		    for(String str:pairs){
		    	String key=str.split("=")[0];
		    	String val=str.replaceFirst(key+"=", "");
		    	    	
		    	System.out.println(key+"!"+val);
		    	
		    	words.put(key, val);
		    	System.out.println(suggest.size());
		    	suggest.add(key);
		    	suggest.add(val);
		    }
		    
		   
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
