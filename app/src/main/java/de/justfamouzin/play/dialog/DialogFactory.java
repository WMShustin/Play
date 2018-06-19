package de.justfamouzin.play.dialog;

import de.justfamouzin.play.CardsAdapter;
import de.justfamouzin.play.R;
import de.justfamouzin.play.model.Game;

/**
 * @author Justin@Famouz
 */

public class DialogFactory {

    private DialogFactory() {
    }

    public static OneButtonDialog makeSuccessDialog(Game game, int pos, CardsAdapter holder) {
        return OneButtonDialog.newInstance(game,
                holder,
                pos,
                R.drawable.ic_checked);
    }

    public static OneButtonDialog makeErrorDialog(Game game, int pos, CardsAdapter holder) {
        return OneButtonDialog.newInstance(game,
                holder,
                pos,
                R.drawable.ic_close);
    }

    public static TippDialog winDialog(String text, int points) {
        return TippDialog.newInstance(text, points,
                R.drawable.ic_checked);
    }

    public static TippDialog loseDialog(String text, int points) {
        return TippDialog.newInstance(text, points,
                R.drawable.ic_close);
    }

    public static WMTippDialog getWMDialog() {
        return WMTippDialog.newInstance(R.drawable.ic_checked);
    }

    public static RankDialog getRankDialog() {
        return RankDialog.newInstance();
    }

}
