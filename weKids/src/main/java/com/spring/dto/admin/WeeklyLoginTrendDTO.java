package com.spring.dto.admin;

import lombok.Data;

@Data
public class WeeklyLoginTrendDTO {

    private String dayLabel;
    private int loginCount;

    public String getDayLabel() {
        return dayLabel;
    }

    public void setDayLabel(String dayLabel) {
        this.dayLabel = dayLabel;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }
}