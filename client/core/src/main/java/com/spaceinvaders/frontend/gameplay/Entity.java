package com.spaceinvaders.frontend.gameplay;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.spaceinvaders.backend.utils.Coordinate;

import java.util.ArrayList;

public interface Entity {
    public void render(Batch batch, ArrayList<Coordinate> coordinateList);
}
