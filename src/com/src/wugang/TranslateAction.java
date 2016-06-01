package com.src.wugang;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.util.TextUtils;

import java.awt.*;
import java.net.URLEncoder;

/**
 * Created by lwg on 2016/6/1.
 */
public class TranslateAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor data = e.getData(PlatformDataKeys.EDITOR);
        if (data == null)
            return;
        SelectionModel selectionModel = data.getSelectionModel();
        final String selectedText = selectionModel.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        String baseUrl = "http://fanyi.youdao.com/openapi.do?keyfrom=Skykai521&key=977124034&type=data&doctype=json&version=1.1&q=";
        HttpUtils.doAsyncGet(baseUrl +  URLEncoder.encode(selectedText), new HttpUtils.OnRequestListener() {
            @Override
            public void onRequestCompleted(String result) {
                com.src.wugang.Translate translate = new Gson().fromJson(result, com.src.wugang.Translate.class);
                StringBuilder sb = new StringBuilder();
                sb.append(selectedText).append(":").append(list2String(translate.translation, ","));
                if (translate.basic != null) {
                    sb.append("<br/>").append("美式：").append(translate.basic.us_phonetic)
                            .append(";\t").append("英式：").append(translate.basic.uk_phonetic);
                    sb.append("<br/>").append(list2String(translate.basic.explains, "<br/>"));
                    sb.append("网络释义：");
                    for (int i = 0; i < translate.web.size(); i++) {
                        Translate.Web web = translate.web.get(i);
                        sb.append(web.key).append(":").append(list2String(web.value, ",")).append("\n");
                    }
                }
                sb.append("<br/>.");
                showPopupBalloon(data, sb.toString());
            }
        });
    }

    String list2String(java.util.List<String> stringList, String split) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            sb.append(stringList.get(i)).append(split);
        }
        return sb.toString();
    }

    private void showPopupBalloon(final Editor editor, final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(editor), Balloon.Position.below);
            }
        });
    }

}
