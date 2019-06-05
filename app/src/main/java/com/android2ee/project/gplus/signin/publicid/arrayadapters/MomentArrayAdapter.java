/**<ul>
 * <li>Goi13SignInUsingGPlus</li>
 * <li>com.android2ee.project.gplus.signin.publicid.model</li>
 * <li>30 mai 2013</li>
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
package com.android2ee.project.gplus.signin.publicid.arrayadapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android2ee.project.gplus.signin.publicid.R;
import com.android2ee.project.gplus.signin.publicid.model.SimpleMoment;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class is the array adapter that displays moment
 */
public class MomentArrayAdapter extends ArrayAdapter<SimpleMoment> {
	/******************************************************************************************/
	/** Attributes **************************************************************************/
	/******************************************************************************************/

	/**
	 * The inflater
	 */
	LayoutInflater inflater;
	/**
	 * The CallBack
	 */
	MomentArrayAdapterCallBack callBack;
	/**
	 * The maxPosition
	 */
	int maxPosition = 100;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param context should be the fragment
	 * @param callBack should be its model
	 * @param objects should be the moments to display
	 */
	public MomentArrayAdapter(Context context, MomentArrayAdapterCallBack callBack, List<SimpleMoment> objects) {
		super(context, R.layout.moment_item, objects);
		inflater = LayoutInflater.from(context);
		this.callBack = callBack;
	}

	/******************************************************************************************/
	/** Temp variables to avoid using temp variable as method's variable **************************************************************************/
	/******************************************************************************************/
	// Avoid using temp variable as method's variable
	private static SimpleMoment simpleMoment;
	private static View myview;
	private static ViewHolder viewHolder;
	private static int reloadLimit;

	/******************************************************************************************/
	/** GetView **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		simpleMoment = getItem(position);
		myview = convertView;
		if (null == myview) {
			myview = inflater.inflate(R.layout.moment_item, null);
			viewHolder = new ViewHolder(myview);
			myview.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) myview.getTag();
		}
		viewHolder.getTxvMessage().setText(simpleMoment.getMessage());
		viewHolder.getTxvName().setText(simpleMoment.getType());

		reloadLimit = Math.min(15, getCount() / 10);
		if (position > maxPosition - reloadLimit) {
			callBack.loadMoreMoments();
			maxPosition = maxPosition + 100;
		}
		return myview;
	}

	/******************************************************************************************/
	/** The ViewHolder **************************************************************************/
	/******************************************************************************************/
	/**
	 * @author Mathias Seguy (Android2EE)
	 * @goals
	 *        This class aims to be the ViewHolder of this
	 */
	public static class ViewHolder {
		View boundView;
		TextView txvName;
		TextView txvMessage;

		/**
		 * @param boundView
		 */
		private ViewHolder(View boundView) {
			super();
			this.boundView = boundView;
		}

		/**
		 * @return the txvName
		 */
		public final TextView getTxvName() {
			if (null == txvName) {
				txvName = (TextView) boundView.findViewById(R.id.nom);
			}
			return txvName;
		}

		/**
		 * @param txvName
		 *            the txvName to set
		 */
		public final void setTxvName(TextView txvName) {
			this.txvName = txvName;
		}

		/**
		 * @return the txvMessage
		 */
		public final TextView getTxvMessage() {
			if (null == txvMessage) {
				txvMessage = (TextView) boundView.findViewById(R.id.message);
			}
			return txvMessage;
		}

		/**
		 * @param txvMessage
		 *            the txvMessage to set
		 */
		public final void setTxvMessage(TextView txvMessage) {
			this.txvMessage = txvMessage;
		}

	}

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public MomentArrayAdapter(Context context, int resource, int textViewResourceId, List<SimpleMoment> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public MomentArrayAdapter(Context context, int resource, int textViewResourceId, SimpleMoment[] objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 */
	public MomentArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public MomentArrayAdapter(Context context, int textViewResourceId, List<SimpleMoment> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public MomentArrayAdapter(Context context, int textViewResourceId, SimpleMoment[] objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 */
	public MomentArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}

}
