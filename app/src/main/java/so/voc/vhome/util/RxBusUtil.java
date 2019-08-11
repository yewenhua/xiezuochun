package so.voc.vhome.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/11 10:51
 */
public class RxBusUtil {

    public static final String TAG = RxBusUtil.class.getSimpleName();

    private static RxBusUtil instance;
    private static boolean DEBUG = false;

    public static RxBusUtil getInstance() {
        if (instance == null){
            synchronized (RxBusUtil.class){
                if (instance == null){
                    instance = new RxBusUtil();
                }
            }
        }
        return instance;
    }

    private RxBusUtil(){

    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz){
        List<Subject> subjects = subjectMapper.get(tag);
        if (null == subjects){
            subjects = new ArrayList<>();
            subjectMapper.put(tag, subjects);
        }

        Subject<T> subject;
        subjects.add(subject = PublishSubject.create());
        if (DEBUG) {
            Log.d(TAG, "[register]subjectMapper: " + subjectMapper);
        }
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable){
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects){
            if (observable != null && subjects.contains(observable)){
                subjects.remove((Subject)observable);
            }

            if (isEmpty(subjects)) {
                subjectMapper.remove(tag);
            }
        }
        if (DEBUG){
            Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper);
        }
    }

    public void post(@NonNull Object content){
        post(content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content){
        List<Subject> subjects = subjectMapper.get(tag);

        if (!isEmpty(subjects)){
            for (Subject subject : subjects){
                subject.onNext(content);
            }
        }

        if (DEBUG){
            Log.d(TAG, "[send]subjectMapper: " + subjectMapper);
        }
    }

    private boolean isEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }
}
