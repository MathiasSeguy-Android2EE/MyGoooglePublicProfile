/**<ul>
 * <li>Goi13SignInUsingGPlus</li>
 * <li>com.android2ee.project.gplus.signin.publicid</li>
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
package com.android2ee.project.gplus.signin.publicid.arrayadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android2ee.project.gplus.signin.publicid.R;
import com.android2ee.project.gplus.signin.publicid.model.SimplePerson;

import java.util.List;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to displays the social graph of the user
 */
public class PersonArrayAdapter extends ArrayAdapter<SimplePerson> {
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
	PersonArrayAdapterCallBack callBack;
	/**
	 * The maxPosition
	 */
	int maxPosition = 100;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param context
	 *            should be the fragment
	 * @param callBack
	 *            should be its model
	 * @param objects
	 *            should be the persons to display
	 */
	public PersonArrayAdapter(Context context, PersonArrayAdapterCallBack callBack, List<SimplePerson> objects) {
		super(context, R.layout.item, objects);
		inflater = LayoutInflater.from(context);
		this.callBack = callBack;
	}

	/******************************************************************************************/
	/** Temp variables to avoid using temp variable as method's variable **************************************************************************/
	/******************************************************************************************/
	// Avoid using temp variable as method's variable
	private static SimplePerson simplePerson;
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
		simplePerson = getItem(position);
		myview = convertView;
		if (null == myview) {
			if (getItemViewType(position) == 0) {
				myview = inflater.inflate(R.layout.empty_item, null);
			} else {
				myview = inflater.inflate(R.layout.item, null);
			}
			viewHolder = new ViewHolder(myview);
			myview.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) myview.getTag();
		}
		viewHolder.getTxvMessage().setText(simplePerson.getMessage());
		viewHolder.getTxvName().setText(simplePerson.getName());
        if(simplePerson.getPicture(viewHolder.getImvPicture(),viewHolder.getPrb())==null){
            //then it will be uploaded when the picture will be available
            //so you just have to ensure the progressbar is visible
            viewHolder.getImvPicture().setVisibility(View.GONE);
            viewHolder.getPrb().setVisibility(View.VISIBLE);
        }else{
            viewHolder.getImvPicture().setImageDrawable(simplePerson.getPicture());
        }
		// Log.e("PersonArrayAdapter:getView",
		// "display "+simplePerson.getName()+" drawable "+simplePerson.getPicture());
		reloadLimit = Math.min(15, getCount() / 10);
		if (position > maxPosition - reloadLimit) {
			callBack.loadMorePeople();
			maxPosition = maxPosition + 100;
		}
		return myview;
	}

	/******************************************************************************************/
	/** Method to manage the different view type (in progress or with the drawable set) **************************************************************************/
	/******************************************************************************************/
	@Override
	public int getViewTypeCount() {
		// return the number of type managed by the list view:
		// We have two types, one for the even line, the other for the odd lines
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		// return the type of the element to be displayed at position position
		// We have two types, one for the even line, the other for the odd lines
		if (getItem(position).picture == null) {
			return 0;
		} else {
			return 1;
		}
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
		ImageView imvPicture;
		ProgressBar prb;

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
		 * @return the txvMessage
		 */
		public final TextView getTxvMessage() {
			if (null == txvMessage) {
				txvMessage = (TextView) boundView.findViewById(R.id.message);
			}
			return txvMessage;
		}

		/**
		 * @return the imvPicture
		 */
		public ImageView getImvPicture() {
			if (null == imvPicture) {
				imvPicture = (ImageView) boundView.findViewById(R.id.picture);
			}
			return imvPicture;
		}
		/**
		 * @return the prb
		 */
		public ProgressBar getPrb() {
			if(null==prb) {
				prb=(ProgressBar)boundView.findViewById(R.id.progress);
			}
			return prb;
		}

		/**
		 * @param prb the prb to set
		 */
		public void setPrb(ProgressBar prb) {
			this.prb = prb;
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
	public PersonArrayAdapter(Context context, int resource, int textViewResourceId, List<SimplePerson> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 * @param objects
	 */
	public PersonArrayAdapter(Context context, int resource, int textViewResourceId, SimplePerson[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	/**
	 * @param context
	 * @param resource
	 * @param textViewResourceId
	 */
	public PersonArrayAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public PersonArrayAdapter(Context context, int textViewResourceId, List<SimplePerson> objects) {
		super(context, textViewResourceId, objects);
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public PersonArrayAdapter(Context context, int textViewResourceId, SimplePerson[] objects) {
		super(context, textViewResourceId, objects);
	}

	/**
	 * @param context
	 * @param textViewResourceId
	 */
	public PersonArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

}
