package com.moontlik.liozasa.homeup.CoacherTemlate;

/**
 * Created by Sergei on 9/6/2015.
 */
public class CoacherTemplate {

    String subject;
    String group;
    String description;
    String repeat;

    public CoacherTemplate(String subject, String group, String description, String repeat) {
        this.subject = subject;
        this.group = group;
        this.description = description;
        this.repeat = repeat;
    }

    public String getSubject() {
        return subject;
    }

    public String getGroup() {
        return group;
    }

    public String getDescription() {
        return description;
    }

    public String getRepeat() {
        return repeat;
    }
}
