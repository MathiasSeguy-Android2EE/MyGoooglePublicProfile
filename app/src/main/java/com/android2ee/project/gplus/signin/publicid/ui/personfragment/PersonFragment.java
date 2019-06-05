package com.android2ee.project.gplus.signin.publicid.ui.personfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.android2ee.project.gplus.signin.publicid.R;
import com.android2ee.project.gplus.signin.publicid.arrayadapters.MomentArrayAdapter;
import com.android2ee.project.gplus.signin.publicid.arrayadapters.PersonArrayAdapter;

/**<ul>
 * <li>Goi13SignInUsingGPlus</li>
 * <li></li>
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

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to display a Person
 */
public class PersonFragment extends Fragment {
	/**
	 * The tag for log
	 */
	private static final String TAG = "PersonFragment";
	/**
	 * The model that manage the data displayed by this
	 */
	PersonFragmentModel model;

	/**
	 * @return the model
	 */
	public PersonFragmentModel getModel() {
		return model;
	}

	/******************************************************************************************/
	/** TabHost Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The TabsHost
	 */
	TabHost tabs;
	/**
	 * The last selected tab (it means the current one but not exactly)
	 */
	int lastTabs = 0;
	/**
	 * The horizontal scroll that displays tabWidgets
	 */
	HorizontalScrollView hscvTabWidget;

	/******************************************************************************************/
	/** PhotoPart Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The person badge information
	 */
	TextView txvPersonBadge;
	/**
	 * Message to say watching a friend
	 */
	TextView txvPostAFriendProfile;
	/**
	 * The layout that handle the Photos Part
	 */
	ViewGroup ownerPictureLayout;
	/**
	 * The photo cover
	 */
	ImageView imvPhotoCover;
	/**
	 * Owner Picture
	 */
	ImageView imvOwnerPicture;
	/**
	 * The progressBar when image cover is loading
	 */
	ProgressBar prbImageLoading;
	/**
	 * The progressBar when image owner is loading
	 */
	ProgressBar prbOwnerImageLoading;
	/******************************************************************************************/
	/** Biography Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The webview that handles the biography
	 */
	WebView wevBio;

	/******************************************************************************************/
	/** Bragging Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The text view that displays the Bragging
	 */
	TextView txvBragging;

	/******************************************************************************************/
	/** Personal Information Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The TextView that displays the honoric prefix of the person
	 */
	TextView txvHonoricPrefix;
	/**
	 * The TextView that displays the given nameof the person
	 */
	TextView txvGivenName;
	/**
	 * The TextView that displays the middle name of the person
	 */
	TextView txvMiddleName;
	/**
	 * The TextView that displays the name of the person
	 */
	TextView txvName;
	/**
	 * The TextView that displays the honoric suffix of the person
	 */
	TextView txvHonoricSuffix;
	/**
	 * The TextView that displays the nickname of the person
	 */
	TextView txvNickName;
	/**
	 * The TextView that displays the account of the person
	 */
	TextView txvAccount;
	/**
	 * The TextView that displays the tagline of the person
	 */
	TextView txvTagline;
	/**
	 * The TextView that displays the Google+ id of the person
	 */
	TextView txvGplusId;
	/**
	 * The TextView that displays the birthday of the person
	 */
	TextView txvBirthday;
	/**
	 * The TextView that displays the min age of the person
	 */
	TextView txvMinAge;
	/**
	 * The TextView that displays the max age of the person
	 */
	TextView txvMaxAge;
	/**
	 * The TextView that displays the places lived of the person
	 */
	TextView txvPlacesLived;
	/**
	 * The TextView that displays the organizations of the person
	 */
	TextView txvOrganizations;
	/**
	 * The TextView that displays the links of the person
	 */
	TextView txvLinks;
	/**
	 * The TextView that displays the emails of the person
	 */
	TextView txvEmails;
	/******************************************************************************************/
	/** People Tab Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The adapter of the lsvPeopleList
	 */
	PersonArrayAdapter personArrayAdapter;
	/**
	 * The ListView that displays people
	 */
	ListView lsvPeopleList;
	/**
	 * The TextView that displays the emails of the person
	 */
	TextView txvEmptyPeopleList;
	/**
	 * A boolean to know if the list have been initialized
	 */
	boolean listViewInitialize = false;
	/******************************************************************************************/
	/** I.Post Tab Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The button to send a moment
	 */
	Button btnPost;
	/**
	 * The edit text to add a message to the moment
	 */
	EditText edtIPostMessage;

