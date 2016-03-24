package com.example.broaad;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

//import com.example.wifi.MainActivity.WifiReceiver;


import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ppt extends Activity {
	TextView mainText;
    List<ScanResult> wifiList;
    ListView scan;
    StringBuilder sb = new StringBuilder();
    ArrayAdapter<String> at;
    ArrayList<String> stringArrayList = new ArrayList<String>();
    
       int j=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.ppt);
		scan=(ListView) findViewById(R.id.listView1);
		at=new ArrayAdapter<String>(getApplicationContext(),R.layout.items);
		
		
		
		 //String str="/mnt/ext_card/";
		 String str="/mnt/ext_card/";
		 File f=new File(str);
		    final File[] listFile = f.listFiles();
		    Toast.makeText(getApplicationContext(), ""+f, Toast.LENGTH_SHORT).show();
		    walkdir(f);
		scan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String name=at.getItem(arg2);
				try
				{
					File open=new File(stringArrayList.get(arg2));
				Toast.makeText(getApplicationContext(), ""+open, Toast.LENGTH_SHORT).show();
				
				Intent i=new Intent(Intent.ACTION_VIEW);
				i.setDataAndType(Uri.fromFile(open), "application/pdf");
				i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(i);
				}
				catch(Exception e)
				{
					
				}
			
				// TODO Auto-generated method stub
				
			}
		});
		
		
		
	}
	public void walkdir(File dir) {
		 sb = new StringBuilder();
       String pdfPattern = ".ppt";

       File[] listFile = dir.listFiles();

       if (listFile != null) {
           for (int i = 0; i < listFile.length; i++) {

               if (listFile[i].isDirectory()) {
                   walkdir(listFile[i]);
               } else {
                 if (listFile[i].getName().endsWith(pdfPattern)){
                        
               	  //Do what ever u want
               	  //sb.append(listFile[i].getName().toString());
                	 at.add(listFile[i].getName());
                	 stringArrayList.add(listFile[i].getPath());
                	 
                	 j++;
                	 
                	 scan.setAdapter(at);
                	// Toast.makeText(getApplicationContext(), ""+listFile[i].getName(), Toast.LENGTH_SHORT).show();
               	  
                 }
               }
             }
       }
	}
        
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	

}
