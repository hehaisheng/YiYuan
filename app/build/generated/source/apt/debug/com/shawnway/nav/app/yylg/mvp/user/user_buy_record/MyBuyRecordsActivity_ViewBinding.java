// Generated code from Butter Knife. Do not modify!
package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.view.TabButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyBuyRecordsActivity_ViewBinding<T extends MyBuyRecordsActivity> implements Unbinder {
  protected T target;

  private View view2131559169;

  private View view2131559514;

  private View view2131559516;

  private View view2131559515;

  @UiThread
  public MyBuyRecordsActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.top_back, "field 'topBack' and method 'onClick'");
    target.topBack = Utils.castView(view, R.id.top_back, "field 'topBack'", ImageButton.class);
    view2131559169 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.topBackText = Utils.findRequiredViewAsType(source, R.id.top_back_text, "field 'topBackText'", TextView.class);
    target.topTextCenter = Utils.findRequiredViewAsType(source, R.id.top_text_center, "field 'topTextCenter'", TextView.class);
    target.topCart = Utils.findRequiredViewAsType(source, R.id.top_cart, "field 'topCart'", TabButton.class);
    target.actionEdit = Utils.findRequiredViewAsType(source, R.id.action_edit, "field 'actionEdit'", TextView.class);
    target.actionGrayCommon = Utils.findRequiredViewAsType(source, R.id.action_gray_common, "field 'actionGrayCommon'", TextView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.tab_all, "field 'tabAll' and method 'onClick'");
    target.tabAll = Utils.castView(view, R.id.tab_all, "field 'tabAll'", TextView.class);
    view2131559514 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tab_annount, "field 'tabAnnount' and method 'onClick'");
    target.tabAnnount = Utils.castView(view, R.id.tab_annount, "field 'tabAnnount'", TextView.class);
    view2131559516 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tab_open, "field 'tabOpen' and method 'onClick'");
    target.tabOpen = Utils.castView(view, R.id.tab_open, "field 'tabOpen'", TextView.class);
    view2131559515 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.framelayoutBuyrecords = Utils.findRequiredViewAsType(source, R.id.framelayout_buyrecords, "field 'framelayoutBuyrecords'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.topBack = null;
    target.topBackText = null;
    target.topTextCenter = null;
    target.topCart = null;
    target.actionEdit = null;
    target.actionGrayCommon = null;
    target.toolbar = null;
    target.tabAll = null;
    target.tabAnnount = null;
    target.tabOpen = null;
    target.framelayoutBuyrecords = null;

    view2131559169.setOnClickListener(null);
    view2131559169 = null;
    view2131559514.setOnClickListener(null);
    view2131559514 = null;
    view2131559516.setOnClickListener(null);
    view2131559516 = null;
    view2131559515.setOnClickListener(null);
    view2131559515 = null;

    this.target = null;
  }
}
