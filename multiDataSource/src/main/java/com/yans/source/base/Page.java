package com.yans.source.base;

public class Page {
private int curPage;
private int pageSize=10;
private int total;

public int getCurPage() {
	return curPage;
}
public void setCurPage(int curPage) {
	this.curPage = curPage;
}
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}
public int getTotal() {
	return total;
}
public void setTotal(int total) {
	this.total = total;
}
public int getPageNumber() {
	return (total-1)/pageSize+1;
}

}
