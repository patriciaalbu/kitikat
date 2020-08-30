package unit.com.ciutz.kitikat.service;

import com.ciutz.kitikat.entities.User;
import com.ciutz.kitikat.repository.UserRepository;
import com.ciutz.kitikat.service.AuthenticationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceUnitTest {

    @Mock
    UserRepository userRepositoryMock;

    AuthenticationService target;
    User dummyUser;

    /**
     * this method executes before every test
     */
    @Before
    public void beforeEach() {
        // setup mocks
        MockitoAnnotations.initMocks(this);

        // setup target
        target = new AuthenticationService(userRepositoryMock);

        // setup other objects
        dummyUser = new User();
        dummyUser.setName("Ciutz");
        dummyUser.setEmail("test@test.com");
    }

    @Test
    public void getUserByCredentialsWhenUserExistsInDBTest() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.findByEmailAndPassword(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(dummyUser));

        // when - trigger the desired action
        User actual = target.getUserByCredentials("test@test.com", "test");

        // then - assert the side effects
        Assert.assertEquals("name not the same", "Ciutz", actual.getName());

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByEmailAndPassword(Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void getUserByCredentialsWhenUserDoesNotExistInDBTest() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.findByEmailAndPassword("test@test.com", "test"))
                .thenReturn(Optional.empty());

        // when - trigger the desired action
        User actual = target.getUserByCredentials("test@test.com", "test");

        // then - assert the side effects
        Assert.assertEquals("null was expected", null, actual);

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByEmailAndPassword(Mockito.anyString(), Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void getRegisterUser() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.save(Mockito.any(User.class)))
                .thenReturn(dummyUser);

        // when - trigger the desired action
        User actual = target.registerUser("test@test.com", "test", "name");

        // then - assert the side effects
        Assert.assertEquals("name not the same", "Ciutz", actual.getName());
        Assert.assertEquals("email not the same", "test@test.com", actual.getEmail());

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void getUserByEmailWhenUserExistsInDBTest() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(dummyUser));

        // when - trigger the desired action
        User actual = target.getUserByEmail("test@test.com");

        // then - assert the side effects
        Assert.assertEquals("test@test.com", actual.getEmail());

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByEmail(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void getUserByEmailWhenUserDoesNotExistInDBTest() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        // when - trigger the desired action
        User actual = target.getUserByEmail("test@test.com");

        // then - assert the side effects
        Assert.assertEquals("null was expected", null, actual);

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByEmail(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void updateUserTest() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.save(Mockito.any(User.class)))
                .thenReturn(dummyUser);

        // when
        User actual = target.updateUser(dummyUser);

        // then - assert the side effects
        Assert.assertEquals("name not the same", "Ciutz", actual.getName());

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).save(Mockito.any(User.class));
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void userExistsTest() {
        // given - predefined existing situation
        Mockito
                .when(userRepositoryMock.findByEmail(Mockito.any(String.class)))
                .thenReturn(Optional.of(dummyUser));

        // when
        boolean actual = target.userExists("a@a.com");

        // then - assert the side effects
        Assert.assertTrue("user should exist", actual);

        // then - make sure no undesired side effects happened
        Mockito.verify(userRepositoryMock, Mockito.times(1)).findByEmail(Mockito.any(String.class));
        Mockito.verifyNoMoreInteractions(userRepositoryMock);
    }
}
