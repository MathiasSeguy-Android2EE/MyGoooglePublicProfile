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
package com.android2ee.project.gplus.signin.publicid.model;

import com.google.android.gms.plus.model.moments.ItemScope;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to be a SimpleMoment
 */
public class SimpleMoment {
	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/
	String message;
	String id;
	ItemScope result;
	String startDate;
	ItemScope target;
	String type;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param message
	 * @param id
	 * @param result
	 * @param startDate
	 * @param target
	 * @param type
	 */
	public SimpleMoment(String message, String id, ItemScope result, String startDate, ItemScope target, String type) {
		this.message = message;
		this.id = id;
		this.result = result;
		this.startDate = startDate;
		this.target = target;
		this.type = type;
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
		if (o instanceof SimpleMoment) {
			return this.getId().equals(((SimpleMoment) o).getId());
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

	/**
	 * @return the result
	 */
	public ItemScope getResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(ItemScope result) {
		this.result = result;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the target
	 */
	public ItemScope getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(ItemScope target) {
		this.target = target;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

}
