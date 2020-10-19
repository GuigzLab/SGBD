package up.mi.sgbdr;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DiskManager {

    private static DiskManager INSTANCE;
    private String info = "Disk Manager";

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
        //Ces fichiers s’appelleront Data_x.rf, avec x entier >=0 l’identifiant du fichier : Data_ .rf , Data_ .rf
        //etc. (rf veut dire « relation file » ; c’est une extension que nous inventons pour ce TP : ) )
        //Tous ces fichiers seront placés dans votre sous-répertoire DB.
        //Pour lire et écrire dans ces fichiers, vous allez utiliser des méthodes de lecture/écriture dans les
        //fichiers binaires.


        //Crée un fichier dans le sous dossier DB
        try {
            File newFile = new File(DBParams.DBPath +"Data_" + fileIdx + ".rf");
            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    public PageID AddPage(int fileIdx) {

        //Cette méthode rajoute une page au fichier spécifié par fileIdx (c’est à dire, elle rajoute
        //pageSize octets, avec une valeur quelconque, à la fin du fichier) et retourne un PageId
        //correspondant à la page nouvellement rajoutée !
        //À vous de comprendre comment remplir ce PageId !


        //Il faut: le nombre de pages du fichier, multiplier par pagesize
        //Ecrire les octets a la suite
        //avoir l'id de la page et remplacer le 1 par l'id

        //Pour éviter les erreurs /!\ à modifier
        return new PageID(fileIdx, 1);
    }

    public void ReadPage(PageID pageId, ByteBuffer buff) {
        //Cette méthode doit remplir l’argument buff avec le contenu disque de la page identifiée par
        //l’argument pageId.
        //Attention : c’est l’appelant de cette méthode qui crée et fournit le buffer à remplir!

        //RandomAccessFile
        //http://imss-www.upmf-grenoble.fr/prevert/Prog/Java/Flux/RandomAccessFile.html
    }

    public void WritePage(PageID pageId, ByteBuffer buff) {
        //écrit le contenu de l’argument buff dans le fichier et à la
        //position indiqués par l’argument pageId.
    }

}
