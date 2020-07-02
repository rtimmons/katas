package actor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class ContextLoaderTest {

    @Test
    public void testCreateSimple() {
        ContextLoader loader = new ContextLoader();
        Context ctx = loader.load(ImmutableMap.of(
            "actors", ImmutableList.of(
                ImmutableMap.of(
                    "type",     "PrintActor",
                    "prefix",   "yup"
                )
            )
        ));
        ctx.submit(new Message());
    }
}
