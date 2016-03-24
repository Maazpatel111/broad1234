package com.example.broaad;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class paint extends Activity {
Button b1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.paint);
		
				getPackages();
				Intent i;
				PackageManager manager = getPackageManager();
				try {
				    i = manager.getLaunchIntentForPackage("com.doodlejoy.studio.doodleworld");
				    if (i == null)
				        throw new PackageManager.NameNotFoundException();
				    i.addCategory(Intent.CATEGORY_LAUNCHER);
				    startActivity(i);
				} catch (PackageManager.NameNotFoundException e) {

				}
		}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	class PInfo {
	    private String appname = "";
	    private String pname = "";
	    private String versionName = "";
	    private int versionCode = 0;
	    private Drawable icon;
	    private void prettyPrint() {
	       // Toast.makeText(getApplicationContext(), ""+appname + "\t" + pname + "\t" + versionName + "\t" + versionCode,Toast.LENGTH_SHORT).show();
	    }
	}

private ArrayList<PInfo> getPackages() {
    ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
    final int max = apps.size();
    for (int i=0; i<max; i++) {
        apps.get(i).prettyPrint();
    }
    return apps;
}

private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
    ArrayList<PInfo> res = new ArrayList<PInfo>();        
    List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
    for(int i=0;i<packs.size();i++) {
        PackageInfo p = packs.get(i);
        if ((!getSysPackages) && (p.versionName == null)) {
            continue ;
        }
        PInfo newInfo = new PInfo();
        newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
        newInfo.pname = p.packageName;
        newInfo.versionName = p.versionName;
        newInfo.versionCode = p.versionCode;
        newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
        res.add(newInfo);
    }
    return res; 
}
}
