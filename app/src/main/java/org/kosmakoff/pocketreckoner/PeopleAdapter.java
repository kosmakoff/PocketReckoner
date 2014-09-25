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

import org.kosmakoff.pocketreckoner.data.Person;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeopleAdapter extends BaseAdapter {
    public Context getContext() {
        return context;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    private final Context context;

    private ArrayList<Person> people;

    public PeopleAdapter(Context context, ArrayList<Person> people) {
        this.context = context;
        this.people = people;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int position) {
        return people.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ((Person) getItem(position)).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView != null) {
            view = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.person_list_item,
                    parent, false);
        }


        // map controls
        TextView tvName = (TextView) view.findViewById(R.id.text_name);
        TextView tvPhone = (TextView) view.findViewById(R.id.text_phone);
        TextView tvEmail = (TextView) view.findViewById(R.id.text_email);

        // set values
        Person person = (Person) getItem(position);
        tvName.setText(person.getName());
        tvPhone.setText(person.getPhone());
        tvEmail.setText(person.getEmail());

        return view;
    }

    public void updatePeople(ArrayList<Person> people) {
        this.people = people;
        notifyDataSetChanged();
    }
}
