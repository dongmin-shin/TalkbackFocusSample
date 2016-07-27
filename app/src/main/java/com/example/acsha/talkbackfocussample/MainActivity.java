package com.example.acsha.talkbackfocussample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;

/**
 * Talkback이 활성화 됐을 경우, Swipe를 통해 button1 -> button2 -> button3 -> button1 로 포커싱이 가도록 처리하기 위한 Sample 클래스
 * button4는 button3의 nextItem 이며, button3에서 button4로 swipe 됐을 경우 button1로 재포커싱 되도록 처리하기 위해 존재하는 View 이다.
 * <p>
 * 실제로 해당 로직을 스무스하게 처리하기 위해서는 화면에 표시되지 않지만 포커싱이 가능한 더미 형태의 View를 만들어서 제어하면 원활한 처리가 가능하리라 생각된다.
 */
public class MainActivity extends AppCompatActivity {


    private Button button1, button2, button3;
    private Button button4;

    public View.AccessibilityDelegate accessibilityDelegate = new View.AccessibilityDelegate() {

        private boolean buttonClicked = false;

        @Override
        public void onInitializeAccessibilityEvent(View host, AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            Log.d("TEST", "[" + getButtonName(host) + "] onInitializeAccessibilityEvent eventType(" + getEventType(event.getEventType()) + ")");

            // 버튼4를 클릭했을 경우 클릭 상태를 저장
            if (host.getId() == R.id.btn_4) {
                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
                    buttonClicked = true;
                    return;
                } else if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_HOVER_EXIT) {
                    buttonClicked = false;
                    return;
                }
            }

            // 버튼4가 클릭 되지 않고, Swipe를 통해 포커싱이 되었을 경우 Button1로 재포커싱이 되도록 요청
            if (!buttonClicked && host.getId() == R.id.btn_4 && event.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                button1.requestFocus();
                return;
            }

            // 각 View가 포커싱 됐을 때 requestFocusing 되도록 함
            if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
                host.requestFocus();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button) findViewById(R.id.btn_1);
        button2 = (Button) findViewById(R.id.btn_2);
        button3 = (Button) findViewById(R.id.btn_3);
        button4 = (Button) findViewById(R.id.btn_4);

        // 순회 요구사항이 button1 -> button2 -> button3 -> button1 ... 인 경우
        button1.setAccessibilityDelegate(accessibilityDelegate);
        button2.setAccessibilityDelegate(accessibilityDelegate);
        button3.setAccessibilityDelegate(accessibilityDelegate);
        button4.setAccessibilityDelegate(accessibilityDelegate);
    }

    private String getButtonName(View host) {
        if (host.getId() == R.id.btn_1) {
            return "Button1";
        } else if (host.getId() == R.id.btn_2) {
            return "Button2";
        } else if (host.getId() == R.id.btn_3) {
            return "Button3";
        } else if (host.getId() == R.id.btn_4) {
            return "Button4";
        }

        return "";
    }

    private String getEventType(int eventType) {
        String retValue = "";

        if (eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) {
            retValue = "TYPE_VIEW_ACCESSIBILITY_FOCUSED";

        } else if (eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED) {
            retValue = "TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED";

        } else if (eventType == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            retValue = "TYPE_VIEW_FOCUSED";

        } else if (eventType == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
            retValue = "TYPE_VIEW_HOVER_ENTER";

        } else if (eventType == AccessibilityEvent.TYPE_VIEW_HOVER_EXIT) {
            retValue = "TYPE_VIEW_HOVER_EXIT";

        }

        return retValue + "(" + eventType + ")";
    }
}
