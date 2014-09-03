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
        super(context, "reckonerDb", null, 3);
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

        // create reckoning sessions table
        // reckoning_sessions(description)
        sql = "create table reckoning_sessions(" +
                "id integer primary key autoincrement," +
                "description text," +
                "date_created integer," +
                "date_modified integer)";
        db.execSQL(sql);

        // create billable items table
        // billable_items(reckoning_session_id, description)
        sql = "create table billable_items (" +
                "id integer primary key autoincrement," +
                "reckoning_session_id integer," +
                "description text," +
                "foreign key (reckoning_session_id) references reckoning_sessions(id));";
        db.execSQL(sql);

        // create expenditures table
        // expenditures(billable_item_id, person_id, price)
        sql = "create table expenditures (" +
                "id integer primary key autoincrement," +
                "billable_item_id integer," +
                "person_id integer," +
                "price integer," +
                "foreign key (billable_item_id) references billable_items(id)," +
                "foreign key (person_id) references people(id));";
        db.execSQL(sql);

        // create purchasers table
        // purchasers(billable_item_id,person_id,part)
        sql = "create table purchasers (" +
                "id integer primary key autoincrement," +
                "billable_item_id integer," +
                "person_id integer," +
                "part integer," +
                "foreign key (billable_item_id) references billable_items(id)," +
                "foreign key (person_id) references people(id));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int curVersion) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        for (int i = prevVersion; i < curVersion; i++) {
            switch (i) {
                case 1:
                    upgrade001to002(db);
                    break;
                case 2:
                    upgrade002to003(db);
                    break;
            }
        }
    }

    private void upgrade001to002(SQLiteDatabase db) {
        String sql = "alter table people add column photo blob;";
        db.execSQL(sql);
    }

    private void upgrade002to003(SQLiteDatabase db) {
        // create reckoning sessions table
        // reckoning_sessions(description)
        String sql = "create table reckoning_sessions(" +
                "id integer primary key autoincrement," +
                "description text," +
                "date_created integer," +
                "date_modified integer)";
        db.execSQL(sql);

        // create billable items table
        // billable_items(description)
        sql = "create table billable_items (" +
                "id integer primary key autoincrement," +
                "reckoning_session_id integer," +
                "description text," +
                "foreign key (reckoning_session_id) references reckoning_sessions(id));";
        db.execSQL(sql);

        // create expenditures table
        // expenditures(billable_item_id, person_id, price)
        sql = "create table expenditures (" +
                "id integer primary key autoincrement," +
                "billable_item_id integer," +
                "person_id integer," +
                "price integer," +
                "foreign key (billable_item_id) references billable_items(id)," +
                "foreign key (person_id) references people(id));";
        db.execSQL(sql);

        // create purchasers table
        // purchasers(billable_item_id,person_id,part)
        sql = "create table purchasers (" +
                "id integer primary key autoincrement," +
                "billable_item_id integer," +
                "person_id integer," +
                "part integer," +
                "foreign key (billable_item_id) references billable_items(id)," +
                "foreign key (person_id) references people(id));";
        db.execSQL(sql);
    }
}
