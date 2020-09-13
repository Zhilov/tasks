package com.example.androidsocialnet.Model;

public class Story {

    public Story() {

    }


    private String storyTitle;
    private String storyBody;
    private String storyGroup;
    private String storyUser;

    public Story (String storyTitle, String storyBody, String storyGroup, String storyUser){
        this.storyTitle = storyTitle;
        this.storyBody = storyBody;
        this.storyGroup = storyGroup;
        this.storyUser = storyUser;
    }


    public String getStoryTitle() {
        return storyTitle;
    }

    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    public String getStoryBody() {
        return storyBody;
    }

    public void setStoryBody(String storyBody) {
        this.storyBody = storyBody;
    }

    public String getStoryGroup() {
        return storyGroup;
    }

    public void setStoryGroup(String storyGroup) {
        this.storyGroup = storyGroup;
    }

    public String getStoryUser() {
        return storyUser;
    }

    public void setStoryUser(String storyUser) {
        this.storyUser = storyUser;
    }
}
