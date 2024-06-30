package org.beatengine.filebrowser.websocket.protocol;

import java.util.HashMap;
import java.util.Map;

public class HTMLComponentMessage {

    private String id;

    private Boolean request;

    private Boolean response;

    private String html;

    private String version = "1.0.0";

    private Map<String, String> props = new HashMap<>();

    public HTMLComponentMessage() {
    }

    public HTMLComponentMessage(String id, String html, Boolean request, Boolean response) {
        this.id = id;
        this.html = html;
        this.request = request;
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Boolean getRequest() {
        return request;
    }

    public void setRequest(Boolean request) {
        this.request = request;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }
}
