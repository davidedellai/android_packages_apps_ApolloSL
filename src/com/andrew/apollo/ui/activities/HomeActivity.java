package com.andrew.apollo.ui.activities;


import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andrew.apollo.R;
import com.andrew.apollo.ui.fragments.AlbumFragment;
import com.andrew.apollo.ui.fragments.ArtistFragment;
import com.andrew.apollo.ui.fragments.GenreFragment;
import com.andrew.apollo.ui.fragments.PlaylistFragment;
import com.andrew.apollo.ui.fragments.RecentFragment;
import com.andrew.apollo.ui.fragments.SongFragment;


public class HomeActivity extends BaseActivity {
	private ActionBarDrawerToggle mDrawerToggle;
    private CustomDrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;
    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;

    private static int DRAWER_MODE = 0;
    private SharedPreferences mPreferences;

    private static final int MENU_BACK = Menu.FIRST;
    String titleString[];
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	 mDrawerListView = (ListView) findViewById(R.id.navigation_drawer);
    	 mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 selectItem(position);
             }
         });

         mDrawerListView.setAdapter(new ArrayAdapter<String>(
                 getActionBar().getThemedContext(),
                 R.layout.drawer_list,
                 android.R.id.text1,
                 getTitles()));
         mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

         setUpNavigationDrawer(
                 findViewById(R.id.navigation_drawer),
                 (CustomDrawerLayout) findViewById(R.id.dw_drawer_layout));

         ActionBar ab = getActionBar();
         ab.setDisplayHomeAsUpEnabled(true);
         ab.setHomeButtonEnabled(true);

    }
    
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     //   menu.add(0, MENU_BACK, 0, R.string.toggle_back_cfibers)
       //         .setIcon(R.drawable.ic_back)
         //       .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case MENU_BACK:
                onBackPressed();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
  //==================================
    // Methods
    //==================================

    /**
     * Users of this fragment must call this method to set up the
     * navigation menu_drawer interactions.
     *
     * @param fragmentContainerView The view of this fragment in its activity's layout.
     * @param drawerLayout          The DrawerLayout containing this fragment's UI.
     */
    public void setUpNavigationDrawer(View fragmentContainerView, CustomDrawerLayout drawerLayout) {
        mFragmentContainerView = fragmentContainerView;
        mDrawerLayout = drawerLayout;

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

                invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        if (!mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItem(mCurrentSelectedPosition);
    }
    
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Restores the action bar after closing the menu_drawer
     */
    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setTitle(getTitle());
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.dw_container, PlaceholderFragment.newInstance(getPosition(position)))
                .commit();
    }
    
    /**
     * Depending on if the item is shown or not, it increases
     * the position to make the activity load the right fragment.
     *
     * @param pos The selected position
     * @return the modified position
     */
    public int getPosition(int pos) {
        int position = pos;
        switch (DRAWER_MODE) {
            default:
            case 0:
                position = pos;
                break;
            case 1:
                if (pos > 0) position = pos + 1;
                break;
            case 2:
                if (pos > 4) position = pos + 1;
                break;
            case 3:
                if (pos > 0) position = pos + 1;
                if (pos > 4) position = pos + 3;
                break;
        }
        return position;
    }

    /**
     * Get a list of titles for the tabstrip to display depending on if the
     * voltage control fragment and battery fragment will be displayed. (Depends on the result of
     * Helpers.voltageTableExists() & Helpers.showBattery()
     *
     * @return String[] containing titles
     */
    private String[] getTitles() {
        String titleString[];
        DRAWER_MODE = 0;
        titleString = new String[]{
                "Songs","Artist","Genre","Playlist","Albums","Recent"};
        return titleString;
    }

    //==================================
    // Internal Classes
    //==================================

    /**
     * Loads our Fragments.
     */

    public static final int FRAGMENT_ID_ALBUM = 4;
    public static final int FRAGMENT_ID_ARTIST = 1;
    public static final int FRAGMENT_ID_GENRE = 2;
    public static final int FRAGMENT_ID_PLAYLIST = 3;
    public static final int FRAGMENT_ID_SONGS = 0;
    public static final int FRAGMENT_ID_RECENT = 5;
    
    public static class PlaceholderFragment extends Fragment {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int fragmentId) {
            Fragment fragment;
           switch (fragmentId) {
                default:
                case FRAGMENT_ID_ALBUM:
                    fragment = new AlbumFragment();
                    break;
                case FRAGMENT_ID_ARTIST:
                    fragment = new ArtistFragment();
                    break;
                case FRAGMENT_ID_GENRE:
                    fragment = new GenreFragment();
                    break;
                case FRAGMENT_ID_PLAYLIST:
                    fragment = new PlaylistFragment();
                    break;
                case FRAGMENT_ID_SONGS:
                    fragment = new SongFragment();
                    break;
                case FRAGMENT_ID_RECENT:
                    fragment = new RecentFragment();
                    break;
            }

            return fragment;
        }

        public PlaceholderFragment() {
            // intentionally left blank
        }
}

	@Override
	public int setContentView() {
		return R.layout.activity_base;
	}
}
