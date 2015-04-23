package co.olivemedia.olivemediasampleproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vivek on 4/23/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "meetingManager";

    // Contacts table name
    private static final String TABLE_NAME = "MEETING_RECORDS";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_LOCATION = "LOCATION";
    private static final String KEY_TIME = "TIME";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_LOCATION + " TEXT,"
                + KEY_TIME + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void addRecords(Meetingrecords records) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues cv = new ContentValues();

           cv.put("TITLE",records.getMeetingTitle());
           cv.put("LOCATION",records.getMeetingLocation());
           cv.put("TIME",records.getMeetingTime());

        db.insert("MEETING_RECORDS", "id", cv);
            Log.v("data", "" + cv + "");
        } catch (Exception e) {
            Log.d("", e.getMessage());

        } finally {
           db.close();
        }

    }
    public Cursor selectRecords() {
        Cursor cursorValue = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String argu = "SELECT DISTINCT * FROM MEETING_RECORDS";
        Log.v("argu", argu);
        cursorValue = db.rawQuery(argu, null);
        return cursorValue;

    }
    public Cursor EmptyRecords() {
        // Cursor c;
        Cursor deleteCursor = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteContact = "DELETE FROM MEETING_RECORDS";
        db.execSQL(deleteContact);
        return deleteCursor;
    }

}