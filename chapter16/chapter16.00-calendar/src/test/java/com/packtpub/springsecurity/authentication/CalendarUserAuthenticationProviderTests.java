package com.packtpub.springsecurity.authentication;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Calendar;
import java.util.List;

import com.packtpub.springsecurity.CalendarApplication;
import com.packtpub.springsecurity.domain.CalendarUser;
import com.packtpub.springsecurity.domain.Event;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 *
 * @author Rob Winch
 * @author Mick Knutson
 *
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CalendarApplication.class)
@AutoConfigureMockMvc

//@WithUserDetails("user1@example.com")

// TODO:
//@WebMvcTest(controllers = EventController.class)
//@AutoConfigureMockMvc(secure=false)
public class CalendarUserAuthenticationProviderTests {


    @Autowired
    private CalendarUserAuthenticationProvider authenticationProvider;

    @Test
    public void validateUser_User() {
        String username = "user1@example.com";
        String password = "user1";


        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication result = authenticationProvider.authenticate(authRequest);
        CalendarUser user = (CalendarUser)result.getPrincipal();

        assertThat(user.getEmail()).isEqualTo(username);
        assertThat(user.getRoles().size()).isEqualTo(1);
    }

} // The End...
