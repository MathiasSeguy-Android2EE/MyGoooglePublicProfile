/**<ul>
 * <li>Goi13SignInUsingGPlus</li>
 * <li>com.android2ee.project.gplus.signin.publicid.ui</li>
 * <li>31 mai 2013</li>
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
package com.android2ee.project.gplus.signin.publicid.ui.personfragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android2ee.project.gplus.signin.publicid.MAppInstance;
import com.android2ee.project.gplus.signin.publicid.MApplication;
import com.android2ee.project.gplus.signin.publicid.R;
import com.android2ee.project.gplus.signin.publicid.arrayadapters.MomentArrayAdapterCallBack;
import com.android2ee.project.gplus.signin.publicid.arrayadapters.PersonArrayAdapterCallBack;
import com.android2ee.project.gplus.signin.publicid.helpers.GooglePlusHelper;
import com.android2ee.project.gplus.signin.publicid.model.SimpleMoment;
import com.android2ee.project.gplus.signin.publicid.model.SimplePerson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.moments.ItemScope;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.Person.AgeRange;
import com.google.android.gms.plus.model.people.Person.Cover;
import com.google.android.gms.plus.model.people.Person.Cover.CoverPhoto;
import com.google.android.gms.plus.model.people.Person.Urls;
import com.google.android.gms.plus.model.people.PersonBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to manage the Person displayed by the fragment PersonFragment
 */
