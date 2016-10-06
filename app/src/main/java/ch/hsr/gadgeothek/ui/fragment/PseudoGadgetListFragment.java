package ch.hsr.gadgeothek.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.domain.Gadget;
import ch.hsr.gadgeothek.ui.GadgetListCallback;

public class PseudoGadgetListFragment extends Fragment {

    // TODO: remove / implement "mode"
    public static final String PAGE_TITLE = "PAGE_TITLE";

    private GadgetListCallback callback;

    private String pageTitle;

    public PseudoGadgetListFragment() {
    }

    public static PseudoGadgetListFragment getInstance(String pageTitle){
        PseudoGadgetListFragment fragment = new PseudoGadgetListFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_TITLE, pageTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            this.pageTitle = getArguments().getString(PAGE_TITLE);
            Log.d("TAG", "Page Title: "+pageTitle);
        }else{
            throw new RuntimeException("No title given!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pseudo_gadget_list, container, false);
        TextView content = ((TextView)view.findViewById(R.id.pager_label));
        content.setText(pageTitle);

        content.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                callback.onGadgetClicked(
                        // TODO: Call with actual gadget!
                        new Gadget(((TextView)v).getText().toString())
                );
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof GadgetListCallback){
            callback = (GadgetListCallback) context;
        }else{
            throw new RuntimeException(context.toString() + " must implement the reuqired interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }
}
