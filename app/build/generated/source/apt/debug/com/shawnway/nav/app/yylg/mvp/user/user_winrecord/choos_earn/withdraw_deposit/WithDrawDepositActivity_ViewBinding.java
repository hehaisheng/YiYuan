// Generated code from Butter Knife. Do not modify!
package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit;

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

public class WithDrawDepositActivity_ViewBinding<T extends WithDrawDepositActivity> implements Unbinder {
  protected T target;

  private View view2131559169;

  private View view2131559591;

  @UiThread
  public WithDrawDepositActivity_ViewBinding(final T target, View source) {
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
    target.withdrawDepositEditPhone = Utils.findRequiredViewAsType(source, R.id.withdraw_deposit_edit_phone, "field 'withdrawDepositEditPhone'", EditText.class);
    target.withdrawDepositEditUserName = Utils.findRequiredViewAsType(source, R.id.withdraw_deposit_edit_user_name, "field 'withdrawDepositEditUserName'", EditText.class);
    target.withdrawDepositEditUserTbAccount = Utils.findRequiredViewAsType(source, R.id.withdraw_deposit_edit_user_tb_account, "field 'withdrawDepositEditUserTbAccount'", EditText.class);
    target.withdrawDepositAmountOfcard = Utils.findRequiredViewAsType(source, R.id.withdraw_deposit_amountOfcard, "field 'withdrawDepositAmountOfcard'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_withdraw_deposit, "field 'btnWithdrawDeposit' and method 'onClick'");
    target.btnWithdrawDeposit = Utils.castView(view, R.id.btn_withdraw_deposit, "field 'btnWithdrawDeposit'", Button.class);
    view2131559591 = view;
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
    target.withdrawDepositEditPhone = null;
    target.withdrawDepositEditUserName = null;
    target.withdrawDepositEditUserTbAccount = null;
    target.withdrawDepositAmountOfcard = null;
    target.btnWithdrawDeposit = null;

    view2131559169.setOnClickListener(null);
    view2131559169 = null;
    view2131559591.setOnClickListener(null);
    view2131559591 = null;

    this.target = null;
  }
}
