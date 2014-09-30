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

package org.kosmakoff.pocketreckoner.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import org.kosmakoff.pocketreckoner.model.Person;

public class PeopleRepository extends CommonRepository {

    public PeopleRepository(Context context) {
        super(context);
    }

    public PeopleRepository(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Cursor fetchRecord(long rowId) {
        return fetchRecord(DatabaseSchema.PersonEntry.TABLE_NAME, rowId);
    }

    @Override
    public Cursor fetchAllRecords() {
        Log.v(TAG, "Fetching all people from db.");
        return fetchAllRecords(DatabaseSchema.PersonEntry.TABLE_NAME);
    }

    public ArrayList<Person> getAllPeople() {
        ArrayList<Person> people = new ArrayList<Person>();
        Cursor c = fetchAllRecords();

        if (c == null) {
            return people;
        }

        try {
            while (c.moveToNext()) {
                people.add(buildPersonInstance(c));
            }
        } finally {
            c.close();
        }

        return people;
    }

    public long addPerson(Person person) {
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.PersonEntry.COLUMN_NAME, person.getName());
        values.put(DatabaseSchema.PersonEntry.COLUMN_PHONE, person.getPhone());
        values.put(DatabaseSchema.PersonEntry.COLUMN_EMAIL, person.getEmail());
        putPhotoIfNotNull(values, person.getPhoto());

        return insertRecord(DatabaseSchema.PersonEntry.TABLE_NAME, values);
    }

    public void updatePerson(Person person) {
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.PersonEntry.COLUMN_NAME, person.getName());
        values.put(DatabaseSchema.PersonEntry.COLUMN_PHONE, person.getPhone());
        values.put(DatabaseSchema.PersonEntry.COLUMN_EMAIL, person.getEmail());
        putPhotoIfNotNull(values, person.getPhoto());

        updateRecord(DatabaseSchema.PersonEntry.TABLE_NAME, person.getId(), values);
    }

    private void putPhotoIfNotNull(ContentValues cv, Bitmap photo) {
        if (photo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] blob = stream.toByteArray();
            cv.put(DatabaseSchema.PersonEntry.COLUMN_PHOTO, blob);
        }
    }

    public Person getPerson(long id) {

        if (id <= 0) return null;

        Person person = null;
        Cursor c = fetchRecord(id);

        if (c != null) {
            if (c.moveToFirst()) {
                person = buildPersonInstance(c);
            }
            c.close();
        }

        return person;
    }

    public boolean deletePerson(long personId) {
        return deleteRecord(personId);
    }

    @Override
    public boolean deleteRecord(long rowId) {
        return deleteRecord(DatabaseSchema.PersonEntry.TABLE_NAME, rowId);
    }

    @Override
    public int deleteAllRecords() {
        return deleteAllRecords(DatabaseSchema.PersonEntry.TABLE_NAME);
    }

    private Person buildPersonInstance(Cursor c) {
        return new Person(
                c.getLong(c.getColumnIndexOrThrow(DatabaseSchema.PersonEntry._ID)),
                c.getString(c.getColumnIndexOrThrow(DatabaseSchema.PersonEntry.COLUMN_NAME)),
                c.getString(c.getColumnIndexOrThrow(DatabaseSchema.PersonEntry.COLUMN_PHONE)),
                c.getString(c.getColumnIndexOrThrow(DatabaseSchema.PersonEntry.COLUMN_EMAIL)));
    }
}
