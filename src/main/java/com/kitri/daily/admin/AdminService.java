package com.kitri.daily.admin;

import java.util.List;

import com.kitri.daily.search.Hashtag;

public interface AdminService {
	List<Hashtag> getBlockList(); //금지 태그 리스트 불러오기
	void addBlocktag(String tagname); //금지할 해시태그 추가하기 
	void cancleblock(String tagcancle); //금지 해시태그 낱개 활성화하기
}
