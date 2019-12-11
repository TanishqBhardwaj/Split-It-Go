package com.example.SplitItGo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SplitItGo.R;

public class SetPasscodeFragment extends Fragment implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener{

    private EditText editText1, editText2, editText3, editText4;
    private TextView textViewUsername;
    private ImageView imageViewStart;
    private int whoHasFocus;
    private char[] code = new char[4];
    private Context mContext;
    private Activity mActivity;
    private String username;

    public SetPasscodeFragment(String username) {
        this.username = username;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        if(context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_passcode, container, false);
        editText1 = view.findViewById(R.id.editTextSetPasscode1);
        editText2 = view.findViewById(R.id.editTextSetPasscode2);
        editText3 = view.findViewById(R.id.editTextSetPasscode3);
        editText4 = view.findViewById(R.id.editTextSetPasscode4);
        textViewUsername = view.findViewById(R.id.textViewUsernameSetPasscode);
        imageViewStart = view.findViewById(R.id.imageViewStartSetPasscode);
        textViewUsername.setText(username);
        setListners();

        imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passcode="";
                    for(int i=0; i<code.length; i++) {
                        if(code[i]!='\u0000') {
                            passcode = passcode + code[i];
                            Log.d(String.valueOf(code[i]), "onClick: ");
                        }
                    }
                Log.d(passcode, "PassCode: ");
                    if(passcode.length() == 4) {
                        getFragmentManager().beginTransaction().replace(R.id.fragment_frame_main,
                                new ConfirmPasscodeFragment(username, passcode)).commit();
                    }
                    else {
                        Toast.makeText(mContext, "Fill all the fields", Toast.LENGTH_LONG).show();
                    }
            }
        });
        return view;
    }

    private void setListners()
    {
        editText1.addTextChangedListener(this);
        editText2.addTextChangedListener(this);
        editText3.addTextChangedListener(this);
        editText4.addTextChangedListener(this);

        editText1.setOnKeyListener(this);
        editText2.setOnKeyListener(this);
        editText3.setOnKeyListener(this);
        editText4.setOnKeyListener(this);

        editText1.setOnFocusChangeListener(this);
        editText2.setOnFocusChangeListener(this);
        editText3.setOnFocusChangeListener(this);
        editText4.setOnFocusChangeListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        switch (whoHasFocus)
        {
            case 1:
                if(!editText1.getText().toString().isEmpty())
                {
                    code[0]= editText1.getText().toString().charAt(0);
                    editText2.requestFocus();
                }
                break;

            case 2:
                if(!editText2.getText().toString().isEmpty())
                {
                    code[1]= editText2.getText().toString().charAt(0);
                    editText3.requestFocus();
                }
                break;

            case 3:
                if(!editText3.getText().toString().isEmpty())
                {
                    code[2]= editText3.getText().toString().charAt(0);
                    editText4.requestFocus();
                }
                break;

            case 4:
                if(!editText4.getText().toString().isEmpty())
                {
                    code[3]= editText4.getText().toString().charAt(0);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch(view.getId())
        {
            case R.id.editTextSetPasscode1:
                whoHasFocus=1;
                break;

            case R.id.editTextSetPasscode2:
                whoHasFocus=2;
                break;

            case R.id.editTextSetPasscode3:
                whoHasFocus=3;
                break;

            case R.id.editTextSetPasscode4:
                whoHasFocus=4;
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
        {
            if (i == KeyEvent.KEYCODE_DEL)
            {
                switch(view.getId())
                {
                    case R.id.editTextSetPasscode2:
                        if (editText2.getText().toString().isEmpty())
                            editText1.requestFocus();
                        break;

                    case R.id.editTextSetPasscode3:
                        if (editText3.getText().toString().isEmpty())
                            editText2.requestFocus();
                        break;

                    case R.id.editTextSetPasscode4:
                        if (editText4.getText().toString().isEmpty())
                            editText3.requestFocus();
                        break;

                    default:
                        break;
                }
            }
        }
        return false;
    }

}
