package org.kosmakoff.pocketreckoner;

/**
 * Created by okosmakov on 30.07.13.
 */
public class DrawerMenuItem {
    public DrawerMenuItemType getMenuItemType() {
        return mMenuItemType;
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    private DrawerMenuItemType mMenuItemType;
    private CharSequence mTitle;
    private int mDrawableId;

    public DrawerMenuItem(DrawerMenuItemType mMenuItemId, CharSequence mTitle, int mDrawableId) {
        this.mMenuItemType = mMenuItemId;
        this.mTitle = mTitle;
        this.mDrawableId = mDrawableId;
    }
}
