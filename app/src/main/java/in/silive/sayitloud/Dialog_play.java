package in.silive.sayitloud;


import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dialog_play extends DialogFragment implements View.OnClickListener {

    Button ok;
    TextView text;

    public Dialog_play() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_dialog_play, container, false);
        ok = (Button) view.findViewById(R.id.ok);
        ok.setOnClickListener(this);
        text = (TextView) view.findViewById(R.id.text);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ok) {
            dismiss();

        }

    }

}
