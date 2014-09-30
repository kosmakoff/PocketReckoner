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

import org.kosmakoff.pocketreckoner.app.PocketReckonerApplication;

public abstract class CommonRepository {

    protected static final String TAG = CommonRepository.class.getName();

    protected ReckonerDbHelper mDbHelper;
    protected SQLiteDatabase mDb;

    /**
     * Application context
     */
    // protected Context mContext;

    public CommonRepository(Context context) {
        mDbHelper = new ReckonerDbHelper(context);
        // mContext = context.getApplicationContext();
        open();
    }

    public CommonRepository(SQLiteDatabase db) {
        this.mDb = db;
        // this.mContext = PocketReckonerApplication.getAppContext();

        if (!mDb.isOpen() || mDb.isReadOnly())
            throw new IllegalArgumentException("Database not open or is read-only. Require writeable database");
    }

    public CommonRepository open() {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
            mDb.close();
        }
    }

    /**
     * Retrieves record with id <code>rowId</code> from table <code>tableName</code>
     *
     * @param tableName Name of table where record is found
     * @param rowId     ID of record to be retrieved
     * @return {@link android.database.Cursor} to record retrieved
     */
    protected Cursor fetchRecord(String tableName, long rowId) {
        return mDb.query(tableName, null, DatabaseSchema.CommonColumns._ID + "=" + rowId,
                null, null, null, null);
    }

    /**
     * Retrieves all records from database table <code>tableName</code>
     *
     * @param tableName Name of table in database
     * @return {@link Cursor} to all records in table <code>tableName</code>
     */
    protected Cursor fetchAllRecords(String tableName) {
        return mDb.query(tableName,
                null, null, null, null, null, null);
    }

    public long insertRecord(String tableName, ContentValues values) {
        return mDb.insert(tableName, null, values);
    }

    /**
     * Updates a record in the table
     *
     * @param recordId  Database ID of the record to be updated
     * @return Number of records affected
     */
    public int updateRecord(String tableName, long recordId, ContentValues values) {
        return mDb.update(tableName, values,
                DatabaseSchema.CommonColumns._ID + "=" + recordId, null);
    }

    /**
     * Deletes record with ID <code>rowID</code> from database table <code>tableName</code>
     * This does not delete the transactions and splits associated with the account
     *
     * @param tableName Name of table in database
     * @param rowId     ID of record to be deleted
     * @return <code>true</code> if deletion was successful, <code>false</code> otherwise
     */
    protected boolean deleteRecord(String tableName, long rowId) {
        return mDb.delete(tableName, DatabaseSchema.CommonColumns._ID + "=" + rowId, null) > 0;
    }

    /**
     * Deletes all records in the database
     *
     * @return Number of deleted records
     */
    protected int deleteAllRecords(String tableName) {
        return mDb.delete(tableName, null, null);
    }

    /**
     * Retrieves record with id <code>rowId</code> from table
     *
     * @param rowId ID of record to be retrieved
     * @return {@link Cursor} to record retrieved
     */
    public abstract Cursor fetchRecord(long rowId);

    /**
     * Retrieves all records from database table corresponding to this adapter
     *
     * @return {@link Cursor} to all records in table
     */
    public abstract Cursor fetchAllRecords();

    /**
     * Deletes record with ID <code>rowID</code> from database table
     *
     * @param rowId ID of record to be deleted
     * @return <code>true</code> if deletion was successful, <code>false</code> otherwise
     */
    public abstract boolean deleteRecord(long rowId);

    /**
     * Deletes all records in the database table
     *
     * @return Count of database records which have been deleted
     */
    public abstract int deleteAllRecords();
}
