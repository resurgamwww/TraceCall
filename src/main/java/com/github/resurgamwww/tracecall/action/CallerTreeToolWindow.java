package com.github.resurgamwww.tracecall.action;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiMethod;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

public class CallerTreeToolWindow {

    private final Project project;
    private JPanel contentPanel;
    private Tree tree;

    public CallerTreeToolWindow(Project project) {
        this.project = project;
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
    }

    public JPanel getContent() {
        return contentPanel;
    }

    public void showCallerTree(DefaultMutableTreeNode root) {


        tree = new Tree(new DefaultTreeModel(root));
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node != null && node.getUserObject() instanceof PsiMethod) {
                PsiMethod selectedMethod = (PsiMethod) node.getUserObject();
                selectedMethod.navigate(true); // 跳转到对应位置
            }
        });

        contentPanel.removeAll();
        contentPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }


}