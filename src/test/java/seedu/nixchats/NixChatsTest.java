package seedu.nixchats;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class NixChatsTest {

    @Test
    void hasMainClass() throws Exception {
        Class<?> cls = Class.forName("nixchats.NixChats");
        assertNotNull(cls, "Class nixchats.NixChats should exist on the test classpath");
    }

    @Test
    void mainMethodSignatureIsValid() throws Exception {
        Class<?> cls = Class.forName("nixchats.NixChats");
        Method m = cls.getDeclaredMethod("main", String[].class);
        assertTrue(Modifier.isPublic(m.getModifiers()), "main should be public");
        assertTrue(Modifier.isStatic(m.getModifiers()), "main should be static");
        assertEquals(void.class, m.getReturnType(), "main should return void");
    }
}
