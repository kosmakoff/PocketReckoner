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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReckonerDbHelper extends SQLiteOpenHelper {

    public ReckonerDbHelper(Context context) {
        super(context, "reckonerDb", null, 1);
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
        String sql = "create table people (" +
                "id integer primary key autoincrement," +
                "name text," +
                "phone text," +
                "email text," +
                "photo blob);";
        db.execSQL(sql);

        // create checks table
        // checks(description)
        sql = "create table checks(" +
                "id integer primary key autoincrement," +
                "description text," +
                "date_created integer," +
                "date_modified integer)";
        db.execSQL(sql);

        // create check items table
        // check_items(check_id, description)
        sql = "create table check_items (" +
                "id integer primary key autoincrement," +
                "check_id integer," +
                "description text," +
                "foreign key (check_id) references checks(id));";
        db.execSQL(sql);

        // create payers table
        // payers(check_item_id, person_id, price)
        sql = "create table payers (" +
                "id integer primary key autoincrement," +
                "check_item_id integer," +
                "person_id integer," +
                "price integer," +
                "foreign key (check_item_id) references check_items(id)," +
                "foreign key (person_id) references people(id));";
        db.execSQL(sql);

        // create buyers table
        // buyers(check_item_id,person_id,part)
        sql = "create table buyers (" +
                "id integer primary key autoincrement," +
                "check_item_id integer," +
                "person_id integer," +
                "part integer," +
                "foreign key (check_item_id) references check_items(id)," +
                "foreign key (person_id) references people(id));";
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
