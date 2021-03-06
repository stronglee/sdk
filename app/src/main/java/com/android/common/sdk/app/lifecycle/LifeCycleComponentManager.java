package com.android.common.sdk.app.lifecycle;

import java.util.HashMap;
import java.util.Map.Entry;

public class LifeCycleComponentManager implements IComponentContainer {

    private HashMap<String, LifeCycleComponent> mComponentList;

    public LifeCycleComponentManager() {
    }

    public static boolean addComponentToContainer(LifeCycleComponent component,
                                                     Object matrixContainer, boolean throwEx) {
        if (matrixContainer instanceof IComponentContainer) {
            ((IComponentContainer) matrixContainer).addComponent(component);
            return true;
        } else {
            if (throwEx) {
                throw new IllegalArgumentException("componentContainerContext "
                        + "should implements IComponentContainer");
            }
            return false;
        }
    }

    public void addComponent(LifeCycleComponent component) {
        if (component != null) {
            if (mComponentList == null) {
                mComponentList = new HashMap<String,LifeCycleComponent>();
            }
            mComponentList.put(component.toString(), component);
        }
    }

    public void onBecomesVisibleFromTotallyInvisible() {
        if (mComponentList == null) {
            return;
        }
        for (Entry<String, LifeCycleComponent> stringLifeCycleComponentEntry : mComponentList.entrySet()) {
            LifeCycleComponent component = stringLifeCycleComponentEntry.getValue();
            if (component != null) {
                component.onBecomesVisibleFromTotallyInvisible();
            }
        }
    }

    public void onBecomesTotallyInvisible() {
        if (mComponentList == null) {
            return;
        }
        for (Entry<String, LifeCycleComponent> stringLifeCycleComponentEntry : mComponentList.entrySet()) {
            LifeCycleComponent component = stringLifeCycleComponentEntry.getValue();
            if (component != null) {
                component.onBecomesTotallyInvisible();
            }
        }
    }

    public void onBecomesPartiallyInvisible() {
        if (mComponentList == null) {
            return;
        }
        for (Entry<String, LifeCycleComponent> stringLifeCycleComponentEntry : mComponentList.entrySet()) {
            LifeCycleComponent component = stringLifeCycleComponentEntry.getValue();
            if (component != null) {
                component.onBecomesPartiallyInvisible();
            }
        }
    }

    public void onBecomesVisibleFromPartiallyInvisible() {
        if (mComponentList == null) {
            return;
        }
        for (Entry<String, LifeCycleComponent> stringLifeCycleComponentEntry : mComponentList.entrySet()) {
            LifeCycleComponent component = stringLifeCycleComponentEntry.getValue();
            if (component != null) {
                component.onBecomesVisible();
            }
        }
    }

    public void onDestroy() {
        if (mComponentList == null) {
            return;
        }
        for (Entry<String, LifeCycleComponent> stringLifeCycleComponentEntry : mComponentList.entrySet()) {
            LifeCycleComponent component = stringLifeCycleComponentEntry.getValue();
            if (component != null) {
                component.onDestroy();
            }
        }
    }
}
