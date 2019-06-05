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
package com.android2ee.project.gplus.signin.publicid.helpers;

import android.content.Context;
import android.content.res.Resources;

import com.android2ee.project.gplus.signin.publicid.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.Urls;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to displays GooglePlus constant
 */
public class GooglePlusHelper {
	/**
	 * The resources to grab the string
	 */
	Resources res;

	/**
	 * @param res
	 *            The resources to grab the string
	 */
	public GooglePlusHelper(Resources res) {
		this.res = res;
	}

	/**
	 * Convert a constant of type Person.Gender into a readable string
	 * 
	 * @param gender
	 *            the constant to convert in string (should be a gender)
	 * @return the string to display the gender
	 */
	public String getGender(int gender) {
		switch (gender) {
		case Person.Gender.FEMALE:
			return res.getString(R.string.female);
		case Person.Gender.MALE:
			return res.getString(R.string.male);
		case Person.Gender.OTHER:
			return res.getString(R.string.other);
		default:
			return res.getString(R.string.other);
		}
	}

	/**
	 * Convert a constant of type Person.RelationshipStatus into a readable string
	 * 
	 * @param relation
	 *            the constant to convert in string (should be a relation)
	 * @return the string to display the gender
	 */
	public String getRelationShip(int relation) {
		switch (relation) {
		case Person.RelationshipStatus.ENGAGED:
			return res.getString(R.string.engaged);
		case Person.RelationshipStatus.IN_A_RELATIONSHIP:
			return res.getString(R.string.in_a_relation);
		case Person.RelationshipStatus.IN_CIVIL_UNION:
			return res.getString(R.string.civil_union);
		case Person.RelationshipStatus.IN_DOMESTIC_PARTNERSHIP:
			return res.getString(R.string.domestic_partner);
		case Person.RelationshipStatus.ITS_COMPLICATED:
			return res.getString(R.string.complicated);
		case Person.RelationshipStatus.MARRIED:
			return res.getString(R.string.married);
		case Person.RelationshipStatus.OPEN_RELATIONSHIP:
			return res.getString(R.string.open_relation);
		case Person.RelationshipStatus.SINGLE:
			return res.getString(R.string.single);
		case Person.RelationshipStatus.WIDOWED:
			return res.getString(R.string.widowed);
		default:
			return res.getString(R.string.other);
		}
	}

	/**
	 * @param connectionResult
	 * @param context
	 * @return the readable connection result
	 */
	public static String getError(int connectionResult, Context context) {
		String result = null;
		switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(context)) {
		case ConnectionResult.DEVELOPER_ERROR:
			result = "DEVELOPER_ERROR";
			break;
		case ConnectionResult.INTERNAL_ERROR:
			result = "INTERNAL_ERROR";
			break;
		case ConnectionResult.INVALID_ACCOUNT:
			result = "INVALID_ACCOUNT";
			break;
		case ConnectionResult.LICENSE_CHECK_FAILED:
			result = "LICENSE_CHECK_FAILED";
			break;
		case ConnectionResult.NETWORK_ERROR:
			result = "NETWORK_ERROR";
			break;
		case ConnectionResult.RESOLUTION_REQUIRED:
			result = "RESOLUTION_REQUIRED";
			break;
		case ConnectionResult.SERVICE_DISABLED:
			result = "SERVICE_DISABLED";
			break;
		case ConnectionResult.SERVICE_MISSING:
			result = "SERVICE_MISSING";
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			result = "SERVICE_VERSION_UPDATE_REQUIRED";
			break;
		case ConnectionResult.SIGN_IN_REQUIRED:
			result = "SIGN_IN_REQUIRED";
			break;
		case ConnectionResult.SUCCESS:
			result = "SUCCESS";
			break;
		}
		return result;
	}

	public static String getUrlsType(int type) {
		String result = null;
		switch (type) {
		case Urls.Type.CONTRIBUTOR:
			result = "HOME";
			break;
		case Urls.Type.OTHER_PROFILE:
			result = "BLOG";
			break;
		case Urls.Type.OTHER:
			result = "OTHER";
			break;
		case Urls.Type.WEBSITE:
			result = "PROFILE";
			break;
		default:
			result = "Unknown";
			break;

		}
		return result;
	}
}
