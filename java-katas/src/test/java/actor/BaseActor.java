package actor;

public abstract class BaseActor implements IActor {
    @Override
    public void onBeforeTest(Context context) {
        // nop
    }

    @Override
    public void onMessage(Context context, Message message) {
        // nop
    }

    @Override
    public void onAfterTest(Context context) {
        // nop
    }

    @Override
    public void onTest(Context context) {
        // nop
    }

    @Override
    public final void __dont_implement_IActor_extend_BaseActor() {
        throw new RuntimeException("Don't call me!");
    }
}
