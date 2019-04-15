package com.company.model.bean;

import java.time.LocalDateTime;

public class Event {

    private int id;
    private String topic;
    private LocalDateTime date;
    private String category;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return topic + ", " + category + ", " + getDate();
    }

    public boolean isUpComming() {
        return date != null && date.isAfter(LocalDateTime.now());
    }

}
