package com.displays.view;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Browser {

    final WebView browser;
    final WebEngine webEngine;



    public Browser(WebView webView) {
        browser = webView;
        webEngine = browser.getEngine();
        webEngine.load("https://maps.google.com");

    }
}
