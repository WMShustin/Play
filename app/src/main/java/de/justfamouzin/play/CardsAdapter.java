package de.justfamouzin.play;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import de.justfamouzin.play.dialog.DialogFactory;
import de.justfamouzin.play.dialog.OneButtonDialog;
import de.justfamouzin.play.dialog.TippDialog;
import de.justfamouzin.play.model.Game;
import de.justfamouzin.play.model.GameBet;

/**
 * @author Justin@Famouz
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardsViewHolder> {

    private List<Game> games;
    private FragmentManager fragmentManager;

    private String[] text = new String[]{"Dein Tipp war falsch", "Du hast auf das richtige Team gesetzt", "Du hast die Differenz richtig getippt", "Du hast das Ergebnis richtig getippt"};

    public class CardsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView gameEmojis, gameStadium, gameDate, gameBet;
        public CardView cardView;
        public Resources resources;
        private int points;

        public CardsViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            gameEmojis = (TextView) view.findViewById(R.id.game_Emojis);
            gameStadium = (TextView) view.findViewById(R.id.game_Stadium);
            gameDate = (TextView) view.findViewById(R.id.game_Date);
            gameBet = (TextView) view.findViewById(R.id.game_Bet);
            cardView = view.findViewById(R.id.card);
            resources = view.getResources();
        }

        @Override
        public void onClick(final View view) {
            Game game = games.get(getAdapterPosition());
            if (!game.isFinished()) {
                if(Play.getInstance().getUtil().timeLeft(game)) {
                    OneButtonDialog oneButtonDialog =
                            DialogFactory.makeSuccessDialog(game, getAdapterPosition(), CardsAdapter.this);
                    oneButtonDialog.show(fragmentManager, OneButtonDialog.TAG);
                } else {
                    Toast.makeText(view.getContext(), "Das Spiel läuft bereits", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(points > 0) {
                    TippDialog tippDialog = DialogFactory.winDialog(text[points], points);
                    tippDialog.show(fragmentManager, TippDialog.TAG);
                } else {
                    TippDialog tippDialog = DialogFactory.loseDialog(text[points], points);
                    tippDialog.show(fragmentManager, TippDialog.TAG);
                }
            }
        }
    }

    public CardsAdapter(List<Game> games, FragmentManager fragmentManager) {
        this.games = games;
        this.fragmentManager = fragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardsAdapter.CardsViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_card, parent, false);
        CardsViewHolder vh = new CardsViewHolder(itemView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CardsViewHolder holder, int position) {
        onBind(holder);
    }

    @Override
    public void onBindViewHolder(CardsViewHolder holder, int position, List<Object> payloads) {
        if(payloads.size() > 0 )
            onBind(holder, payloads.get(0));
        else
            onBind(holder);
    }

    private void onBind(CardsViewHolder cardsViewHolder) {
        Game game = games.get(cardsViewHolder.getAdapterPosition());
        cardsViewHolder.gameEmojis.setText(game.getHome().getEmoji() + "  -  " + game.getAway().getEmoji());
        cardsViewHolder.gameStadium.setText(game.getStadium().getName() + "\n" + game.getStadium().getCity());
        String day = game.getDate().get(Calendar.DAY_OF_MONTH) < 10 ? "0" + game.getDate().get(Calendar.DAY_OF_MONTH) : String.valueOf(game.getDate().get(Calendar.DAY_OF_MONTH));
        String minute = game.getDate().get(Calendar.MINUTE) < 10 ? "0" + game.getDate().get(Calendar.MINUTE) : String.valueOf(game.getDate().get(Calendar.MINUTE));
        GameBet gameBet = Play.getInstance().getBet(game.getName());
        if(gameBet != null) {
            cardsViewHolder.gameBet.setText("Tipp: " +gameBet.getHome() + ":" + gameBet.getAway());
        } else {
            cardsViewHolder.gameBet.setText("Tipp: _:_");
        }
        if(game.isFinished()) {
            cardsViewHolder.gameDate.setTextSize(16);
            String text = "End: " + game.getHome_result() + ":" + game.getAway_result();
            if(game.isHadPenalty()) text = text + " | n.E. " + game.getHome_penalty() + ":" + game.getAway_penalty();
            cardsViewHolder.gameDate.setText(text);
            if(gameBet != null) {
                int points = Play.getInstance().getUtil().getBetPoints(game, gameBet);
                if(points == 3) {
                    cardsViewHolder.cardView.setCardBackgroundColor(cardsViewHolder.resources.getColor(R.color.bet_green_right));
                    cardsViewHolder.points = points;
                } else if(points == 2) {
                    cardsViewHolder.cardView.setCardBackgroundColor(cardsViewHolder.resources.getColor(R.color.bet_green_diff));
                    cardsViewHolder.points = points;
                } else if(points == 1) {
                    cardsViewHolder.cardView.setCardBackgroundColor(cardsViewHolder.resources.getColor(R.color.bet_yellow_team));
                    cardsViewHolder.points = points;
                } else if(points == 0){
                    cardsViewHolder.cardView.setCardBackgroundColor(cardsViewHolder.resources.getColor(R.color.bet_red));
                    cardsViewHolder.points = points;
                }
                Play.getInstance().setPoints(Play.getInstance().getPoints()+points);
            }
        } else {
            cardsViewHolder.gameDate.setText(Play.getInstance().getUtil().getDay(game.getDate().get(Calendar.DAY_OF_WEEK)) + ", " + day + ".0" + (game.getDate().get(Calendar.MONTH)+1) + ". "
                    + game.getDate().get(Calendar.HOUR_OF_DAY) + ":" + minute);
        }
    }

    private void onBind(CardsViewHolder cardsViewHolder, Object o) {
        onBind(cardsViewHolder);
        if(o instanceof GameBet) {
            GameBet gameBet = (GameBet) o;
            cardsViewHolder.gameBet.setText("Tipp: " + gameBet.getHome() + ":" + gameBet.getAway());
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return games.size();
    }
}
