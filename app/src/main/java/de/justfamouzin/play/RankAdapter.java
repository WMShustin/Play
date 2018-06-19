package de.justfamouzin.play;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.justfamouzin.play.model.Player;
import de.justfamouzin.play.model.Team;

/**
 * @author Justin@Famouz
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankViewHolder> {

    private List<Player> playerList;
    private FragmentManager fragmentManager;

    public class RankViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView userPoints;
        public TextView userEmoji;

        public RankViewHolder(View view) {
            super(view);
            playerList = Play.getInstance().getPlayerList();
            Log.d("XXX", playerList.toString());
            userName = view.findViewById(R.id.rank_username);
            userPoints = view.findViewById(R.id.rank_points);
            userEmoji = view.findViewById(R.id.rank_emoji);
        }
    }

    public RankAdapter(FragmentManager fragmentManager) {
        this.playerList = Play.getInstance().getPlayerList();
        this.fragmentManager = fragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RankAdapter.RankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rank_adapter_text, parent, false);
        return new RankViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RankViewHolder holder, int position) {
        onBind(holder);
    }

    @Override
    public void onBindViewHolder(RankViewHolder holder, int position, List<Object> payloads) {
        onBind(holder);
    }

    private void onBind(RankViewHolder cardsViewHolder) {
        Player player = playerList.get(cardsViewHolder.getAdapterPosition());
        String rankName = cardsViewHolder.getAdapterPosition()+1 + ". ";
        cardsViewHolder.userName.setText(rankName + (player.getName().contains(" ") ? player.getName().split(" ")[0] : player.getName()));
        cardsViewHolder.userPoints.setText(String.valueOf(player.getPoints()));
        Team team = null;
        if(player.getTeamBet() != null) {
            team = Play.getInstance().getTeam(player.getTeamBet().getTeamId());
        }
        cardsViewHolder.userEmoji.setText(team == null ? "N/A" : team.getEmoji());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return playerList.size();
    }
}
