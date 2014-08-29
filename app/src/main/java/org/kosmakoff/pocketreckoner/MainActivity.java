package org.kosmakoff.pocketreckoner;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

    private final String KEY_SELECTED_TAB = "SELECTED TAB";

    static final int REQUEST_ADD_NEW_PERSON = 1;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerMenuItemType currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        DrawerMenuItem items[] = new DrawerMenuItem[]{
                new DrawerMenuItem(DrawerMenuItemType.PEOPLE,
                        getString(R.string.people),
                        R.drawable.ic_action_emo_laugh),
                new DrawerMenuItem(DrawerMenuItemType.SESSIONS,
                        getString(R.string.sessions),
                        R.drawable.ic_action_calculator)};

        mDrawerList.setAdapter(new DrawerMenuItemsAdapter(this, items));

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                selectItem(i);
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_SELECTED_TAB)) {
            selectItem(0);
        } else {
            selectItem(savedInstanceState.getInt(KEY_SELECTED_TAB));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SELECTED_TAB, mDrawerList.getCheckedItemPosition());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.menu_item_add_person:
                Intent addPersonIntent = new Intent(this, AddEditPersonActivity.class);
                startActivityForResult(addPersonIntent, REQUEST_ADD_NEW_PERSON);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        switch (currentMenu) {
            case PEOPLE:
                MenuItem addPerson = menu.add(Menu.NONE, R.id.menu_item_add_person,
                        10, R.string.add_person);
                addPerson.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
                addPerson.setIcon(R.drawable.ic_social_add_person);
                break;
            case SESSIONS:
                break;
            default:
                break;
        }

        return true;
    }

    private void selectItem(int position) {
        DrawerMenuItem selectedItem = (DrawerMenuItem) mDrawerList.getAdapter()
                .getItem(position);
        setTitle(selectedItem.getTitle());
        mDrawerList.setItemChecked(position, true);

        Fragment fragmentToShow = null;

        currentMenu = selectedItem.getMenuItemType();

        switch (currentMenu) {
            case PEOPLE:
                fragmentToShow = new PeopleFragment();

                Log.d(getString(R.string.app_name), "Showing people fragment");
                break;
            case SESSIONS:
                fragmentToShow = new SessionsFragment();
                Log.d(getString(R.string.app_name), "Showing sessions fragment");
                break;
            default:
                throw new IllegalArgumentException("Unsupported menu item type: "
                        + String.valueOf(selectedItem.getMenuItemType()));
        }

        FragmentManager fragmentManager = getFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragmentToShow).commit();

        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ADD_NEW_PERSON:
                selectItem(0);
                break;
        }
    }
}