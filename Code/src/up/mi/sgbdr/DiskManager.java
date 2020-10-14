package up.mi.sgbdr;

import java.nio.ByteBuffer;

public class DiskManager {

    private static DiskManager INSTANCE;
    private String info = "Initial info class";

    private DiskManager() {
    }

    public static DiskManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DiskManager();
        }

        return INSTANCE;
    }

    public void CreateFile(int fileIdx) {
        //Cette méthode crée (dans le sous-dossier DB) un fichier Data_fileIdx.rf initialement
        //vide.



        //Votre SGBD stockera chaque relation dans un fichier (→ fichier « normal », au sens OS).
        //Ces fichiers s’appelleront Data_x.rf, avec x entier >=0 l’identifiant du fichier : Data_ .rf ₀ , Data_ .rf ₁
        //etc. (rf veut dire « relation file » ; c’est une extension que nous inventons pour ce TP : ) )
        //Tous ces fichiers seront placés dans votre sous-répertoire DB.
        //Pour lire et écrire dans ces fichiers, vous allez utiliser des méthodes de lecture/écriture dans les
        //fichiers binaires.
    }

    public PageID AddPage(int fileIdx) {

        //Cette méthode rajoute une page au fichier spécifié par fileIdx (c’est à dire, elle rajoute
        //pageSize octets, avec une valeur quelconque, à la fin du fichier) et retourne un PageId
        //correspondant à la page nouvellement rajoutée !
        //À vous de comprendre comment remplir ce PageId !

        //Pour éviter les erreurs /!\ à modifier
        return new PageID(fileIdx, 1);
    }

    public void ReadPage(PageID pageId, ByteBuffer buff) {
        //Cette méthode doit remplir l’argument buff avec le contenu disque de la page identifiée par
        //l’argument pageId.
        //Attention : c’est l’appelant de cette méthode qui crée et fournit le buffer à remplir!
    }

    public void WritePage(PageID pageId, ByteBuffer buff) {
        //écrit le contenu de l’argument buff dans le fichier et à la
        //position indiqués par l’argument pageId.
    }

}
