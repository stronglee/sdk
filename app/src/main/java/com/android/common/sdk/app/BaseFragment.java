package com.android.common.sdk.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.common.sdk.app.lifecycle.IComponentContainer;
import com.android.common.sdk.app.lifecycle.LifeCycleComponent;
import com.android.common.sdk.app.lifecycle.LifeCycleComponentManager;
import com.orhanobut.logger.Logger;

/**
 * Implement {@link ILifecycleFragment}, {@link IComponentContainer}
 * Ignore {@link LifeCycleComponentManager#onBecomesPartiallyInvisible}
 */
public abstract class BaseFragment extends Fragment implements ILifecycleFragment, IComponentContainer {
    protected Object mDataIn;
    private boolean mFirstResume = true;

    private LifeCycleComponentManager mComponentContainer = new LifeCycleComponentManager();

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public BaseFragmentActivity getContext() {
        return (BaseFragmentActivity) getActivity();
    }

    /**
     *
     * Implements {@link ILifecycleFragment}
     *
     */
    @Override
    public void onEnter(Object data) {
        mDataIn = data;
        Logger.d("onEnter");
    }

    @Override
    public void onLeave() {
        Logger.d("onLeave");
        mComponentContainer.onBecomesTotallyInvisible();
    }

    @Override
    public void onBackWithData(Object data) {
        Logger.d("onBackWithData");
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public boolean processBackPressed() {
        return false;
    }

    @Override
    public void onBack() {
        Logger.d("onBack");
        mComponentContainer.onBecomesVisibleFromTotallyInvisible();
    }

    @Override
    public void addComponent(LifeCycleComponent component) {
        mComponentContainer.addComponent(component);
    }

    /**
     * Not add self to back stack when removed, so only when Activity stop
     */
    @Override
    public void onStop() {
        super.onStop();
        Logger.d("onStop");
        onLeave();
    }

    /**
     * Only when Activity resume, not very precise.
     * When activity recover from partly invisible, onBecomesPartiallyInvisible will be triggered.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!mFirstResume) {
            onBack();
        }
        if (mFirstResume) {
            mFirstResume = false;
        }
        Logger.d("onResume");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Logger.d("onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d("onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Logger.d("onPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("onDestroy");
        mComponentContainer.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.d("onDetach");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.d("onCreateView");
        // TODO
        return createView(inflater,container,savedInstanceState);
    }

}
