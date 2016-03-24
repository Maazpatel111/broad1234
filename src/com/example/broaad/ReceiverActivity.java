package com.example.broaad;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

public class ReceiverActivity extends Activity
{
	Bitmap bmp;

	Paint paint = new Paint();
	Rect src=new Rect();
	Rect dst=new Rect();

	private Canvas canvas;
	{
		paint.setColor(0xff000000);
	}

	class ReceiverTask extends AsyncTask<Void, Object, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... arg0)
		{
			// msocket
			int count=0;
			try
			{	
				MulticastSocket ms = new MulticastSocket(2000);
//				MainActivity main = null;
//				EditText str = main.ipaddress;
				// String groupaddress =str.getText().toString();// "224.0.0.1";
				String groupaddress = "224.0.0.1";
				ms.joinGroup(InetAddress.getByName(groupaddress));
				//Toast.makeText(getApplicationContext(), "in recent activity", Toast.LENGTH_SHORT).show();
				// MulticastSocket ms=null;
				byte[] data = new byte[124000];
				DatagramPacket dp = new DatagramPacket(data, data.length);

				while (true)
				{
					try
					{
						System.err.println("Waiting for packet");
						ms.receive(dp);
						count++;
						//Toast.makeText(getApplicationContext(), "in recent activity", Toast.LENGTH_SHORT).show();
						System.err.println("Got packet : " + dp.getAddress().getHostAddress() + ":" + dp.getLength() + " bytes");
						
						DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
						int x = dis.readInt();
						int y = dis.readInt();
						int w = dis.readInt();
						int h = dis.readInt();
						System.err.println("x:"+x+"Y:"+y+"W"+w+"H"+h);
						
						Bitmap b = BitmapFactory.decodeByteArray(data, 16, dp.getLength() - 16);
						int h1 = b.getWidth();
						int w1 = b.getHeight();
						
						System.err.println("" + h1 + "" + w1);

						publishProgress(x, y, w, h, b);
						System.out.println("" + h1 + "" + w1);
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				// return true;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			System.err.println(""+count);
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Object... values)
		{
			int x = (Integer) values[0];
			int y = (Integer) values[1];
			int w = (Integer) values[2];
			int h = (Integer) values[3];
			Bitmap p = (Bitmap) values[4];
			canvas.drawBitmap(p, x, y, paint);
			v.invalidate();
		}
	}

	class BitmapView extends View
	{

		public BitmapView(Context context)
		{
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void draw(Canvas canvas)
		{
			// TODO Auto-generated method stub
			super.draw(canvas);
			canvas.drawBitmap(bmp, src, dst, paint);
			//canvas.drawBitmap(bmp, src, dst, paint);
		}
	}

	BitmapView v;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

		bmp = Bitmap.createBitmap(320, 480, Config.ARGB_8888);
		canvas = new Canvas(bmp);
		v = new BitmapView(this);
		int h = LayoutParams.MATCH_PARENT;
		int w = LayoutParams.MATCH_PARENT;
		// Toast.makeText(getApplicationContext(), "in recent activity",
		// Toast.LENGTH_SHORT).show();
		LayoutParams l = new LayoutParams(w, h);
		setContentView(v, l);
		// System.out.println("FOUND IMage"+h+""+w);
		 
		Display display = getWindowManager().getDefaultDisplay();
		src.left=0;
		src.top=0;
		src.right=display.getWidth();   //sender
		src.bottom=display.getHeight();	//sender
		
		dst.left=0;
		dst.top=0;
		dst.right=display.getWidth(); //system
		dst.bottom=display.getHeight(); //system
		//Toast.makeText(getApplicationContext(),""+dst.right+"   "+dst.bottom, Toast.LENGTH_SHORT).show();
		new ReceiverTask().execute();

	}

}
