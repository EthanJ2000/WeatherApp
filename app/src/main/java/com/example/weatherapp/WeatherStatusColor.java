package com.example.weatherapp;

public enum WeatherStatusColor
{
    MORNING(R.drawable.morning, R.style.morningOverlay),
    AFTERNOON(R.drawable.afternoon, R.style.afternoonOverlay),
    NIGHT(R.drawable.night, R.style.nightOverlay);

    private int background;
    private int color;

    WeatherStatusColor(int background, int color) {
        this.background = background;
        this.color = color;
    }

    public int getBackground() {
        return background;
    }

    public int getColor() {
        return color;
    }


}