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

import java.util.ArrayList;

public class SqlBuilder {

    private String mTableName;
    private ArrayList<FieldDefinition> mFieldsDefinitions;
    private ArrayList<ForeignKeyDefinition> mForeignKeyDefinitions;

    public SqlBuilder(String mTableName) {
        this.mTableName = mTableName;
        mFieldsDefinitions = new ArrayList<FieldDefinition>(8);
        mForeignKeyDefinitions = new ArrayList<ForeignKeyDefinition>(4);
    }

    public SqlBuilder addPrimaryKeyField(String name) {
        return addField(name, "integer primary key autoincrement");
    }

    public SqlBuilder addField(String fieldName, String fieldType) {
        mFieldsDefinitions.add(new FieldDefinition(fieldName, fieldType));
        return this;
    }

    public SqlBuilder addForeignKey(String localFieldName, String remoteTableName, String remoteFieldName) {
        mForeignKeyDefinitions.add(new ForeignKeyDefinition(localFieldName, remoteTableName, remoteFieldName));
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(1024);

        builder.append("create table ");
        builder.append(mTableName);
        builder.append("(");

        boolean isFirstDefinition = true;

        for (FieldDefinition definition : mFieldsDefinitions) {
            if (!isFirstDefinition){
                builder.append(",");
            }

            builder.append(definition.getFieldName());
            builder.append(" ");
            builder.append(definition.getFieldType());

            isFirstDefinition = false;
        }

        for (ForeignKeyDefinition definition : mForeignKeyDefinitions) {
            if (!isFirstDefinition){
                builder.append(",");
            }

            builder.append("foreign key (");
            builder.append(definition.getLocalFieldName());
            builder.append(") references ");
            builder.append(definition.getRemoteTableName());
            builder.append("(");
            builder.append(definition.getRemoteFieldName());
            builder.append(")");

            isFirstDefinition = false;
        }

        builder.append(");");

        return builder.toString();
    }

    public class FieldDefinition {
        private String mFieldName;
        private String mFieldType;

        public FieldDefinition(String fieldName, String fieldType) {
            this.mFieldName = fieldName;
            this.mFieldType = fieldType;
        }

        public String getFieldName() {
            return mFieldName;
        }

        public String getFieldType() {
            return mFieldType;
        }
    }

    public class ForeignKeyDefinition {
        private String mLocalFieldName;
        private String mRemoteTableName;
        private String mRemoteFieldName;

        public ForeignKeyDefinition(String localFieldName, String remoteTableName, String remoteFieldName) {
            this.mLocalFieldName = localFieldName;
            this.mRemoteTableName = remoteTableName;
            this.mRemoteFieldName = remoteFieldName;
        }

        public String getLocalFieldName() {
            return mLocalFieldName;
        }

        public String getRemoteTableName() {
            return mRemoteTableName;
        }

        public String getRemoteFieldName() {
            return mRemoteFieldName;
        }
    }
}
