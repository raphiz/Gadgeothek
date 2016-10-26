package ch.hsr.gadgeothek.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ch.hsr.gadgeothek.R;
import ch.hsr.gadgeothek.util.SharedPreferencesHandler;

public class ServerAddressDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.dialog_server_address, null);

        final EditText serverAddress = (EditText) rootView.findViewById(R.id.serverAddress);
        serverAddress.setText(SharedPreferencesHandler.getServerAddress(getActivity()), TextView.BufferType.EDITABLE);

        builder.setView(rootView)
            .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferencesHandler.setServerAddress(getActivity(), serverAddress.getText().toString());
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ServerAddressDialogFragment.this.getDialog().cancel();
                }
            });
        builder.setTitle(R.string.change_server_url);
        return builder.create();
    }
}
