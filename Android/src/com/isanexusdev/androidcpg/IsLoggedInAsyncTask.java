package com.isanexusdev.androidcpg;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

public class IsLoggedInAsyncTask extends AsyncTask<String, Integer, Integer> {
	private static final String TAG = IsLoggedInAsyncTask.class.getName();
	URL connectURL;
	boolean success;
	String mReply = "";
	private IsLoggedInListener mListener = null;
	public static interface IsLoggedInListener {
		public void result(int result, IsLoggedInAsyncTask isLoggedInAsyncTask);
	}
	
	public IsLoggedInAsyncTask(IsLoggedInListener listener){
		mListener = listener;
		try{
			connectURL = new URL(Utils.mHost+"plugins/androidcpg/login.php");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public String getReply(){
		return mReply;
	}
	
	@Override
	protected void onCancelled() {
		if (mListener != null){
			try {
				mListener.result(0, this);
			} catch (Exception e) {}
		}
	}

	@Override
	protected void onPostExecute(Integer result) {
		if (mListener != null){
			try {
				mListener.result(result, this);
			} catch (Exception e) {}
		}
	}
	@Override
	protected Integer doInBackground(String... params) {
		success = true;
		HttpURLConnection conn = null;
		try
		{
			//------------------ CLIENT REQUEST

			// Open a HTTP connection to the URL
			System.setProperty("http.keepAlive", "false");
			conn = (HttpURLConnection) connectURL.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			try {
				mReply=b.toString();
			} catch (Exception e) {
				mReply = Utils.getStackTrace(e);
				success = false;
			}
			try {
				is.close();
			} catch (Exception e){

			}
			try {
				conn.disconnect();
			} catch (Exception e){}
		}
		catch (MalformedURLException ex){
			mReply = Utils.getStackTrace(ex);
			success = false;
		}catch (IOException ioe){
			mReply = Utils.getStackTrace(ioe);
			success = false;
		}catch (Exception ioe){
			mReply = Utils.getStackTrace(ioe);
			success = false;
		}

		if (success && mReply != null){
			if (mReply.toLowerCase().contains("form action=\"login.php?referer=") || mReply.toLowerCase().contains("form action=\"member.php\" method=\"post\"")){
				try
				{
					String lineEnd = "\r\n";
					String twoHyphens = "--";
					String boundary = "*****";
					//------------------ CLIENT REQUEST
					try {
						if (conn != null) {
							conn.disconnect();
						}
					} catch (Exception e){}
					conn = (HttpURLConnection) connectURL.openConnection();

					// Allow Inputs
					conn.setDoInput(true);

					// Allow Outputs
					conn.setDoOutput(true);

					// Don't use a cached copy.
					conn.setUseCaches(false);
					conn.setInstanceFollowRedirects(mReply.toLowerCase().contains("form action=\"member.php\" method=\"post\""));
					// Use a post method.
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

					DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"username\""+ lineEnd);
					dos.writeBytes("Content-Type: text/plain; charset=utf8"+lineEnd);
					dos.writeBytes(lineEnd);
					dos.write(params[0].getBytes("UTF-8"));
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"password\""+ lineEnd);
					dos.writeBytes("Content-Type: text/plain; charset=utf8"+lineEnd);
					dos.writeBytes(lineEnd);
					dos.write(params[1].getBytes("UTF-8"));
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"remember_me\""+ lineEnd + "" + lineEnd+ "1");
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"submitted\""+ lineEnd + "" + lineEnd+ "1");
					dos.writeBytes(lineEnd);
					dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

					// close streams
					dos.flush();
					dos.close();
					InputStream is = conn.getInputStream();
					// retrieve the response from server
					int ch;

					StringBuffer b =new StringBuffer();
					while( ( ch = is.read() ) != -1 ){
						b.append( (char)ch );
					}
					try {
						mReply=b.toString();
					} catch (Exception e) {
						mReply = Utils.getStackTrace(e);
						success = false;
					}
					try {
						is.close();
					} catch (Exception e){

					}
					try {
						conn.disconnect();
					} catch (Exception e){}
				}
				catch (MalformedURLException ex){
					mReply = Utils.getStackTrace(ex);
					success = false;
				}catch (IOException ioe){
					mReply = Utils.getStackTrace(ioe);
					success = false;
				}catch (Exception ioe){
					mReply = Utils.getStackTrace(ioe);
					success = false;
				}

				if (success && mReply != null){
					if (mReply.toLowerCase().contains("<div class=\"cpg_message_success\">")){
						SharedPreferences settings = AndroidCPG.getSharedPreferences();
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("username", params[0]);
						editor.putString("password", params[1]);
						editor.commit();

						return 1;
					} else {
						try
						{
							//------------------ CLIENT REQUEST
							try {
								if (conn != null) {
									conn.disconnect();
								}
							} catch (Exception e){}
							conn = (HttpURLConnection) new URL(Utils.mHost+"plugins/androidcpg/islogged.php").openConnection();

							// Open a HTTP connection to the URL
							System.setProperty("http.keepAlive", "false");

							// Allow Inputs
							conn.setDoInput(true);

							// Allow Outputs
							conn.setDoOutput(true);

							// Don't use a cached copy.
							conn.setUseCaches(false);
							conn.setInstanceFollowRedirects(false);
							InputStream is = conn.getInputStream();
							// retrieve the response from server
							int ch;

							StringBuffer b =new StringBuffer();
							while( ( ch = is.read() ) != -1 ){
								b.append( (char)ch );
							}
							try {
								mReply=b.toString();
							} catch (Exception e) {
								mReply = Utils.getStackTrace(e);
								success = false;
							}
							try {
								is.close();
							} catch (Exception e){

							}
							try {
								conn.disconnect();
							} catch (Exception e){}
						}
						catch (MalformedURLException ex){
							mReply = Utils.getStackTrace(ex);
							success = false;
						}catch (IOException ioe){
							mReply = Utils.getStackTrace(ioe);
							success = false;
						}catch (Exception ioe){
							mReply = Utils.getStackTrace(ioe);
							success = false;
						}

						if (success && mReply != null){
							if (mReply.trim().equalsIgnoreCase("true")){
								SharedPreferences settings = AndroidCPG.getSharedPreferences();
								SharedPreferences.Editor editor = settings.edit();
								editor.putString("username", params[0]);
								editor.putString("password", params[1]);
								editor.commit();
								
								return 1;
							} else {
								return 0;
							}
						} else {
							return 0;
						}
					}
				} else {
					return 0;
				}
			} else if (mReply.toLowerCase().contains("<h2>error</h2>")){
				if (mReply.toLowerCase().contains("plugin not enabled")){
					mReply = "Plugin not enabled";
					return 0;
				} else {
					//Already logged in (logged at browser probably)
					SharedPreferences settings = AndroidCPG.getSharedPreferences();
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("username", params[0]);
					editor.putString("password", params[1]);
					editor.commit();
					return 1;
				}
			} else {
				return 0;
			}
		}
		return -1;
	}

}

