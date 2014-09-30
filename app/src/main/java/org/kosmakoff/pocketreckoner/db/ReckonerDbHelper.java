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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReckonerDbHelper extends SQLiteOpenHelper {

    public ReckonerDbHelper(Context context) {
        super(context, "reckonerDb", null, DatabaseSchema.DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        // TODO: when I decide to abandon API level 15 - move this code to onConfigure
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        // create people table
        // people(name, phone, email)

        String sql = new SqlBuilder(DatabaseSchema.PersonEntry.TABLE_NAME)
                .addPrimaryKeyField(DatabaseSchema.PersonEntry._ID)
                .addField(DatabaseSchema.PersonEntry.COLUMN_NAME, "text")
                .addField(DatabaseSchema.PersonEntry.COLUMN_PHONE, "text")
                .addField(DatabaseSchema.PersonEntry.COLUMN_EMAIL, "text")
                .addField(DatabaseSchema.PersonEntry.COLUMN_PHOTO, "blob")
                .build();
        db.execSQL(sql);

        // create checks table
        // checks(description)
        sql = new SqlBuilder(DatabaseSchema.CheckEntry.TABLE_NAME)
                .addPrimaryKeyField(DatabaseSchema.CheckEntry._ID)
                .addField(DatabaseSchema.CheckEntry.COLUMN_DESCRIPTION, "text")
                .addField(DatabaseSchema.CheckEntry.COLUMN_DATE_CREATED, "integer")
                .addField(DatabaseSchema.CheckEntry.COLUMN_DATE_MODIFIED, "integer")
                .build();
        db.execSQL(sql);

        // create check items table
        // check_items(check_id, description)
        sql = new SqlBuilder(DatabaseSchema.CheckItemEntry.TABLE_NAME)
                .addPrimaryKeyField(DatabaseSchema.CheckItemEntry._ID)
                .addField(DatabaseSchema.CheckItemEntry.COLUMN_CHECK_ID, "integer")
                .addField(DatabaseSchema.CheckItemEntry.COLUMN_DESCRIPTION, "text")
                .addForeignKey(DatabaseSchema.CheckItemEntry.COLUMN_CHECK_ID,
                        DatabaseSchema.CheckEntry.TABLE_NAME, DatabaseSchema.CheckEntry._ID)
                .build();
        db.execSQL(sql);

        // create payers table
        // payers(check_item_id, person_id, price)
        sql = new SqlBuilder(DatabaseSchema.PayerEntry.TABLE_NAME)
                .addPrimaryKeyField(DatabaseSchema.PayerEntry._ID)
                .addField(DatabaseSchema.PayerEntry.COLUMN_CHECK_ITEM_ID, "integer")
                .addField(DatabaseSchema.PayerEntry.COLUMN_PERSON_ID, "integer")
                .addField(DatabaseSchema.PayerEntry.COLUMN_PRICE, "integer")
                .addForeignKey(DatabaseSchema.PayerEntry.COLUMN_CHECK_ITEM_ID,
                        DatabaseSchema.CheckItemEntry.TABLE_NAME, DatabaseSchema.CheckItemEntry._ID)
                .addForeignKey(DatabaseSchema.PayerEntry.COLUMN_PERSON_ID,
                        DatabaseSchema.PersonEntry.TABLE_NAME, DatabaseSchema.PersonEntry._ID)
                .build();
        db.execSQL(sql);

        // create buyers table
        // buyers(check_item_id,person_id,part)
        sql = new SqlBuilder(DatabaseSchema.BuyerEntry.TABLE_NAME)
                .addPrimaryKeyField(DatabaseSchema.BuyerEntry._ID)
                .addField(DatabaseSchema.BuyerEntry.COLUMN_CHECK_ITEM_ID, "integer")
                .addField(DatabaseSchema.BuyerEntry.COLUMN_PERSON_ID, "integer")
                .addField(DatabaseSchema.BuyerEntry.COLUMN_PART, "integer")
                .addForeignKey(DatabaseSchema.BuyerEntry.COLUMN_CHECK_ITEM_ID,
                        DatabaseSchema.CheckItemEntry.TABLE_NAME, DatabaseSchema.CheckItemEntry._ID)
                .addForeignKey(DatabaseSchema.BuyerEntry.COLUMN_PERSON_ID,
                        DatabaseSchema.PersonEntry.TABLE_NAME, DatabaseSchema.PersonEntry._ID)
                .build();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int curVersion) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        for (int i = prevVersion; i < curVersion; i++) {
            switch (i) {
                default:
                    break;
            }
        }
    }
}
