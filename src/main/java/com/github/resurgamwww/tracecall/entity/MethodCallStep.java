package com.github.resurgamwww.tracecall.entity;

/**
 * @author wanghaitong02
 * @date 2025/1/11 20:52
 */
public class MethodCallStep {

    private String className;

    private String methodName;

    private String methodSignature;

    private String lineNumber;

    private MethodCallStep parent;
}
