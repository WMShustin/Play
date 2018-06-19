package de.justfamouzin.play.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.justfamouzin.play.R;
import de.justfamouzin.play.RankAdapter;
import de.justfamouzin.play.activity.GameMenuActivity;

/**
 * @author Justin@Famouz
 */

public class RankDialog extends DialogFragment {

    public static final String TAG = "OneButtonDialogTag";

    protected static final String ARG_BUTTON_TEXT = "ARG_BUTTON_TEXT";
    protected static final String ARG_COLOR_RESOURCE_ID = "ARG_COLOR_RESOURCE_ID";
    protected static final String ARG_TITLE = "ARG_TITLE";
    protected static final String ARG_MESSAGE = "ARG_MESSAGE";
    protected static final String ARG_IMAGE_RESOURCE_ID = "ARG_IMAGE_RESOURCE_ID";

    private static final double DIALOG_WINDOW_WIDTH = 0.85;

    @BindView(R.id.rank_list)
    RecyclerView mRecyclerView;

    RecyclerView.Adapter mAdapter;

    private GameMenuActivity gameMenuActivity;

    private int getContentView() {
        return R.layout.dialog_rank;
    }

    public static RankDialog newInstance() {
        RankDialog oneButtonDialog = new RankDialog();

        return oneButtonDialog;
    }

    public void setActivity(GameMenuActivity gameMenuActivity) {
        this.gameMenuActivity = gameMenuActivity;
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

        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager
                = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        });

        mAdapter = new RankAdapter(this.getActivity().getSupportFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogWindowSize(DIALOG_WINDOW_WIDTH);
    }

    private void setDialogWindowSize(double width) {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display;
        if (window != null) {
            display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            int maxWidth = size.x;
            int maxHigh = size.y;
            window.setLayout((int) (maxWidth * width), (int)((maxHigh * 0.65)));
            window.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
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
