package metro;

import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Station implements Serializable {

    public Station(int sNum, int pNum) {

        this.plataforms = new ArrayList<Plataform>();

        this.stationNumber = sNum;
        for(int i = 0;i < pNum; i++){
            plataforms.add(new Plataform(i));
        }

    }

    private String stationName;
    private int stationNumber;
    public ArrayList<Plataform> plataforms;
    // in case both trains are at both plataforms Station is full
    private boolean stationFull;
    private int trainFrequency; // train interval in minutes
    private int trainDefaultDockTime; // Default stop time for boarding passengers
    private Train dockedTrain;
    private int numberOfPassengers = new Random().nextInt(300)+100;


    public int getNumberOfPassengers(){
        return numberOfPassengers;
    }

    public Train getDockedTrain() {
        return dockedTrain;
    }

    public void setDockedTrain(Train dockedTrain) {
        this.dockedTrain = dockedTrain;
    }

    public int getTrainDefaultDockTime() {
        return trainDefaultDockTime;
    }

    public void setTrainDefaultDockTime(int trainDefaultDockTime) {
        this.trainDefaultDockTime = trainDefaultDockTime;
    }

    public int getTrainDockTime() {
        return trainDockTime;
    }

    public void setTrainDockTime(int trainDockTime) {
        this.trainDockTime = trainDockTime;
    }

    private int trainDockTime; // current stop time for boarding passengers

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


    public AID getTrainID(int plataformNumber){
        return plataforms.get(plataformNumber).getDockedtrainID();
    }

    public void setTrainID(AID trainID, int plataformNumber){
        plataforms.get(plataformNumber).setDockedtrainID(trainID);
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

    private class Plataform implements Serializable{

        private int plataformID;
        private AID dockedtrainID; // current train docked
        private int plataformStatus = 0;
        private long dockTime = System.currentTimeMillis(); // time that train docked

        Plataform(int num){
            this.plataformID = num;
        }

        public AID getDockedtrainID() {
            return dockedtrainID;
        }

        public void setDockedtrainID(AID dockedtrainID) {
            this.dockedtrainID = dockedtrainID;
        }

        public int getPlataformStatus() {
            return plataformStatus;
        }

        public void setPlataformStatus(int plataformStatus) {
            this.plataformStatus = plataformStatus;
        }


        public int getPlataformID() {
            return plataformID;
        }

        public void setPlataformID(int plataformID) {
            this.plataformID = plataformID;
        }
    }



}