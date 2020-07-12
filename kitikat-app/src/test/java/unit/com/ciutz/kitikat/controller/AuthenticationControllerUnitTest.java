package unit.com.ciutz.kitikat.controller;

import com.ciutz.kitikat.controller.AuthenticationController;
import com.ciutz.kitikat.entities.User;
import com.ciutz.kitikat.service.AuthenticationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationControllerUnitTest {

    @Mock
    AuthenticationService authenticationServiceMock;

    @Mock
    Model modelMock;

    @Mock
    HttpSession sessionMock;

    AuthenticationController target;

    User dummyUser;

    /**
     * this method executes before every test
     */
    @Before
    public void beforeEach() {
        // setup mocks
        MockitoAnnotations.initMocks(this);

        // setup target
        target = new AuthenticationController(authenticationServiceMock);

        // setup other objects
        dummyUser = new User();
        dummyUser.setName("Ciutz");
        dummyUser.setEmail("test@test.com");
    }

    @Test
    public void loginTest() {
        String actual = target.login(modelMock, sessionMock);

        Assert.assertEquals("template is not as expected", "login", actual);
    }

    @Test
    public void doLoginSuccessfulTest() {
        // given - predefined existing situation
        Mockito
                .when(authenticationServiceMock.getUserByCredentials(Mockito.anyString(), Mockito.anyObject()))
                .thenReturn(dummyUser);
        Mockito.
                when(modelMock.addAttribute(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(modelMock);

        //because it returns void
        Mockito
                .doNothing()
                .when(sessionMock).setAttribute(Mockito.eq("sessionId"), Mockito.anyObject());

        // when
        String actual = target.doLogin("test@test.com", "test", modelMock, sessionMock);

        // then
        Assert.assertEquals("template is not as expected", "redirect:/profile", actual);

        // then - make sure no undesired side effects happened
        Mockito.verify(authenticationServiceMock, Mockito.times(1)).getUserByCredentials(Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(authenticationServiceMock);

        Mockito.verify(modelMock, Mockito.times(2)).addAttribute(Mockito.anyString(), Mockito.anyObject());
        Mockito.verifyNoMoreInteractions(modelMock);

        Mockito.verify(sessionMock, Mockito.times(1)).setAttribute(Mockito.eq("sessionId"), Mockito.anyObject());
        Mockito.verifyNoMoreInteractions(sessionMock);
    }

    @Test
    public void doLoginFailedTest() {
        // given - predefined existing situation
        Mockito
                .when(authenticationServiceMock.getUserByCredentials(Mockito.anyString(), Mockito.anyObject()))
                .thenReturn(null);
        Mockito.
                when(modelMock.addAttribute(Mockito.eq("error"), Mockito.anyString()))
                .thenReturn(modelMock);

        // when
        String actual = target.doLogin("test@test.com", "test", modelMock, sessionMock);

        // then
        Assert.assertEquals("template is not as expected", "login", actual);

        // then - make sure no undesired side effects happened
        Mockito.verify(authenticationServiceMock, Mockito.times(1)).getUserByCredentials(Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(authenticationServiceMock);

        Mockito.verify(modelMock, Mockito.times(1)).addAttribute(Mockito.eq("error"), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(modelMock);
    }

    @Test
    public void doLogoutExistingSessionTest() throws NoSuchFieldException, IllegalAccessException {
        // obtain a field of a class by name
        Field field = AuthenticationController.class.getDeclaredField("knownUserSessions");

        //make this field accessible (not private anymore in this context)
        field.setAccessible(true);

        // get the private object and modify it so that it contains our dummy session id
        Map<String, String> privateKnownUserSessions = (Map<String, String>) field.get(target);
        privateKnownUserSessions.put("dummy-session-id", "test@test.com");

        Mockito
                .doNothing()
                .when(sessionMock).removeAttribute(Mockito.eq("sessionId"));

        // when
        String actual = target.doLogout("dummy-session-id", modelMock, sessionMock);

        // then
        Assert.assertEquals("template is not as expected", "redirect:/login", actual);
        Assert.assertNull("seesion should have been removed", privateKnownUserSessions.get("dummy-session-id"));

        Mockito.verify(sessionMock, Mockito.times(1)).removeAttribute(Mockito.eq("sessionId"));
        Mockito.verifyNoMoreInteractions(sessionMock);
    }

    @Test
    public void signupTest() {
        String actual = target.signup(modelMock, sessionMock);

        Assert.assertEquals("template is not as expected", "register", actual);
    }
}
