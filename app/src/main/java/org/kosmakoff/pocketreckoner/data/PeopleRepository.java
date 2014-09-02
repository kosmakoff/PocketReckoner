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

package org.kosmakoff.pocketreckoner.data;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

public class PeopleRepository {
    private ReckonerDbHelper helper;

    public PeopleRepository(Context context) {
        helper = new ReckonerDbHelper(context);
    }

    public ArrayList<Person> getPeople() {
        SQLiteDatabase db = null;
        Cursor c = null;
        ArrayList<Person> people = new ArrayList<Person>();

        try {
            db = helper.getReadableDatabase();
            c = db.query("people", null, null, null, null, null, null);

            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int phoneColIndex = c.getColumnIndex("phone");
                int emailColIndex = c.getColumnIndex("email");
                int photoIndex = c.getColumnIndex("photo");

                do {
                    int id = c.getInt(idColIndex);
                    String name = c.getString(nameColIndex);
                    String phone = c.getString(phoneColIndex);
                    String email = c.getString(emailColIndex);
                    byte[] blob = c.getBlob(photoIndex);

                    Person person = new Person(id, name, phone, email);

                    people.add(person);
                } while (c.moveToNext());
            }
        } finally {
            if (c != null) {
                c.close();
            }

            if (db != null) {
                db.close();
            }
        }

        return people;
    }

    public Person addPerson(String name, String phone, String email, Bitmap photo) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("email", email);
        putPhotoIfNotNull(values, photo);

        long newId = db.insert("people", null, values);

        if (newId != -1) {
            return getPerson(newId);
        }

        return null;
    }

    public void updatePerson(long personId, String name, String phone, String email, Bitmap photo) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("email", email);
        putPhotoIfNotNull(values, photo);

        db.update("people", values, "id = ?", new String[]{String.valueOf(personId)});
    }

    private void putPhotoIfNotNull(ContentValues cv, Bitmap photo) {
        if (photo != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] blob = stream.toByteArray();
            cv.put("photo", blob);
        }
    }

    public Person getPerson(long id) {
        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = helper.getReadableDatabase();
            c = db.query("people", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (c.moveToFirst()) {
                int idColIndex = c.getColumnIndex("id");
                int nameColIndex = c.getColumnIndex("name");
                int phoneColIndex = c.getColumnIndex("phone");
                int emailColIndex = c.getColumnIndex("email");

                int pId = c.getInt(idColIndex);
                String name = c.getString(nameColIndex);
                String phone = c.getString(phoneColIndex);
                String email = c.getString(emailColIndex);

                Person person = new Person(pId, name, phone, email);

                return person;
            } else {
                return null;
            }
        } finally {
            if (c != null) {
                c.close();
            }

            if (db != null) {
                db.close();
            }
        }
    }

    public void deletePerson(long personId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("people", "id = ?", new String[]{String.valueOf(personId)});
    }
}
