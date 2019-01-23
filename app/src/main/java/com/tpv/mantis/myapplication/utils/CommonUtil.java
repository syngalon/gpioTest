package com.tpv.mantis.myapplication.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    public static void inputWatch(final Context context, final EditText input) {
        new TextWatcher() {
            private String outStr = ""; //这个值存储输入超过两位数时候显示的内容

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String edit = s.toString();
                if (edit.length() == 2 && Integer.parseInt(edit) >= 10) {
                    outStr = edit;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String words = s.toString(); //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) { //0-255的正则验证
                    Pattern p = Pattern.compile("^([0-9]|([1-9]\\d)|(1\\d\\d)|(2([0-4]\\d|5[0-5])))$");
                    Matcher m = p.matcher(words);
                    if (m.find() || ("").equals(words)) { //这个时候输入的是合法范围内的值
                    } else {
                        if (words.length() > 2) { //若输入不合规，且长度超过2位，继续输入只显示之前存储的outStr
                            input.setText(outStr); //重置输入框内容后默认光标位置会回到索引0的地方，要改变光标位置
                            input.setSelection(2);
                        }
                        ToastUtil.showToast(context,"请输入范围在0-255之间的整数");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) { //这里的处理是不让输入0开头的值
                String words = s.toString(); //首先内容进行非空判断，空内容（""和null）不处理
                if (!TextUtils.isEmpty(words)) {
                    if (Integer.parseInt(s.toString()) <= 0) {
                        input.setText("");
                        ToastUtil.showToast(context, "请输入范围在0-255之间的整数");
                    }
                }
            }
        };
    }

}
