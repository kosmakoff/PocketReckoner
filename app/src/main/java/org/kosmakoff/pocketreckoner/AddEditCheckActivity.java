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

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.kosmakoff.pocketreckoner.data.CheckItem;
import org.kosmakoff.pocketreckoner.data.Check;
import org.kosmakoff.pocketreckoner.data.ChecksRepository;

import java.util.ArrayList;

public class AddEditCheckActivity extends ListActivity {

    static final String LOG_TAG = "ADD_EDIT_CHECK";

    private ChecksRepository checksRepository;

    private long checkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checksRepository = new ChecksRepository(this);

        setContentView(R.layout.activity_add_edit_check);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // setting up purchased things

        Intent intent = getIntent();
        checkId = intent.getLongExtra("checkId", 0);

        if (checkId != 0) {
            Check check = checksRepository.getCheck(checkId);
            if (check == null)
                throw new NullPointerException("No check found with id = " + checkId);

            // loading occurs here
            getActionBar().setTitle(R.string.update_check);
        } else {
            getActionBar().setTitle(R.string.new_check);
        }

        // setting adapter now
        ArrayList<CheckItem> checkItems = checksRepository.getCheckItems(checkId);
        setListAdapter(new CheckItemsAdapter(this, checkItems));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
