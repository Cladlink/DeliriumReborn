package view;

import com.sun.javafx.geom.Vec2d;
import controller.ControllerGame;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import model.*;
import tools.Path;

import java.util.ArrayList;

public class ViewGame {
    //GERE L'AFFICHAGE DU JEU

    public final static int TAILLE_IMAGES = 64;
    private Group root;
    private Partie partie;

    private StackPane plateau;

    /**
     * Constructeur de la vue du jeu
     * @param root : élément de base dans lequel seront ajoutés tous les autres éléments de la vue
     */
    ViewGame(Group root, Partie partie){
        this.root = root;
        this.partie = partie;
        initPlateau();
        rafraichirVue();
    }

    private void initPlateau() {
        root.getChildren().clear();
        plateau = new StackPane();

        ArrayList<ImageView> images = new ArrayList<>();

        // On met le fond partout
        for (int i = 0; i < Niveau.currentLvl.length; i++) {
            for (int j = 0; j < Niveau.currentLvl[i].length; j++) {
                images.add(new ImageView(Path.background));
                images.get(images.size() - 1).setTranslateY(i * TAILLE_IMAGES);
                images.get(images.size() - 1).setTranslateX(j * TAILLE_IMAGES);
            }
        }

        // Les blocs de matière
        for (Bloc bloc : partie.getNiveau().getBlocList())
                images.add(bloc.getView());


        // Les monstres
        for (Monstre monstre : partie.getNiveau().getMonstreList())
            images.add(monstre.vue);


        // Le mineur
        images.add(partie.getNiveau().getMineur().vue);

        plateau.getChildren().addAll(images);
        root.getChildren().clear();
        root.getChildren().add(plateau);
    }

    public void rafraichirVue() {

        // Modfification des coordonnées de l'image en fonction des coordonnées de l'objet
        // On multiplie par la taille des images pour ne pas qu'elles se chevauches
        for (Bloc bloc : partie.getNiveau().getBlocList()) {
            bloc.getView().setTranslateY(bloc.getPosition().x * TAILLE_IMAGES);
            bloc.getView().setTranslateX(bloc.getPosition().y * TAILLE_IMAGES);
        }
        for (Monstre monstre : partie.getNiveau().getMonstreList()) {
            monstre.vue.setTranslateY(monstre.getPosition().x * TAILLE_IMAGES);
            monstre.vue.setTranslateX(monstre.getPosition().y * TAILLE_IMAGES);
        }
        partie.getNiveau().getMineur().vue.setTranslateY(partie.getNiveau().getMineur().getPosition().x * TAILLE_IMAGES);
        partie.getNiveau().getMineur().vue.setTranslateX(partie.getNiveau().getMineur().getPosition().y * TAILLE_IMAGES);

        placerLaCamera();
    }

    private void placerLaCamera() {
        Vec2d windowSize = new Vec2d(root.getScene().getWidth(), root.getScene().getHeight()),
        plateauSize = new Vec2d(Niveau.currentLvl[0].length * TAILLE_IMAGES, Niveau.currentLvl.length * TAILLE_IMAGES);

        double finalTranslateX,finalTranslateY;


        if(windowSize.x < plateauSize.x) {
            finalTranslateX = -partie.getNiveau().getMineur().getPosition().y * TAILLE_IMAGES + windowSize.x/2;
            if(finalTranslateX>0) finalTranslateX = 0;
            if(finalTranslateX<windowSize.x-plateauSize.x) finalTranslateX = windowSize.x-plateauSize.x;
        } else {
            finalTranslateX = (windowSize.x-plateauSize.x)/2;
        }

        if(windowSize.y < plateauSize.y) {
            finalTranslateY = -partie.getNiveau().getMineur().getPosition().x * TAILLE_IMAGES + windowSize.y/2;
            if(finalTranslateY>0) finalTranslateY = 0;
            if(finalTranslateY<windowSize.y-plateauSize.y) finalTranslateY = windowSize.y-plateauSize.y;
        } else {
            finalTranslateY = (windowSize.y-plateauSize.y)/2;
        }

        plateau.setTranslateX(finalTranslateX);
        plateau.setTranslateY(finalTranslateY);
    }

    /**
     * active l'écoute du clavier
     * @param kc (Controlleur du clavier)
     */
    void setEvents(ControllerGame kc) {
        root.getScene().setOnKeyPressed(kc);
        root.getScene().setOnKeyReleased(kc);
    }

    /**
     * Affiche une popup informant le joueur qu'il à gagné
     */
    public void affichageVictoire()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin");
        alert.setHeaderText("Victoire !");
        alert.setContentText("Félicitation, vous avez gagnez !");
        alert.show();
    }

    public void affichageDefaite() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin");
        alert.setHeaderText("Défaite");
        alert.setContentText("Dommage... Vous avez perdu");
        alert.show();
    }
}
