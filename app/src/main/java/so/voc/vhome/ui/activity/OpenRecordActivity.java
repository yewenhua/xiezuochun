package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.TimeUtils;

import java.util.Calendar;
import java.util.Date;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityOpenRecordBinding;
import so.voc.vhome.util.AppDateUtil;
import so.voc.vhome.util.CommonUtils;

/**
 * Fun：开门记录
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 18:38
 */
public class OpenRecordActivity extends BaseActivity<ActivityOpenRecordBinding> {

    private TimePickerView pvTime;
    private Calendar startDate;
    private Calendar endDate;
    private String startTime = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_record);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.open_record));

        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();
        startTime = TimeUtils.getNowString();
    }

    public void openClick(View view){
        switch (view.getId()){
            case R.id.ll_openType:

                break;
            case R.id.ll_openTime:
                pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        bindingView.tvOpenTime.setText(AppDateUtil.parseYyyyMmDd(date));
                    }
                })
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setCancelText(CommonUtils.getString(R.string.cancel))
                        .setCancelColor(CommonUtils.getColor(R.color.colorRed))
                        .setSubmitText(CommonUtils.getString(R.string.confirm))
                        .setSubmitColor(CommonUtils.getColor(R.color.colorAccent))
                        .setTitleText(CommonUtils.getString(R.string.open_time))
                        .setTitleSize(16)
                        .setTitleColor(CommonUtils.getColor(R.color.colorInput))
                        .build();
                pvTime.show();
                break;
        }
    }
}
