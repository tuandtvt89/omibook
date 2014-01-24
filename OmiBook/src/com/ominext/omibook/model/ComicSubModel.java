package com.ominext.omibook.model;

public class ComicSubModel {
	private int pageIndex;
	private String pageImageName;
	private String japanSub;
	private String engSub;
	
	public int getPageIndex() {
		return this.pageIndex;
	}
	
	public void setPageIndex( int pPageIndex) {
		this.pageIndex = pPageIndex;
	}
	
	public String getPageImageName() {
		return this.pageImageName;
	}
	
	public void setPageImageName( String pPageImageName) {
		this.pageImageName = pPageImageName;
	}
	
	public String getJapanSub() {
		return this.japanSub;
	}
	
	public void setJapanSub( String pJapanSub) {
		this.japanSub = pJapanSub;
	}
	
	public String getEngSub() {
		return this.engSub;
	}
	
	public void setEngSub( String pEngSub) {
		this.engSub = pEngSub;
	}
}
