package actor;

public class PrintActor extends BaseActor {

    @Override
    public void onMessage(Context context, Message message) {
        System.out.println(String.format("%s%s", prefix, message));
    }

    private String prefix;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
