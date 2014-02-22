package com.luzi82.hikari.client.android.demo;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.luzi82.hikari.client.HsClient;
import com.luzi82.hikari.client.HsMemStorage;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.HsQuestEntryData;
import com.luzi82.hikari.client.protocol.QuestProtocolDef.QuestInstance;
import com.luzi82.homuvalue.Value.Variable;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public HsClient hsClient;

	ExecutorService executorService;

	// public List<HsQuestEntryData> questEntryAry;

	public final Variable<List<HsQuestEntryData>> questEntryListVar = new Variable<List<HsQuestEntryData>>();

	public final Variable<QuestInstance> questInstanceVar = new Variable<QuestInstance>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		executorService = Executors.newCachedThreadPool();

		try {
			hsClient = new HsClient("http://192.168.1.50", new HsMemStorage(
					executorService), executorService, new HttpClient(
					executorService));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new Error(e);
		}

		// Create the adapter that will return a fragment for each of the
		// three primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			System.err.println("KXrISxgP getItem position " + position);
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Main";
			case 1:
				return "Quest list";
			case 2:
				return "Quest";
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Integer section_number =
			// savedInstanceState.getInt(ARG_SECTION_NUMBER);
			int section_number = getArguments().getInt(ARG_SECTION_NUMBER);
			// System.err.println("znl2P9it section_number " + section_number);
			switch (section_number) {
			case 0:
				return new LoginView(container.getContext());
			case 1:
				return new QuestListView(container.getContext());
			case 2:
				return new QuestView(container.getContext());
			default:
				return null;
			}
		}
	}

	ProgressDialog startLoadDialog;

	// private void startLoad() {
	// // mSectionsPagerAdapter.startUpdate(mViewPager);
	// startLoadDialog = ProgressDialog.show(MainActivity.this, "Sync",
	// "Wait...", false, false, null);
	// new StartLoadFuture(new FutureCallback<Void>() {
	// @Override
	// public void failed(Exception ex) {
	// System.err.println("gvpMHtXv failed");
	// startLoadDialog.dismiss();
	// startLoadDialog = null;
	// }
	//
	// @Override
	// public void completed(Void result) {
	// System.err.println("gvpMHtXv completed");
	// startLoadDialog.dismiss();
	// startLoadDialog = null;
	// // runOnUiThread(new Runnable() {
	// // @Override
	// // public void run() {
	// // mSectionsPagerAdapter = new SectionsPagerAdapter(
	// // getSupportFragmentManager());
	// // mViewPager.setAdapter(mSectionsPagerAdapter);
	// // }
	// // });
	// }
	//
	// @Override
	// public void cancelled() {
	// System.err.println("rMcN7Rkw cancelled");
	// }
	// }).start();
	// }

}
