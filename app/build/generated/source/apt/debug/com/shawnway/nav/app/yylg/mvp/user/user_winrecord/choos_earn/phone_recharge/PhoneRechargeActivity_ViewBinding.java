// Generated code from Butter Knife. Do not modify!
package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class PhoneRechargeActivity_ViewBinding<T extends PhoneRechargeActivity> implements Unbinder {
  protected T target;

  private View view2131559169;

  private View view2131559556;

  @UiThread
  public PhoneRechargeActivity_ViewBinding(final T target, View source) {
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
    target.phoneRechargeEditPhone = Utils.findRequiredViewAsType(source, R.id.phone_recharge_edit_phone, "field 'phoneRechargeEditPhone'", EditText.class);
    target.phoneRechargeEditUserName = Utils.findRequiredViewAsType(source, R.id.phone_recharge_edit_user_name, "field 'phoneRechargeEditUserName'", EditText.class);
    target.phoneRechargeAmountOfcard = Utils.findRequiredViewAsType(source, R.id.phone_recharge_amountOfcard, "field 'phoneRechargeAmountOfcard'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_phone_recharge, "field 'btnPhoneRecharge' and method 'onClick'");
    target.btnPhoneRecharge = Utils.castView(view, R.id.btn_phone_recharge, "field 'btnPhoneRecharge'", Button.class);
    view2131559556 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
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
    target.phoneRechargeEditPhone = null;
    target.phoneRechargeEditUserName = null;
    target.phoneRechargeAmountOfcard = null;
    target.btnPhoneRecharge = null;

    view2131559169.setOnClickListener(null);
    view2131559169 = null;
    view2131559556.setOnClickListener(null);
    view2131559556 = null;

    this.target = null;
  }
}
