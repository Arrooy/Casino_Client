package Model;

import Controlador.Sounds;

public class AssetManager {

    public static void loadData() {
        System.out.println("[LOADER]: " + Baralla.loadContent() + " images.");
        /*System.out.println("50% data loaded");*///TODO:millorar amb splash screen
        System.out.println("[LOADER]: " + Sounds.loadAllSounds() + " sound clips");
    }
}
