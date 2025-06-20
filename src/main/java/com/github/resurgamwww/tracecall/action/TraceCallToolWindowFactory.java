package com.github.resurgamwww.tracecall.action;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import javax.swing.*;

public class TraceCallToolWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        CallerTreeToolWindow callerTreeToolWindow = new CallerTreeToolWindow(project);

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(callerTreeToolWindow.getContent(), "TraceCall", false);
        toolWindow.getContentManager().addContent(content);
    }
}
