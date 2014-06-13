package rippin.bullyscraft.com;


public enum ArenaState {
VACANT ("VACANT"), OCCUPIED("OCCUPIED"), STARTING("STARTING"), ENDING("ENDING");

    private String state;

    ArenaState(String state){
        this.state = state;
    }

    public String toString(){
        return state;
    }
}
