/**<ul>
 * <li>Goi13SignInUsingGPlus</li>
 * <li>com.android2ee.project.gplus.signin.publicid.model</li>
 * <li>29 mai 2013</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.android2ee.project.gplus.signin.publicid.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to be a SimplePerson
 */
public class SimplePerson {
	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/
	public String url;
	public String id;
	public Drawable picture;
	public String name;
	public String message;

	/**
	 * Only used for the callBack of the imageloading
	 */
    WeakReference<ImageView> imageViewWeak=null;
    WeakReference<ProgressBar> progressBarWeak=null;
	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param url
	 * @param id
	 * @param name
	 */
	public SimplePerson(String url, String id, String name) {
		this.url = url;
		this.id = id;
		this.name = name;
		if (url != null && picture == null) {
			new LoadPictureAsyncTask(this).execute(url);
		}
	}

	/**
	 * @param picture
	 * @param name
	 */
	public SimplePerson(Drawable picture, String name) {
		this.picture = picture;
		this.name = name;
	}

	/******************************************************************************************/
	/** Equals and HashCode **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof SimplePerson) {
			return this.getId().equals(((SimplePerson) o).getId());
		}
		return super.equals(o);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	/******************************************************************************************/
	/** Getters/Setters **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return the picture
	 */
	public Drawable getPicture() {
		return picture;
	}
    /**
     * This method is to used when loading the picture
     * @return the picture
     */
    public Drawable getPicture(ImageView imageView,ProgressBar progressbar) {
        if(picture!=null){
            return picture;
        }else{
            imageViewWeak=new WeakReference<ImageView>(imageView);
            progressBarWeak=new WeakReference<ProgressBar>(progressbar);
            return null;
        }
    }

	/**
	 * @param picture
	 *            the picture to set
	 */
	public void setPicture(Drawable picture) {
		this.picture = picture;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/******************************************************************************************/
	/** Asynchronous Loading of data from web **************************************************************************/
	/******************************************************************************************/
	/**
	 * @author Mathias Seguy (Android2EE)
	 * @goals
	 *        This class aims to load the Drawable from the web using a background thread
	 *        When the drawable is loaded it is set to the field picture
	 *        And TODO it is saved in the Application Object depending on its person.id
	 */
	private static class LoadPictureAsyncTask extends AsyncTask<String, Void, Drawable> {
		/******************************************************************************************/
		/** Attribute **************************************************************************/
		/******************************************************************************************/
		private static final String TAG = "SimplePerson";
		/**
		 * The object that wait for its drawable
		 */
		SimplePerson simplePerson;

		/******************************************************************************************/
		/** Constructors **************************************************************************/
		/******************************************************************************************/

		public LoadPictureAsyncTask(SimplePerson simplePerson) {
			this.simplePerson = simplePerson;
		}

		/******************************************************************************************/
		/** AsyncTask usual methods **************************************************************************/
		/******************************************************************************************/
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Drawable doInBackground(String... params) {
			Bitmap x = null;
			String url = params[0];
			HttpURLConnection connection;
			try {
				connection = (HttpURLConnection) new URL(url).openConnection();
				connection.connect();
				InputStream input = connection.getInputStream();

				x = BitmapFactory.decodeStream(input);
				// Log.e(TAG, "Picture Loaded "+url);
				return new BitmapDrawable(x);
			} catch (MalformedURLException e) {
				Log.e(TAG, "MalformedURLException", e);
			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
			} catch (Exception e) {
				Log.e(TAG, "Exception", e);
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Drawable result) {
			// Log.e(TAG, "Load picture of: "+simplePerson.name);
			simplePerson.picture = result;
			//then tell it to the ImageView if there is one waiting to be updated
			if(simplePerson.imageViewWeak!=null&&simplePerson.imageViewWeak.get()!=null){
                simplePerson.imageViewWeak.get().setVisibility(View.VISIBLE);
                simplePerson.imageViewWeak.get().setImageDrawable(result);
                simplePerson.progressBarWeak.get().setVisibility(View.GONE);
            }
		}

	}
}
