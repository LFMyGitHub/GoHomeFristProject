package com.example.modulecommon.utils;

import com.example.modulecommon.bean.modulemain.AppBean;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    /**
     * 按照每一页的个数即size，拆分为若干个子list
     *
     * @param appModels 所有的数据
     * @param size      每页显示的个数
     * @return
     */
    public static List<List<AppBean>> getSubList(List<AppBean> appModels, int size) {
        List<List<AppBean>> listArr = new ArrayList<>();
        //获取被拆分的数组个数
        int arrSize = appModels.size() % size == 0 ? appModels.size() / size : appModels.size() / size + 1;
        for (int i = 0; i < arrSize; i++) {
            List<AppBean> sub = new ArrayList<>();
            //把指定索引数据放入到list中
            for (int j = i * size; j <= size * (i + 1) - 1; j++) {
                if (j <= appModels.size() - 1) {
                    sub.add(appModels.get(j));
                }
            }
            listArr.add(sub);
        }
        return listArr;
    }
}
