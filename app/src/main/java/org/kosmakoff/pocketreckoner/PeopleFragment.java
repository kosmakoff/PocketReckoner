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

import java.util.ArrayList;

import org.kosmakoff.pocketreckoner.data.PeopleRepository;
import org.kosmakoff.pocketreckoner.data.Person;
import org.kosmakoff.pocketreckoner.infrastructure.PersonEditor;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PeopleFragment extends ListFragment
        implements OnItemClickListener {

    final static int MENU_PERSON_DELETE = 1;

    final static String LOG_TAG = "PEOPLE_FRAGMENT";

    private PeopleRepository peopleRepository;
    private PersonEditor personEditor;

    public PeopleFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            personEditor = (PersonEditor) activity;
        } catch (ClassCastException castException) {
            // the activity cannot edit people. but we know it can ;)
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Person> people = peopleRepository.getPeople();
        PeopleAdapter adapter = new PeopleAdapter(getActivity(), people);
        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
        registerForContextMenu(getListView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        peopleRepository = new PeopleRepository(getActivity());

        return inflater.inflate(R.layout.fragment_people, null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (personEditor == null)
            return;

        personEditor.startEditingPerson(id);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.clearHeader();
        menu.clear();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Log.d(LOG_TAG, "Create Context Menu for ID = " + info.id + ", position = " + info.position);

        // add DELETE menu item
        long personId = info.id;
        MenuItem menuItem = menu.add(0, MENU_PERSON_DELETE, 0, R.string.delete);
        Intent menuItemIntent = new Intent();
        menuItemIntent.putExtra("personId", personId);
        menuItem.setIntent(menuItemIntent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_PERSON_DELETE:
                long personId = item.getIntent().getLongExtra("personId", 0);

                if (personId != 0)
                    personEditor.deletePerson(personId);

                refreshPeopleList();

                return true;
        }

        return false;
    }

    private void refreshPeopleList() {
        ArrayList<Person> people = peopleRepository.getPeople();
        ((PeopleAdapter)getListAdapter()).updatePeople(people);
    }
}
