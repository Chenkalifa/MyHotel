package hyperactive.co.il.mehearthotel;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tal on 24/01/2016.
 */
public class InputDialogFragment extends DialogFragment implements View.OnClickListener {
    EditText inputEt;
    ImageView ic_close;
    TextView setBtn,titleTv;
    String title;
    InputUpDater upDater;

    public static final InputDialogFragment newInstance(String title){
        InputDialogFragment inputDialogFragment=new InputDialogFragment();
        inputDialogFragment.title=title;
        return  inputDialogFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.input_dailogfragment_layout, container, false);
        titleTv= (TextView) view.findViewById(R.id.input_fragment_title);
        titleTv.setTypeface(MyApp.FONT_MAIN);
        inputEt= (EditText) view.findViewById(R.id.input_inputVlaueEt);
        inputEt.setTypeface(MyApp.FONT_MAIN);
        setBtn= (TextView) view.findViewById(R.id.input_setBtn);
        setBtn.setTypeface(MyApp.FONT_SECONDARY);
        ic_close= (ImageView) view.findViewById(R.id.ic_close);
        titleTv.setText(title);
        setBtn.setOnClickListener(this);
        ic_close.setOnClickListener(this);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        final View vv=v;
        switch (vv.getId()){
            case R.id.ic_close:
                dismiss();
                break;
            case R.id.input_setBtn:
                String inputValue=inputEt.getText().toString().toLowerCase().trim();
                if(checkInputValueValidation(inputValue)){
                    upDater.updateChanges(inputValue);
                    dismiss();
                }

//                InputUpDater.updateChanges(String type)
                break;
        }

    }

    private boolean checkInputValueValidation(String inputValue) {
//        final String username="Username";
        final String email="Email";
        final String password="Password";
        if(inputValue.length()==0){
            MyToast.makeToast(getActivity(), title + " can't be empty");
//            Toast.makeText(getActivity(), title + " can't be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if(inputValue.contains(" ")){
            MyToast.makeToast(getActivity(), title+" can't contains spaces");
//            Toast.makeText(getActivity(), title+" can't contains spaces", Toast.LENGTH_LONG).show();
            return false;
        }
        switch (title){
            case email:
                if(isValidEmail(inputValue))
                    return true;
                else
                MyToast.makeToast(getActivity(), inputValue+" isn't valid email");
//                    Toast.makeText(getActivity(), inputValue+" isn't valid email", Toast.LENGTH_LONG).show();
                break;
            case password:
                if(inputValue.length()>=6)
                    return true;
                else
                MyToast.makeToast(getActivity(), title+" must be at least 6 chars");
//                    Toast.makeText(getActivity(), title+" must be at least 6 chars", Toast.LENGTH_LONG).show();
                break;
                default:
                    return true;
        }
        return false;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void setUpdater(InputUpDater inputUpDater){
        upDater=inputUpDater;
    }

    public interface InputUpDater{
        void updateChanges(String value);
    }
}
