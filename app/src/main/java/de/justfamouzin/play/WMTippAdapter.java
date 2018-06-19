package de.justfamouzin.play;

import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.justfamouzin.play.dialog.WMTippDialog;
import de.justfamouzin.play.model.Team;

/**
 * @author Justin@Famouz
 */

public class WMTippAdapter extends RecyclerView.Adapter<WMTippAdapter.WMTippViewHolder> {

    private List<Team> teams;
    private FragmentManager fragmentManager;
    private WMTippDialog wmTippDialog;

    public class WMTippViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView emoji;

        public WMTippViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            emoji = view.findViewById(R.id.wmtipp_emoji_text);
        }

        @Override
        public void onClick(final View view) {
            wmTippDialog.closeDialog(teams.get(getAdapterPosition()).getId());
        }
    }

    public WMTippAdapter(FragmentManager fragmentManager, WMTippDialog wmTippDialog) {
        this.teams = Play.getInstance().getTeamList();
        this.fragmentManager = fragmentManager;
        this.wmTippDialog = wmTippDialog;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WMTippAdapter.WMTippViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wmtipp_adapter_text, parent, false);
        return new WMTippViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(WMTippViewHolder holder, int position) {
        onBind(holder);
    }

    @Override
    public void onBindViewHolder(WMTippViewHolder holder, int position, List<Object> payloads) {
        onBind(holder);
    }

    private void onBind(WMTippViewHolder cardsViewHolder) {
        Team team = teams.get(cardsViewHolder.getAdapterPosition());
        cardsViewHolder.emoji.setText(team.getEmoji() + "\n" + team.getName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return teams.size();
    }
}
