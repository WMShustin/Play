package de.justfamouzin.play.database;

import android.provider.BaseColumns;

/**
 * @author Justin@Famouz
 */

public class GameStatsContract {

    public static final class GameStatsEntry implements BaseColumns {
        public static final String TABLE_NAME = "game_stats";
        public static final String COLUMN_OPPONENT_NAME = "opponentName";
        public static final String COLUMN_OPPONENTS_POINTS = "opponentPoints";
        public static final String COLUMN_MY_POINTS = "myPoints";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }

}
