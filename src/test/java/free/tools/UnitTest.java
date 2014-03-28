package free.tools;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import free.tools.Lecture;

public class UnitTest {
    Lecture lire;
    public UnitTest() {
    }

    @Before
    public void setUp() throws Exception {
        lire = new Lecture();
    }

    @Test 
    public void test() {
        assertEquals(lire.sayHello(),"Hello");
        //fail("Not yet implemented");
    }

    @Test 
    public void testEscapeBr() {
        String s = "<p>Hello</p><br/><br/><p>world</p>";
        String regex = "/(<br[^>]*>[ \\n\\r\\t]*){2,}/i";
        s = s.replaceAll(regex,"<p></p>");
        assertEquals(s, "<p>Hello</p><p></p><p>world</p>");
    }
}
