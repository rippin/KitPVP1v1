package rippin.bullyscraft.com;


public enum ArenaState {
VACANT ("VACANT"), OCCUPIED("OCCUPIED"), STARTING("STARTING");

    private String state;

    ArenaState(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }
}
