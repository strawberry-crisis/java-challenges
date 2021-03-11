package com.hazyrain.jvm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class XClassLoader extends ClassLoader {

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = this.decodeBytes(name);
        return defineClass(name, bytes, 0, bytes.length);
    }

    private byte[] decodeBytes(String name) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                name.replace('.', File.separatorChar) + ".xlass");
        byte[] buffer;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int nextValue;
        try {
            while ((nextValue = inputStream.read()) != -1) {
                byteArrayOutputStream.write(255 - nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteArrayOutputStream.toByteArray();
        return buffer;
    }

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        XClassLoader loader = new XClassLoader();
        System.out.println(loader.getClass().getClassLoader().getResource("Hello.xlass"));
        Class<?> clazz = loader.findClass("Hello");
        Object instance = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(instance);
    }
}
