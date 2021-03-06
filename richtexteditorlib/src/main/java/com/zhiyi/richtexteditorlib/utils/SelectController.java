package com.zhiyi.richtexteditorlib.utils;

import com.zhiyicx.common.utils.log.LogUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Jliuer
 * @Date 18/01/22 10:19
 * @Email Jliuer@aliyun.com
 * @Description  双状态选择控制器，保证其中的选择是互斥的
 */
@SuppressWarnings("WeakerAccess")
public class SelectController {
    private ArrayList<Long> stateAList;
    private ArrayDeque<Long> stateBList;

    private StatesTransHandler handler;
    private int num;

    public static SelectController createController() {
        return new SelectController();
    }

    private SelectController() {
        num = 1;
        stateBList = new ArrayDeque<>(num);
        stateAList = new ArrayList<>();
    }

    public SelectController add(long id) {
        stateAList.add(id);
        return this;
    }

    public SelectController addAll(Long... id) {
        Collections.addAll(stateAList, id);
        return this;
    }

    public SelectController setStateBNum(int num) {
        this.num = num;
        return this;
    }

    public void changeState(long id) {
        long temp;
        if (stateAList.contains(id)) {
            stateAList.remove(id);
            if (num > 0 && stateBList.size() >= num) {
                temp = stateBList.poll();
                stateAList.add(temp);
                if (handler != null) {
                    handler.handleB2A(temp);
                }
            }
            stateBList.add(id);
            if (handler != null) {
                handler.handleA2B(id);
            }
        } else if (stateBList.contains(id)) {
            stateBList.remove(id);
            stateAList.add(id);
            if (handler != null) {
                handler.handleB2A(id);
            }
        }

    }

    public void reset() {
        moveAll2StateA();
    }

    private void moveAll2StateA() {
        while (!stateBList.isEmpty()) {
            long temp = stateBList.poll();
            stateAList.add(temp);
            handler.handleB2A(temp);
        }
    }

    public boolean contain(long id) {
        return stateAList.contains(id) || stateBList.contains(id);
    }

    public void setHandler(StatesTransHandler handler) {
        this.handler = handler;
    }


    public interface StatesTransHandler {
        void handleA2B(long id);

        void handleB2A(long id);
    }

    @SuppressWarnings("unused")
    public abstract class StatesTransAdapter implements StatesTransHandler {
        @Override
        public void handleA2B(long id) {
            LogUtils.d("handleA2B", id + "");
        }

        @Override
        public void handleB2A(long id) {
            LogUtils.d("handleB2A", id + "");
        }
    }
}
