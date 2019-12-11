package com.example.SplitItGo.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.SplitItGo.Activity.HomeActivity;
import com.example.SplitItGo.R;
import com.example.SplitItGo.Utils.PreferenceUtils;

import java.util.Map;
import java.util.Set;

public class ConfirmPasscodeFragment extends Fragment implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener{

    private EditText editText1, editText2, editText3, editText4;
    private TextView textViewUsername;
    private ImageView imageViewStart;
    private int whoHasFocus;
    private char[] code1 = new char[4];
    private Context mContext;
    private Activity mActivity;
    private String username, passcode;
    private PreferenceUtils pref;

    public ConfirmPasscodeFragment(String username, String passcode) {
        this.username = username;
        this.passcode = passcode;
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

        View view = inflater.inflate(R.layout.fragment_confirm_passcode, container, false);

        pref = new PreferenceUtils(mContext);

        editText1 = view.findViewById(R.id.editTextConfirmPasscode1);
        editText2 = view.findViewById(R.id.editTextConfirmPasscode2);
        editText3 = view.findViewById(R.id.editTextConfirmPasscode3);
        editText4 = view.findViewById(R.id.editTextConfirmPasscode4);
        textViewUsername = view.findViewById(R.id.textViewUsernameConfirmPasscode);
        imageViewStart = view.findViewById(R.id.imageViewStartConfirmPasscode);
        textViewUsername.setText(username);
        setListners();

        imageViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passcode1="";
                    for(int i=0; i<code1.length; i++) {
                        if(code1[i]!='\u0000') {
                            passcode1 = passcode1 + code1[i];
                            Log.d(String.valueOf(code1[i]), "onClick: ");
                        }
                    }

                    if(passcode1.length() == 4) {
                        if(passcode1.equals(passcode)) {
                            pref.setKeyPasscode(passcode);
                            Toast.makeText(mContext, passcode, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, HomeActivity.class);
                            startActivity(intent);
                            mActivity.finish();
                        }
                        else {
                            Toast.makeText(mContext, "Passwords do not match!", Toast.LENGTH_LONG).show();
                        }
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
                    code1[0]= editText1.getText().toString().charAt(0);
                    editText2.requestFocus();
                }
                break;

            case 2:
                if(!editText2.getText().toString().isEmpty())
                {
                    code1[1]= editText2.getText().toString().charAt(0);
                    editText3.requestFocus();
                }
                break;

            case 3:
                if(!editText3.getText().toString().isEmpty())
                {
                    code1[2]= editText3.getText().toString().charAt(0);
                    editText4.requestFocus();
                }
                break;

            case 4:
                if(!editText4.getText().toString().isEmpty())
                {
                    code1[3]= editText4.getText().toString().charAt(0);
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
            case R.id.editTextConfirmPasscode1:
                whoHasFocus=1;
                break;

            case R.id.editTextConfirmPasscode2:
                whoHasFocus=2;
                break;

            case R.id.editTextConfirmPasscode3:
                whoHasFocus=3;
                break;

            case R.id.editTextConfirmPasscode4:
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
                    case R.id.editTextConfirmPasscode2:
                        if (editText2.getText().toString().isEmpty())
                            editText1.requestFocus();
                        break;

                    case R.id.editTextConfirmPasscode3:
                        if (editText3.getText().toString().isEmpty())
                            editText2.requestFocus();
                        break;

                    case R.id.editTextConfirmPasscode4:
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
