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

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to be the singleton pattern for the Application object.
 *        You call MAppInstance.ins.get() to retrieve a pointer on the application object
 */
public enum MAppInstance {
	/**
	 * The instance
	 */
	ins;
	/**
	 * The application object
	 */
	MApplication application;

	/**
	 * @return the application object
	 */
	public MApplication getApplication() {
		return application;
	}

	/**
	 * @return the application object
	 */
	public MApplication get() {
		return application;
	}

	/**
	 * Set the application Object
	 * Should be called only by the Application object in its onCreate method
	 * 
	 * @param appli
	 */
	public void setApplication(MApplication appli) {
		application = appli;
	}
}
