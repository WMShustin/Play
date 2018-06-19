package de.justfamouzin.play.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Justin@Famouz
 */

public class GameStatsDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "game_stats.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public GameStatsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + GameStatsContract.GameStatsEntry.TABLE_NAME + " (" +
                GameStatsContract.GameStatsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                GameStatsContract.GameStatsEntry.COLUMN_OPPONENT_NAME + " TEXT NOT NULL, " +
                GameStatsContract.GameStatsEntry.COLUMN_OPPONENTS_POINTS + " INTEGER NOT NULL, " +
                GameStatsContract.GameStatsEntry.COLUMN_MY_POINTS + " INTEGER NOT NULL, " +
                GameStatsContract.GameStatsEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WaitlistEntry.TABLE_NAME);
        //onCreate(sqLiteDatabase);
    }
}
