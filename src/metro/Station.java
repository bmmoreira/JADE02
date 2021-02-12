package metro;

public class Station {

    public Station(String stationName) {
        this.stationName = stationName;
    }

    private String stationName;
    // Status da plataforma pode ser 1(ocupada) 2(Livre) 0 (Desabitada)
    private int plataformOneStatus;
    private int plataformTwoStatus;
    private int plataformOneCurrentPassengers;
    private int plataformTwoCurrentPassengers;

    public int getPlataformOneCurrentPassengers() {
        return plataformOneCurrentPassengers;
    }

    public void setPlataformOneCurrentPassengers(int plataformOneCurrentPassengers) {
        this.plataformOneCurrentPassengers = plataformOneCurrentPassengers;
    }

    public int getPlataformTwoCurrentPassengers() {
        return plataformTwoCurrentPassengers;
    }

    public void setPlataformTwoCurrentPassengers(int plataformTwoCurrentPassengers) {
        this.plataformTwoCurrentPassengers = plataformTwoCurrentPassengers;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getPlataformOneStatus() {
        return plataformOneStatus;
    }

    public void setPlataformOneStatus(int plataformOneStatus) {
        this.plataformOneStatus = plataformOneStatus;
    }

    public int getPlataformTwoStatus() {
        return plataformTwoStatus;
    }

    public void setPlataformTwoStatus(int plataformTwoStatus) {
        this.plataformTwoStatus = plataformTwoStatus;
    }




}
