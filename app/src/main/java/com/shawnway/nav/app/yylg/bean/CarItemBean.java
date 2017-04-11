package com.shawnway.nav.app.yylg.bean;

import java.io.Serializable;

public class CarItemBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3607583040080839218L;


	private GoodBean good;
	private boolean checked;
	private boolean editing;
	private int buyed=1;
	private int buyUnit;

	private PurchaseDetailsBean purchaseDetailsBean;

	public int getBuyUnit() {
		return getGood().getBuyUnit();
	}

	public void setBuyUnit(int buyUnit) {
		this.buyUnit = buyUnit;
	}

	public boolean isEditing() {
		return editing;
	}
	
	public boolean isChecked(){
		return checked;
	}
	
	public void setChecked(boolean checked){
		this.checked=checked;
	}

	public void setEditing(boolean editing) {
		this.editing = editing;
	}

	public CarItemBean(){
		editing=false;
	}

	public CarItemBean (GoodBean bean){
		good=bean;
		editing=false;
	}


	public GoodBean toCarItemBean(){
		return good;
	}

	public int getBuyed() {
		return buyed;
	}

	public void setBuyed(int buyed) {
		this.buyed = buyed;
	}


	public GoodBean getGood() {
		return good;
	}

	public void setGood(GoodBean good) {
		this.good = good;
	}

	public int calWorth()
	{
		return buyed*getGood().getBuyUnit();//价值就是参与人次。。。以后还要改回来的。。。
	}


	public PurchaseDetailsBean getPurchaseDetailsBean() {
		return purchaseDetailsBean;
	}

	public void setPurchaseDetailsBean(PurchaseDetailsBean purchaseDetailsBean) {
		this.purchaseDetailsBean = purchaseDetailsBean;
	}
}
