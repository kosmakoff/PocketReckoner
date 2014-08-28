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
