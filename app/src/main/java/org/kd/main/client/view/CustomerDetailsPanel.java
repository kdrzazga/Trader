package org.kd.main.client.view;

import org.kd.main.client.presenter.PresenterHandler;

class CustomerDetailsPanel {

    private static PresenterHandler handler;

    public static void setHandler(PresenterHandler handler) {
        CustomerDetailsPanel.handler = handler;
    }
}