package crixec.app.imagefactory.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import crixec.app.imagefactory.R;
import crixec.app.imagefactory.core.ImageFactory;
import crixec.app.imagefactory.ui.Toast;
import crixec.app.imagefactory.utils.NativeUtils;

public class MainActivity extends AppCompatActivity implements ListView.OnItemClickListener
{
	private Toolbar mToolbar;
	private DrawerLayout drawer;
	private ListView mDrawerList;
	private ListView mCardList;
	private ActionBarDrawerToggle drawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	public void init()
	{
		mToolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
		mToolbar.setTitle(R.string.app_name);
		setSupportActionBar(mToolbar);
		drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
		drawerToggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
		drawerToggle.setDrawerIndicatorEnabled(true);
		drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
				@Override
				public void onDrawerSlide(View drawerView, float slideOffset)
				{
					drawerToggle.onDrawerSlide(drawerView, slideOffset);
				}

				@Override
				public void onDrawerOpened(View drawerView)
				{
					drawerToggle.onDrawerOpened(drawerView);
					setTitle(R.string.drawer_more);
				}

				@Override
				public void onDrawerClosed(View drawerView)
				{
					drawerToggle.onDrawerClosed(drawerView);
					setTitle(R.string.app_name);
				}

				@Override
				public void onDrawerStateChanged(int newState)
				{
					drawerToggle.onDrawerStateChanged(newState);
				}
			});
		drawerToggle.syncState();
		mDrawerList = (ListView) findViewById(R.id.activity_main_drawer_ListView);
		mDrawerList.setAdapter(getDrawerItemAdapter());
		mDrawerList.setOnItemClickListener(this);
		mCardList = (ListView) findViewById(R.id.activity_main_card_listview);
		mCardList.setAdapter(getCardItemAdapter());
		mCardList.setOnItemClickListener(this);
	}

	public SimpleAdapter getCardItemAdapter()
	{
		ArrayList<HashMap<String, ?>> savedList = new ArrayList<HashMap<String, ?>>();
		String[] ITEM_NAME = getResources().getStringArray(R.array.function_item_name);
		for (int i = 0; i < ITEM_NAME.length; i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("NAME", ITEM_NAME[i]);
			map.put("ICON", R.drawable.ic_item_demo);
			savedList.add(map);
		}
		return new SimpleAdapter(getApplicationContext(), savedList, R.layout.activity_main_card_list_item,
								 new String[] { "NAME", "ICON" },
								 new int[] { R.id.activity_main_card_list_item_TextView, R.id.activity_main_card_list_item_ImageView });
	}

	public SimpleAdapter getDrawerItemAdapter()
	{
		ArrayList<HashMap<String, ?>> list = new ArrayList<HashMap<String, ?>>();
		String[] items = getResources().getStringArray(R.array.drawer_item_name);
		int[] icons = { R.drawable.ic_feedback, R.drawable.ic_update, R.drawable.ic_setting, R.drawable.ic_about };
		for (int i = 0; i < items.length; i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("NAME", items[i]);
			map.put("ICON", icons[i]);
			list.add(map);
		}
		return new SimpleAdapter(getApplicationContext(), list, R.layout.activity_main_drawer_listview_item,
								 new String[] { "NAME", "ICON" }, new int[] { R.id.activity_main_drawer_ListView_item_textview,
									 R.id.activity_main_drawer_ListView_item_imageview });
	}

	private void toggleDrawer()
	{
		if (!drawer.isDrawerOpen(GravityCompat.START))
		{
			openDrawer();
		}
		else
		{
			closeDrawer();
		}
	}

	private boolean closeDrawer()
	{
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}
		return false;
	}

	private void openDrawer()
	{
		if (!drawer.isDrawerOpen(GravityCompat.START))
		{
			drawer.openDrawer(GravityCompat.START);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO: Implement this method
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_BACK: {
					if (closeDrawer())
					{
						return true;
					}
					break;
				}
			case KeyEvent.KEYCODE_MENU: {
					toggleDrawer();
					return true;
				}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onItemClick(AdapterView<?> view, View p2, int p, long p4)
	{
		// TODO: Implement this method
		switch (view.getId())
		{
			case R.id.activity_main_drawer_ListView: {
					Intent intent = null;
					if (p == 3)
					{
						intent = new Intent(MainActivity.this, AboutActivity.class);
					}
					else if (p == 0)
					{
						intent = new Intent(MainActivity.this, FeedbackActivity.class);
					}
					else if (p == 1)
					{
						intent = new Intent(MainActivity.this, UpdateActivity.class);
					}
					else if (p == 2)
					{
						intent = new Intent(MainActivity.this, SettingActivity.class);
					}
					if (intent != null)
						startActivity(intent);
					break;
				}
			case R.id.activity_main_card_listview: {
					Intent intent = null;
					if (p == 0)
					{
						intent = new Intent(MainActivity.this, UnpackbootimgActivity.class);
					}
					else if (p == 1)
					{
						intent = new Intent(MainActivity.this, RepackbootimgActivity.class);
					}
					else if (p == 2)
					{
						intent = new Intent(MainActivity.this, FlashbootimgActivity.class);
					}
					else if (p == 3)
					{
						intent = new Intent(MainActivity.this, BackupbootimgActivity.class);
					}
					else if (p == 6)
					{
						intent = new Intent(MainActivity.this, RebootActivity.class);
					}
					else if (p == 4)
					{
						intent = new Intent(MainActivity.this, FirmwareActivity.class);
					}
					else if (p == 5)
					{
						intent = new Intent(MainActivity.this, ImageConvertActivity.class);
					}
					if (intent != null)
						startActivity(intent);
					break;
				}
		}
	}
}
