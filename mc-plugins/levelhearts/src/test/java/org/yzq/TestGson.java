package org.yzq;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

/**
 * @author yanni
 * @date time 2021/11/1 20:30
 * @modified By:
 */
public class TestGson {
    @Test
    public void getGson() {
        UserSimple userObject = new UserSimple(
                "Norman",
                "norman@futurestud.io",
                26,
                true
        );
        Gson gson = new Gson();
        String userJson = gson.toJson(userObject);
        System.out.println(userJson);

    }
}
