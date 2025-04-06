//package com.example.demo.controller;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//
//// 定义接口
//interface Hello {
//    void sayHello();
//}
//
//// 实现接口的具体类
//class HelloImpl implements Hello {
//    @Override
//    public void sayHello() {
//        System.out.println("Hello from HelloImpl");
//    }
//}
//
//// 创建处理器类
//class HelloInvocationHandler implements InvocationHandler {
//    private Object target;
//
//    public HelloInvocationHandler(Object target) {
//        this.target = target;
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        System.out.println("Before method call");
//        Object result = method.invoke(target, args);
//        System.out.println("After method call");
//        return result;
//    }
//}
//
//public class JavaDynamicProxyExample {
//    public static void main(String[] args) {
//        HelloImpl helloImpl = new HelloImpl();
//        Hello proxyInstance = (Hello) Proxy.newProxyInstance(
//                HelloImpl.class.getClassLoader(),
//                HelloImpl.class.getInterfaces(),
//                new HelloInvocationHandler(helloImpl)
//        );
//
//        proxyInstance.sayHello();
//    }
//}