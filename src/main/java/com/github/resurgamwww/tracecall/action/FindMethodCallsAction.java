package com.github.resurgamwww.tracecall.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Query;

public class FindMethodCallsAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        PsiMethod selectedMethod = getSelectedMethod(e);
        if (selectedMethod == null) {
            System.out.println("No method selected.");
            return;
        }

        System.out.println("Tracing calls for method: " + selectedMethod.getName());

        traceMethodCalls(selectedMethod, project);

    }

    private PsiMethod getSelectedMethod(AnActionEvent e) {
        PsiElement element = e.getData(CommonDataKeys.PSI_ELEMENT);
        if (element instanceof PsiMethod) {
            return (PsiMethod) element;
        }
        return null;
    }

    private void traceMethodCalls(PsiMethod method, Project project) {
        if (method == null) {
            return;
        }

        Query<PsiReference> references = ReferencesSearch.search(method, GlobalSearchScope.projectScope(project));
        for (PsiReference reference : references) {
            PsiElement element = reference.getElement();

            if (element instanceof PsiReferenceExpression) {
                PsiReferenceExpression referenceExpression = (PsiReferenceExpression) element;
//                System.out.println("Found reference: " + referenceExpression.getText());

                PsiElement parent = referenceExpression.getParent();
                if (parent instanceof PsiMethodCallExpression) {
                    element = parent;
                    // 往上递归,直到找到调用该方法的方法PsiMethod
                    PsiMethod lastMethod = null;
                    while (true) {
                        if (element instanceof PsiMethod callerMethod) {
//                            System.out.println("Caller method: " + callerMethod.getName());
                            lastMethod = callerMethod;
//                            traceMethodCalls(callerMethod, project);
                        }
                        if (element == null || element instanceof PsiClass) {
                            System.out.println("Caller method: " + lastMethod.getName());
                            break;
                        }
                        element = element.getParent();
                    }

                    // 到达类里方法调用的最上层,继续递归
                    traceMethodCalls(lastMethod, project);
                }
            }
        }
    }
}
