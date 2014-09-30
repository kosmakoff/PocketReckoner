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

package org.kosmakoff.pocketreckoner.ui;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.kosmakoff.pocketreckoner.CheckItemsAdapter;
import org.kosmakoff.pocketreckoner.R;
import org.kosmakoff.pocketreckoner.model.CheckItem;
import org.kosmakoff.pocketreckoner.model.Check;
import org.kosmakoff.pocketreckoner.db.ChecksRepository;
import org.kosmakoff.pocketreckoner.ui.dialog.CheckItemEditorDialogFragment;

import java.util.ArrayList;

public class AddEditCheckActivity extends ListActivity {

    static final String LOG_TAG = "ADD_EDIT_CHECK";
    static final String TAG_CHECK_ITEM_EDITOR_DIALOG = "CHECK_ITEM_EDITOR_DIALOG";

    private ChecksRepository checksRepository;

    private long checkId;

    Button btnAddCheckItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_edit_check);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        checksRepository = new ChecksRepository(this);

        // mapping controls
        btnAddCheckItem = (Button) findViewById(R.id.btn_add_check_item);
        btnAddCheckItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewCheckItemClicked();
            }
        });

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
    protected void onDestroy() {
        super.onDestroy();
        checksRepository.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(Menu.NONE, R.id.menu_item_add_check_item, 0, R.string.add_item);
        menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.drawable.ic_action_new_dark);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_item_add_check_item:
                onNewCheckItemClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onNewCheckItemClicked() {
        CheckItemEditorDialogFragment.newInstance().show(getFragmentManager(), TAG_CHECK_ITEM_EDITOR_DIALOG);
    }
}
