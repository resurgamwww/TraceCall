package com.github.resurgamwww.tracecall.action;

import com.intellij.openapi.util.Computable;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;

import java.util.ArrayList;
import java.util.List;

public class CallerFinder {

    public static List<PsiMethod> findCallers(PsiMethod method) {
        List<PsiMethod> callers = new ArrayList<>();
        ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();

        Query<PsiReference> search = ReferencesSearch.search(method);
        for (PsiReference reference : search) {
            // 检查进度指示器是否已取消
            if (indicator != null && indicator.isCanceled()) {
                break;
            }

            PsiElement element = reference.getElement();
            PsiMethod caller = PsiTreeUtil.getParentOfType(element, PsiMethod.class);
            if (caller != null) {
                callers.add(caller);
                callers.addAll(findCallers(caller));
            }
        }

        return callers;
    }
}