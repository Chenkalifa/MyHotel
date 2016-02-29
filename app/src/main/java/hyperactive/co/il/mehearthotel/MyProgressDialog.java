package hyperactive.co.il.mehearthotel;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Tal on 28/02/2016.
 */
public class MyProgressDialog extends ProgressDialog {
    String title;
    String message;
    Context context;
    public MyProgressDialog(Context context, String title, String message) {
        super(context);
        this.context=context;
        this.title=title;
        this.message=message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prgress_dialog_layout);
        TextView titleTv= (TextView) findViewById(R.id.progress_title_tv);
        TextView messageTv= (TextView) findViewById(R.id.progress_message_tv);
        titleTv.setText(title);
        messageTv.setText(message);
//        ProgressBar progressBar= (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.getIndeterminateDrawable().setColorFilter(
//                context.getResources().getColor(R.color.colorPrimary),
//                android.graphics.PorterDuff.Mode.SRC_IN);
    }
}
