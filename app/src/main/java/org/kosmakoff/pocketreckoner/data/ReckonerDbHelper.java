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
        super(context, "reckonerDb", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create people DB
        // people(name, phone, email)
        String sql = "create table people (id integer primary key autoincrement, name text, phone text, email text, photo blob);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int curVersion) {
        // TODO Auto-generated method stub
        for (int i = prevVersion; i < curVersion; i++) {
            switch (i) {
                case 1:
                    upgrade001to002(db);
                    break;
            }
        }

    }

    public void upgrade001to002(SQLiteDatabase db) {
        String sql = "alter table people add column photo blob;";
        db.execSQL(sql);
    }
}
