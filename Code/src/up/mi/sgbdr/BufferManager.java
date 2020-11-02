package up.mi.sgbdr;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

public class BufferManager {

    private ArrayList<Frame> frames = new ArrayList<>();

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

        // TODO - Ajouter une variable buffer aux frames à la place de ReadPage ?;


        // On crée un byteBuffer vide
        byte[] byteBuffer = new byte[DBParams.pageSize];

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
                frame.setPinCount(frame.getPinCount() + 1);
                break;
            }

            // Si le BM ne contient pas déjà la page, faire simplement un DiskManager.getInstance().ReadPage(pageID)
            if (frame.getPageID() == null) {
                frame.setPageID(pageID);
                frame.setPinCount(frame.getPinCount() + 1);
                break;
            }

            // Si le BM est plein, politique de remplacement (LRU)
            if (frame.getUnpinned() == LRU && frame.getPinCount() < 1) {
                // TODO - Ecrire si dirty vaut 1
                frame.setPinCount(1);
                frame.setDirty(0);
                frame.setUnpinned(LocalDateTime.now().plusYears(1));
                frame.setPageID(pageID);

                break;
            }
        }

        // remplie le byteBuffer avec le contenu de la page
        DiskManager.getInstance().ReadPage(pageID, byteBuffer);

        // Aide visuelle
        System.out.println("LRU : " + LRU);
        for (Frame frame : this.frames) {
            System.out.println("FRAME");
            System.out.println("PageID : " + frame.getPageID());
            System.out.println("Pin Count : " + frame.getPinCount());
            System.out.println("Dirty : " + frame.getDirty());
            System.out.println("Unpinned : " + frame.getUnpinned());
            System.out.println("----------");
        }

        return byteBuffer;
    }

    /**
     * Décrémente le pinCount et actualise le dirty de la page passée en paramètres.
     * Actualise le flag unpinned si nécessaire pour la politique de remplacement.
     *
     * @param pageID - PageID qui représente la page
     */
    public void FreePage(PageID pageID, int dirty) {

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

    public void FlushAll() {
        // TODO - Ecrire toutes les pages dont le flag dirty vaut 1
        // TODO - Remise à zéro de tous les flags/informations et contenus des buffers
    }

}
