package ru.stoliarenkoas.tm.webserver.webservice.rest.client;

import feign.Feign;
import feign.Request;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jaxrs.JAXRSContract;
import feign.okhttp.OkHttpClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

public interface UserRestServiceClient {

    static UserRestServiceClient client() {
        final String baseUrl = "http://localhost:8080/webservice/rs/user/";
        return Feign.builder()
                .contract(new JAXRSContract())
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .options(new Request.Options(50, 50))
                .target(UserRestServiceClient.class, baseUrl);
    }

    @POST
    @Path("/login")
    String login(@QueryParam("login") String login, @QueryParam("password") String password);

}
