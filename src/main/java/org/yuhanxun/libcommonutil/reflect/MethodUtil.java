package org.yuhanxun.libcommonutil.reflect;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuhanxun
 * 2018/5/2
 */
public class MethodUtil {
    final String TAG = "MethodUtil";

    public void invokeMethod(Object listener, String methodName, Object[] params) {
        Method method = getMethod(listener, methodName, params);
        if (method != null) {
            try {
                method.invoke(listener, params);
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "IllegalAccessException", e);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "IllegalArgumentException", e);
            } catch (InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "InvocationTargetException", e);
            }
        } else {
//            System.out.println("METHOD NULL!");
        }
    }

    Map<String, Method> methodCache = new HashMap<>();

    private Method getMethodCache(String methodName) {
        return methodCache.get(methodName);
    }

    private void saveMethodCache(String methodName, Method method) {
        methodCache.put(methodName, method);
    }

    private boolean checkMethod(Method method, String methodName, Object[] params) {
        boolean ret = false;
        if (method.getName().equals(methodName) && method.getParameterTypes().length == params.length) {
//            System.out.println("find method:" + method.getName());
            Class<?>[] types = method.getParameterTypes();
            if (isMethodParameterMatch(types, params)) {
                ret = true;
            }
        }
        return ret;
    }

    public Method getMethod(Object listener, String methodName, Object[] params) {
        Method ret = null;

//        Method ret = getMethodCache(methodName);
//        if (ret != null && checkMethod(ret, methodName, params)) {
//            return ret;
//        }

        Method[] methods = listener.getClass().getMethods();
        for (Method method : methods) {
            if (checkMethod(method, methodName, params)) {
                ret = method;
                saveMethodCache(methodName, method);
                break;
            }
        }
        return ret;
    }

    private boolean isMethodParameterMatch(Class<?>[] methodParams, Object[] params) {
        boolean ret = true;
        for (int i = 0; i < methodParams.length; i++) {
            if (isBasicType(params[i])) {
//                System.out.println("---------------------");
//                System.out.print("isBasicType ");
//                System.out.print("matching " + methodParams[i] + " :: " + getType(params[i].getClass()));
                if (methodParams[i] != getType(params[i].getClass())) {
//                    System.out.println(" false");
                    ret = false;
                    break;
                } else {
//                    System.out.println(" true");
                }
            } else {
//                System.out.print("notBasicType ");
//                System.out.print("matching " + methodParams[i] + " :: " + params[i].getClass());
                if (methodParams[i] != params[i].getClass() && !methodParams[i].isInstance(params[i])) {
//                    System.out.println("!= && isInstance false");
                    ret = false;
                    break;
                } else {
//                    System.out.println(" true");
                }

            }
        }
        return ret;
    }

    private boolean isBasicType(Object obj) {
        if (obj instanceof Boolean ||
                obj instanceof Integer ||
                obj instanceof Float ||
                obj instanceof Double ||
                obj instanceof Byte ||
                obj instanceof Short ||
                obj instanceof Long ||
                obj instanceof Character)
            return true;
        else
            return false;
    }

    private Object getType(Class c) {
        Field f;
        Object obj = null;

        try {
            f = c.getDeclaredField("TYPE");
            obj = f.get(c);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
