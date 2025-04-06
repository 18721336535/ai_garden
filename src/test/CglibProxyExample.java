//package com.example.demo.controller;
//
//import net.sf.cglib.proxy.Enhancer;
//import net.sf.cglib.proxy.MethodInterceptor;
//import net.sf.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.Method;
//
//// 创建具体的类
//class HelloCglib {
//    public void sayHello() {
//        System.out.println("Hello from HelloCglib");
//    }
//}
//
//// 创建拦截器类
//class HelloCglibInterceptor implements MethodInterceptor {
//    @Override
//    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
//        System.out.println("Before method call");
//        Object result = proxy.invokeSuper(obj, args);
//        System.out.println("After method call");
//        return result;
//    }
//}
//
//public class CglibProxyExample {
//    public static void main(String[] args) {
//        Enhancer enhancer = new Enhancer();
//        enhancer.setSuperclass(HelloCglib.class);
//        enhancer.setCallback(new HelloCglibInterceptor());
//
//        HelloCglib proxyInstance = (HelloCglib) enhancer.create();
//        proxyInstance.sayHello();
//    }
//}