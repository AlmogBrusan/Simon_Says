package com.example.simonsaysproject;

public class UserScore
{
    private String name;
    private int score;
    private String level;

    public UserScore(){}

    public UserScore(String name, int score , String level)
    {
        this.name = name;
        this.score = score;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
