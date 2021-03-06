package model;

import com.sun.javafx.geom.Vec2d;
import javafx.scene.image.ImageView;
import view.PorteView;

public class Porte extends Bloc {
    public PorteView vue;
    private boolean locked;

    Porte(int i, int j) {
        position = new Vec2d(i, j);
        locked = true;
        vue = new PorteView();
    }

    boolean isLocked() {
        return locked;
    }

    void unLock() {
        locked = false;
        vue.ouvrirPorte();
    }

    @Override
    public ImageView getView() {
        return vue;
    }
}
