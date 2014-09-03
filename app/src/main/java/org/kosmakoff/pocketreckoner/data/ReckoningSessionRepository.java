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

public class ReckoningSessionRepository {
    private ReckonerDbHelper helper;

    public ReckoningSessionRepository(Context context) {
        helper = new ReckonerDbHelper(context);
    }

    // getters
    public ArrayList<ReckoningSession> getSessions(Date from, Date to) {
        return null;
    }

    public ArrayList<BillableItem> getBillableItems(int sessionId) {
        return null;
    }

    public ArrayList<Expenditure> getExpenditures(int billableItemId) {
        return null;
    }

    public ArrayList<Purchaser> getPurchasers(int billableItemId) {
        return null;
    }

    // setters
    // session
    public long addNewSession(String description) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("date_created", date.getTime());
        values.put("date_modified", date.getTime());

        long newId = db.insert("reckoning_sessions", null, values);

        return newId;
    }

    public void updateSessionDescription(int sessionId, String description) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("description", description);
        values.put("date_modified", date.getTime());

        db.update("reckoning_sessions", values, "id = ?", new String[]{String.valueOf(sessionId)});
    }

    public void updateSessionDate(int sessionId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Date date = new Date();

        ContentValues values = new ContentValues();
        values.put("date_modified", date.getTime());

        db.update("reckoning_sessions", values, "id = ?", new String[]{String.valueOf(sessionId)});
    }
}
