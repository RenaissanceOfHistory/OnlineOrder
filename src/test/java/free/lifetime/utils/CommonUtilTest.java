package free.lifetime.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class CommonUtilTest {

    @Test
    void require() {
        Assert.assertThrows(Throwable.class, () -> CommonUtil.require(Throwable.class, false, "A error happens"));
        Assert.assertThrows(Exception.class, () -> CommonUtil.require(Exception.class, false, "A error happens"));
        Assert.assertThrows(RuntimeException.class, () -> CommonUtil.require(RuntimeException.class, false, "A error happens"));
        Assert.assertThrows(Error.class, () -> CommonUtil.require(Error.class, false, "A error happens"));
    }
}