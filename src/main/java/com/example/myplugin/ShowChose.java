package com.example.myplugin;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
import java.sql.Array;
import java.util.Arrays;

import static com.example.myplugin.DialogPlugin.getTargetClass;

/**
 * @author: Mr.You
 * @create: 2022-10-12 13:26
 * @description:
 **/
public class ShowChose  extends JDialog{

    private JPanel panelContent;
    private JButton commitButton;
    private JLabel nameTextField;
    private JLabel classNameLabel;
    private JList list1;

    ShowChose(AnActionEvent event){
        /**获取当前文件**/
        PsiFile psiFile = event.getData(CommonDataKeys.PSI_FILE);
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        /**获取当前文件的Class类**/
        PsiClass targetClass = getTargetClass(editor, psiFile);
        /**获取实体类的所有成员属性**/
        PsiField[] allFields = targetClass.getAllFields();
        /**设置弹窗标题**/
       setTitle(new String("添加GET和SET方法".getBytes(),StandardCharsets.UTF_8));
       /**设置弹窗内容**/
       setContentPane(panelContent);
       /**设置弹窗显示类名**/
       classNameLabel.setText(targetClass.getQualifiedName());
       nameTextField.setText(new String("当前类名:".getBytes(),StandardCharsets.UTF_8));
        String [] list = new String[allFields.length];
        for (int i = 0; i < allFields.length; i++) {
            list[i] = allFields[i].getName();
        }
        list1.setListData(list);
        /**设置弹窗显示大小**/
       setSize(600,400);

       final Toolkit toolkit = Toolkit.getDefaultToolkit();
       final Dimension screenSize = toolkit.getScreenSize();
       final int x = (screenSize.width - getWidth()) / 2;
       final int y = (screenSize.height - getHeight()) / 2;
        /**设置弹窗位置**/
       setLocation(x, y);
        /**点击提交按钮生成代码**/
       commitButton.addActionListener(new AbstractAction() {
           @Override
           public void actionPerformed(ActionEvent actionEvent) {
               insertCode(allFields,editor);
               hide();
           }
       });
   }
    public void insertCode(PsiField[] psiField,Editor editor){
        for (int i = 0; i < psiField.length; i++) {
            PsiField field = psiField[i];
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("\n    public "+field.getTypeElement().getText()
                    +" get"+field.getName().substring(0,1).toUpperCase()
                    +field.getName().substring(1)+"() {\n" +
                    "        return "+field.getName()+";\n" +
                    "    }\n" +
                    "\n" +
                    "    public void set"+field.getName().substring(0,1).toUpperCase()
                    +field.getName().substring(1)+"("+
                    field.getTypeElement().getText()+" "+field.getName()+") {\n" +
                    "        this."+field.getName()+" = "+field.getName()+";\n" +
                    "    }");

            Document document = editor.getDocument();
            Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
            int start = primaryCaret.getSelectionStart();
            int end = primaryCaret.getSelectionEnd();
            WriteCommandAction.runWriteCommandAction(editor.getProject(), () ->
                    document.insertString(start,stringBuffer.toString())
            );
            primaryCaret.removeSelection();
        }
    }



}
