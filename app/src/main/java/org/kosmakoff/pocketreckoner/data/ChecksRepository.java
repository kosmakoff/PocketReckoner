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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class ChecksRepository {
    private ReckonerDbHelper helper;

    public ChecksRepository(Context context) {
        helper = new ReckonerDbHelper(context);
    }

    // getters
    public ArrayList<Check> getChecks(Date from, Date to) {
        return null;
    }

    public Check getCheck(long checkId) {
        return null;
    }

    public ArrayList<CheckItem> getCheckItems(long checkId) {
        return null;
    }

    public ArrayList<Payer> getPayers(long checkItemId) {
        return null;
    }

    public ArrayList<Buyer> getBuyers(long checkItemId) {
        return null;
    }

    // setters
    // check
    public long getNewCheck(String description) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("date_created", date.getTime());
        values.put("date_modified", date.getTime());

        long newId = db.insert("checks", null, values);

        return newId;
    }

    public void updateCheckDescription(int checkId, String description) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("date_modified", date.getTime());

        db.update("checks", values, "id = ?", new String[]{String.valueOf(checkId)});
    }

    public void updateCheckDate(int checkId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("date_modified", date.getTime());

        db.update("checks", values, "id = ?", new String[]{String.valueOf(checkId)});
    }
}
