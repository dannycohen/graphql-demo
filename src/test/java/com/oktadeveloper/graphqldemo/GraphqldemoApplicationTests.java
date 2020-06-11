package com.oktadeveloper.graphqldemo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {GraphqldemoApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
class GraphqldemoApplicationTests {

    @LocalServerPort
    int localServerPort;

    @Autowired
    MockMvc mockMvc;

   @Test
    void listFoods() throws Exception {
        String expectedResponse = "{\"data\":{\"foods\":[" +
                "{\"id\":1,\"name\":\"Pizza\",\"isGood\":true}," +
                "{\"id\":2,\"name\":\"Spam\",\"isGood\":false}," +
                "{\"id\":3,\"name\":\"Eggs\",\"isGood\":true}," +
                "{\"id\":4,\"name\":\"Avocado\",\"isGood\":false}" +
                "]}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/graphql")
                .content("{\"query\":\"{ foods { id name isGood } }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse))
                .andReturn();
    }

    @Test
    public void get_all_foods_returns_as_expected() throws Exception {

        String fileName = "getAllFoods.graphql";
        String query = getResourceAsString(fileName);

        HttpResponse<String> response = sendToGraphQL(query);

        System.out.println(response.statusCode());


        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(mapper.readTree(response.body()));

    }

    private HttpResponse<String> sendToGraphQL(String query) throws IOException, InterruptedException {
       try {

           String payload = constructGraphQLPayload(query);

           HttpClient client = HttpClient.newBuilder()
                   .version(HttpClient.Version.HTTP_1_1)
                   .build();

           HttpRequest request = HttpRequest.newBuilder()
                   .uri(getGraphQLUri())
                   .header("Content-Type", MediaType.APPLICATION_JSON.toString())
                   .POST(HttpRequest.BodyPublishers.ofString(payload))
                   .build();

           return client.send(request, HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));

       } catch (IOException | InterruptedException ex){
           throw new RuntimeException(ex);
       }
    }

    private String constructGraphQLPayload(String query) {
       try {
           HashMap<String, String> map = new HashMap<>();
           map.put("query", query);

           ObjectMapper mapper = new ObjectMapper();
           return mapper.writeValueAsString(map);
       } catch (JsonProcessingException ex) {
           throw new RuntimeException(ex);
       }
    }

    public String getResourceAsString(String resourceFileName) {
        try {
            Resource resource = new ClassPathResource(resourceFileName);

            InputStream input = resource.getInputStream();

            String text = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            return text;

        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }


    @Test
    public void resource_file_can_be_read_as_string(){
        String s = this.getResourceAsString("curl.sh");
        assertTrue(s.length() > 0);
    }

    @Test
    public void resource_file_does_not_exist_throws_exception(){

        assertThrows(RuntimeException.class, () -> {
            getResourceAsString("does-not-exist");
        } );
    }


    public URI getGraphQLUri(){

        String protocol = "http";
        String host = "localhost";
        int port = localServerPort;
        String path = "/graphql";

        try {

            URI url = new URL(protocol, host, port, path, null).toURI();
            return url;

        } catch (URISyntaxException | MalformedURLException ex){
            String constructedURI = String.format("%s://%s:%d%s", protocol,host,port,path);
            throw new RuntimeException(constructedURI,ex);
        }
    }

}