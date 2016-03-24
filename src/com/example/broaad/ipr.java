package com.example.broaad;

import java.util.List;



import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ipr extends Activity{

	WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();

    View v;
    WifiConfiguration wifiConfiguration = new WifiConfiguration();
    ArrayAdapter< String> st;
    
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.ipr);
		
		
		
		
			//=(ListView) findViewById(R.id.listView1);
	       mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
	       mainWifi.setWifiEnabled(true);
	       receiverWifi = new WifiReceiver();
	       st=new ArrayAdapter<String>(getApplicationContext(),R.layout.my_list_item);
	       
	       
	       registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	       mainWifi.startScan();
	     //  mainWifi.startScan();
	       
		
		
		
		/*
		
		*/
	       final Dialog d=new Dialog(ipr.this);
			
	    Button scan=(Button) findViewById(R.id.button2);
	    scan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d.setContentView(R.layout.wifi_list);
				ListView lv=(ListView) d.findViewById(R.id.listView1);
				d.setTitle("Wifi List");
				lv.setAdapter(st);
				d.show();
				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {
						// TODO Auto-generated method stub
					//	Toast.makeText(getApplicationContext(), ""+st.getItem(arg2), Toast.LENGTH_SHORT).show();	// TODO Auto-generated method stub
						//WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				      String wifi_name=st.getItem(arg2);
						// setup a wifi configuration
				      wifiConfiguration.SSID = "\"" + wifi_name + "\"";
				      wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
				     mainWifi.reconnect();
				      int res = mainWifi.addNetwork(wifiConfiguration);
				     System.err.print(""+res);

				      boolean b = mainWifi.enableNetwork(res, true);
				      
				      System.err.print("" + b);

				      mainWifi.setWifiEnabled(true);
				     String sg= mainWifi.getConnectionInfo().getSSID();
				      Toast.makeText(getApplicationContext(),"Connected to "+sg, Toast.LENGTH_LONG).show();
				      d.dismiss();
				      startActivity(new Intent(ipr.this,ReceiverActivity.class));
				      
				     // Toast.makeText(getApplicationContext(), ""+wifi_name, Toast.LENGTH_SHORT).show();
						
					}
				});
				Button b=(Button) d.findViewById(R.id.button1);
				b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						code();
						d.dismiss();
					}
				});
				
				
			}
		});
	       
		
	}
	 protected void onPause() {
	        unregisterReceiver(receiverWifi);
	        super.onPause();
	    }

	    protected void onResume() {
	        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
	        super.onResume();
	        //mainWifi.startScan();
	    }
	void code(){
		//EditText ipAddress = (EditText)findViewById(R.id.editText1);
		InputFilter[] filters = new InputFilter[1];
		    filters[0] = new InputFilter() {
		        @Override
		        public CharSequence filter(CharSequence source, int start, int end,
		                android.text.Spanned dest, int dstart, int dend) {
		            if (end > start) {
		                String destTxt = dest.toString();
		                String resultingTxt = destTxt.substring(0, dstart) + source.subSequence(start, end) + destTxt.substring(dend);
		                if (!resultingTxt.matches ("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) { 
		                    return "";
		                } else {
		                    String[] splits = resultingTxt.split("\\.");
		                    for (int i=0; i<splits.length; i++) {
		                        if (Integer.valueOf(splits[i]) > 255) {
		                            return "";
		                        }
		                    }
		                }
		            }
		            return null;
		        }

		    };
		//ipAddress.setFilters(filters);
		Button b1=(Button) findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent());
			}
		});
	}

	class WifiReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
        	   st.clear();
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            String str,str1;
            for(int i = 0; i < wifiList.size(); i++){
              
                str=((wifiList.get(i)).SSID);
                
              //str=mainWifi.getConnectionInfo().getSSID();
               // st.add(""+sb)
              //  st.clear();
                st.add(str);
                //Toast.makeText(c, str, Toast.LENGTH_SHORT).show();
                //l.setAdapter(st);
                str1="";
                }
           
          //mainText.setText(sb);
        }
    }
	
}
