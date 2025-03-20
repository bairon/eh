package com.eldritch.model;


public class UserData {
    public UserData(String id, String login, String email, String nickname, String language) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.nickname = nickname;
        this.language = language;
    }

    private String id; // Change to String or UUID

    private String login;

    private String email;

    private String nickname;

    private String language;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
