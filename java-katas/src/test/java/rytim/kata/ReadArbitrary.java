package rytim.kata;

import org.junit.Assert;
import org.junit.Test;

public class ReadArbitrary {

    private static class Read4k {
        final int max;
        int written = 0;
        private Read4k(int max) {
            this.max = max;
        }
        int read(int many, int[] into) {
            many = Math.min(many, 4096);
            int i;
            for(i=0; i < many && written < max; i++) {
                into[i] = written++;
            }
            return i;
        }
    }

    private final Read4k rk;
    private final int[] q;

    private int qstart = 0;
    private int qend   = 0;

    private ReadArbitrary(Read4k rk) {
        this.rk = rk;
        this.q = new int[4096];
    }

    private void populate() {
        if (qend > qstart) {
            return;
        }
        if (qend >= 4096) {
            qstart = 0;
            qend = qstart + rk.read(4096, q);
        }
        else {
            qstart = qend;
            qend = qstart + rk.read(4096-qstart, q);
        }
    }

    public int read(int many, int[] buf) {
        return read(many, buf, 0);
    }

    private int remaining() {
        populate();
        return qend - qstart;
    }

    private int read(int many, int[] buf, int bufStart) {
        if (many == 0) {
            return 0;
        }
        int remain = remaining();
        if ( remain == 0 ) {
            return 0;
        }
        int copy = Math.min(many, remain);
        System.arraycopy(
            this.q, this.qstart, buf, bufStart, copy);

        this.qstart = this.qstart + copy;
        return copy +
            read(many - copy, buf, bufStart + copy);
    }

    public static class ReadArbitraryTests {

        @Test public void read10atATime() {
            ReadArbitrary r = new ReadArbitrary(new Read4k(1024));
            int[] buf = new int[12];
            for(int i=0; i< 1024/10; i++) {
                Assert.assertEquals(10, r.read(10, buf));
                Assert.assertEquals((i+1)*10-1, buf[9]);
            }
            Assert.assertEquals(1014, buf[4]);

            Assert.assertEquals(1024 % 10, r.read(10, buf));
            Assert.assertEquals(1020, buf[0]);
            Assert.assertEquals(1021, buf[1]);
            Assert.assertEquals(1022, buf[2]);
            Assert.assertEquals(1023, buf[3]);

            // from previous read
            Assert.assertEquals(1014, buf[4]);
        }
        @Test public void read4099atATime() {
            ReadArbitrary r = new ReadArbitrary(new Read4k(4099));
            int[] buf = new int[4099];
            Assert.assertEquals(4099, r.read(4099, buf));
            Assert.assertEquals(4099-1, buf[buf.length-1]);

            Assert.assertEquals(0, r.read(4099, buf));
            Assert.assertEquals(0, r.read(0, buf));
        }
    }

    public static class Read4KTests {
        @Test public void test4kPlus1() {
            Read4k rk = new Read4k(4097);
            int[] buf = new int[1024];
            Assert.assertEquals(0, rk.read(0, buf));

            Assert.assertEquals(1024, rk.read(1024, buf));
            Assert.assertEquals(1024, rk.read(1024, buf));
            Assert.assertEquals(1024, rk.read(1024, buf));
            Assert.assertEquals(1024, rk.read(1024, buf));

            Assert.assertEquals(1, rk.read(1024, buf));
            Assert.assertEquals(0, rk.read(1024, buf));
            Assert.assertEquals(0, rk.read(1024, buf));
        }
        @Test public void test4kPlus1Read256atOnce() {
            Read4k rk = new Read4k(4097);
            int[] buf = new int[256];
            for(int i=0; i < 16; i++) {
                Assert.assertEquals(256, rk.read(256, buf));
            }
            Assert.assertEquals(1, rk.read(256, buf));
            Assert.assertEquals(0, rk.read(256, buf));
            Assert.assertEquals(0, rk.read(256, buf));
        }
    }

}
