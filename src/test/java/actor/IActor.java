package actor;

public interface IActor {
    void onBeforeTest(Context context);
    void onMessage(Context context, Message message);
    void onAfterTest(Context context);
    void onTest(Context context);

    void __dont_implement_IActor_extend_BaseActor();
}
