package de.justfamouzin.play.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Justin@Famouz
 */

public class DbUtil {

    public long addNewGame(SQLiteDatabase sqLiteDatabase, String opponentName, int opponentPoints, int myPoints) {
        ContentValues cv = new ContentValues();
        cv.put(GameStatsContract.GameStatsEntry.COLUMN_OPPONENT_NAME, opponentName);
        cv.put(GameStatsContract.GameStatsEntry.COLUMN_OPPONENTS_POINTS, opponentPoints);
        cv.put(GameStatsContract.GameStatsEntry.COLUMN_MY_POINTS, myPoints);
        return sqLiteDatabase.insert(GameStatsContract.GameStatsEntry.TABLE_NAME, null, cv);
    }

    public boolean removeGame(SQLiteDatabase sqLiteDatabase, long gameId) {
        return sqLiteDatabase.delete(GameStatsContract.GameStatsEntry.TABLE_NAME, GameStatsContract.GameStatsEntry._ID + "=" + gameId, null) > 0;
    }

    public Cursor getAllGames(SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.query(
                GameStatsContract.GameStatsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                GameStatsContract.GameStatsEntry._ID);

    }

}
