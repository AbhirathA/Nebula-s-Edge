package com.spaceinvaders.frontend.utils;

public interface Command
{
    public void execute();

    public boolean update();

    public void onUpdate();
}
