package client;

import com.google.gson.Gson;
import model.AuthData;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import service.RegisterRequest;


public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(int port){
        serverUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var path = "/user";
        var request = new RegisterRequest(username, password, email);
        return makeRequest("POST", path, request, AuthData.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        URL url = (new URI(serverUrl + path)).toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);

        http.setDoOutput(true);

        String reqData = new Gson().toJson(request);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }

        http.connect();

        InputStream respBody = http.getInputStream();
        InputStreamReader reader = new InputStreamReader(respBody);

        T response = new Gson().fromJson(reader, responseClass);
        return response;

    }
}
