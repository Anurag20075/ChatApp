package com.chat_app.request;

public class UpdateuserRequest {

    private String full_name;
    private String profile_pic;

    public UpdateuserRequest(String full_name, String profile_pic) {
        this.full_name = full_name;
        this.profile_pic = profile_pic;
    }
    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public String getProfile_pic() {
        return profile_pic;
    }
    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
