package com.kitri.daily.board;

import java.sql.Date;


public class Board {
	private int board_seq;
	private String writer;
	private String content;
	private Date posted;
	private String img;
	private String public_yn;
	
	public Board() {}	
	public Board(int board_seq, String writer, String content, Date posted, String img, String public_yn) {
		super();
		this.board_seq = board_seq;
		this.writer = writer;
		this.content = content;
		this.posted = posted;
		this.img = img;
		this.public_yn = public_yn;
	}

	public int getBoard_seq() {
		return board_seq;
	}
	public void setBoard_seq(int board_seq) {
		this.board_seq = board_seq;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getPosted() {
		return posted;
	}
	public void setPosted(Date posted) {
		this.posted = posted;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPublic_yn() {
		return public_yn;
	}
	public void setPublic_yn(String public_yn) {
		this.public_yn = public_yn;
	}
	@Override
	public String toString() {
		return "Board [board_seq=" + board_seq + ", writer=" + writer + ", content=" + content + ", posted=" + posted
				+ ", img=" + img + ", public_yn=" + public_yn + "]";
	}
	
}
