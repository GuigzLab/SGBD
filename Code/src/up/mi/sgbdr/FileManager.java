package up.mi.sgbdr;

import java.util.ArrayList;

public class FileManager {

    private static FileManager INSTANCE;
    private String info = "File Manager";
    private ArrayList<HeapFile> heapFiles = new ArrayList<>();

    private FileManager() {
    }

    public static FileManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FileManager();
        }

        return INSTANCE;
    }

    public void Reset(){
        this.heapFiles.clear();
    }

    public void Init(){
        for (RelationInfo relationInfo: DBInfo.getInstance().list){
            this.heapFiles.add(new HeapFile(relationInfo));
        }
    }

    public void CreateRelationFile(RelationInfo relationInfo){
        HeapFile heapFile = new HeapFile(relationInfo);
        this.heapFiles.add(heapFile);
        heapFile.createNewOnDisk();
    }

    public Rid InsertRecordInRelation (Record record, String relName){
        /*
        * ex: dans la methode InsertRecordInRelation (relation en entrée + record à inserer))
    ->chercher la relation dans la liste des relations
    (chaque relation est référée dans chaque heapfile)
       parcourir la liste des heapfiles
        si HeapFile.getRelation=relation(la relation en entrée)
            on apelle HeapFile.InsertRecord (codé dans le tp5)
dans chaque methode on fait appel à ce qu'on a fait dans le tp5"
        * */
        for (HeapFile heapFile: this.heapFiles){
            if (heapFile.getRelationInfo().getName() == relName){
                return heapFile.InsertRecord(record);
            }
        }
        return null;
    }

    public ArrayList<Record> SelectAllFromRelation(String relName){

        for (HeapFile heapFile: this.heapFiles){
            if (heapFile.getRelationInfo().getName() == relName){
                return heapFile.GetAllRecords();
            }
        }

        return null;
    }


}
