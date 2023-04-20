public class mapEvents {
    private String eventType;

    public mapEvents(){
        double randomChance = Math.random() * 100;
        if (randomChance < 100){
            this.eventType = "Choice";
        }
        if (randomChance < 60){
            this.eventType = "Item";
        }
        if (randomChance < 30){
            this.eventType = "Fight";
        }

    }

    public String getEventType() {
        return eventType;
    }
}
