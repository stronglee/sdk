package com.example.stronglee.demo.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.example.stronglee.demo.R;

/**
 * Created by cg on 2015/12/30.
 */
public class EditDialog extends DialogFragment {

    private long id;
    private int mPosition;
    private TextView mUpdateView;
    private TextView mDeleteView;

    public interface EditUserOnClickListener {
        //flag标识,0表示删除,1表示修改
        void onEditUserOnClick(long id, int position, int flag);
    }


    public EditDialog(long id, int position) {
        this.id = id;
        this.mPosition = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_onetable_itemdialog, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUpdateView = (TextView) view.findViewById(R.id.txt_onetable_update);
        mUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserOnClickListener listener = (EditUserOnClickListener) getActivity();
                listener.onEditUserOnClick(id, mPosition, 1);
            }
        });
        mDeleteView = (TextView) view.findViewById(R.id.txt_onetable_delete);
        mDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditUserOnClickListener listener = (EditUserOnClickListener) getActivity();
                listener.onEditUserOnClick(id, mPosition, 0);
            }
        });
    }
}
