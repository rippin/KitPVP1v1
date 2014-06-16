package rippin.bullyscraft.com;


public enum ArenaState {
VACANT ("Vacant"), OCCUPIED("Occupied"), STARTING("Starting"), ENDING("Ending"), DISABLED("Disabled");

    private String state;

    ArenaState(String state){
        this.state = state;
    }

    public String toString(){
        return state;
    }
}
