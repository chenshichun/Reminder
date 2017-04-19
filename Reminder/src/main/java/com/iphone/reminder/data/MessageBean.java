package com.iphone.reminder.data;

import com.iphone.reminder.listview.SlideView;

public class MessageBean {
	
	private int id;
	public String title;
	public String time;
	public String location;
	public String note;
    public int repeat;
    public boolean isCheck;

	public SlideView slideView;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

    public int getRepeat(int repeat){
        return repeat;
    }

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public SlideView getSlideView() {
		return slideView;
	}

	public void setSlideView(SlideView slideView) {
		this.slideView = slideView;
	}
}
