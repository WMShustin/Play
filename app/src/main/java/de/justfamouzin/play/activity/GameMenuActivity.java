package de.justfamouzin.play.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;
import com.google.common.collect.Lists;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import de.justfamouzin.play.CardsAdapter;
import de.justfamouzin.play.Play;
import de.justfamouzin.play.R;
import de.justfamouzin.play.dialog.DialogFactory;
import de.justfamouzin.play.dialog.RankDialog;
import de.justfamouzin.play.model.Game;
import de.justfamouzin.play.model.GameBet;
import de.justfamouzin.play.model.Group;
import de.justfamouzin.play.model.Player;
import de.justfamouzin.play.model.Team;
import de.justfamouzin.play.model.TeamBet;

public class GameMenuActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private List<String> players = Lists.newArrayList();

    private RankDialog rankDialog;

    private int totalMatches = 0;

    private TextView tPoints;
    private TextView wmbet;
    private TextView rank;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_game_menu);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        for(int i = 0; i < Play.getInstance().getGroupList().size(); i++) {
            totalMatches+=Play.getInstance().getGroupList().get(i).getMatches().size();
        }
        TextView username = findViewById(R.id.username);
        username.setText(Play.getInstance().getFirebaseUser().getDisplayName());
        rank = findViewById(R.id.rang);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankDialog.show(getSupportFragmentManager(), "RANKINGLIST");
            }
        });
        wmbet = findViewById(R.id.wmbet);
        tPoints = findViewById(R.id.points);
        tPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rankDialog.show(getSupportFragmentManager(), "RANKINGLIST");
            }
        });
        initAllUsernames();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        // Set up the ViewPager with the sections adapter.
    }

    public void setWmBet(TeamBet teamBet) {
        TextView wmbet = findViewById(R.id.wmbet);
        Play.getInstance().getUtil().setWMBet(teamBet);
        wmbet.setText(Play.getInstance().getTeam(teamBet.getTeamId()).getEmoji());
    }

    private void initAllUsernames() {
        Play.getInstance().getUtil().getRef().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    DataSnapshot s = iterator.next();
                    if (s.getKey().equalsIgnoreCase(Play.getInstance().getFirebaseUser().getDisplayName())) {
                        if(i == dataSnapshot.getChildrenCount() -1) initOtherPlayers();
                    } else {
                        players.add(s.getKey());
                        if (i == dataSnapshot.getChildrenCount()-1) {
                            initOtherPlayers();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initOtherPlayers() {
        if(players.size() == 0) {
            initBets(progressDialog, tPoints, wmbet);
        }
        for(int n = 0; n < players.size(); n++) {
            final int finalN = n;
            Play.getInstance().getUtil().getRef().child(players.get(n)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Player player = new Player(dataSnapshot.getKey());
                    for(int i = 0; i < totalMatches+1; i++) {
                        GameBet gameBet = dataSnapshot.child(String.valueOf(i)).getValue(GameBet.class);
                        if(gameBet != null) {
                            player.setPoints(player.getPoints() + getSingleBetPoints(gameBet));
                        }
                        if (i == totalMatches) {
                            if(dataSnapshot.hasChild(String.valueOf(13012001))) {
                                TeamBet teamBet = dataSnapshot.child(String.valueOf(13012001)).getValue(TeamBet.class);
                                player.setTeamBet(teamBet);
                            }
                            Play.getInstance().addPlayer(player);
                            if (finalN == players.size() - 1) {
                                initBets(progressDialog, tPoints, wmbet);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void initBets(final ProgressDialog progressDialog, final TextView points, final TextView wm) {
        DatabaseReference usersRef = Play.getInstance().getUtil().getRef().child(Play.getInstance().getFirebaseUser().getDisplayName());
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < totalMatches+1; i++) {
                    GameBet gameBet = dataSnapshot.child(String.valueOf(i)).getValue(GameBet.class);
                    if (gameBet != null) {
                        Play.getInstance().addBet(gameBet);
                        Game game = Play.getInstance().getMatch(gameBet.getId());
                        if(game.isFinished()) {
                            int points = Play.getInstance().getUtil().getBetPoints(game, gameBet);
                            int lpoints = Play.getInstance().getPoints();
                            Play.getInstance().setPoints(lpoints + points);
                        }
                    }
                    if (i == totalMatches) {
                        if(dataSnapshot.hasChild(String.valueOf(13012001))) {
                            TeamBet teamBet = dataSnapshot.child(String.valueOf(13012001)).getValue(TeamBet.class);
                            Player player = new Player(Play.getInstance().getFirebaseUser().getDisplayName());
                            player.setPoints(Play.getInstance().getPoints());
                            if (teamBet != null) {
                                Play.getInstance().setTeamBet(teamBet);
                                wm.setText(Play.getInstance().getTeam(teamBet.getTeamId()).getEmoji());
                                player.setTeamBet(teamBet);
                            }
                            Play.getInstance().addPlayer(player);
                        }
                        rankDialog = DialogFactory.getRankDialog();
                        for(int l = 0; l < Play.getInstance().getPlayerList().size(); l++) {
                            if(Play.getInstance().getPlayerList().get(l).getName().equalsIgnoreCase(Play.getInstance().getFirebaseUser().getDisplayName())) {
                                rank.setText(String.valueOf(l+1));
                                rankDialog = DialogFactory.getRankDialog();
                            }
                        }
                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                        mViewPager = (ViewPager) findViewById(R.id.container);
                        mViewPager.setAdapter(mSectionsPagerAdapter);
                        mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                        Team wm = Play.getInstance().getWm();
                        if(wm != null) {
                            for (Player player : Play.getInstance().getPlayerList()) {
                                TeamBet teamBet = player.getTeamBet();
                                if(wm.getId() == teamBet.getTeamId()) {
                                    if(player.getName().equalsIgnoreCase(Play.getInstance().getFirebaseUser().getDisplayName())) Play.getInstance().setPoints(Play.getInstance().getPoints()+7);
                                    player.setPoints(player.getPoints() + 7);
                                    points.setTextColor(getResources().getColor(R.color.bet_green_right));
                                }
                            }
                        }
                        points.setText(Play.getInstance().getPoints() + "");
                        progressDialog.cancel();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getSingleBetPoints(GameBet gameBet) {
        if (gameBet != null) {
            Game game = Play.getInstance().getMatch(gameBet.getId());
            if(!game.isFinished()) return 0;
            return Play.getInstance().getUtil().getBetPoints(game, gameBet);
        }
        return 0;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_group, container, false);
            String dataName = "_" + getArguments().getInt(ARG_SECTION_NUMBER);
            Group group = Play.getInstance().getGroupList().get(getArguments().getInt(ARG_SECTION_NUMBER));
            TextView groupName = rootView.findViewById(R.id.textGroupName);
            groupName.setText(group.getName());
            TextView teamEmojis = rootView.findViewById(R.id.teamEmojis);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < group.getTeams().size(); i++) {
                Team team = group.getTeams().get(i);
                if(!group.isKo()) {
                    stringBuilder.append(i + 1)
                            .append(". ")
                            .append(team.getEmoji())
                            .append("  ")
                            .append(team.getPlayed())
                            .append(" | ")
                            .append(team.getGoals())
                            .append(":")
                            .append(team.getAgainst())
                            .append(" | ")
                            .append(team.getPoints())
                            .append(" P.")
                            .append("\n");
                } else {
                    stringBuilder.append(team.getEmoji()).append(" ");
                }
            }
            String emojis = stringBuilder.toString();
            if(group.isKo()) {
                if(emojis.length() > 8 ) {
                    teamEmojis.setLines(2);
                    int splitCount = (int) emojis.length() / 2;
                    String first = emojis.substring(0, splitCount);
                    String second = emojis.substring(splitCount);
                    teamEmojis.setText(first + "\n" + second + "\n");
                }
            } else {
                teamEmojis.setText(stringBuilder.toString());
            }

            RecyclerView mRecyclerView;
            RecyclerView.Adapter mAdapter;
            RecyclerView.LayoutManager mLayoutManager;

            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);

            mRecyclerView.setHasFixedSize(true);

            mLayoutManager = new LinearLayoutManager(rootView.getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
                @Override
                public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                    return true;
                }
            });

            mAdapter = new CardsAdapter(group.getMatches(), this.getActivity().getSupportFragmentManager());
            mRecyclerView.setAdapter(mAdapter);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        minimizeApp();
    }

    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return Play.getInstance().getGroupList().size();
        }
    }
}
