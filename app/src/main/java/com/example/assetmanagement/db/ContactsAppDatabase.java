package com.example.assetmanagement.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.teapink.ocr_reader.db.entity.Contact;

@Database(entities = {Contact.class},version = 1)
public abstract class ContactsAppDatabase extends RoomDatabase {

    public abstract ContactDAO getContactDAO();



}