package com.example.broaad;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.os.IBinder;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.Toast;

public class SenderService extends Service
{
	SenderThread currentSender;

	public class SenderThread extends Thread
	{
		public void run()
		{
			currentSender=this;
			try
			{
				//create network with multicast socket so that anyoine who listens to this port can join to our network 
				MulticastSocket ms = new MulticastSocket(2000);
				MainActivity main = null;
				System.out.println("maaz");
				//EditText str=main.ipaddress;
				
				
				//String groupaddress =str.getText().toString();// 
				String groupaddress="224.0.0.1";
				
				ms.joinGroup(InetAddress.getByName(groupaddress));
				System.out.println("maaz");
				Bitmap bmp = null;
				byte[] b = new byte[102400];
				int width = 320;
				int height = 480;
				ByteArrayOutputStream baos = new ByteArrayOutputStream(width * height * 2 * 3);
				System.out.println("maaz");
			//	File f=new File("/mnt/sdcard/img.png");
				while (Thread.currentThread() == currentSender)
				{	
					//f.delete();
					try
					{
						long start = System.currentTimeMillis();
						if (bmp == null)
						{
							bmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
						}
						System.out.println("maaz");
						//This line capture the screen 
						Process proc = Runtime.getRuntime().exec("su -c cat /dev/graphics/fb0");
						
						InputStream fis = proc.getInputStream();
						
						int n;
						baos.reset();
						while ((n = fis.read(b)) != -1)
						{
							baos.write(b, 0, n);
						}
			
						byte[] b2 = baos.toByteArray();
						System.out.println("BYTES READ:" + b.length);

						byte data[] = b2; // new byte[fis1.available()];

						int f1;
						int row = 0;
						int col = 0;
						// f1 = fis1.read(data);
						int pos = width * height * 4;

						for (int y = 0; y < height; y++)
						{
							for (int x = 0; x < width; x++)
							{
								int b1 = data[pos] | (data[pos + 1] << 8);
								int c = 0;
								int red = (255 * (b1 & 0x001F)) / 32;
								int green = (255 * ((b1 & 0x07E0) >> 5)) / 64;
								int blue = (255 * ((b1 & 0xF800) >> 11)) / 32;
								// c=(red<<16)|(red<<8)|red;
								c = (0xff << 24) | (red << 16) | (red << 8) | red;
								// c=(blue<<16)|(blue<<8)|blue;
								// c=(red<<16 )|(green<<8)|(blue);
								// if(red>240 && green>240 && blue>240)
								{
									// System.out.printf("%x %02d %02d %02d %03d %03d\n",b&0xffff,red,green,blue,x,y);
									// System.out.printf("%x\n",c);
								}
								bmp.setPixel(x, y, c);

								pos += 2;
							}

							// bmp.setPixel(100, y, color)

						}
						// Toast.makeText(getApplicationContext(), "" + y,
						// Toast.LENGTH_SHORT).show();
						long end = System.currentTimeMillis();
						long res = end - start;
						// Toast.makeText(getApplicationContext(), "" + bmp,
						// Toast.LENGTH_SHORT).show();// duration)
						System.out.println("difference timing" + res);
						// FileOutputStream fos = new FileOutputStream(new
						// File(f, "screen.png"));
							
						// bmp1.createBitmap(bmp, 100, 100, 400, 400);
						for (int y = 0; y < height; y += 100)
						{
							for (int x = 0; x < width; x += 100)
							{
								int w = Math.min(100,width-x);
								int h = Math.min(100, height-y);
								Bitmap bmppart=Bitmap.createBitmap(bmp, x, y, w, h);
								baos.reset();
								DataOutputStream dos=new DataOutputStream(baos);
								dos.writeInt(x);
								dos.writeInt(y);
								dos.writeInt(w);
								dos.writeInt(h);
								dos.flush();
								
								bmppart.compress(CompressFormat.PNG, 80, baos);
								byte[] b3 = baos.toByteArray();

								// byte []b="hellos".getBytes();
								
								DatagramPacket dp = new	 DatagramPacket(b3, b3.length);
								dp.setAddress(InetAddress.getByName(groupaddress));
								dp.setPort(2000);
								ms.send(dp);
							}
						}
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			} catch (Exception e)
			{
				//Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT);
				e.printStackTrace();
			}

		}
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		new SenderThread().start();
		return Service.START_STICKY;
	}

}
