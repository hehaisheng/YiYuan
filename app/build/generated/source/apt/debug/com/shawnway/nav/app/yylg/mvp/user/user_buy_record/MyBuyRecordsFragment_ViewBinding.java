// Generated code from Butter Knife. Do not modify!
package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.shawnway.nav.app.yylg.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyBuyRecordsFragment_ViewBinding<T extends MyBuyRecordsFragment> implements Unbinder {
  protected T target;

  @UiThread
  public MyBuyRecordsFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.rvMybuyRecords = Utils.findRequiredViewAsType(source, R.id.rv_mybuy_records, "field 'rvMybuyRecords'", RecyclerView.class);
    target.swipeRefresh = Utils.findRequiredViewAsType(source, R.id.swipe_refresh, "field 'swipeRefresh'", SwipeRefreshLayout.class);
    target.loadingViewStub = Utils.findRequiredViewAsType(source, R.id.loading_view_stub, "field 'loadingViewStub'", ViewStub.class);
    target.noDataViewStub = Utils.findRequiredViewAsType(source, R.id.nodata_view_stub, "field 'noDataViewStub'", ViewStub.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rvMybuyRecords = null;
    target.swipeRefresh = null;
    target.loadingViewStub = null;
    target.noDataViewStub = null;

    this.target = null;
  }
}
