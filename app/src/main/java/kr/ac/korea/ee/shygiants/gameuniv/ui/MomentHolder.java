package kr.ac.korea.ee.shygiants.gameuniv.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.ac.korea.ee.shygiants.gameuniv.R;
import kr.ac.korea.ee.shygiants.gameuniv.models.Moment;
import kr.ac.korea.ee.shygiants.gameuniv.utils.ContentsStore;

/**
 * Created by SHYBook_Air on 15. 11. 19..
 */
public class MomentHolder extends RecyclerView.ViewHolder {

    TextView authorText;
    TextView timestampText;
    TextView contentText;

    public MomentHolder(View view) {
        super(view);

        authorText = (TextView) view.findViewById(R.id.author);
        timestampText = (TextView) view.findViewById(R.id.created_at);
        contentText = (TextView) view.findViewById(R.id.content);
    }

    public void populate(int position) {
        Moment moment = ContentsStore.getMomentAt(position);

        authorText.setText(moment.getAuthor().getUserName());
        timestampText.setText(moment.getTimeStamp());
        contentText.setText(moment.getContent());
    }
}
