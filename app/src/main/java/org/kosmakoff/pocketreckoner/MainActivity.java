/**
 The MIT License (MIT)

 Copyright (c) 2014 Oleg Kosmakov

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

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
import android.widget.Toast;

import org.kosmakoff.pocketreckoner.data.PeopleRepository;
import org.kosmakoff.pocketreckoner.infrastructure.PersonEditor;

public class MainActivity extends Activity implements PersonEditor {

    static final int REQUEST_ADD_NEW_PERSON = 1;
    static final int REQUEST_EDIT_PERSON = 2;
    static final int REQUEST_ADD_NEW_CHECK = 3;
    static final int REQUEST_EDIT_CHECK = 4;

    static final String LOG_TAG = "RECKONER";

    private final String KEY_SELECTED_TAB = "SELECTED TAB";

    private PeopleRepository peopleRepository;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerMenuItemType currentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        peopleRepository = new PeopleRepository(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        DrawerMenuItem items[] = new DrawerMenuItem[]{
                new DrawerMenuItem(DrawerMenuItemType.PEOPLE,
                        getString(R.string.people),
                        R.drawable.ic_action_emo_laugh),
                new DrawerMenuItem(DrawerMenuItemType.CHECKS,
                        getString(R.string.checks),
                        R.drawable.ic_action_calculator)};

        mDrawerList.setAdapter(new DrawerMenuItemsAdapter(this, items));

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                selectMenuItem(i);
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
            selectMenuItem(0);
        } else {
            selectMenuItem(savedInstanceState.getInt(KEY_SELECTED_TAB));
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
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.menu_item_add_person:
                startAddPersonActivity();
                return true;

            case R.id.menu_item_add_check:
                startAddCheckActivity();
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
                MenuItem addPersonMenuItem = menu.add(Menu.NONE, R.id.menu_item_add_person,
                        10, R.string.add_person);
                addPersonMenuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
                addPersonMenuItem.setIcon(R.drawable.ic_social_add_person);
                break;
            case CHECKS:
                MenuItem addCheckMenuItem = menu.add(Menu.NONE, R.id.menu_item_add_check,
                        10, R.string.add_check);
                addCheckMenuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
                addCheckMenuItem.setIcon(R.drawable.ic_action_new);
                break;
            default:
                break;
        }

        return true;
    }

    private void selectMenuItem(int position) {
        DrawerMenuItem selectedItem = (DrawerMenuItem) mDrawerList.getAdapter()
                .getItem(position);
        setTitle(selectedItem.getTitle());
        mDrawerList.setItemChecked(position, true);

        Fragment fragmentToShow;

        currentMenu = selectedItem.getMenuItemType();

        switch (currentMenu) {
            case PEOPLE:
                fragmentToShow = new PeopleFragment();

                Log.d(LOG_TAG, "Showing people fragment");
                break;
            case CHECKS:
                fragmentToShow = new ChecksFragment();
                Log.d(LOG_TAG, "Showing checks fragment");
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

    private void startAddPersonActivity() {
        Intent addPersonIntent = new Intent(this, AddEditPersonActivity.class);
        startActivityForResult(addPersonIntent, REQUEST_ADD_NEW_PERSON);
    }

    private void startEditPersonActivity(long personId) {
        Intent editPersonIntent = new Intent(this, AddEditPersonActivity.class);
        editPersonIntent.putExtra("personId", personId);
        startActivityForResult(editPersonIntent, REQUEST_EDIT_PERSON);
    }

    private void startAddCheckActivity() {
        Intent addCheckIntent = new Intent(this, AddEditCheckActivity.class);
        startActivityForResult(addCheckIntent, REQUEST_ADD_NEW_CHECK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_ADD_NEW_PERSON:
                selectMenuItem(0);
                Toast.makeText(this, R.string.person_added, Toast.LENGTH_SHORT).show();
                break;
            case REQUEST_EDIT_PERSON:
                selectMenuItem(0);

                if (data!= null && data.getStringExtra("result").equals("deleted")) {
                    Toast.makeText(this, R.string.person_deleted, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.person_updated, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void startEditingPerson(long personId) {
        startEditPersonActivity(personId);
    }

    @Override
    public void deletePerson(long personId) {
        peopleRepository.deletePerson(personId);
        Toast.makeText(this, R.string.person_deleted, Toast.LENGTH_SHORT).show();
    }
}