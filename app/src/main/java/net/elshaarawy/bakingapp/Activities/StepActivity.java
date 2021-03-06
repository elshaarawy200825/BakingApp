package net.elshaarawy.bakingapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.elshaarawy.bakingapp.Data.Entities.StepEntity;
import net.elshaarawy.bakingapp.Fragments.StepFragment;
import net.elshaarawy.bakingapp.Fragments.VideoPlayerFragment;
import net.elshaarawy.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class StepActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_STEPS = "EXTRA_STEPS";
    private static final String EXTRA_ID = "extra_id";
    private List<StepEntity> mStepEntities;
    private int mPosition, mStepsCount;
    private TextView mStepIndicator;
    private ImageButton mStepForward, mStepBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        getSupportActionBar().hide();
        mStepEntities = intent.getParcelableArrayListExtra(EXTRA_STEPS);

        mPosition = savedInstanceState == null
                ? intent.getIntExtra(EXTRA_ID, 0)
                : savedInstanceState.getInt(EXTRA_ID);

        mStepsCount = mStepEntities.size();

        if (findViewById(R.id.step_detail_portrait) != null) {
            StepFragment.attachMe(getSupportFragmentManager(),
                    R.id.step_detail_portrait, mStepEntities.get(mPosition));
            mStepIndicator = (TextView) findViewById(R.id.step_indicator);
            mStepIndicator.setText(getString(R.string.format_step_indicator, mPosition + 1, mStepsCount));

            mStepForward = (ImageButton) findViewById(R.id.step_forward);
            mStepBack = (ImageButton) findViewById(R.id.step_back);
            mStepForward.setOnClickListener(this);
            mStepBack.setOnClickListener(this);

            if (mPosition == 0) {
                mStepBack.setEnabled(false);
                mStepBack.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
            }

            if (mPosition == mStepsCount - 1) {
                mStepForward.setEnabled(false);
                mStepForward.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
            }

        } else if (findViewById(R.id.step_detail_land) != null ) {
            if (!mStepEntities.get(mPosition).getVideoURL().equals("")){
                VideoPlayerFragment.attachMe(getSupportFragmentManager(),
                        R.id.step_detail_land,
                        mStepEntities.get(mPosition).getVideoURL());
            }
            else {
                ImageView imageView = (ImageView) findViewById(R.id.step_detail_land_img);
                imageView.setVisibility(View.VISIBLE);
                if (!mStepEntities.get(mPosition).getThumbnailURL().equals("")){
                    Picasso.with(this)
                            .load(mStepEntities.get(mPosition).getThumbnailURL())
                            .error(R.drawable.error)
                            .placeholder(R.drawable.placeholder)
                            .into(imageView);
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_ID, mPosition);
    }

    public static void startMe(Context context, List<StepEntity> stepEntities, int itemIndex) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, (ArrayList) stepEntities);
        intent.putExtra(EXTRA_ID, itemIndex);
        context.startActivity(intent);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.step_forward:
                mStepIndicator.setText(getString(R.string.format_step_indicator, ++mPosition + 1, mStepsCount));

                StepFragment.attachMe(getSupportFragmentManager(),
                        R.id.step_detail_portrait, mStepEntities.get(mPosition));
                if (mPosition > mStepsCount - 2) {
                    mStepForward.setEnabled(false);
                    mStepForward.setImageResource(R.drawable.ic_keyboard_arrow_right_black_24dp);
                } else {
                    mStepBack.setEnabled(true);
                    mStepBack.setImageResource(R.drawable.ic_keyboard_arrow_left_accent_24dp);
                }
                break;
            case R.id.step_back:
                mStepIndicator.setText(getString(R.string.format_step_indicator, --mPosition + 1, mStepsCount));

                StepFragment.attachMe(getSupportFragmentManager(),
                        R.id.step_detail_portrait, mStepEntities.get(mPosition));
                if (mPosition == 0) {
                    mStepBack.setEnabled(false);
                    mStepBack.setImageResource(R.drawable.ic_keyboard_arrow_left_black_24dp);
                } else {
                    mStepForward.setEnabled(true);
                    mStepForward.setImageResource(R.drawable.ic_keyboard_arrow_right_accent_24dp);
                }
                break;
        }
    }
}
