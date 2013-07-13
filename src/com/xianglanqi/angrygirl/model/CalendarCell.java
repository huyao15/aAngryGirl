package com.xianglanqi.angrygirl.model;

import java.io.Serializable;
import java.util.Date;

public class CalendarCell implements Serializable {

	private static final long serialVersionUID = -626339171749981413L;

	private int year;

	private int month;

	private int day;

	private Date updatedTime;

	private boolean canChange = false;

	private boolean isToday;

	private String text;

	private CellType cellType;

	private Mood mood = Mood.UNKNOWN;
	
	private String description = "";

	public CalendarCell(int year, int month, int day, String text,
			CellType cellType) {
		this.year = year;
		this.month = month;
		this.day = day;
		this.text = text;
		this.setCellType(cellType);
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Mood getMood() {
		return mood;
	}

	public void setMood(Mood mood) {
		this.mood = mood;
	}

	public CellType getCellType() {
		return cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public boolean isCanChange() {
		return canChange;
	}

	public void setCanChange(boolean canChange) {
		this.canChange = canChange;
	}

	public boolean isToday() {
		return isToday;
	}

	public void setToday(boolean isToday) {
		this.isToday = isToday;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
