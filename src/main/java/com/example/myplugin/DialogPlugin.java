package com.example.myplugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;


public class DialogPlugin extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        new  ShowChose(event).setVisible(true);
    }
















    public static PsiClass getTargetClass( Editor editor,  PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element != null) {
            PsiClass target = PsiTreeUtil.getParentOfType(element, PsiClass.class);

            return target instanceof SyntheticElement ? null : target;
        }
        return null;
    }
}
