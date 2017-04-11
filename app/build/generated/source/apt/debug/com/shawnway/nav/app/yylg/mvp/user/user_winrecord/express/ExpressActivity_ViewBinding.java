// Generated code from Butter Knife. Do not modify!
package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.express;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
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

public class ExpressActivity_ViewBinding<T extends ExpressActivity> implements Unbinder {
  protected T target;

  private View view2131559169;

  private View view2131559546;

  @UiThread
  public ExpressActivity_ViewBinding(final T target, View source) {
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
    target.expressDate = Utils.findRequiredViewAsType(source, R.id.express_date, "field 'expressDate'", TextView.class);
    target.expressOrderId = Utils.findRequiredViewAsType(source, R.id.express_orderId, "field 'expressOrderId'", TextView.class);
    target.expressCompany = Utils.findRequiredViewAsType(source, R.id.express_company, "field 'expressCompany'", TextView.class);
    target.expressCustomerService = Utils.findRequiredViewAsType(source, R.id.express_customer_service, "field 'expressCustomerService'", TextView.class);
    target.expressRecevier = Utils.findRequiredViewAsType(source, R.id.express_recevier, "field 'expressRecevier'", TextView.class);
    target.expressRecevierAddress = Utils.findRequiredViewAsType(source, R.id.express_recevier_address, "field 'expressRecevierAddress'", TextView.class);
    view = Utils.findRequiredView(source, R.id.express_ensure_receipt, "field 'expressEnsureReceipt' and method 'onClick'");
    target.expressEnsureReceipt = Utils.castView(view, R.id.express_ensure_receipt, "field 'expressEnsureReceipt'", Button.class);
    view2131559546 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.layoutMwinExpress = Utils.findRequiredViewAsType(source, R.id.layout_mwin_express, "field 'layoutMwinExpress'", RelativeLayout.class);
    target.layoutNoExpressInfo = Utils.findRequiredViewAsType(source, R.id.layout_not_express_info, "field 'layoutNoExpressInfo'", RelativeLayout.class);
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
    target.expressDate = null;
    target.expressOrderId = null;
    target.expressCompany = null;
    target.expressCustomerService = null;
    target.expressRecevier = null;
    target.expressRecevierAddress = null;
    target.expressEnsureReceipt = null;
    target.layoutMwinExpress = null;
    target.layoutNoExpressInfo = null;

    view2131559169.setOnClickListener(null);
    view2131559169 = null;
    view2131559546.setOnClickListener(null);
    view2131559546 = null;

    this.target = null;
  }
}
