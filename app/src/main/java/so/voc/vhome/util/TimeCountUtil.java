package so.voc.vhome.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.Button;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/1 11:24
 */
public class TimeCountUtil extends CountDownTimer {

    private Activity activity;
    private Button btn;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, Button btn) {
        super(millisInFuture, countDownInterval);
        this.activity = mActivity;
        this.btn = btn;
    }

    @SuppressLint("NewApi")
    @Override
    public void onTick(long millisUntilFinished) {
        btn.setClickable(false);
        btn.setText(millisUntilFinished / 1000 + " 秒");

        Spannable span = new SpannableString(btn.getText().toString());
        btn.setText(span);
    }

    @SuppressLint("NewApi")
    @Override
    public void onFinish() {
        btn.setText("获取验证码");
        btn.setClickable(true);
    }
}
