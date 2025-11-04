package ua;

import java.io.IOException;

public interface ISimpleHttpClient {
    public String doHttpGet(String http) throws IOException;
}
