package ngds.net.me;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author wangyt
 * @date 2016/12/29
 * : gson工具;int、string、list等类型容错处理
 */

@SuppressWarnings("unchecked")
public class GsonUtils {
    private static Gson norGson;
    private static Gson lenientGson;
    /**
     * string容错反序列化类
     */
    private static JsonDeserializer<String> stringJsonDeserializer = new JsonDeserializer<String>() {
        @Override
        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsString();
            } catch (Exception e) {
                return "";
            }
        }
    };
    /**
     * int容错反序列化类
     */
    private static JsonDeserializer<Integer> intJsonDeserializer = new JsonDeserializer<Integer>() {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsInt();
            } catch (Exception e) {
                return -1;
            }
        }
    };
    /**
     * long容错反序列化类
     */
    private static JsonDeserializer<Long> longJsonDeserializer = new JsonDeserializer<Long>() {
        @Override
        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsLong();
            } catch (Exception e) {
                return -1L;
            }
        }
    };
    /**
     * float容错反序列化类
     */
    private static JsonDeserializer<Float> floatJsonDeserializer = new JsonDeserializer<Float>() {
        @Override
        public Float deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsFloat();
            } catch (Exception e) {
                return -1F;
            }
        }
    };
    /**
     * double容错反序列化类
     */
    private static JsonDeserializer<Double> doubleJsonDeserializer = new JsonDeserializer<Double>() {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return json.getAsDouble();
            } catch (Exception e) {
                return -1D;
            }
        }
    };
    /**
     * list容错反序列化类
     */
    private static JsonDeserializer<List<?>> listJsonDeserializer = new JsonDeserializer<List<?>>() {
        @Override
        public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                try {
                    JsonArray array = json.getAsJsonArray();
                    Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                    List list = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        JsonElement element = array.get(i);
                        Object item = context.deserialize(element, itemType);
                        list.add(item);
                    }
                    return list;
                } catch (Exception e) {
                    //若列表item解析错误，直接返回空列表
                    return Collections.EMPTY_LIST;
                }
            } else {
                return Collections.EMPTY_LIST;
            }
        }
    };

    /**
     * 获取普通Gson
     */
    public static Gson getGson() {
        if (norGson != null) {
            return norGson;
        }
        GsonBuilder norBuilder = new GsonBuilder();
        //几种关键字过滤，使其不序列化
        norBuilder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        norGson = norBuilder.create();
        return norGson;
    }

    /**
     * 获取有容错性的Gson
     */
    public static Gson getLenientGson() {
        if (lenientGson != null) {
            return lenientGson;
        }
        GsonBuilder lenientBuilder = new GsonBuilder();
        lenientGson = wrapLenientGson(lenientBuilder)
                //几种关键字过滤，使其不序列化
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();
        return lenientGson;
    }

    /**
     * 包装成容错builder
     *
     * @param builder GsonBuilder
     */
    public static GsonBuilder wrapLenientGson(GsonBuilder builder) {
        if (builder == null) {
            builder = new GsonBuilder();
        }
        //宽松模式下解析
        return builder.setLenient()
                .registerTypeAdapter(int.class, intJsonDeserializer)
                .registerTypeAdapter(long.class, longJsonDeserializer)
                .registerTypeAdapter(float.class, floatJsonDeserializer)
                .registerTypeAdapter(double.class, doubleJsonDeserializer)
                .registerTypeAdapter(String.class, stringJsonDeserializer)
                .registerTypeHierarchyAdapter(List.class, listJsonDeserializer);
    }

    /**
     * 获取 <T> 的实现类类型
     *
     * @param subclass 具体类
     */
    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterize = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterize.getActualTypeArguments()[0]);
    }
}
