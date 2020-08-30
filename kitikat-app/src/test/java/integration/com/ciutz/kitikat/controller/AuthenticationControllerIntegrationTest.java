package integration.com.ciutz.kitikat.controller;

import com.ciutz.kitikat.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:application-integration.properties")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void loginPageShouldContainLoginTitle() throws Exception {
        this.mockMvc
                .perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Login</title>")));
    }

    @Test
    public void registerPageShouldContainSignUpTitle() throws Exception {
        this.mockMvc
                .perform(get("/register"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Signup</title>")));
    }

    @Test
    public void invalidLoginShouldReturnErrorMessage() throws Exception {
        this.mockMvc
                .perform(post("/login").param("email", "ciutz@ciutz.com").param("password", "ciutz"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span style=\"color: red;\">Invalid login!</span>")));

    }

    @Sql(statements = {"insert into user(id, birthdate, email, hobbies, img_src, name, password, race) " +
            "values(1, '2000-01-01', 'ciutz@ciutz.com', 'h1', " +
            "'https://www.vettedpetcare.com/vetted-blog/wp-content/uploads/2017/09/How-To-Travel-With-a-Super-Anxious-Cat-square.jpeg', '" +
            "ciutz', 'ciutz', 'r1');"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void validLoginShouldRedirectToProfile() throws Exception {
        this.mockMvc
                .perform(post("/login").param("email", "ciutz@ciutz.com").param("password", "ciutz"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/profile"));

    }

    @Test
    public void profilePageShouldContainLoginTitleForInvalidSession() throws Exception {
        this.mockMvc
                .perform(get("/profile"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/login"));
    }

    @Sql(statements = {"insert into user(id, birthdate, email, hobbies, img_src, name, password, race) " +
            "values(2, '2000-01-01', 'ciutz2@ciutz2.com', 'h1', " +
            "'https://www.vettedpetcare.com/vetted-blog/wp-content/uploads/2017/09/How-To-Travel-With-a-Super-Anxious-Cat-square.jpeg', '" +
            "ciutz2', 'ciutz2', 'r1');"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void profilePageShouldContainProfileTitleForValidSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        this.mockMvc
                .perform(post("/login").param("email", "ciutz2@ciutz2.com").param("password", "ciutz2").session(session))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/profile"));

        this.mockMvc
                .perform(get("/profile").session(session))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Profile page</title>")))
                .andExpect(content().string(containsString("<span>ciutz2@ciutz2.com</span>")));
    }

    @Test
    public void invalidRegisterShouldReturnErrorMessage() throws Exception {
        this.mockMvc
                .perform(post("/register").param("email", "").param("password", "").param("name", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span style=\"color:red;\">email, name and password are required</span>")));

    }

    @Sql(statements = {"delete from user;"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Test
    public void validRegisterShouldRedirectToProfile () throws Exception {
        this.mockMvc
                .perform(post("/register").param("email", "ciutz3@ciutz3.com").param("password", "ciutz3").param("name", "ciutz"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/profile"));
    }
}
