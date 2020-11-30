package up.mi.sgbdr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class BufferManager {

    private ArrayList<Frame> frames = new ArrayList<>();

    public ArrayList<Frame> getFrames() {
        return frames;
    }

    private static BufferManager INSTANCE;
    private String info = "Buffer Manager";

    private BufferManager() {
        for (int i = 0; i < DBParams.frameCount; i++) {
            frames.add(new Frame());
        }
    }

    public static BufferManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BufferManager();
        }

        return INSTANCE;
    }

    /**
     * Renvoie un byteBuffer qui correspond aux données de la page passée en paramètres.
     * Cette fonction s'occupe d'attribuer aux frames du buffer des pages et de les remplacer selon LRU.
     *
     * @param pageID - PageID qui représente la page à renvoyer sous forme de buffer
     */
    public byte[] GetPage(PageID pageID) {
        /*
        Cette méthode doit répondre à une demande de page venant des couches plus hautes, et donc
        retourner un des buffers associés à une frame.
        Le buffer sera rempli avec le contenu de la page désignée par l’argument pageId.
        Attention : ne pas créer de buffer supplémentaire, « récupérer » simplement celui qui
        correspond à la bonne frame, après l’avoir rempli si besoin par un appel au DiskManager.
                Attention aussi : cette méthode devra s’occuper du remplacement du contenu d’une frame si
        besoin (donc politique de remplacement).
        */

        // Copie des frames pour trier la collection et en sortir la date la plus lointaine
        ArrayList<Frame> copy = new ArrayList<>(this.frames);
        // Trie de la collection
        Collections.sort(copy);
        // Sort le premier element, le plus petit (donc la plus petite date = la plus vieille)
        LocalDateTime LRU = copy.get(0).getUnpinned();

        // On parcourt toutes les frames
        for (Frame frame : this.frames) {

            // Si le BM contient déjà la page, récupérer le buffer courant
            if (frame.getPageID() == pageID) {
                frame.incrementPinCount();
                return frame.buffer;
            }

            // Si le BM ne contient pas déjà la page, faire simplement un DiskManager.getInstance().ReadPage(pageID)
            if (frame.getPageID() == null) {
                frame.setPageID(pageID);
                frame.incrementPinCount();
                DiskManager.getInstance().ReadPage(pageID, frame.buffer);
                return frame.buffer;
            }

            // Si le BM est plein, politique de remplacement (LRU)
            if (frame.getUnpinned() == LRU && frame.getPinCount() == 0) {
                // Ecrire la page si dirty vaut 1
                if (frame.isDirty()) {
                    DiskManager.getInstance().WritePage(pageID, frame.buffer);
                }
                // Réinitialise la frame
                frame.resetFrame();
                frame.setPageID(pageID);
                frame.incrementPinCount();
                DiskManager.getInstance().ReadPage(pageID, frame.buffer);
                return frame.buffer;
            }
        }

        // Retourne null en cas d'erreur
        // TODO - Exception si le buffer est plein et en cours d'utilisation.
        return null;

    }

    /**
     * Décrémente le pinCount et actualise le dirty de la page passée en paramètres.
     * Actualise le flag unpinned si nécessaire pour la politique de remplacement.
     *
     * @param pageID - PageID qui représente la page
     */
    public void FreePage(PageID pageID, boolean dirty) {

        // On parcourt toutes les frames
        for (Frame frame : this.frames) {
            if (frame.getPageID() == pageID) {
                // Décrémente le pinCount de la case dans laquelle se trouve pageID
                frame.setPinCount(frame.getPinCount() - 1);
                // Actualiser le unpinned si pinCount = 0
                if (frame.getPinCount() == 0){
                    frame.setUnpinned(LocalDateTime.now());
                }
                frame.setDirty(dirty);
                break;
            }
        }

    }

    /**
     * Libère toutes les frames du Buffer en écrivant sur le disque les pages modifiées.
     */
    public void FlushAll() {
        for (Frame frame : this.frames) {
            // Ecrire toutes les pages dont le flag dirty vaut 1
            if (frame.isDirty()){
                DiskManager.getInstance().WritePage(frame.getPageID(), frame.buffer);
            }
            // Remise à zéro de tous les flags/informations et contenus des buffers
            frame.resetFrame();
        }
    }

    /**
     * Permet d'afficher le contenu de chaque frame dans la console.
     */
    public void DisplayFrames() {
        for (Frame frame : this.frames) {
            System.out.println("----------");
            System.out.println("PageID : " + frame.getPageID());
            System.out.println("Pin Count : " + frame.getPinCount());
            System.out.println("Dirty : " + frame.isDirty());
            System.out.println("Unpinned : " + frame.getUnpinned());
            System.out.println("----------");
        }
    }

}
