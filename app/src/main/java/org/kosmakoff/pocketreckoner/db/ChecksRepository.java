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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.kosmakoff.pocketreckoner.model.Buyer;
import org.kosmakoff.pocketreckoner.model.Check;
import org.kosmakoff.pocketreckoner.model.CheckItem;
import org.kosmakoff.pocketreckoner.model.Payer;

import java.util.ArrayList;
import java.util.Date;

public class ChecksRepository extends CommonRepository {
    public ChecksRepository(Context context) {
        super(context);
    }

    public ChecksRepository(SQLiteDatabase db) {
        super(db);
    }

    public Check getCheck(long id) {
        if (id <= 0) return null;

        Check check = null;
        Cursor c = fetchRecord(id);

        if (c != null) {
            if (c.moveToFirst()) {
                check = buildCheckInstance(c);
            }
            c.close();
        }

        return check;
    }

    public ArrayList<Check> getAllChecks() {
        return getChecksImpl(null, null);
    }

    public ArrayList<Check> getChecks(Date from, Date to) {
        return getChecksImpl(from, to);
    }

    private ArrayList<Check> getChecksImpl(Date from, Date to) {
        ArrayList<Check> checks = new ArrayList<Check>();

        Cursor c;

        if (from == null && to == null) {
            c = fetchAllRecords();
        } else {
            c = mDb.query(DatabaseSchema.CheckEntry.TABLE_NAME,
                    null,
                    String.format("%s between ? and ?", DatabaseSchema.CheckEntry.COLUMN_DATE_MODIFIED, from.getTime(), to.getTime()),
                    new String[]{String.valueOf(from.getTime()), String.valueOf(to.getTime())},
                    null, null, null);
        }

        if (c == null) {
            return checks;
        }

        try {
            while (c.moveToNext()) {
                checks.add(buildCheckInstance(c));
            }
        } finally {
            c.close();
        }

        return checks;
    }

    public long addCheck(Check check) {
        Date date = new Date();
        check.setDateCreated(date);

        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.CheckEntry.COLUMN_DESCRIPTION, check.getDescription());
        values.put(DatabaseSchema.CheckEntry.COLUMN_DATE_CREATED, date.getTime());
        values.put(DatabaseSchema.CheckEntry.COLUMN_DATE_MODIFIED, date.getTime());

        long newId = insertRecord(DatabaseSchema.CheckEntry.TABLE_NAME, values);

        return newId;
    }

    public long updateCheck(Check check) {
        Date date = new Date();
        check.setDateModified(date);

        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.CheckEntry.COLUMN_DESCRIPTION, check.getDescription());
        values.put(DatabaseSchema.CheckEntry.COLUMN_DATE_MODIFIED, date.getTime());

        return updateRecord(DatabaseSchema.CheckEntry.TABLE_NAME, check.getId(), values);
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

    private Check buildCheckInstance(Cursor c) {
        Check check = new Check();

        check.setId(c.getLong(c.getColumnIndexOrThrow(DatabaseSchema.CheckEntry._ID)));
        check.setDescription(c.getString(c.getColumnIndexOrThrow(DatabaseSchema.CheckEntry.COLUMN_DESCRIPTION)));
        check.setDateCreatedRaw(c.getLong(c.getColumnIndexOrThrow(DatabaseSchema.CheckEntry.COLUMN_DATE_CREATED)));
        check.setDateModifiedRaw(c.getLong(c.getColumnIndexOrThrow(DatabaseSchema.CheckEntry.COLUMN_DATE_MODIFIED)));

        return check;
    }

    @Override
    public Cursor fetchRecord(long rowId) {
        return fetchRecord(DatabaseSchema.CheckEntry.TABLE_NAME, rowId);
    }

    @Override
    public Cursor fetchAllRecords() {
        return fetchAllRecords(DatabaseSchema.CheckEntry.TABLE_NAME);
    }

    @Override
    public boolean deleteRecord(long rowId) {
        return deleteRecord(DatabaseSchema.CheckEntry.TABLE_NAME, rowId);
    }

    @Override
    public int deleteAllRecords() {
        return deleteAllRecords(DatabaseSchema.CheckEntry.TABLE_NAME);
    }
}
