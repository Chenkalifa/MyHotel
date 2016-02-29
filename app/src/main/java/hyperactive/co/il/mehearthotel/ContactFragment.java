package hyperactive.co.il.mehearthotel;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tal on 18/02/2016.
 */
public class ContactFragment extends DialogFragment implements View.OnClickListener {
    TextView emailBodyEt, titleTv;
    Spinner subjectsSpinner;
    ImageView ic_close;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.contact_fragment_layout, container, false);
        emailBodyEt= (TextView) view.findViewById(R.id.contact_email_body_Et);
        emailBodyEt.setTypeface(MyApp.FONT_MAIN);
        titleTv= (TextView) view.findViewById(R.id.contact_title);
        titleTv.setTypeface(MyApp.FONT_MAIN);
        String[] subjects=getActivity().getResources().getStringArray(R.array.contact_subjects);
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, subjects){

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(MyApp.FONT_SECONDARY);

                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                ((TextView) v).setTypeface(MyApp.FONT_SECONDARY);
                v.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                return v;
            }
        };
        subjectsSpinner= (Spinner) view.findViewById(R.id.contact_subjects_spinner);
        subjectsSpinner.setAdapter(spinner_adapter);
        ic_close= (ImageView) view.findViewById(R.id.ic_close);
        Button continueBtn= (Button) view.findViewById(R.id.contact_continueBtn);
        continueBtn.setTypeface(MyApp.FONT_SECONDARY);
        continueBtn.setOnClickListener(this);
        ic_close.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onClick(View v) {
        final View vv=v;
        switch (vv.getId()){
            case R.id.contact_continueBtn:
                Intent contactIntent = new Intent(Intent.ACTION_SEND);
                contactIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.hotelEmail)});
                String subject=subjectsSpinner.getSelectedItem().toString();
                if(!subject.equals(getResources().getString(R.string.contact_subject_hint))){
                    contactIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    contactIntent.putExtra(Intent.EXTRA_TEXT, emailBodyEt.getText().toString());
                    contactIntent.setType("message/rfc822");
                    startActivity(Intent.createChooser(contactIntent, "Send email..."));
                    getDialog().dismiss();
                }else{
                    MyToast.makeToast(getActivity(), getResources().getString(R.string.contact_no_subject_picked));
//                    Toast.makeText(getActivity(), getResources().getString(R.string.contact_no_subject_picked), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.ic_close:
                dismiss();
                break;
        }
    }
}
