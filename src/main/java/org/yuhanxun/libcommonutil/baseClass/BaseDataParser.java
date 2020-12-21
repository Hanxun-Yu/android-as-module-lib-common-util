package org.yuhanxun.libcommonutil.baseClass;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

import org.yuhanxun.libcommonutil.file.FileRW;

/**
 * Created by Crashxun dbstar-mac on 2017/1/12.
 */

public class BaseDataParser {
    public <T> BaseJson<T> getJSONObj(String json, final Type clazz) {
        Gson gson = new Gson();
        T t = null;
        try {
            t = gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new BaseJson<T>(t, json);
    }

    public <T> BaseJson<T> getJSONObjFromFile(String jsonFilePath, final Type clazz) {
        String content = FileRW.fileToString(jsonFilePath);
        if (content != null) {
            BaseJson<T> baseJson = getJSONObj(content, clazz);
            return baseJson;
        } else {
            return null;
        }
    }

//    public <T> void getJSONObj(String url, String json, final Type clazz, final BaseDispatcherHandler<T> handler) {
//        BaseJson<T> baseJson = getJSONObj(json, clazz);
//        if (handler != null) {
//            handler.onCommonSuccess(url, json, baseJson.t);
//        }
//    }
//
//    public <T> void getJSONObjFromFile(String jsonFilePath, final Type clazz, final BaseDispatcherHandler<T> handler) {
//        String content = FileRW.fileToString(jsonFilePath);
//        if (content != null) {
//            BaseJson<T> baseJson = getJSONObj(content, clazz);
//            if (handler != null) {
//                handler.onCommonSuccess(jsonFilePath, content, baseJson.t);
//            }
//        } else {
//            if (handler != null) {
//                handler.onCommonFail(jsonFilePath, new FileNotFoundException("no json file"));
//            }
//        }
//    }

//

    public class BaseJson<T> {
        public T t;
        public String jsonContent;

        BaseJson(T t, String jsonContent) {
            this.t = t;
            this.jsonContent = jsonContent;
        }
    }
}
