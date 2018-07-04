package com.kitri.daily.board;

import java.util.List;

import com.kitri.daily.member.Member;

public interface BoardService {
	void uploadBoard(Board b);
	Board detailBoard(int board_seq);
	void editBoard(Board b);
	void delBoard(int board_seq, String writer);
	List<Board> getMyList(String id);
	List<Comment> getComments(int bseq);
	void insertNewComment(Comment co);
	void insertRepComment(Comment co);
	List<Board> getList(Board board);
	Like getType(Like like);
	void delType(Like like);
	void addLike(Like like);
	void addSiren(Like like);
	Member friend (String writer);
}
