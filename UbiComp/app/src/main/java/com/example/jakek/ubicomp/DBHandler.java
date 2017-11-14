package com.example.jakek.ubicomp;

/**
 * Created by jakek on 14/11/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 4;
    // Database Name
    private static final String DATABASE_NAME = "shopsInfo1";
    // Contacts table name
    private static final String TABLE_SHOPS = "shops";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SH_ADDR = "shop_address";
    private static final String KEY_DATE = "date";
    private static final String KEY_ITEM = "item_name";
    private static final String KEY_PRICE = "item_price";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SHOPS + "("
        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
        + KEY_SH_ADDR + " TEXT," + KEY_DATE + " TEXT," + KEY_ITEM + " TEXT,"
                + KEY_PRICE + " DOUBLE" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPS);
// Creating tables again
        onCreate(db);
    }

    public void addShop(ShopEntry shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName()); // Shop Name
        values.put(KEY_SH_ADDR, shop.getAddress()); // Shop Phone Number
        values.put(KEY_DATE, shop.getDate());
        values.put(KEY_ITEM, shop.getItemName());
        values.put(KEY_PRICE, shop.getItemPrice());
// Inserting Row
        db.insert(TABLE_SHOPS, null, values);
        db.close(); // Closing database connection
    }

    public List<ShopEntry> getAllShops() {
        List<ShopEntry> shopList = new ArrayList<ShopEntry>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_SHOPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShopEntry shop = new ShopEntry();
                shop.setId(Integer.parseInt(cursor.getString(0)));
                shop.setName(cursor.getString(1));
                shop.setAddress(cursor.getString(2));
                shop.setDate(cursor.getString(3));
                shop.setItemName(cursor.getString(4));
                shop.setItemPrice(cursor.getDouble(5));
// Adding contact to list
                shopList.add(shop);
            } while (cursor.moveToNext());
        }
// return contact list
        return shopList;
    }

    public List<ShopEntry> getAllUniqueShops() {
        List<ShopEntry> shopList = new ArrayList<>();
        String selectQuery = "SELECT DISTINCT name, shop_address, date FROM " + TABLE_SHOPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShopEntry shop = new ShopEntry();
                shop.setName(cursor.getString(0));
                shop.setAddress(cursor.getString(1));
                shop.setDate(cursor.getString(2));

                shopList.add(shop);
            } while (cursor.moveToNext());
        }
// return contact list
        return shopList;
    }

    public List<ShopEntry> getItemsFromShop(String name, String address, String date) {
        List<ShopEntry> shopList = new ArrayList<>();
        String selectQuery = "SELECT item_name, item_price FROM " + TABLE_SHOPS
                + " WHERE name = '" + name + "' AND shop_address = '" +address+ "' AND date = '" + date + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ShopEntry shop = new ShopEntry();
                shop.setItemName(cursor.getString(0));
                shop.setItemPrice(cursor.getInt(1));

                shopList.add(shop);
            } while (cursor.moveToNext());
        }
// return contact list
        return shopList;
    }
    public int getShopsCount() {
        String countQuery = "SELECT * FROM " + TABLE_SHOPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
// return count
        return cursor.getCount();
    }

    public int updateShop(ShopEntry shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_SH_ADDR, shop.getAddress());
        values.put(KEY_DATE, shop.getDate());
        values.put(KEY_ITEM, shop.getItemName());
        values.put(KEY_PRICE, shop.getItemPrice());
// updating row
        return db.update(TABLE_SHOPS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(shop.getId())});
    }

    // Deleting a shop
    public void deleteShop(ShopEntry shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPS, KEY_ID + " = ?",
                new String[] { String.valueOf(shop.getId()) });
        db.close();
    }

}