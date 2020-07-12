package unit.com.ciutz.kitikat.entities;

import com.ciutz.kitikat.entities.User;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class UserUnitTest {

    @Test
    public void newObjectHasNullAttributesForNewObjectTest() {
        User target = new User();
        Assert.assertNull(target.getName());
        Assert.assertNull(target.getEmail());
    }

    @Test
    public void setNameTest() {
        User target = new User();

        target.setName("Ciutz");
        String expected = "Ciutz";
        String actual = target.getName();

        Assert.assertEquals("Name is not the same", expected, actual);
    }

    @Test
    public void setIdTest() {
        User target = new User();

        target.setId(1L);
        long expected = 1L;
        long actual = target.getId();

        Assert.assertEquals("Id is not the same", expected, actual);
    }

    @Test
    public void setEmailTest() {
        User target = new User();

        target.setEmail("ciutz@ciutz.com");
        String expected = "ciutz@ciutz.com";
        String actual = target.getEmail();

        Assert.assertEquals("Email is not the same", expected, actual);
    }

    @Test
    public void setBirthdateTest() {
        User target = new User();

        target.setBirthdate("05.03.1991");
        String expected = "05.03.1991";
        String actual = target.getBirthdate();

        Assert.assertEquals("Birtdate is not the same", expected, actual);
    }

    @Test
    public void setRaceTest() {
        User target = new User();

        target.setRace("main coon");
        String expected = "main coon";
        String actual = target.getRace();

        Assert.assertEquals("Race is not the same", expected, actual);
    }

    @Test
    public void setHobbiesTest() {
        User target = new User();

        target.setHobbies("eating, sleeping");
        String expected = "eating, sleeping";
        String actual = target.getHobbies();

        Assert.assertEquals("Hobbies are not the same", expected, actual);
    }

    @Test
    public void setImgSrcTest() {
        User target = new User();

        target.setImgSrc("http://test.com/image.png");
        String expected = "http://test.com/image.png";
        String actual = target.getImgSrc();

        Assert.assertEquals("ImgSrcs are not the same", expected, actual);
    }

    @Test
    public void setPasswordTest() throws NoSuchFieldException, IllegalAccessException {
        User target = new User();

        target.setPassword("pass");
        String expected = "pass";

        /** print the list of fields **/
        Class  userClass = User.class;
        Field[] fields = userClass.getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f.getName() + " of type " + f.getType() + " and has modifiers " + f.getModifiers());
        }

        // obtain a field of a class by name
        Field field = userClass.getDeclaredField("password");

        //make this field accessible (not private anymore in this context)
        field.setAccessible(true);

        // get the value of the field
        String actual = (String) field.get(target);

        Assert.assertEquals("Passwords are not the same", expected, actual);
    }
}
