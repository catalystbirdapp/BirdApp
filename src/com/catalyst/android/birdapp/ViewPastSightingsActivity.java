package com.catalyst.android.birdapp;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;


	public class ViewPastSightingsActivity extends Activity{

		ViewPager mViewPager;
	    TabsAdapter mTabsAdapter;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        mViewPager = new ViewPager(this);
	        mViewPager.setId(R.id.pager);
	        setContentView(mViewPager);
	        
	        final ActionBar bar = getActionBar();
	        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
	        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

	        mTabsAdapter = new TabsAdapter(this, mViewPager);
	        mTabsAdapter.addTab(bar.newTab().setText("List"),
	                RecordsFragment.class, null);
	        mTabsAdapter.addTab(bar.newTab().setText("Map"),
	                MapFragment.class, null);
	        
	        setTheme(android.R.style.Theme_Light);

	        if (savedInstanceState != null) {
	            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
	        }
	    }

	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	    }
	    
	    public static class TabsAdapter extends FragmentStatePagerAdapter
	    	implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
	    	private final Context mContext;
	    	private final ActionBar mActionBar;
	    	private final ViewPager mViewPager;
	    	private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

	    	static final class TabInfo {
	    		private final Class<?> clss;
	    		private final Bundle args;

	    		TabInfo(Class<?> _class, Bundle _args) {
	    			clss = _class;
	    			args = _args;
	    		}
	    	}

	    	public TabsAdapter(Activity activity, ViewPager pager) {
	    		super(activity.getFragmentManager());
	    		mContext = activity;
	    		mActionBar = activity.getActionBar();
	    		mViewPager = pager;
	    		mViewPager.setAdapter(this);
	    		mViewPager.setOnPageChangeListener(this);
	    	}

	    	public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
	    		TabInfo info = new TabInfo(clss, args);
	    		tab.setTag(info);
	    		tab.setTabListener(this);
	    		mTabs.add(info);
	    		mActionBar.addTab(tab);
	    		notifyDataSetChanged();
	    	}

	    	@Override
	    	public int getCount() {
	    		return mTabs.size();
	    	}

	    	@Override
	    	public Fragment getItem(int position) {
	    		TabInfo info = mTabs.get(position);
	    		return Fragment.instantiate(mContext, info.clss.getName(), info.args);
	    	}

	    	@Override
	    	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	    	@Override
	    	public void onPageSelected(int position) {
	    		mActionBar.setSelectedNavigationItem(position);
	    	}

	    	@Override
	    	public void onPageScrollStateChanged(int state) {}

	    	@Override
	    	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    		Object tag = tab.getTag();
	    		for (int i=0; i<mTabs.size(); i++) {
	    			if (mTabs.get(i) == tag) {
	    				mViewPager.setCurrentItem(i);
	    			}
	    		}
	    	}
	    	
	    	@Override
	    	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

	    	@Override
	    	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	    }
	}

