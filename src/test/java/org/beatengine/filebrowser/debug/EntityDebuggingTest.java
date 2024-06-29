package org.beatengine.filebrowser.debug;

import org.beatengine.filebrowser.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigInteger;

public class EntityDebuggingTest {

    @Test
    public void debugEntityTest()
    {
        User user = new User();
        user.setId(5L);
        user.setPictureId(7L);
        user.setEmail("mail@test.de");
        user.setSha256Pass("38473hrj4j%&/&(Df78efs");
        user.setDisplayName("Tester Test");

        final String debug = user.toString();
        Assertions.assertEquals("{\"class\":\"User\", \"object\": { \"id\": \"5\" , \"email\": \"mail@test.de\" , \"displayName\": \"Tester Test\" , \"sha256Pass\": \"38473hrj4j%&/&(Df78efs\" , \"pictureId\": \"7\" , \"roles\": null, \"orders\": null, \"ratings\": null}}", debug);
    }

}
