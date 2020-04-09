package com.yiqulius.hellocode;

/**
 * @author yiqulius
 */
public class MainContract {

    interface View extends BaseView<Presenter> {
        void showView(String data);
    }

    interface Presenter extends BasePresenter {
        void loadData(int position);
    }
}
