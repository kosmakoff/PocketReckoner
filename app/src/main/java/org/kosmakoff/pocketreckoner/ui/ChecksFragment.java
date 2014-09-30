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

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.kosmakoff.pocketreckoner.R;
import org.kosmakoff.pocketreckoner.db.ChecksRepository;
import org.kosmakoff.pocketreckoner.util.CheckEditor;

public class ChecksFragment extends ListFragment {

    final static String LOG_TAG = "CHECKS_FRAGMENT";

    private ChecksRepository checksRepository;
    private CheckEditor checkEditor;

    public ChecksFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            checkEditor = (CheckEditor) activity;

        } catch (ClassCastException castException) {
            // the activity cannot edit people. but we know it can ;)
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Activity created");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checksRepository = new ChecksRepository(getActivity());

        View checksFragment = inflater.inflate(R.layout.fragment_checks, container, false);
        Button btnAddNewPerson = (Button) checksFragment.findViewById(R.id.btn_add_check);
        btnAddNewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEditor.startEditingCheck(0);
            }
        });

        return checksFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        checksRepository.close();
    }
}
