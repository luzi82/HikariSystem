package com.luzi82.hikari.client.android.demo;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.luzi82.hikari.client.protocol.HikariQuestProtocolDef.QuestInstance;
import com.luzi82.lang.GuriObservable;

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

	// public final Variable<List<HsQuestEntryData>> questEntryListVar = new
	// Variable<List<HsQuestEntryData>>();
	// public final Variable<Long> dataSyncTimeVar = new Variable<Long>();

	public final GuriObservable<Long> dataSyncTimeObservable = new GuriObservable<Long>();

	public final GuriObservable<Boolean> foregroundObservable = new GuriObservable<Boolean>();

	public final GuriObservable<QuestInstance> questInstanceObservableVar = new GuriObservable<QuestInstance>();

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

		// dataSyncTimeVar.setAlwaysCallback(true);
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
			return PAGE_DEF_ARY.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return PAGE_DEF_ARY[position].title;
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
			// switch (section_number) {
			// case 0:
			// return new LoginView(container.getContext());
			// case 1:
			// return new QuestListView(container.getContext());
			// case 2:
			// return new QuestView(container.getContext());
			// default:
			// return null;
			// }
			return PAGE_DEF_ARY[section_number].onCreateView(container
					.getContext());
		}
	}

	ProgressDialog startLoadDialog;

	@Override
	protected void onPause() {
		foregroundObservable.setNotify(false);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		foregroundObservable.setNotify(true);
	}

	public static abstract class PageDef {
		public final String title;

		public abstract View onCreateView(Context context);

		public PageDef(String title) {
			this.title = title;
		}
	}

	public static final PageDef[] PAGE_DEF_ARY = { //
	new PageDef("User") {
		@Override
		public View onCreateView(Context context) {
			return new LoginView(context);
		}
	}, //
			new PageDef("Value") {
				@Override
				public View onCreateView(Context context) {
					return new ValueView(context);
				}
			}, //
			new PageDef("Card") {
				@Override
				public View onCreateView(Context context) {
					return new CardView(context);
				}
			}, //
			new PageDef("Quest list") {
				@Override
				public View onCreateView(Context context) {
					return new QuestListView(context);
				}
			},//
			new PageDef("Quest") {
				@Override
				public View onCreateView(Context context) {
					return new QuestView(context);
				}
			}, //
			new PageDef("Convert") {
				@Override
				public View onCreateView(Context context) {
					return new ConvertListView(context);
				}
			},//
			new PageDef("Gacha") {
				@Override
				public View onCreateView(Context context) {
					return new GachaListView(context);
				}
			},//
	};

}
