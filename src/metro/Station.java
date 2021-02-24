package metro;

import jade.core.AID;

import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;

public class Station {


    private String stationName;
    private int stationNumber;
    public ArrayList<Plataform> plataforms;
    // in case both trains are at both plataforms Station is full
    private boolean stationFull;
    // train interval in minutes
    private int trainFrequency;

    public int getTrainFrequency() {
        return trainFrequency;
    }

    public void setTrainFrequency(int trainFrequency) {
        this.trainFrequency = trainFrequency;
    }

    public boolean isStationFull() {
        return stationFull;
    }

    public void setStationFull(boolean stationFull) {
        this.stationFull = stationFull;
    }


    public Station(int sNum, int pNum) {

        this.plataforms = new ArrayList<Plataform>();

        this.stationNumber = sNum;
        for(int i = 0;i < pNum; i++){
            plataforms.add(new Plataform(i));
        }

    }

    public AID getTrainID(int plataformNumber){
        return plataforms.get(plataformNumber).getTrainID();
    }

    public void setTrainID(AID trainID, int plataformNumber){
        plataforms.get(plataformNumber).setTrainID(trainID);
    }

    public String getStationName() {
        return stationName;
    }


    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    // retorna o index da primeira plataforma livre
    public int getFreePlataform(){

        for(int i = 0;i < plataforms.size();i++){
            if(this.plataforms.get(i).getPlataformStatus() == 0)
                return i;
        }
        return 2;
        // retorna 2 se nao plataformas livres
    }
    // 0 = Livre, 1 = ocupada, 2 = desabilitada
    public void setPlataformStatus(int index, int num){
        this.plataforms.get(index).setPlataformStatus(num);
    }

    private class Plataform{

        private int plataformID;
        private AID trainID;

        Plataform(int num){
            this.plataformID = num;
        }

        public AID getTrainID() {
            return trainID;
        }

        public void setTrainID(AID trainID) {
            this.trainID = trainID;
        }

        public int getPlataformStatus() {
            return plataformStatus;
        }

        public void setPlataformStatus(int plataformStatus) {
            this.plataformStatus = plataformStatus;
        }

        private int plataformStatus = 0;

        public int getPlataformID() {
            return plataformID;
        }

        public void setPlataformID(int plataformID) {
            this.plataformID = plataformID;
        }
    }



}
