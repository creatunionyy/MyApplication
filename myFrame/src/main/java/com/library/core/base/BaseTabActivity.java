/*package com.library.core.base;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.library.core.bean.IndicatorInfo;
import com.library.core.view.DummyTabContent;
import com.zzc.frame.R;

public abstract class BaseTabActivity extends BaseActivity {

	private List<IndicatorInfo> listIndicatorInfo;

	private static TabHost tabHost;
	private TabWidget tabWidget;
	private int CURRENT_TAB = 0;
	private android.support.v4.app.FragmentTransaction ft;
	// private NewFragment fragment1;
	// private ProductListFragment fragment2;
	// private AssetsFragment fragment3;
	// private MineFragment fragment4;
	// private LinearLayout tabIndicator1, tabIndicator2, tabIndicator3,
	// tabIndicator4;

	public static final String TAB1 = "one";
	public static final String TAB2 = "two";
	public static final String TAB3 = "three";
	public static final String TAB4 = "four";
	private ImageView ivTab1, ivTab2, ivTab3, ivTab4;
	private TextView tvTab1, tvTab2, tvTab3, tvTab4;

	private int currentTab;

	@Override
	protected void onInitAttribute(BaseAttribute ba) {
		ba.mActivityLayoutId = R.layout.activity_tab;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		LinearLayout layout = (LinearLayout) tabHost.getChildAt(0);
		TabWidget tw = (TabWidget) layout.getChildAt(2);
		
		

		listIndicatorInfo = setTabParams();
		for (int i = 0; i < listIndicatorInfo.size(); i++) {
			IndicatorInfo info = listIndicatorInfo.get(i);

			LinearLayout tabIndicator = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_indicator, tw, false);
			tvTab2 = (TextView) tabIndicator.getChildAt(1);
			ivTab2 = (ImageView) tabIndicator.getChildAt(0);
			tvTab2.setText(info.getTabName());
			ivTab2.setImageResource(info.getDrawable());
			
			
			TabHost.TabSpec tSpecHome = tabHost.newTabSpec(i+"");
			tSpecHome.setIndicator(tabIndicator);
			tSpecHome.setContent(new DummyTabContent(getBaseContext()));
			tabHost.addTab(tSpecHome);

		}
		OnDateChangedListener
		
		TabHost.OnTabChangeListener tabChangeListener = new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				FragmentManager fm = getSupportFragmentManager();
				
				List<Fragment> listFragment = setFragment();
				for (int i = 0; i < listFragment.size(); i++) {
					Fragment fragment = listFragment.get(i);
					fm.findFragmentByTag(i+"");
				}
				
				fragment1 = (NewFragment) ;
				fragment2 = (ProductListFragment) fm.findFragmentByTag(TAB2);
				fragment3 = (AssetsFragment) fm.findFragmentByTag(TAB3);
				fragment4 = (MineFragment) fm.findFragmentByTag(TAB4);
				ft = fm.beginTransaction();

				if (fragment1 != null)
					ft.detach(fragment1);

				if (fragment2 != null)
					ft.detach(fragment2);

				if (fragment3 != null)
					ft.detach(fragment3);

				if (fragment4 != null)
					ft.detach(fragment4);

				if (tabId.equalsIgnoreCase(TAB1)) {
					isTabHome();
					CURRENT_TAB = 1;
					setCurrentTab();

				} else if (tabId.equalsIgnoreCase(TAB2)) {
					isTabWall();
					CURRENT_TAB = 2;
					setCurrentTab();

				} else if (tabId.equalsIgnoreCase(TAB3)) {
					isTabMessage();
					CURRENT_TAB = 3;
					setCurrentTab();

				} else if (tabId.equalsIgnoreCase(TAB4)) {
					isTabMe();
					CURRENT_TAB = 4;
					setCurrentTab();
				} else {
					switch (CURRENT_TAB) {
					case 1:
						isTabHome();
						break;
					case 2:
						isTabWall();
						break;
					case 3:
						isTabMessage();
						break;
					case 4:
						isTabMe();
						break;
					default:
						isTabHome();
						break;
					}

				}
				currentTab = tabHost.getCurrentTab();
				ft.commit();
			}

		};

	}

	public abstract List<IndicatorInfo> setTabParams();
	public abstract List<Fragment> setFragment();

}
*/