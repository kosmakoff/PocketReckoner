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

package org.kosmakoff.pocketreckoner.model;

import java.util.Date;

public class Check {
    private long id;
    private String description;

    private long dateCreatedRaw;
    private long dateModifiedRaw;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDateCreatedRaw() {
        return dateCreatedRaw;
    }

    public void setDateCreatedRaw(long dateCreatedRaw) {
        this.dateCreatedRaw = dateCreatedRaw;
    }

    public long getDateModifiedRaw() {
        return dateModifiedRaw;
    }

    public void setDateModifiedRaw(long dateModifiedRaw) {
        this.dateModifiedRaw = dateModifiedRaw;
    }

    public Date getDateCreated() {
        return new Date(dateCreatedRaw);
    }

    public void setDateCreated(Date dateCreated) {
        dateCreatedRaw = dateCreated.getTime();
    }

    public Date getDateModified() {
        return new Date(dateModifiedRaw);
    }

    public void setDateModified(Date dateModified) {
        dateModifiedRaw = dateModified.getTime();
    }
}
