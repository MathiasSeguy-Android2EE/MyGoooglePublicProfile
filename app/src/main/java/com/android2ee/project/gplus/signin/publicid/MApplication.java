/**<ul>
 * <li>ServiceHelper1</li>
 * <li>com.android2ee.formation.service.helper1</li>
 * <li>26 janv. 2013</li>
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
package com.android2ee.project.gplus.signin.publicid;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android2ee.project.gplus.signin.publicid.model.SimpleMoment;
import com.android2ee.project.gplus.signin.publicid.model.SimplePerson;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to be your casual Application.<br/>
 *        You should extends it instead of Application.
 *        If you don't want to extends it, just copy paste the code within your own application
 *        And don't forget to declare it in your manifest.xml file.
 */
public class MApplication extends Application {
	/******************************************************************************************/
	/** CONSTANTS **************************************************************************/
	/******************************************************************************************/
	/**
	 * Constant to specify that the drawable type is photo cover
	 */
	public final static int COVER_PICTURE = 0;
	/**
	 * Constant to specify that the drawable type is photo of the owner
	 */
	public final static int OWNER_PHOTO = 1;
	/**
	 * Constant to specify that the drawable type is not Cachaed
	 */
	public final static int NOT_CACHED = 2;
	/**
	 * Constant to specify there is no person selected
	 */
	public final static String NO_SELECTION = "NoPersonSelected";
	/******************************************************************************************/
	/** Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The list of people of the owner
	 */
	List<SimplePerson> people = null;
	/**
	 * The list of Moments of the owner
	 * TODO or of the current displayed person
	 */
	List<SimpleMoment> moments = null;
	/**
	 * The picture of the owner
	 */
	Drawable ownerPhoto = null;
	/**
	 * The phot cover
	 */
	Drawable coverPicture = null;
	/**
	 * Current selected person
	 */
	String selectedPerson = NO_SELECTION;

	/******************************************************************************************/
	/** MAnaging Life cycle **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.e("MApplication", "MApplication:onCreate called");
		super.onCreate();
		// register the App
		MAppInstance.ins.setApplication(this);
		// instantiate the list
		moments = new ArrayList<SimpleMoment>();
		people = new ArrayList<SimplePerson>();
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// There is a rumor on this method that it is not called by the system
		// this method is called only with emulator ?o?
		// ok so test
		Log.e("MApplication", "MApplication:onTerminate called");
		// test is ok, this method is never called on device.

		super.onTerminate();
	}

	/******************************************************************************************/
	/** Getters/Setters **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return the people
	 */
	public List<SimplePerson> getPeople() {
		if (people == null) {
			people = new ArrayList<SimplePerson>();
		}
		return people;
	}

	/**
	 * @return the moments
	 */
	public List<SimpleMoment> getMoments() {
		if (moments == null) {
			moments = new ArrayList<SimpleMoment>();
		}
		return moments;
	}

	/**
	 * @return the ownerPhoto
	 */
	public Drawable getOwnerPhoto() {
		// Log.e("TAG", "getOwnerPhoto called returns "+ownerPhoto);
		return ownerPhoto;
	}

	/**
	 * @param ownerPhoto
	 *            the ownerPhoto to set
	 */
	public void setOwnerPhoto(Drawable ownerPhoto) {
		// Log.e("TAG", "setOwnerPhoto called");
		this.ownerPhoto = ownerPhoto;
	}

	/**
	 * @return the coverPicture
	 */
	public Drawable getCoverPicture() {
		// Log.e("TAG", "getCoverPicture called"+coverPicture);
		return coverPicture;
	}

	/**
	 * @param coverPicture
	 *            the coverPicture to set
	 */
	public void setCoverPicture(Drawable coverPicture) {
		// Log.e("TAG", "setCoverPicture called");
		this.coverPicture = coverPicture;
	}

	/**
	 * @return the selectedPerson
	 */
	public String getSelectedPerson() {
		if (selectedPerson == null) {
			selectedPerson = this.NO_SELECTION;
		}
		return selectedPerson;
	}

	/**
	 * @param selectedPerson
	 *            the selectedPerson to set
	 */
	public void setSelectedPerson(String selectedPerson) {

		Log.e("TAG", "setSelectedPerson to " + selectedPerson);
		this.selectedPerson = selectedPerson;
	}
}
