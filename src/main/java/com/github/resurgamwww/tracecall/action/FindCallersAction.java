package com.github.resurgamwww.tracecall.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

public class FindCallersAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);

        if (element instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element;

//            // 获取 ToolWindow

//            if (toolWindow != null) {
//                toolWindow.show(() -> {
//
//                });
//            }

            // 使用 ProgressManager 包装耗时操作
            ProgressManager.getInstance().run(new Task.Backgroundable(project, "Finding Callers", true) {
                @Override
                public void run(@NotNull ProgressIndicator indicator) {
                    // 在后台线程中执行耗时操作
//                    List<PsiMethod> callers = ApplicationManager.getApplication().runReadAction((Computable<List<PsiMethod>>) () -> CallerFinder.findCallers(method));
                    List<PsiMethod> callers = CallerFinder.findCallers(method);

                    // 回到 UI 线程更新 ToolWindow
                    ApplicationManager.getApplication().invokeLater(() -> {
                        CallerTreeToolWindow callerTreeToolWindow = new CallerTreeToolWindow(project);
                        DefaultMutableTreeNode root = new DefaultMutableTreeNode(method.getName());
                        buildTree(root, method, callers);
                        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("CallerTreeToolWindow");
                        callerTreeToolWindow.showCallerTree(root);
//                        toolWindow.getContentManager().getContent(0).setComponent(callerTreeToolWindow.getContent());
                    });
                }
            });
        }
    }

    @Override
    public void update(AnActionEvent e) {
        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);
        e.getPresentation().setEnabledAndVisible(element instanceof PsiMethod);
    }

    private void buildTree(DefaultMutableTreeNode parent, PsiMethod method, List<PsiMethod> callers) {
        for (PsiMethod caller : callers) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(caller);
            parent.add(node);
//             递归构建树
            List<PsiMethod> callerCallers = CallerFinder.findCallers(caller);
            buildTree(node, caller, callerCallers);
        }
    }
}