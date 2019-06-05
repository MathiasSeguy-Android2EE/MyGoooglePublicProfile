package com.android2ee.project.gplus.signin.publicid.ui;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android2ee.project.gplus.signin.publicid.MAppInstance;
import com.android2ee.project.gplus.signin.publicid.MApplication;
import com.android2ee.project.gplus.signin.publicid.R;
import com.android2ee.project.gplus.signin.publicid.ui.personfragment.PersonFragment;
import com.android2ee.project.gplus.signin.publicid.ui.personfragment.PersonFragmentCallBack;
import com.android2ee.project.gplus.signin.publicid.ui.personfragment.PersonFragmentModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class MainActivity extends ActionBarActivity implements  GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener , PersonFragmentCallBack {
	/******************************************************************************************/
	/** Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The Tag for log
	 */
	private static final String TAG = "MainActivity";
	/**
	 * The PlusClient
	 */
	private GoogleApiClient mPlusClient;
	/**
	 * The connectionResult returned by the last connection try
	 */
	ConnectionResult mConnectionResult;
	/**
	 * TextView that displays the MajorInformation of the owner which is mPlusClient instance
	 */
	TextView txvOwnerMajorInformation;
	/**
	 * The SignIn native button
	 */
	SignInButton signInButton;
	/**
	 * The SignOut button
	 */
	// Button signOutButton;
	/**
	 * The revokeMenuItem
	 */
	MenuItem revokeMenuItem;
	/**
	 * The disconnectMenuItem
	 */
	MenuItem disconnectMenuItem;
	/**
	 * The fragment that displays the rest of the gui
	 */
	PersonFragment personFragment;
	/**
	 * Its model
	 */
	PersonFragmentModel personFragModel;
	/**
	 * The request code for the Intent connection launch by startActi
	 */
	int requestCodeResolverError = 12354;
	/**
	 * A boolean to know is the user asked for the connection or if it has be done by code (ex: in onStart)
	 */
	boolean connectionAskedByUser = false;
	/**
	 * A boolean to know is the activity has already reload the person it has to reload
	 */
	boolean alreadyReloadedPersonInContext = false;
	/**
	 * Boolean to know if the menu has to be updated
	 * Sometimes onConnected is called before menuConstruction
	 */
	boolean haveToUpdateMenuItemDisconnection = false;
	/**
	 * Boolean to know if the menu has to be updated
	 * Sometimes onConnected is called before menuConstruction
	 */
	boolean menuItemTargetVisibility = true;
	/**
	 * The current selected tab
	 */
	int currentTabs = 0;
	/**
	 * The string to store the currentTab in the bundle: savedInstanceState
	 */
	private final String CURRENT_TAB_KEY = "currentTabs";

	/******************************************************************************************/
	/** Managing Life Cycle **************************************************************************/
	/******************************************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		personFragment = (PersonFragment) getSupportFragmentManager().findFragmentById(R.id.person_fragment);
		personFragModel = personFragment.getModel();

		txvOwnerMajorInformation = (TextView) findViewById(R.id.owner_name);
		
		getSupportActionBar().setSubtitle(R.string.action_bar_subtitle);
		// Build your GPlus Person:
		// Register for AddActivity:This type of app activity is the generic fallback type. Use it when no other app
		// activity type is appropriate.
		// for DiscoverActivity:Use this type when your user discovers something in your app, such as discovering a
		// new
		// album.
		// And for CommentActivity: Use this type when your user comments on an article, blog entry, or other
		// creative
		// work.
		// And define your scope because writeMoment requires the PLUS_LOGIN OAuth 2.0 scope specified in the
		// PlusClient
		// constructor.
		// http://stuff.mit.edu:8001/afs/sipb/project/android/OldFiles/sdk/android-sdk-linux/extras/google/google_play_services/docs/reference/com/google/android/gms/plus/PlusClient.html
		// Build the Option
		Plus.PlusOptions plusOptions = new Plus.PlusOptions.Builder()
				.addActivityTypes("http://schemas.google.com/AddActivity",
						"http://schemas.google.com/ReviewActivity")
				.build();
		//Build the client
		mPlusClient = new GoogleApiClient.Builder(this)
				.addApi(Plus.API, plusOptions)
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		// .setScopes(Scopes.PLUS_LOGIN, Scopes.PLUS_PROFILE).build();
		// Add a listener to the SignInButton and link it to the connection
		signInButton = (SignInButton) findViewById(R.id.signinbutton);
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gPlusSignInClicked();

			}
		});

		// Add a listener to the SignOutButton and link it to the disconnection
		// signOutButton = (Button) findViewById(R.id.signoutbutton);
		// signOutButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// gPlusSignOutClicked();
		//
		// }
		// });

		if (savedInstanceState == null) {
			// Only if it's a creation not a destroy/create
			String userId = null;
			if (getIntent() != null && getIntent().getStringExtra("profileId") != null) {
				userId = getIntent().getStringExtra("profileId");
			}
			if (userId != null) {
				MAppInstance.ins.get().setSelectedPerson(userId);
			}
		}
		Log.e(TAG, "onCreate savedInstanceState="+savedInstanceState);
		if (savedInstanceState != null && savedInstanceState.getInt(CURRENT_TAB_KEY, -1) != -1) {
			Log.e(TAG, "onCreate currentTab "+savedInstanceState.getInt(CURRENT_TAB_KEY, 0));
			currentTabs=savedInstanceState.getInt(CURRENT_TAB_KEY, 0);
		}
		

	}

	/*
	 * (non-Javadoc)
	 * @see androidx.fragment.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(CURRENT_TAB_KEY, currentTabs);
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.e(TAG, "onStart");
		// Ensure calling that method in onStart not in onResume (because onResume is called to often and to late in the
		// activity life cycle.
		// Begin the service connection: retrieve if the user is already connected (throug the web or something else)
		// and/or just connect the user
		// It has to be done at the beginning of your activity/application to ensure an user is in
		if (!mPlusClient.isConnected() && !mPlusClient.isConnecting()) {
			// Be sure you'are not attempting to connect when you are connected or when you are connecting
			mPlusClient.connect();
			signInButton.setEnabled(false);
		}
		// If is connecting or connected, also disable the signin button
		if (mPlusClient.isConnected() || mPlusClient.isConnecting()) {
			// Be sure you'are not attempting to connect when you are connected or when you are connecting
//			mPlusClient.connect();
			signInButton.setEnabled(false);
		}
		// When the result is coming back, it will be handle by onConnected or onConnectionFailed
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
		// sure to not leak, close the connection. Each time you call connect you should call disconnect
		mPlusClient.disconnect();
	}

	/*
	 * (non-Javadoc)
	 * @see androidx.fragment.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "onResume state: Mapp.getSelected = " + MAppInstance.ins.get().getSelectedPerson()
				+ ", isConnected " + mPlusClient.isConnected());
		// detect if a person has to be reload
		if (!MAppInstance.ins.get().getSelectedPerson().equals(MApplication.NO_SELECTION)) {
			alreadyReloadedPersonInContext = false;
		}
		// then is connection is done,
		if (mPlusClient.isConnected() && !MAppInstance.ins.get().getSelectedPerson().equals(MApplication.NO_SELECTION)) {
			if (!alreadyReloadedPersonInContext) {
				alreadyReloadedPersonInContext = true;
				restoreSelectedPerson();

			}
		}

	}

	/**
	 * Restore the selected person stored in MAppInstance.ins.get().getSelectedPerson()
	 */
	private void restoreSelectedPerson() {
		Log.e(TAG, "restoreSelectedPerson state: Mapp.getSelected = " + MAppInstance.ins.get().getSelectedPerson()
				+ ", isConnected " + mPlusClient.isConnected());
		// If the user is connected
		// Else it will be done in the onConnect Method
		if (mPlusClient.isConnected()) {
			if (!MAppInstance.ins.get().getSelectedPerson().equals(MApplication.NO_SELECTION)) {
				//this is the code that works but I don't remeber what the legacy code wanted to do here
				Plus.PeopleApi.loadVisible(mPlusClient, People.OrderBy.ALPHABETICAL, null).setResultCallback(
						new ResultCallback<People.LoadPeopleResult>() {
							@Override
							public void onResult(People.LoadPeopleResult loadPeopleResult) {
//								loadPeopleResult.getNextPageToken();
//								loadPeopleResult.getStatus().isSuccess();
//								loadPeopleResult.getPersonBuffer();
							}
						}
				);

				//the legacy code that doesn't work anymore
//				mPlusClient.loadPeople(new OnPeopleLoadedListener() {
//
//					@Override
//					public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer, String nextPageToken) {
//						if (status.isSuccess()) {
//							personFragModel.selectedPerson(personBuffer.get(0));
//						}
//					}
//				}, MAppInstance.ins.get().getSelectedPerson());

				//An older legacy code
				//because of deprecation and fucking 4.4 delivery that is not ascendant compatible, fuckers
//				mPlusClient.loadPerson(new OnPersonLoadedListener() {
//					@Override
//					public void onPersonLoaded(ConnectionResult status, Person person) {
//						if (status.isSuccess()) {
//							personFragModel.selectedPerson(person);
//						}
//					}
//				}, MAppInstance.ins.get().getSelectedPerson());
			}
			//and restore selected tab
			personFragment.setCurrentTab(currentTabs);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see androidx.fragment.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		Log.e(TAG, "onBackPressed (back==finished) =" + personFragModel.isOwnerDisplayed());
		// When the back button is clicked, you have to pop the backStack
		// Here it's a fake BackStack following the cycle:
		// OwnerDisplayed->PeopleDisplayed
		if (personFragModel.isOwnerDisplayed()) {
			// So if owner is displayed we just quit
			super.onBackPressed();
		} else {
			// Else it means we have to pop and then display the owner again
			personFragModel.displayOwner();
			MAppInstance.ins.get().setSelectedPerson(MApplication.NO_SELECTION);
		}
	}

	/******************************************************************************************/
	/** Managing Menu **************************************************************************/
	/******************************************************************************************/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		disconnectMenuItem = menu.findItem(R.id.action_disconnect);
		revokeMenuItem = menu.findItem(R.id.action_revoke);
		if (haveToUpdateMenuItemDisconnection) {
			haveToUpdateMenuItemDisconnection = false;
			disconnectMenuItem.setVisible(menuItemTargetVisibility);
			revokeMenuItem.setVisible(menuItemTargetVisibility);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent browserIntent;
		switch (item.getItemId()) {
		case R.id.action_show_android2ee:
			// open browser on the web pages
			browserIntent = new Intent("android.intent.action.VIEW");
			// if (getResources().getConfiguration().locale.getDisplayName().contains("fr")) {
			// open the browser on android2ee
			browserIntent.setData(Uri.parse(getResources().getString(R.string.android2ee_url)));
			// } else {
			// // open the browser on android2ee english version
			// browserIntent.setData(Uri.parse("http://www.android2ee.com/en"));
			// }
			startActivity(browserIntent);
			return true;
		case R.id.action_show_mathias:
			// open browser on the web pages
			browserIntent = new Intent("android.intent.action.VIEW");
			browserIntent.setData(Uri.parse(getResources().getString(R.string.mse_dvp_url)));
			startActivity(browserIntent);
			return true;
		case R.id.action_mail_mathias:
			// load string for email:
			String subject = getResources().getString(R.string.mail_subject);
			String body = getResources().getString(R.string.mail_body);
			// send an email
			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { getResources().getString(R.string.mse_email) });
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
			startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			return true;
		case R.id.action_disconnect:
			mPlusClient.disconnect();
			userDisconnectedGuiUpdate();
			return true;
		case R.id.action_revoke:
			gPlusSignOutClicked();
			return true;
//		case R.id.action_project:
//			return true;
//		case R.id.action_settings:
//			return true;
		default:
			return super.onOptionsItemSelected( item);
		}
	}

	

	/******************************************************************************************/
	/** Managing connection **************************************************************************/
	/******************************************************************************************/
	/**
	 * Called when the plus button is hit
	 */
	private void gPlusSignInClicked() {
		Log.e(TAG, "gPlusSignInClicked");
		Log.e(TAG, "mPlusClient.isConnected(): " + mPlusClient.isConnected() + " mConnectionResult==null "
				+ (mConnectionResult == null) + " => " + (!mPlusClient.isConnected() && mConnectionResult != null));
		connectionAskedByUser = true;
		if (!mPlusClient.isConnected()&&!mPlusClient.isConnecting() && mConnectionResult != null) {
			try {
				Log.e(TAG, "mConnectionResult.startResolutionForResult(this, requestCodeResolverError");
				mConnectionResult.startResolutionForResult(this, requestCodeResolverError);
			} catch (SendIntentException e) {
				Log.e(TAG, "SendIntentException");
				mConnectionResult = null;
				mPlusClient.connect();
				signInButton.setEnabled(false);
			}
		} else if (!mPlusClient.isConnected() && mConnectionResult == null) {
			// This case is called when you haven't already try to connect (meaning you don't have the
			// mPlusClient.connect in your onResume or onStart method)
			// In our case, we will never go through this code
			// just try to connect
			mPlusClient.connect();
			signInButton.setEnabled(false);
		} else {
			// Something forgotten or what ?
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// This method is called when the activity of connection started by onActivityResult(requestCode, resultCode,
		// data) has finished
		// It has displayed the connection activity made by Google Fellow.
		Log.e(TAG, "onActivityResult");
		if (requestCode == requestCodeResolverError && resultCode == RESULT_OK) {
			Log.e(TAG, " onActivityResult RESULT_OK");
			// reset
			mConnectionResult = null;
			mPlusClient.connect();
			signInButton.setEnabled(false);
		} else {
			// ensure the connection is reset, else you won't be able to log your user in
			// because of the condition if (!mPlusClient.isConnected() && mConnectionResult != null) {
			// in the ClickListener of the SignInButton
			mConnectionResult = null;
			Log.e(TAG, "onActivityResult SendIntentException");
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/******************************************************************************************/
	/** Managing disconnection **************************************************************************/
	/******************************************************************************************/
	/**
	 * This method is called when the user click on the disconnect button
	 */
	private void gPlusSignOutClicked() {
		Log.e(TAG, "gPlusSignOutClicked");
		if (mPlusClient.isConnected()) {

			Log.d(TAG, "gPlusSignOutClicked on a connected client");
			// First case: Give your user the ability to choose an another account at the next connection
			// -------------------------------------------------------------------------------------------
			// First reset the G+ account bound to the application
			// Then when the user connect again it can choose an already existing account
//			mPlusClient.clearDefaultAccountAndReconnect();

			// Second case: Give your user the ability to reset its grants for your application
			// -------------------------------------------------------------------------------------------
			// If you want to delete the authorizations the user gave to you
			// you should call that method. At the next connection, the user will give you the new
			// grants
			// (The activity G+Connection will be launched again and the user will grant your
			// application to access to
			// circles, data as if he was connecting for the first time to your app)
			// You should always give your user the ability to do such a reset on the way your
			// application acts in its
			// G+ account
			// It also call disconnect on mPlusClient. If you disconnect the client before the call
			// of onAccessRevoked,
			// the listener below will never work.
			Plus.AccountApi.revokeAccessAndDisconnect(mPlusClient).setResultCallback(new ResultCallback<Status>() {
				@Override
				public void onResult(Status status) {
					Log.d(TAG, "Disconnected");
					mPlusClient.disconnect();
					userAccessRevoked(status);
				}
			});
			// Last case: Just disconnect the user, next call on connect, will connect him again smoothly
			// -------------------------------------------------------------------------------------------
			// He will have the same account and the same grants as before
			// disconnect the user
			// mPlusClient.disconnect();
			// Kill the connectionResult
			// mConnectionResult = null;

			// ok, just for this activity

			// Tips: If you disconnect before clearing account, you'll have the "java.lang.IllegalStateException: Not
			// connected. Call connect() and wait for onConnected() to be called." thrown in your face.
		}
	}

	/**
	 * This method is called when the user has is access revoked
	 * 
	 * @param status
	 *            of the revocation
	 */
	private void userAccessRevoked(Status status) {
		Log.e(TAG, "userAccessRevoked returns " + status);
		userDisconnectedGuiUpdate();
	}

	/**
	 * Update the Gui when no more user is connected
	 */
	private void userDisconnectedGuiUpdate() {
		// Change button visibility
		signInButton.setEnabled(true);
		signInButton.setVisibility(View.VISIBLE);
		txvOwnerMajorInformation.setVisibility(View.GONE);
		txvOwnerMajorInformation.setText("No user logged");
		// signOutButton.setEnabled(false);
		if (disconnectMenuItem != null) {
			// one MenuItem != null the other !=null too
			disconnectMenuItem.setVisible(false);
			revokeMenuItem.setVisible(false);
		} else {
			haveToUpdateMenuItemDisconnection = true;
			menuItemTargetVisibility = false;
		}

		// update the fragment using its model
		if (personFragModel.isOwnerDisplayed()) {
			personFragModel.unloadPerson();
		} else {
			personFragModel.displayEmptyOwner();

		}
		personFragModel.stopLoadingPicture();
	}

	/******************************************************************************************/
	/** Implementing GooglePlayServicesClient.ConnectionCallbacks **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		Log.e(TAG, "onConnected state: Mapp.getSelected = " + MAppInstance.ins.get().getSelectedPerson()
				+ ", isConnected " + mPlusClient.isConnected() + ", alreadyReloadedPersonInContext="
				+ alreadyReloadedPersonInContext);

		// The connection with the user is ok, you can use it
		// retrieve the user
//		Person gPlusGuy = mPlusClient.getCurrentPerson();
		Person accountOwner = Plus.PeopleApi.getCurrentPerson(mPlusClient);
		if (accountOwner != null) {
			// Change button visibility
			signInButton.setEnabled(false);
			signInButton.setVisibility(View.GONE);
			// TODO MSE delete this line
			// txvOwnerMajorInformation.setVisibility(View.GONE);
			// signOutButton.setEnabled(true);
			if (disconnectMenuItem != null) {
				// one MenuItem != null the other !=null too
				disconnectMenuItem.setVisible(true);
				revokeMenuItem.setVisible(true);
			} else {
				haveToUpdateMenuItemDisconnection = true;
				menuItemTargetVisibility = true;
			}
			// If the activity has been launched by an DeepLink find the user to display
			if (!MAppInstance.ins.get().getSelectedPerson().equals(MApplication.NO_SELECTION)) {
				Log.e(TAG, "onConnected :!MAppInstance.ins.get().getSelectedPerson().equals(MApplication.NO_SELECTION)");
				if (!alreadyReloadedPersonInContext) {
					Log.e(TAG, "onConnected :!alreadyReloadedPersonInContext");
					alreadyReloadedPersonInContext = true;
					restoreSelectedPerson();
				} else {
					Log.e(TAG, "onConnected :alreadyReloadedPersonInContext");
					// or initialize the application with the owner
					personFragModel.loadPerson(accountOwner);
				}

			} else {
				Log.e(TAG, "onConnected : MAppInstance.ins.get().getSelectedPerson().equals(MApplication.NO_SELECTION)");
				// or initialize the application with the owner
				personFragModel.loadPerson(accountOwner);
			}
			// And update the ownerBanner
			txvOwnerMajorInformation.setText("Hello " + accountOwner.getDisplayName());

			//and restore selected tab
			personFragment.setCurrentTab(currentTabs);
		} else {
			// don't know why but a problem appears
			// Just disconnect and try connect again
			Toast.makeText(this, "A problem occurs when connecting. Just try again to connect, It should be ok.",
					Toast.LENGTH_LONG).show();
		}
	}

	//it is the deprecated onDisconnected method
	@Override
	public void onConnectionSuspended(int i) {
		// When using AccessRevoked, this method is never called
		Log.e(TAG, "onDisconnected ");
		// Last case: Just disconnect the user, next call on connect, will connect him again smoothly
		// -------------------------------------------------------------------------------------------
		// He will have the same account and the same grants as before
		// disconnect the user
		mPlusClient.disconnect();
		// Kill the connectionResult
		mConnectionResult = null;
		userDisconnectedGuiUpdate();
	}

	/******************************************************************************************/
	/** Implementing GooglePlayServicesClient.OnConnectionFailedListener **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google
	 * .android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.e(TAG,
				"onConnectionFailed " + result + " hasResolution: " + result.hashCode() + " errorCode: "
						+ result.getErrorCode());
		signInButton.setEnabled(true);
		// You can just set the mConnectionResult for your user to be able to connect using the signInButton
		// @see gPlusSignInClicked()
		if (!connectionAskedByUser) {
			// This case is when you want your user to click the g+connection button to connect
			// and not hide it by an automatic connection
			mConnectionResult = result;
		} else {
			// Or you want to automagicly display him the solution to its problem either by showing the native SignIn
			// Activity or the createAccount one. It's handle by the google team throught the GoogleService Api

			try {
				// if auto-connection failed then ensure to display the SignIn Activity to the user for him to be able
				// to sign in if it's a Sign_In_Required else just try to find a solution
				if (result.hasResolution()) {
					result.startResolutionForResult(this, requestCodeResolverError);
				}
			} catch (SendIntentException e) {
				Log.e(TAG, "onConnectionFailed " + result, e);
			} finally {
				connectionAskedByUser = false;
			}
		}
	}

//	/*
//	 * (non-Javadoc)
//	 * @see com.android2ee.project.gplus.signin.publicid.ui.PersonFragmentCallBack#getPlusClient()
//	 */
//	@Override
//	public PlusClient getPlusClient() {
//		return mPlusClient;
//	}


	/**
	 * @return The current user connected
	 */
	@Override
	public GoogleApiClient getPlusClient() {
		return mPlusClient;
	}

	/* (non-Javadoc)
             * @see com.android2ee.project.gplus.signin.publicid.ui.personfragment.PersonFragmentCallBack#onCurrentTabChanged(int)
             */
	@Override
	public void onCurrentTabChanged(int currentTab) {
		Log.e(TAG, "onCurrentTabChanged="+currentTab);
		currentTabs=currentTab;
	}

}