	/******************************************************************************************/
	/** Moment Tab Attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The adapter of the lsvMomentsList
	 */
	MomentArrayAdapter momentArrayAdapter;
	/**
	 * The ListView that displays moment
	 */
	ListView lsvMomentsList;
	/**
	 * The button to send a moment
	 */
	Button btnSend;
	/**
	 * The edit text to add a message to the moment
	 */
	EditText edtMomentMessage;
	/**
	 * The text view that says no moment to displays
	 */
	TextView txvNoMomentMess;
	/******************************************************************************************/
	/** Colors **************************************************************************/
	/******************************************************************************************/
	/**
	 * Red translucent background
	 */
	int translucentRed;
	/**
	 * Green translucent background
	 */
	int translucentGreen;

	/******************************************************************************************/
	/** Managing Life Cycle **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * @see androidx.fragment.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup,
	 * android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// initialiaze colors
		translucentRed = getResources().getColor(R.color.translucent_red);
		translucentGreen = getResources().getColor(R.color.translucent_green);

		// Initialize View and Views
		View view = inflater.inflate(R.layout.person_fragment, container, false);
		imvPhotoCover = (ImageView) view.findViewById(R.id.photocover);
		imvOwnerPicture = (ImageView) view.findViewById(R.id.image);
		txvBragging = (TextView) view.findViewById(R.id.bragging);
		txvHonoricPrefix = (TextView) view.findViewById(R.id.honoric_prefix);
		txvGivenName = (TextView) view.findViewById(R.id.given_name);
		txvMiddleName = (TextView) view.findViewById(R.id.middle_name);
		txvName = (TextView) view.findViewById(R.id.personal_name);
		txvHonoricSuffix = (TextView) view.findViewById(R.id.honoric_suffix);
		txvNickName = (TextView) view.findViewById(R.id.nickname);
		txvAccount = (TextView) view.findViewById(R.id.account);
		txvTagline = (TextView) view.findViewById(R.id.tagline);
		txvGplusId = (TextView) view.findViewById(R.id.gplus_id);
		txvBirthday = (TextView) view.findViewById(R.id.birthday);
		txvMinAge = (TextView) view.findViewById(R.id.min_age);
		txvMaxAge = (TextView) view.findViewById(R.id.max_age);
		txvPlacesLived = (TextView) view.findViewById(R.id.places_lived);
		txvOrganizations = (TextView) view.findViewById(R.id.organizations);
		txvLinks = (TextView) view.findViewById(R.id.links);
		txvEmails = (TextView) view.findViewById(R.id.emails);
		txvNoMomentMess = (TextView) view.findViewById(R.id.moment_empty_txv);
		txvPersonBadge = (TextView) view.findViewById(R.id.name);
		txvPostAFriendProfile = (TextView) view.findViewById(R.id.watching_friend);
		txvEmptyPeopleList = (TextView) view.findViewById(R.id.people_empty_txv);
		prbImageLoading = (ProgressBar) view.findViewById(R.id.progress_pictures);
		prbOwnerImageLoading = (ProgressBar) view.findViewById(R.id.progress_pictures_owner);
		edtMomentMessage = (EditText) view.findViewById(R.id.moment_edt);
		edtIPostMessage = (EditText) view.findViewById(R.id.ipost_edt);
		lsvPeopleList = (ListView) view.findViewById(R.id.people_list);
		lsvMomentsList = (ListView) view.findViewById(R.id.moment_list);
		wevBio = ((WebView) view.findViewById(R.id.biography));
		tabs = (TabHost) view.findViewById(R.id.tabhost);
		ownerPictureLayout = (ViewGroup) view.findViewById(R.id.major_inf_root_layout);
		btnPost = (Button) view.findViewById(R.id.ipost_send);
		btnPost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendIPost();
			}
		});
		btnSend = (Button) view.findViewById(R.id.moment_send);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMoment();
			}
		});
		txvPostAFriendProfile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				model.sendInteractivePostToCurrentPerson();
			}
		});
		// initialize the model
		model = new PersonFragmentModel(this);
		// TabHost management
		instanciateTabHost();
		// Clear all the fields:
		initializeEmptyField();
		// instanciate and display hscvTabWidget
		hscvTabWidget = (HorizontalScrollView) view.findViewById(R.id.hsvTabWidget);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * @see androidx.fragment.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// retrieve the callBack of the model
		model.callBack = (PersonFragmentCallBack) getActivity();
	}

	/******************************************************************************************/
	/** Managing TabsHost **************************************************************************/
	/******************************************************************************************/
	/**
	 * Initialize the TabHost : add elements and listener
	 */
	public void instanciateTabHost() {
		// TabHost management
		// Instanciation of the Tab Host the one that contains the tabPanels
		tabs.setup();
		TabHost.TabSpec spec;
		// Do the same with the personal information
		spec = tabs.newTabSpec("personal");
		spec.setContent(R.id.personal_info_root_layout);
		// Definition of the label to display for the tabPanel
		spec.setIndicator("Personal");
		// Adding the first tabPanel to the TabHost
		tabs.addTab(spec);

		// Instanciation of the first tab component
		spec = tabs.newTabSpec("biography");
		spec.setContent(R.id.scrollView1);
		// Definition of the label to display for the tabPanel
		spec.setIndicator("Bio");
		// Adding the first tabPanel to the TabHost
		tabs.addTab(spec);

		// Do the same with the bragging information
		spec = tabs.newTabSpec("bragging");
		spec.setContent(R.id.bragging_root_layout);
		// Definition of the label to display for the tabPanel
		spec.setIndicator("Bragging");
		// Adding the first tabPanel to the TabHost
		tabs.addTab(spec);

		// Do the same with the people
		spec = tabs.newTabSpec("people");
		spec.setContent(R.id.people_root_layout);
		// Definition of the label to display for the tabPanel
		spec.setIndicator("People");
		// Adding the first tabPanel to the TabHost
		tabs.addTab(spec);

		// Do the same with the moment
		spec = tabs.newTabSpec("ipost");
		spec.setContent(R.id.ipost_write_root_layout);
		// Definition of the label to display for the tabPanel
		spec.setIndicator("I.Posts");
		// Adding the first tabPanel to the TabHost
		tabs.addTab(spec);

		// Do the same with the moment
		spec = tabs.newTabSpec("moment");
		spec.setContent(R.id.moment_root_layout);
		// Definition of the label to display for the tabPanel
		spec.setIndicator("Moment");
		// Adding the first tabPanel to the TabHost
		tabs.addTab(spec);
		// Add tabChangeListener
		tabs.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals("personal")) {
					fadeInMajorInformation();

				} else {
					fadeMajorInformation();
				}
				moveScroll(tabId);
			}
		});
	}
	
	/**
	 * Set the currentTab of the TabHost
	 */
	public void setCurrentTab(int index) {
		if(tabs!=null) {
			tabs.setCurrentTab(index);
		}else {
			lastTabs = index;
		}
	}
	/**
	 * Change the visibility of the Owner picture and cover
	 * Hide them
	 */
	public void fadeMajorInformation() {
		ownerPictureLayout.setVisibility(View.GONE);
	}

	/**
	 * Change the visibility of the Owner picture and cover
	 * Show them
	 */
	public void fadeInMajorInformation() {
		ownerPictureLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * Change the HorizontalScroll of the TabWidget when a Tab is selected
	 * 
	 * @param tabId
	 */
	public void moveScroll(String tabId) {
		int currentTabs = tabs.getCurrentTab();
		hscvTabWidget.smoothScrollBy((currentTabs - lastTabs) * 70, 10);
		lastTabs = currentTabs;
		model.callBack.onCurrentTabChanged(lastTabs);
	}

	/******************************************************************************************/
	/** Managing ListView **************************************************************************/
	/******************************************************************************************/
	/**
	 * Initialize the List Views
	 */
	void initializeListViews() {
		// Instanciate the listView
		Log.e(TAG, "initializeListView begin : " + model.getPeopleStoredInApp().size());
		personArrayAdapter = new PersonArrayAdapter(getActivity(), model, model.getPeopleStoredInApp());
		lsvPeopleList.setAdapter(personArrayAdapter);
		lsvPeopleList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onPersonSelected(position);
			}
		});
		momentArrayAdapter = new MomentArrayAdapter(getActivity(), model, model.getMomentsStoredInApp());
		lsvMomentsList.setAdapter(momentArrayAdapter);
		Log.e(TAG, "initializeListView over");
	}

	/**
	 * Called when a Person is selected in the people list
	 * 
	 * @param position
	 */
	private void onPersonSelected(int position) {
		model.selectedPerson(personArrayAdapter.getItem(position));
	}

	/******************************************************************************************/
	/** Manage the send Moment/iPost **************************************************************************/
	/******************************************************************************************/

	/**
	 * Whend the Send Moment button is clicked, this method is called
	 */
	private void sendMoment() {
		model.writeMomentInStream();
		edtMomentMessage.setText("");
	}

	/**
	 * Whend the Send Interactive Post button is clicked, this method is called
	 */
	private void sendIPost() {
		model.writeInteractivePostInStream();
		edtIPostMessage.setText("");
	}

	/******************************************************************************************/
	/** Clear the View <=> Unload the person displayed **************************************************************************/
	/******************************************************************************************/
	/**
	 * Clear all the fields of the View
	 */
	void clearPerson() {
		// Clear all the fields:
		initializeEmptyField();
		// clearList
		if (personArrayAdapter != null) {
			personArrayAdapter.notifyDataSetChanged();
		}
		if (momentArrayAdapter != null) {
			momentArrayAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * initializeEmptyFields
	 */
	private void initializeEmptyField() {
		imvPhotoCover.setImageResource(R.drawable.ic_no_cover);
		imvOwnerPicture.setImageResource(R.drawable.ic_no_owner_picture);
		txvBragging.setText("No user logged");
		wevBio.loadData("No more user connected", "text/html;charset=UTF-8", null);
		txvHonoricPrefix.setText("HonorPrefix");
		txvGivenName.setText("Given");
		txvMiddleName.setText("Middle");
		txvName.setText("Name");
		txvHonoricSuffix.setText("HonorSuf");
		txvNickName.setText("Nickname");
		txvTagline.setText("Tagline");
		txvAccount.setText("Account");
		txvGplusId.setText("G+ ID");
		txvBirthday.setText("Birthday");
		txvMinAge.setText("Min Age");
		txvMaxAge.setText("Max Age");
		txvPlacesLived.setText("Places Lived");
		txvOrganizations.setText("Organizations");
		txvLinks.setText("Links");
		txvEmails.setText("Emails");

		txvHonoricPrefix.setBackgroundColor(translucentRed);
		txvGivenName.setBackgroundColor(translucentRed);
		txvMiddleName.setBackgroundColor(translucentRed);
		txvName.setBackgroundColor(translucentRed);
		txvHonoricSuffix.setBackgroundColor(translucentRed);
		txvNickName.setBackgroundColor(translucentRed);
		txvTagline.setBackgroundColor(translucentRed);
		txvAccount.setBackgroundColor(translucentRed);
		txvGplusId.setBackgroundColor(translucentRed);
		txvBirthday.setBackgroundColor(translucentRed);
		txvMinAge.setBackgroundColor(translucentRed);
		txvMaxAge.setBackgroundColor(translucentRed);
		txvPlacesLived.setBackgroundColor(translucentRed);
		txvOrganizations.setBackgroundColor(translucentRed);
		txvLinks.setBackgroundColor(translucentRed);
		txvEmails.setBackgroundColor(translucentRed);
		txvPersonBadge.setText("No user logged");
		// then display the ListView
		lsvMomentsList.setVisibility(View.GONE);
		txvNoMomentMess.setVisibility(View.VISIBLE);
		// And disable the send and Post button
		btnPost.setEnabled(false);
		txvPostAFriendProfile.setEnabled(false);
		btnSend.setEnabled(false);
	}

	/**
	 * enable the send and Post button
	 */
	void enableSendPost() {
		// And enable the send and Post button
		btnPost.setEnabled(true);
		txvPostAFriendProfile.setEnabled(true);
		btnSend.setEnabled(true);
	}
}
