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

import android.provider.BaseColumns;

public class DatabaseSchema {
    static final int DATABASE_VERSION = 1;

    private DatabaseSchema() {
    }

    public interface CommonColumns extends BaseColumns {
        public static final String COLUMN_UID = "uid";
    }

    public static abstract class PersonEntry implements CommonColumns {
        public static final String TABLE_NAME = "people";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHOTO = "photo";
    }

    public static abstract class CheckEntry implements CommonColumns {
        public static final String TABLE_NAME = "checks";

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE_CREATED = "date_created";
        public static final String COLUMN_DATE_MODIFIED = "date_modified";
    }

    public static abstract class CheckItemEntry implements CommonColumns {
        public static final String TABLE_NAME = "check_items";

        public static final String COLUMN_CHECK_ID = "check_id";
        public static final String COLUMN_DESCRIPTION = "description";
    }

    public static abstract class PayerEntry implements CommonColumns {
        public static final String TABLE_NAME = "payers";

        public static final String COLUMN_CHECK_ITEM_ID = "check_item_id";
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_PRICE = "price";
    }

    public static abstract class BuyerEntry implements CommonColumns {
        public static final String TABLE_NAME = "buyers";

        public static final String COLUMN_CHECK_ITEM_ID = "check_item_id";
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_PART = "part";
    }
}