/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class PersonFragmentModel implements PersonArrayAdapterCallBack, MomentArrayAdapterCallBack {
	/**
	 * The tag for the log
	 */
	private static final String TAG = "PersonFragmentModel";
	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/
	/**
	 * An Helper to displays the GooglePlus constant using string (married, male...)
	 */
	GooglePlusHelper gPHelper;
	/**
	 * The fragment that disaplys the current Person
	 */
	PersonFragment fragment;
	/**
	 * Current displayed person
	 */
	Person person;
	/**
	 * The owner/ the user connected
	 */
	GoogleApiClient mPlusClient;
	/**
	 * To know if the person displayed is the owner or not
	 */
	boolean isOwnerDisplayed = true;

	/**
	 * @return the isOwnerDisplayed
	 */
	public boolean isOwnerDisplayed() {
		return isOwnerDisplayed;
	}

	/**
	 * The callBack to talk to the parent of the fragment/ or its parent's model
	 */
	PersonFragmentCallBack callBack;
	/**
	 * The next Token to use to load the next bunch of people
	 */
	String nextPersonPageToken = null;
	/**
	 * The next Token to use to load the next bunch of moment
	 */
	String nextMomentPageToken = null;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param fragment
	 *            to be bound with
	 */
	public PersonFragmentModel(PersonFragment fragment) {
		this.fragment = fragment;
		gPHelper = new GooglePlusHelper(fragment.getResources());
	}

	/******************************************************************************************/
	/** Public methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * load a Person :
	 * Load all the fields with the person information
	 * 
	 * @param person
	 *            The person to load
	 */
	public void loadPerson(Person person) {
		stopLoadingPicture.set(false);
		this.person = person;
		//
		mPlusClient = callBack.getPlusClient();
		// enable button
		fragment.enableSendPost();
		displayPersonInformation();
		if (isOwnerDisplayed) {
			// because we can not browse the list of friends of a friend of us
			// no need to reload that list
			displayPersonMoment();
			displayPersonSocialGraph();
		}
	}

	/**
	 * Unload a Person :
	 * Clear all the fields
	 *
	 */
	public void unloadPerson() {
		this.person = null;
		if (isOwnerDisplayed) {
			// because we can not browse the list of friends of a friend of us
			// no need to release that list
			getPeopleStoredInApp().clear();
			getMomentsStoredInApp().clear();
		}
		fragment.clearPerson();
	}

	/**
	 * This method is called to reload the owner
	 * It has to reload the fragment with a owner, the one selected in fact
	 * Called by the Activity when handles OnBackPressed and the current owner is displayed
	 */
	public void displayOwner() {
		// flush the current display
		unloadPerson();
		isOwnerDisplayed = true;
		// hide tabs people and moment
		fragment.tabs.getTabWidget().getChildTabViewAt(3).setVisibility(View.VISIBLE);
		fragment.tabs.getTabWidget().getChildTabViewAt(4).setVisibility(View.VISIBLE);
		fragment.tabs.getTabWidget().getChildTabViewAt(5).setVisibility(View.VISIBLE);
		fragment.tabs.setCurrentTab(3);
		// And load the owner
		loadPerson(Plus.PeopleApi.getCurrentPerson(mPlusClient));
	}

	/**
	 * Display empty owner
	 * Called when the user disconnect as he is looking at another person (than him)
	 * Menaing Called by the Activity when handles OnBackPressed and the current owner is NOT displayed
	 */
	public void displayEmptyOwner() {
		// flush the current display
		isOwnerDisplayed = true;
		unloadPerson();
		// hide tabs people and moment
		fragment.tabs.getTabWidget().getChildTabViewAt(3).setVisibility(View.VISIBLE);
		fragment.tabs.getTabWidget().getChildTabViewAt(4).setVisibility(View.VISIBLE);
		fragment.tabs.getTabWidget().getChildTabViewAt(5).setVisibility(View.VISIBLE);
		fragment.tabs.setCurrentTab(0);
	}

	/**
	 * The boolean to stop loading picture
	 * Not exactly, to not set the loaded picture in fact
	 */
	public AtomicBoolean stopLoadingPicture = new AtomicBoolean(false);

	/**
	 * Called to stop the pictures loading
	 */
	public void stopLoadingPicture() {
		stopLoadingPicture.set(true);
	}

	/**
	 * Called by the activity when started with a deepLink to disaply the person gave by the deeplink
	 * 
	 * @param personSelected
	 */
	public void selectedPerson(Person personSelected) {
		selectedPersonSetContext();
		// Retrieve the Person selected
		loadPerson(personSelected);
	}

	/******************************************************************************************/
	/** Person selection Management **************************************************************************/
	/******************************************************************************************/
	/**
	 * This method is called when a Person is clicked
	 * It has to reload the fragment with a new Person, the one selected in fact
	 * 
	 * @param personSelected
	 *            The Person to display
	 */
	void selectedPerson(SimplePerson personSelected) {
		selectedPersonSetContext();
		MAppInstance.ins.get().setSelectedPerson(personSelected.getId());
		// Retrieve the Person selected
		Plus.PeopleApi.loadVisible(mPlusClient, People.OrderBy.ALPHABETICAL, null).setResultCallback(
				new ResultCallback<People.LoadPeopleResult>() {
					@Override
					public void onResult(People.LoadPeopleResult loadPeopleResult) {
						if(loadPeopleResult.getStatus().isSuccess()){
                            loadPerson(loadPeopleResult.getPersonBuffer().get(0));
                        }
                        loadPeopleResult.getNextPageToken();
					}
				}
		);
//		mPlusClient.loadPeople(new OnPeopleLoadedListener() {
//
//			@Override
//			public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
//				if (status.isSuccess()) {
//					loadPerson(personBuffer.get(0));
//					personBuffer.close();
//				}
//			}
//		}, personSelected.getId());

		
		//because of deprecation and fucking 4.4 delivery that is not ascendant compatible, fuckers
//		mPlusClient.loadPerson(new OnPersonLoadedListener() {
//			@Override
//			public void onPersonLoaded(ConnectionResult status, Person person) {
//				if (status.isSuccess()) {
//					loadPerson(person);
//				}
//			}
//		}, personSelected.getId());

	}

	/**
	 * This method is used to set the context for the fragment (tabs and co) to display a new person
	 */
	private void selectedPersonSetContext() {
		isOwnerDisplayed = false;
		unloadPerson();
		// hide tabs people and moment
		fragment.tabs.setCurrentTab(0);
		fragment.tabs.getTabWidget().getChildTabViewAt(3).setVisibility(View.GONE);
		fragment.tabs.getTabWidget().getChildTabViewAt(4).setVisibility(View.GONE);
		fragment.tabs.getTabWidget().getChildTabViewAt(5).setVisibility(View.GONE);
	}

	/******************************************************************************************/
	/** Moment and People data list assessor **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return the list of People to display
	 */
	List<SimplePerson> getPeopleStoredInApp() {
		return MAppInstance.ins.getApplication().getPeople();
	}

	/**
	 * @return the list of People to display
	 */
	List<SimpleMoment> getMomentsStoredInApp() {
		return MAppInstance.ins.getApplication().getMoments();
	}

	/******************************************************************************************/
	/** Managing Moment write/read **********************************************************/
	/** A moment is an activities in GPlus made by an application **********************************************************/
	/** It's not a post (called activity). **********************************************************/
	/******************************************************************************************/
	/**
	 * Write a moment in the stream of the user
	 * It's also called an "Application Activity"
	 */
	void writeMomentInStream() {
		Person owner = Plus.PeopleApi.getCurrentPerson(mPlusClient);
		ItemScope target = new ItemScope.Builder()
				// Also, use ItemScope.setId() to create a unique application specific ID for the item you are writing.
				// It should NOT be set as the Google+ user ID.
				.setId(new Date().toString())
				.setText(
						"Google Public Identity is used by " + owner.getDisplayName() + " which said "
								+ fragment.edtMomentMessage)
				.setDescription(
						owner.getDisplayName()
								+ " had a look at its google public profile and at his friends profile too using Google Public Profile an application of Android2ee (Taining Expertise in Android)")
				.setThumbnailUrl(owner.getImage().getUrl()).setType("http://schema.org/Comment").build();
		// Ensure in your new PlusClient.Builder(this, this, this).setVisibleActivities(
		// "http://schemas.google.com/AddActivity", "http://schemas.google.com/ListenActivity").build()
		// You have define the activity you want to add as your moment type
		Moment moment = new Moment.Builder().setType("http://schemas.google.com/CommentActivity").setTarget(target)
				.build();

		if (mPlusClient.isConnected()) {
            //TODO what about the next lines :
//            Plus.PeopleApi.
//			mPlusClient.writeMoment(moment);

			Log.e(TAG, "writeMomentInStream called mPlusClient.isConnected()");
		} else {

			Log.e(TAG, "writeMomentInStream called !!!!mPlusClient.isConnected()");
		}
		Toast.makeText(fragment.getActivity(), "Moment has been sent", Toast.LENGTH_SHORT).show();
	}

	/******************************************************************************************/
	/** Managing Interactive Post write/read **********************************************************/
	/** An interactive post is a post is in GPlus made by an application **********************************************************/
	/** When clicked drive you in the application (if you are on Android) or else on a web page. **********************************************************/
	/******************************************************************************************/
	/**
	 * Write a Moment on your GooglePlus friends wall
	 */
	void writeInteractivePostInStream() {
		sendInteractivePost(fragment.edtIPostMessage.getText().toString());
	}

	/**
	 * Send a reformatted moment
	 */
	void sendInteractivePostToCurrentPerson() {
		sendInteractivePost("Should have a look at what the public google profile of " + person.getDisplayName()
				+ " looks like", person.getId());
	}

	/**
	 * Build and send the moment
	 * 
	 * @param message
	 *            the message to post
	 */
	private void sendInteractivePost(String message) {
		sendInteractivePost(message, fragment.getString(R.string.project_gplus_profile_id));
	}

	/**
	 * Build and send the moment
	 * 
	 * @param message
	 *            the message to post
	 * @param userId
	 *            the userToDisplay
	 */
	private void sendInteractivePost(String message, String userId) {
		// Load the string
		String contentUrl = fragment
				.getString(R.string.content_url, fragment.getString(R.string.a2ee_gplus_profile_id));
		String contentDeepLinkIdUrl = fragment.getString(R.string.content_deepl_id_url, userId);
		String contentDeepLinkTitleUrl = fragment.getString(R.string.content_deepl_title_url);
		String contentDeepLinkDescUrl = fragment.getString(R.string.content_deepl_descr_url);
		String contentDeepLinkThumbUrl = fragment.getString(R.string.content_deepl_tumbnail_url, userId);
		Uri contentDeepLinkThumbURI = Uri.parse(contentDeepLinkThumbUrl);
		String addCallLabel = fragment.getString(R.string.add_call_label);
		String addCallUri = fragment.getString(R.string.add_call_uri, userId);
		Uri addCallURI = Uri.parse(addCallUri);
		String addCallDeepLinkId = fragment.getString(R.string.add_call_deeplink_id, userId);

		// then build your content
		PlusShare.Builder psBuilder = new PlusShare.Builder(fragment.getActivity());
		// The message
		psBuilder.setText(message);
		// The ContentUrl (it's a fake one here) content_url with project page
		psBuilder.setContentUrl(Uri.parse(contentUrl));

		// The deep link id: It will be used when coming back on the device
		// Parameters
		// deepLinkId The deep-link ID to a resource to share on Google+. This parameter is required.
		// title The title of the resource. Used if there is no content URL to display. This parameter is optional.
		// description The description of a resource. Used if there is no content URL to display. This parameter is
		// optional.
		// thumbnailUri The thumbnailUri for a resource. Used if there is no content URL to display. This parameter is
		// optional.
		psBuilder.setContentDeepLinkId(contentDeepLinkIdUrl, // content_deepl_id_url
				contentDeepLinkTitleUrl,// content_deepl_title_url
				contentDeepLinkDescUrl,// content_deepl_descr_url
				contentDeepLinkThumbURI);// my picture but why ?o?
		// The Action
		// Adds a call-to-action button for an interactive post. To use this method, you must have passed a signed-in
		// PlusClient to the Builder.Builder(Activity, PlusClient) constructor or an IllegalStateException will be
		// thrown.
		//
		// Parameters
		// label The call-to-action label. Choose a value from the list of list
		// uri The URL to link to when the user clicks the call-to-action. This parameter is required.

		psBuilder.addCallToAction(addCallLabel, addCallURI, addCallDeepLinkId);
		// And then drop the moment in your user stream
		fragment.startActivityForResult(psBuilder.getIntent(), 0);
	}

	/******************************************************************************************/
	/** Read Moments **************************************************************************/
	/******************************************************************************************/
	/**
	 * Load and display the Person's moments
	 */
	private void displayPersonMoment() {
		// You can use this method to load People and set the order by
		// but most important you can ask for bunch of data using maxResult and pageToken
		// mPlusClient.loadPeople(listener, collection, orderBy, maxResults, pageToken)
		// The other simple way is that one
		// mPlusClient.loadMoments(new OnMomentsLoadedListener() {
		//
		// @Override
		// public void onMomentsLoaded(ConnectionResult status, MomentBuffer momentBuffer, String nextPageToken,
		// String updated) {
		// displayLoadedMoments(status, momentBuffer, nextPageToken, updated);
		//
		// }
		// }, maxResult, pageToken , targetUrl , type , mPlusClient.getCurrentPerson().getId());
        //TODO the code below has or has not to be commented ?o? 20/06/2016
//		mPlusClient.loadMoments(new OnMomentsLoadedListener() {
//			public void onMomentsLoaded(ConnectionResult status, MomentBuffer momentBuffer, String nextPageToken,
//					String updated) {
//				displayLoadedMoments(status, momentBuffer, nextPageToken, updated);
//
//			}
//		});

	}

	/*
	 * (non-Javadoc)
	 * @see com.android2ee.project.gplus.signin.publicid.PersonArrayAdapterCallBack#loadMorePeople()
	 */
	@Override
	public void loadMoreMoments() {
		Log.e(TAG, "loadMoreMoment called");
        //TODO the code below has or has not to be commented ?o? 20/06/2016
//		mPlusClient.loadMoments(new OnMomentsLoadedListener() {
//
//			@Override
//			public void onMomentsLoaded(ConnectionResult status, MomentBuffer momentBuffer, String nextPageToken,
//					String updated) {
//				displayLoadedMoments(status, momentBuffer, nextPageToken, updated);
//
//			}
//		}, 100, nextMomentPageToken, null, null, mPlusClient.getCurrentPerson().getId());

	}

	/**
	 * Display loaded moment
	 * 
	 * @param status
	 *            The resulting connection status of the loadMoments(OnMomentsLoadedListener) request.
	 * @param momentBuffer
	 *            The requested moments. The listener must close this object when finished.
	 * @param nextPageToken
	 *            The continuation token, which is used to page through large result sets. Provide this
	 * @param updated
	 *            The time at which this collection of moments was last updated. Formatted as an RFC 3339
	 *            timestamp
	 */
	private void displayLoadedMoments(ConnectionResult status, MomentBuffer momentBuffer, String nextPageToken,
			String updated) {
		Log.e(TAG, "displayLoadedMoments : " + status.isSuccess() + " size " + momentBuffer.getCount() + " update "
				+ updated);
		if (momentBuffer.getCount() > 0) {
			nextMomentPageToken = nextPageToken;
			List<SimpleMoment> momentsCached = getMomentsStoredInApp();
			SimpleMoment current;
			if (status.isSuccess()) {
				try {
					for (Moment moment : momentBuffer) {
						current = new SimpleMoment("message unused", moment.getId(), moment.getResult(),
								moment.getStartDate(), moment.getTarget(), moment.getType());
						if (!momentsCached.contains(current)) {
							momentsCached.add(current);
						}
						Log.e(TAG, "moment loaded : " + moment.getId() + moment.getResult() + moment.getStartDate()
								+ moment.getTarget() + moment.getType());
					}
				} finally {
					momentBuffer.close();
					Log.e(TAG, "initializeListView called");
					if (fragment.listViewInitialize) {
						fragment.momentArrayAdapter.notifyDataSetChanged();
					} else {
						fragment.initializeListViews();
						fragment.listViewInitialize = true;
						// then display the ListView
						fragment.lsvMomentsList.setVisibility(View.VISIBLE);
						fragment.txvNoMomentMess.setVisibility(View.GONE);
					}
				}
			}
		}
	}

	/******************************************************************************************/
	/** Read Social Graph of the Owner !Not of the current displayed person **************************************************************************/
	/******************************************************************************************/
	/**
	 * Load and display the ownerSocial Graph
	 * You can not display the SocialGraph of a friend of you
	 */
	public void displayPersonSocialGraph() {
		// You can use this method to load People and set the order by
		// but most important you can ask for bunch of data using maxResult and pageToken
		// mPlusClient.loadPeople(listener, collection, orderBy, maxResults, pageToken)
		// The other simple way is that one
//		mPlusClient.loadVisiblePeople(new OnPeopleLoadedListener() {
//			@Override
//			public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
//				displayLoadedPeople(status, personBuffer, nextPageToken);
//				Log.e(TAG, "The number of loaded people is: "+personBuffer.getCount());
//			}
//		}, nextPersonPageToken);
        Plus.PeopleApi.loadVisible(mPlusClient, People.OrderBy.ALPHABETICAL, nextPersonPageToken).setResultCallback(
                new ResultCallback<People.LoadPeopleResult>() {
                    @Override
                    public void onResult(People.LoadPeopleResult loadPeopleResult) {
                        displayLoadedPeople(
                                loadPeopleResult.getStatus(),
                                loadPeopleResult.getPersonBuffer(),
                                loadPeopleResult.getNextPageToken());
                    }
                }
        );
//		mPlusClient.loadPeople(new OnPeopleLoadedListener() {
//			@Override
//			public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
//				displayLoadedPeople(status, personBuffer, nextPageToken);
//			}
//		}, Person.Collection.VISIBLE);
	}

	/*
	 * (non-Javadoc)
	 * @see com.android2ee.project.gplus.signin.publicid.PersonArrayAdapterCallBack#loadMorePeople()
	 */
	@Override
	public void loadMorePeople() {
		Log.e(TAG, "loadMorePeople called");
        Plus.PeopleApi.loadVisible(mPlusClient, People.OrderBy.ALPHABETICAL, nextPersonPageToken).setResultCallback(
                new ResultCallback<People.LoadPeopleResult>() {
                    @Override
                    public void onResult(People.LoadPeopleResult loadPeopleResult) {
                        displayLoadedPeople(
                                loadPeopleResult.getStatus(),
                                loadPeopleResult.getPersonBuffer(),
                                loadPeopleResult.getNextPageToken());
                    }
                }
        );

//
//		mPlusClient.loadVisiblePeople(new OnPeopleLoadedListener() {
//			@Override
//			public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
//				displayLoadedPeople(status, personBuffer, nextPageToken);
//				Log.e(TAG, "The number of loaded people is: "+personBuffer.getCount());
//			}
//		}, PlusClient.OrderBy.ALPHABETICAL, nextPersonPageToken);
		
		
		
//		mPlusClient.loadPeople(new OnPeopleLoadedListener() {
//			@Override
//			public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
//				displayLoadedPeople(status, personBuffer, nextPageToken);
//			}
//		}, Person.Collection.VISIBLE, Person.OrderBy.ALPHABETICAL, 100, nextPersonPageToken);
	}

	/**
	 * Display loaded people
	 * 
	 * @param status
	 *            The resulting connection status of the loadPeople(OnPeopleLoadedListener, int) request.
	 * @param personBuffer
	 *            The requested collection of people. The listener must close this object when finished.
	 * @param nextPageToken
	 *            The continuation token, which is used to page through large result sets. Provide this value in a
	 *            subsequent request to return the next page of results.
	 */
	private void displayLoadedPeople(Status status, PersonBuffer personBuffer, String nextPageToken) {
		Log.e(TAG,
				"displayLoadedPeople called status : "
						+ status.getStatusMessage());
		Log.e(TAG, "displayLoadedPeople called personBuffer : " + personBuffer.getCount());
		if (personBuffer.getCount() == 0 && !fragment.listViewInitialize) {
			// Do nothing ensure the EmptyPeopleList TextView is visible
			fragment.txvEmptyPeopleList.setVisibility(View.VISIBLE);
		} else {
			// Ensure the EmptyPeopleList TextView is gone and the listView is visible
			fragment.txvEmptyPeopleList.setVisibility(View.GONE);
			fragment.lsvPeopleList.setVisibility(View.VISIBLE);
			this.nextPersonPageToken = nextPageToken;
			SimplePerson current;
			List<SimplePerson> peopleCached = getPeopleStoredInApp();
			if (status.isSuccess()) {
				try {
					for (Person people : personBuffer) {
						// displayPersonInformation(people);
						if (people.getImage() != null) {
							current = new SimplePerson(people.getImage().getUrl(), people.getId(),
									people.getDisplayName());
						} else {
							current = new SimplePerson(null, people.getId(), people.getDisplayName());
						}
						if (!peopleCached.contains(current)) {
							peopleCached.add(current);
						}

					}
				} finally {
					personBuffer.close();
					Log.e(TAG, "initializeListView called");
					if (fragment.listViewInitialize) {
						fragment.personArrayAdapter.notifyDataSetChanged();
					} else {
						fragment.initializeListViews();
						fragment.listViewInitialize = true;
					}
				}
			}
		}

	}

	/******************************************************************************************/
	/** Update the owner **************************************************************************/
	/******************************************************************************************/

	/******************************************************************************************/
	/** Read the information of the people displayed *************************************************************************/
	/******************************************************************************************/
	/**
	 * 
	 */
	private void displayPersonInformation() {
		// Fill the top of the screen
		buildUserMajorInformation();
		// Fill the bragging
		buildBragging();
		// Fill personal information
		buildPersonalInformation();
		// Fill the bio
		buildBiography();
	}

	/**
	 * Retrieve and display the Personal information
	 *
	 */
	private void buildPersonalInformation() {
		// name
		Person.Name name = person.getName();
		if (name != null) {
			if (name.hasFamilyName()) {
				setTextViewText(fragment.txvName, name.getFamilyName());
			}
			if (name.hasHonorificPrefix()) {
				setTextViewText(fragment.txvHonoricPrefix, name.getHonorificPrefix());
			}
			if (name.hasGivenName()) {
				setTextViewText(fragment.txvGivenName, name.getGivenName());
			}
			if (name.hasGivenName()) {
				setTextViewText(fragment.txvGivenName, name.getGivenName());
			}
			if (name.hasHonorificSuffix()) {
				setTextViewText(fragment.txvHonoricSuffix, name.getHonorificSuffix());
			}
			if (name.hasMiddleName()) {
				setTextViewText(fragment.txvMiddleName, name.getMiddleName());
			}
		} else {
			setTextViewText(fragment.txvName, null);
			setTextViewText(fragment.txvHonoricPrefix, null);
			setTextViewText(fragment.txvGivenName, null);
			setTextViewText(fragment.txvGivenName, null);
			setTextViewText(fragment.txvHonoricSuffix, null);
			setTextViewText(fragment.txvMiddleName, null);
		}
		// NickName line
		setTextViewText(fragment.txvNickName, person.getNickname());

		// Account line
		// To have the account of the user you have to declare the permission in the manifest:
		// <uses-permission android:name="android.permission.GET_ACCOUNTS" />
        //TODO should find a way to do that 20/06/2016
//		setTextViewText(fragment.txvAccount, Plus.PeopleApi.getCurrentPerson(mPlusClient).getAccountName());
//		setTextViewText(fragment.txvAccount,
//				null != mPlusClient.getAccountName() ? "Account : " + mPlusClient.getAccountName() : null);

		// G+ Id
		setTextViewText(fragment.txvGplusId, null != person.getId() ? "G+ ID : " + person.getId() : null);
		// tagline
		setTextViewText(fragment.txvTagline, null != person.getTagline() ? "Tagline : " + person.getTagline() : null);

		// age range
		AgeRange mAgeRange = person.getAgeRange();
		if (mAgeRange != null) {
			if (mAgeRange.hasMax()) {
				setTextViewText(fragment.txvMaxAge, "MaxAge : " + Integer.toString(mAgeRange.getMax()));
			} else {
				setTextViewText(fragment.txvMaxAge, null);
			}
			if (mAgeRange.hasMin()) {
				setTextViewText(fragment.txvMinAge, "MinAge : " + Integer.toString(mAgeRange.getMin()));
			} else {
				setTextViewText(fragment.txvMinAge, null);
			}
		} else {
			setTextViewText(fragment.txvMaxAge, null);
			setTextViewText(fragment.txvMinAge, null);
		}
		// BirthDay
		setTextViewText(fragment.txvBirthday, person.getBirthday());

		// Circle count
		// int circleByCount = person.getCircledByCount();
		// strB.append("circleByCount :" + circleByCount + "\r\n");
		// if(person.hasCircledByCount()) {
		// setTextViewText(fragment.txvCMaxAge, Integer.toString(person.getCircledByCount()));
		// }else {
		// setTextViewText(fragment.txvMaxAge, null);
		// }

		// Builder
		StringBuilder strB = new StringBuilder();
		// Emails : Deprecated at the 4.4 version delivery of the GooglePlayService
//		List<Person.Emails> emails = person.getEmails();
//		if (null != emails && emails.size() != 0) {
//			strB.append("Emails\r\n");
//			for (Person.Emails email : emails) {
//				strB.append("email :" + email.getType() + "(" + email.getValue() + ")\r\n");
//			}
//			setTextViewText(fragment.txvEmails, strB.length() != 0 ? strB.toString() : null);
//			strB.setLength(0);
//		} else {
//			setEmptyTextViewTitle(fragment.txvEmails, "No Public Emails Adresses");
//		}

		// Organizations

		// A lot of data associated to the organization
		List<Person.Organizations> organizations = person.getOrganizations();
		if (null != organizations && organizations.size() != 0) {
			strB.append("Organizations\r\n");
			for (Person.Organizations org : organizations) {
				strB.append(org.getName() + "- " + org.getStartDate() + " - " + org.getStartDate() + "\r\n");
			}
			setTextViewText(fragment.txvOrganizations, strB.length() != 0 ? strB.toString() : null);
			strB.setLength(0);
		} else {
			setEmptyTextViewTitle(fragment.txvOrganizations, "No Public Organizations");
		}

		// Places lived

		List<Person.PlacesLived> placesLived = person.getPlacesLived();
		if (null != placesLived && placesLived.size() != 0) {
			strB.append("Places Lived\r\n");
			for (Person.PlacesLived place : placesLived) {
				strB.append(place.getValue() + "\r\n");
			}
			setTextViewText(fragment.txvPlacesLived, strB.length() != 0 ? strB.toString() : null);
			strB.setLength(0);
		} else {
			setEmptyTextViewTitle(fragment.txvPlacesLived, "No Public Known Places Lived");
		}

		// URL of the person profile
		strB.append("Links\r\n");
		String url = person.getUrl();
		if (url != null) {
			strB.append("" + url + "\r\n");
		}
		List<Urls> urls = person.getUrls();
		if (urls != null) {
			for (Urls currentUrl : urls) {
				strB.append("" + GooglePlusHelper.getUrlsType(currentUrl.getType()) + " " + currentUrl.getValue()
						+ "\r\n");
			}
		}
		setTextViewText(fragment.txvLinks, strB.length() != 0 ? strB.toString() : null);
		strB.setLength(0);

	}

	/**
	 * Set the text to the text view
	 * If the text is null, the text view is backgournded red else green
	 * 
	 * @param txv
	 * @param str
	 */
	private void setTextViewText(TextView txv, String str) {
		if (null == str) {
			txv.setBackgroundColor(fragment.translucentRed);
		} else {
			txv.setBackgroundColor(fragment.translucentGreen);
			txv.setText(str);
		}
	}

	/**
	 * Set the text to the text view
	 * the text view is backgournded red
	 * 
	 * @param txv
	 * @param str
	 */
	private void setEmptyTextViewTitle(TextView txv, String str) {
		txv.setBackgroundColor(fragment.translucentRed);
		if (null != str) {
			txv.setText(str);
		}
	}

	/**
	 * Build the biography of the this.person
	 */
	private void buildBiography() {
		String biography = person.getAboutMe();
		if (biography != null) {
			fragment.wevBio.loadData("bio : " + biography + "...<br/><br/>", "text/html;charset=UTF-8", null);
		} else {
			fragment.wevBio.loadData("bio : absente...<br/><br/>", "text/html;charset=UTF-8", null);
		}
	}

	/**
	 * Build the bragging of the this.person
	 */
	private void buildBragging() {
		// BraggingRights
		String braggingRights = person.getBraggingRights();
		if (braggingRights != null) {
			fragment.txvBragging.setText(braggingRights.replace(".", "\r\n"));
		} else {
			fragment.txvBragging.setText("No information available");
		}
	}

	/**
	 * Build the bragging of the this.person
	 */
	@SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
	private void buildUserMajorInformation() {
		// Cover
		Cover mPhotoCover = person.getCover();
		if (mPhotoCover != null) {
			if (MAppInstance.ins.getApplication().getCoverPicture() == null || !isOwnerDisplayed) {
				CoverPhoto coverPhoto = mPhotoCover.getCoverPhoto();
				String photoCoverUrl = coverPhoto.getUrl();
				fragment.prbImageLoading.setVisibility(View.VISIBLE);
				if (isOwnerDisplayed) {
					new LoadPictureAsyncTask(fragment.imvPhotoCover, MApplication.COVER_PICTURE,
							fragment.prbImageLoading, stopLoadingPicture).execute(photoCoverUrl);
				} else {
					new LoadPictureAsyncTask(fragment.imvPhotoCover, MApplication.NOT_CACHED, fragment.prbImageLoading,
							stopLoadingPicture).execute(photoCoverUrl);
				}
			} else {
				fragment.prbImageLoading.setVisibility(View.GONE);
				fragment.imvPhotoCover.setImageDrawable(MAppInstance.ins.getApplication().getCoverPicture());
			}
		} else {
			fragment.prbImageLoading.setVisibility(View.GONE);
			fragment.imvPhotoCover.setImageDrawable(fragment.getResources().getDrawable(R.drawable.ic_no_cover));
		}
		// Name
		String displayedName = person.getDisplayName();
		// RelationShip and Gender
		int relationShip = person.getRelationshipStatus();
		int gender = person.getGender();
		// Person's picture
		Person.Image mImage = person.getImage();
		if (mImage != null) {
			if (MAppInstance.ins.getApplication().getOwnerPhoto() == null || !isOwnerDisplayed) {
				fragment.prbOwnerImageLoading.setVisibility(View.VISIBLE);
				if (isOwnerDisplayed) {
					new LoadPictureAsyncTask(fragment.imvOwnerPicture, MApplication.OWNER_PHOTO,
							fragment.prbOwnerImageLoading, stopLoadingPicture).execute(mImage.getUrl());
				} else {
					new LoadPictureAsyncTask(fragment.imvOwnerPicture, MApplication.NOT_CACHED,
							fragment.prbOwnerImageLoading, stopLoadingPicture).execute(mImage.getUrl());
				}
			} else {
				fragment.prbOwnerImageLoading.setVisibility(View.GONE);
				fragment.imvOwnerPicture.setImageDrawable(MAppInstance.ins.getApplication().getOwnerPhoto());
			}
		} else {
			fragment.prbOwnerImageLoading.setVisibility(View.GONE);
			if(MAppInstance.ins.getApplication().getResources().getBoolean(R.bool.JellyBean)) {
				fragment.imvOwnerPicture.setBackground(fragment.getResources().getDrawable(R.drawable.ic_no_owner_picture));
			}else {
				fragment.imvOwnerPicture.setBackgroundDrawable(fragment.getResources().getDrawable(R.drawable.ic_no_owner_picture));
			}
		}
		// Location
		String location = person.getCurrentLocation();
		// Language
		String language = person.getLanguage();
		fragment.txvPersonBadge.setText(fragment.getResources().getString(R.string.owner_major_info,
				gPHelper.getGender(gender), displayedName, gPHelper.getRelationShip(relationShip), location, language));
	}

	/******************************************************************************************/
	/** Asynchronous Loading of data from web **************************************************************************/
	/******************************************************************************************/
	/**
	 * @author Mathias Seguy (Android2EE)
	 * @goals
	 *        This class aims to load Drawable from the web using a background thread
	 *        When the drawable is loaded it is set to the component
	 *        And it is saved in the Application Object depending on its type
	 */
	private static class LoadPictureAsyncTask extends AsyncTask<String, Void, Drawable> {
		/******************************************************************************************/
		/** Attribute **************************************************************************/
		/******************************************************************************************/
		/**
		 * The ImageView waiting for the drawable as backgroundImage
		 */
		ImageView imageView;
		/**
		 * The type of the loaded Drawable it can be MApplication.COVER_PICTURE or MApplication.OWNER_PHOTO
		 */
		int type;
		/**
		 * The progress bar to hide when the image is loaded
		 */
		ProgressBar progressbar;
		/**
		 * To stop loading the picture
		 */
		AtomicBoolean stopLoading;

		/******************************************************************************************/
		/** Constructors **************************************************************************/
		/******************************************************************************************/
		/**
		 * @param imageView
		 *            The ImageView waiting for the drawable as backgroundImage
		 * @param type
		 *            is MApplication.COVER_PICTURE or MApplication.OWNER_PHOTO
		 * @param progressbar
		 *            The progress bar to hide when the image is loaded
		 */
		public LoadPictureAsyncTask(ImageView imageView, int type, ProgressBar progressbar, AtomicBoolean stopLoading) {
			super();
			this.type = type;
			this.imageView = imageView;
			this.progressbar = progressbar;
			this.stopLoading = stopLoading;
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
			if (!stopLoading.get()) {
				imageView.setImageDrawable(result);
				if (type == MApplication.OWNER_PHOTO) {
					MAppInstance.ins.getApplication().setOwnerPhoto(result);
				} else if (type == MApplication.COVER_PICTURE) {
					MAppInstance.ins.getApplication().setCoverPicture(result);
				}
				if (progressbar != null) {
					progressbar.setVisibility(View.GONE);
				}
			}
		}

	}
}
