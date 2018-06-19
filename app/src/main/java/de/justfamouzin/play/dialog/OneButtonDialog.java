package de.justfamouzin.play.dialog;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.justfamouzin.play.CardsAdapter;
import de.justfamouzin.play.Play;
import de.justfamouzin.play.R;
import de.justfamouzin.play.model.Game;
import de.justfamouzin.play.model.GameBet;
import de.justfamouzin.play.model.Team;

/**
 * Created by bruno.trovo on 28/06/2017.
 */

public class OneButtonDialog extends DialogFragment {

    public static final String TAG = "OneButtonDialogTag";

    protected static final String ARG_BUTTON_TEXT = "ARG_BUTTON_TEXT";
    protected static final String ARG_COLOR_RESOURCE_ID = "ARG_COLOR_RESOURCE_ID";
    protected static final String ARG_TITLE = "ARG_TITLE";
    protected static final String ARG_MESSAGE = "ARG_MESSAGE";
    protected static final String ARG_IMAGE_RESOURCE_ID = "ARG_IMAGE_RESOURCE_ID";

    private static final double DIALOG_WINDOW_WIDTH = 0.85;

    private static Team home, away;
    private static Game game1;
    private static CardsAdapter adapter;

    private static int position;

    private ButtonDialogAction buttonDialogAction;

    @BindView(R.id.dlg_one_button_iv_icon)
    ImageView ivDialogIcon;

    @BindView(R.id.home_team)
    TextView homeTeam;
    @BindView(R.id.away_team)
    TextView awayTeam;

    @BindView(R.id.home_result)
    EditText homeResult;
    @BindView(R.id.away_result)
    EditText awayResult;

    @BindView(R.id.dlg_one_button_btn_ok)
    Button btnNeutral;

    private int getContentView() {
        return R.layout.dialog_one_button;
    }

    public static OneButtonDialog newInstance(Game game, CardsAdapter ca, int pos, @DrawableRes int imageResId) {
        OneButtonDialog oneButtonDialog = new OneButtonDialog();
        home = game.getHome();
        away = game.getAway();
        game1 = game;
        adapter = ca;
        position = pos;

        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RESOURCE_ID, imageResId);
        oneButtonDialog.setArguments(args);

        return oneButtonDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }

        View view = inflater.inflate(getContentView(), container, false);
        ButterKnife.bind(this, view);

        getDialog().setCanceledOnTouchOutside(false);

        homeResult.setText("0");
        awayResult.setText("0");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeTeam.setText(home.getName()+ home.getEmoji());
        homeTeam.setTextColor(getResources().getColor(R.color.green_500));
        awayTeam.setText(away.getName()+away.getEmoji());
        awayTeam.setTextColor(getResources().getColor(R.color.green_500));
        btnNeutral.setText("Tipp platzieren");
        int image = getArguments().getInt(ARG_IMAGE_RESOURCE_ID);
        ivDialogIcon.setImageResource(image);
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogWindowWidth(DIALOG_WINDOW_WIDTH);
    }

    private void setDialogWindowWidth(double width) {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display;
        if (window != null) {
            display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            int maxWidth = size.x;
            window.setLayout((int) (maxWidth* width), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
    }

    @OnClick(R.id.dlg_one_button_btn_ok)
    public void onButtonClicked() {
        GameBet gameBet = new GameBet(game1.getName(), Integer.valueOf(homeResult.getText().toString()), Integer.valueOf(awayResult.getText().toString()));
        Play.getInstance().getUtil().addData(gameBet);
        adapter.notifyItemChanged(position, gameBet);
        closeDialog();
        if(buttonDialogAction != null) {
            buttonDialogAction.onButtonClicked();
        }
    }

    public void closeDialog() {
        if (getDialog().isShowing()) {
            closeKeyboard();
            getDialog().dismiss();
        }
    }

    protected void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
    }

    public interface ButtonDialogAction {
        void onButtonClicked();
    }

}
