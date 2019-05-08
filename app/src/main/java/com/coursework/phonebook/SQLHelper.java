package com.coursework.phonebook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static java.lang.String.format;

public class SQLHelper  {
    private static final String DATABASE_NAME = "database.db";
    public static final String TABLE_NAME = "contacts";
    public static final String COLUMN_ID= "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SURNAME = "surname";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_PHONE= "phone";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDatabase;
    private static Context context;
    public static void setContext(Context cont){
        context = cont;
    }
    public SQLHelper (){
        if(context!=null) {
            myDatabase = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            this.context = context;
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS contacts(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT ," +
                    "surname TEXT," +
                    "company TEXT," +
                    "phone TEXT," +
                    "photo_path TEXT" +
                    ");");
        }

    }
    public void insert(Contact contact){
        myDatabase.execSQL(format("INSERT INTO contacts(" +
                "name,surname,company,phone,photo_path) VALUES(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\");",
                contact.getName(),
                contact.getSurname(),contact.getCompany(),
                contact.getPhone(),contact.getPhotoPath()));

    }
    public List<Contact>getAll(){
        List contacts =new ArrayList<Contact>();
        Cursor query = myDatabase.rawQuery("SELECT name,surname,company,phone,photo_path FROM contacts ; ",null);
        if(query.moveToFirst()){
            do{
                Contact contact =new Contact();
                contact.setName( query.getString(0));
                contact.setSurname( query.getString(1));
                contact.setCompany( query.getString(2));
                contact.setPhone( query.getString(4));
                contact.setPhotoPath( query.getString(4));
                contacts.add(contact);
            }
            while(query.moveToNext());
        }
        query.close();
        myDatabase.close();
        return contacts;
    }
    public void edit(Contact oldContact,Contact newContact){
        myDatabase.execSQL(format("UPDATE contacts SET name=\"%s\",surname=\"%s\", company=\"%s\" " +
                ",phone=\"%s\" ,photo_path=\"%s\" ",newContact.getName(),newContact.getSurname(),
                newContact.getCompany(),newContact.getPhone(),newContact.getPhotoPath())+
                format("WHERE name=\"%s\" AND surname=\"%s\" AND company=\"%s\" " +
                        "AND phone=\"%s\" ;",oldContact.getName(),oldContact.getSurname(),
                        oldContact.getCompany(),oldContact.getPhone()));
        myDatabase.close();
    }
    public void remove(Contact contact){
        myDatabase.execSQL(format("DELETE FROM contacts WHERE name=\"%s\" AND surname=\"%s\" AND company=\"%s\" " +
                        "AND phone=\"%s\" ;",contact.getName(),contact.getSurname(),
                contact.getCompany(),contact.getPhone()));
        myDatabase.close();
    }
    public void close(){
        myDatabase.close();
    }


}
