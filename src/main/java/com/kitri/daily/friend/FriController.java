package com.kitri.daily.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kitri.daily.alerm.Alerm;
import com.kitri.daily.board.BoardService;
import com.kitri.daily.member.Member;

@Controller
public class FriController {
	@Resource(name = "friService")
	private FriService service;
	@Resource(name = "boardService")
	private BoardService service2;

	public void setService(FriService service) {
		this.service = service;
	}
	public void setService(BoardService service) {
		this.service2 = service;
	}

	@RequestMapping(value = "/friend/knownfriend.do")
	public ModelAndView recommend(HttpServletRequest req, @RequestParam(value = "id") String id) {
		ModelAndView mav = new ModelAndView("friend/knownfriend");
		// HttpSession session = req.getSession(false);
		Friend friend = new Friend();
		// String user_id = (String) session.getAttribute("id");
		friend.setId(id);
		System.out.println("로그인 한 아이디 : " + id);

		// 로그인한 사람의 친구 수 구하기
		List<HashMap<String, Object>> count = service.getFriendRelationshipCount(id);
		ArrayList<Friend> list = null;
		ArrayList<Friend> list3 = null;
		// 로그인한 회원의 intro 가져오기
		String user_intro = service.getUserIntro(id);
		System.out.println("로그인한 회원의 소개글 : " + user_intro);
		
		// -> 해시태그 단위로 잘라서 배열로 만들기
		String[] introArray = null;
		user_intro = user_intro.trim();//공백제거
		if (user_intro != null) {//소개글이 있을 경우 해시태그 자르기
			introArray = user_intro.split(" ");
			System.out.println("해시태그 갯수 : " + introArray.length);
			for (int i = 0; i < introArray.length; i++)
				System.out.println(introArray[i]);
		}
		System.out.println("user intro 길이:" + user_intro.length());
		System.out.println("count size : " + count.size());
		for(int i=0;i<introArray.length;i++) {
			System.out.println("해시태그 :" + introArray[i]);
		}
		System.out.println();
		if (count.size() == 1 && user_intro.trim().length()==0) {
			// 친구 수가 0이고 좋아요 한 글이 없고, intro가 없을 경우--> 최신글순으로 좋아요 많이 받은 회원 추천하기
			System.out.println("친구 0명");
			list = (ArrayList<Friend>) service.getRecommend(id);
			mav.addObject("list", list);
		} else {
			//친구가 없지만, intro가 있는  경우
			//친구가 있는데, intro가 없는 경우
			//친구도 있고, intro도 있는 경우
			System.out.println("친구 여러명");	

			// intro 해시태그 기준으로 회원 추천해주기
			if (user_intro.trim().length()==0) {//소개글 없을 경우
				System.out.println("소개글 없을 경우");
				list = (ArrayList<Friend>) service.getRecommend2(id);
				mav.addObject("list", list);
			}else if (introArray.length == 1) {// 해시태그가 1개 있을 경우
				System.out.println("해시태그 1개");
				friend.setTag1(introArray[0]);
				friend.setId(id);
				list = (ArrayList<Friend>) service.getRecommendHT1(friend);
				mav.addObject("list", list);

			} else if (introArray.length == 2) {// 해시태그가 2개있을 경우
				System.out.println("해시태그 2개");
				friend.setTag1(introArray[0]);
				friend.setTag2(introArray[1]);

				list = (ArrayList<Friend>) service.getRecommendHT2(friend);
				mav.addObject("list", list);

			} else if (introArray.length == 3) {// 해시태그가 3개있을 경우
				System.out.println("해시태그 3개");
				friend.setTag1(introArray[0]);
				friend.setTag2(introArray[1]);
				friend.setTag3(introArray[2]);

				list = (ArrayList<Friend>) service.getRecommendHT3(friend);
				mav.addObject("list", list);

			} else if (introArray.length == 4) {// 해시태그가 4개있을 경우
				System.out.println("해시태그 4개");
				friend.setTag1(introArray[0]);
				friend.setTag2(introArray[1]);
				friend.setTag3(introArray[2]);
				friend.setTag3(introArray[3]);

				list = (ArrayList<Friend>) service.getRecommendHT4(friend);
				mav.addObject("list", list);

			} else if (introArray.length == 5) {// 해시태그가 5개있을 경우
				System.out.println("해시태그 5개");
				friend.setTag1(introArray[0]);
				friend.setTag2(introArray[1]);
				friend.setTag3(introArray[2]);
				friend.setTag3(introArray[3]);
				friend.setTag3(introArray[4]);

				list = (ArrayList<Friend>) service.getRecommendHT5(friend);
				mav.addObject("list", list);
			}
		}
		list3 = list;
		if (list.size() < 10) {//추천 회원이 10명 미만이면 좋아요 기반으로 추천인원  추가함.
			ArrayList<Friend> list2 = (ArrayList<Friend>) service.getRecommend2(id);

			for (int i = 0; i < list2.size(); i++) {
				boolean check = false;
				for (int j = 0; j < list.size(); j++) {
					if (list.get(j).getId().equals(list2.get(i).getId())) {
						check = true;
					}
				}
				if (check == false) {
					list3.add(list2.get(i));
				}
			}
			mav.addObject("list", list3);
		}
		
		ArrayList<Friend> mylist = (ArrayList<Friend>) service.getfollowingList(id);// 내가 팔로잉 하는사람(y)
		ArrayList<Friend> mywaitlist = (ArrayList<Friend>) service.getfollowwaitList(id);// 팔로워 요청 한 목록(n)

		for (int i = 0; i < list3.size(); i++) {
			for (int j = 0; j < mylist.size(); j++) {
				if (list3.get(i).getId().equals(mylist.get(j).getId())) {
					list3.get(i).setStatus("y");// 내 팔로우 리스트에 있으면 상태값 y로 바꿔줌
				}
			}
			for (int j = 0; j < mywaitlist.size(); j++) {
				if (list3.get(i).getId().equals(mywaitlist.get(j).getId())) {
					list3.get(i).setStatus("wait");// 내 팔로우 요청리스트에 있으면 상태값 wait로 바꿔줌
				}
			}
			if (list3.get(i).getStatus() == null)
				list3.get(i).setStatus("no");

		}
		mav.addObject("list", list);
		
		return mav;
	}

	@RequestMapping(value = "/friend/subscribelist.do")
	public ModelAndView subscribeList(HttpServletRequest req, @RequestParam(value = "id") String id) {// 내가 구독하는 사람 리스트
		ModelAndView mav = new ModelAndView("friend/subscribelist");
		Friend friend = new Friend();
		friend.setId(id);
		String cnt = service2.cntBoard(id);
		req.setAttribute("cnt", cnt);
		System.out.println("로그인 한 아이디 : " + id);

		ArrayList<Friend> list = (ArrayList<Friend>) service.getsubscribeList(id);
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value = "/friend/followinglist.do")
	public ModelAndView followingList(HttpServletRequest req, @RequestParam(value = "id") String id) {// 내가 구독하는 사람 리스트
		ModelAndView mav = new ModelAndView("friend/followinglist");
		Friend friend = new Friend();
		friend.setId(id);
		String cnt = service2.cntBoard(id);
		req.setAttribute("cnt", cnt);
		System.out.println("로그인 한 아이디 : " + id);

		ArrayList<Friend> list = (ArrayList<Friend>) service.getfollowingList(id);
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value = "/friend/followerlist.do")
	public ModelAndView followerList(HttpServletRequest req, @RequestParam(value = "id") String id) {// 내가 구독하는 사람 리스트
		ModelAndView mav = new ModelAndView("friend/followerlist");
		Friend friend = new Friend();
		friend.setId(id);
		String cnt = service2.cntBoard(id);
		req.setAttribute("cnt", cnt);
		System.out.println("로그인 한 아이디 : " + id);

		ArrayList<Friend> list = (ArrayList<Friend>) service.getfollowerList(id);// 팔로워 리스트 받아옴
		ArrayList<Friend> mylist = (ArrayList<Friend>) service.getfollowingList(id);// 내가 팔로잉 하는사람(y)
		ArrayList<Friend> mywaitlist = (ArrayList<Friend>) service.getfollowwaitList(id);// 팔로워 요청 한 목록(n)

		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < mylist.size(); j++) {
				if (list.get(i).getId().equals(mylist.get(j).getId())) {
					list.get(i).setStatus("y");// 내 팔로우 리스트에 있으면 상태값 y로 바꿔줌
				}
			}
			for (int j = 0; j < mywaitlist.size(); j++) {
				if (list.get(i).getId().equals(mywaitlist.get(j).getId())) {
					list.get(i).setStatus("wait");// 내 팔로우 요청리스트에 있으면 상태값 wait로 바꿔줌
				}
			}
			if (list.get(i).getStatus() == null)
				list.get(i).setStatus("no");
		}
		System.out.println("list : ");
		for (int i = 0; i < list.size(); i++)
			System.out.print(list.get(i) + ", ");
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value = "/friend/friendfollowerlist.do")
	public ModelAndView friendFollowerList(HttpServletRequest req, @RequestParam(value = "id") String id) {// 내가 구독하는 사람
																											// 리스트
		ModelAndView mav = new ModelAndView("friend/friendfollowerlist");
		Friend friend = new Friend();
		friend.setId(id);
		HttpSession session = req.getSession(false);
		Member mem = (Member) session.getAttribute("memInfo");
		String user_id = mem.getId();
		Member fri = service2.friend(id);
		System.out.println("프로필 상 아이디(친구) : " + id);
		System.out.println("로그인한 아이디 : " + user_id);

		ArrayList<Friend> list = (ArrayList<Friend>) service.getfollowerList(id);// 친구의팔로워 리스트 받아옴
		ArrayList<Friend> mylist = (ArrayList<Friend>) service.getfollowingList(user_id);// 내가 팔로잉 하는사람
		ArrayList<Friend> mywaitlist = (ArrayList<Friend>) service.getfollowwaitList(user_id);// 팔로워 요청 한 목록(n)
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < mylist.size(); j++) {
				if (list.get(i).getId().equals(mylist.get(j).getId())) {
					list.get(i).setStatus("y");// 내 팔로우 리스트에 있으면 상태값 y로 바꿔줌
				}
			}
			for (int j = 0; j < mywaitlist.size(); j++) {
				if (list.get(i).getId().equals(mywaitlist.get(j).getId())) {
					list.get(i).setStatus("wait");// 내 팔로우 요청리스트에 있으면 상태값 wait로 바꿔줌
				}
			}
			if (list.get(i).getStatus() == null)
				list.get(i).setStatus("no");
			if (list.get(i).getId().equals(user_id))
				list.get(i).setStatus("me");
		}
		mav.addObject("fri", fri);
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value = "/friend/friendfollowinglist.do")
	public ModelAndView friendFollowingList(HttpServletRequest req, @RequestParam(value = "id") String id) {// 친구가 구독하는
																											// 사람 리스트
		ModelAndView mav = new ModelAndView("friend/friendfollowinglist");
		Friend friend = new Friend();
		friend.setId(id);
		HttpSession session = req.getSession(false);
		Member mem = (Member) session.getAttribute("memInfo");
		String user_id = mem.getId();
		Member fri = service2.friend(id);
		System.out.println("프로필 상 아이디(친구) : " + id);
		System.out.println("로그인한 아이디 : " + user_id);

		ArrayList<Friend> list = (ArrayList<Friend>) service.getfollowingList(id);// 친구의 팔로잉 리스트
		ArrayList<Friend> mylist = (ArrayList<Friend>) service.getfollowingList(user_id);// 내 팔로잉 리스트
		ArrayList<Friend> mywaitlist = (ArrayList<Friend>) service.getfollowwaitList(user_id);// 팔로워 요청 한 목록(n)

		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < mylist.size(); j++) {
				if (list.get(i).getId().equals(mylist.get(j).getId())) {
					list.get(i).setStatus("y");// 내 팔로우 리스트에 있으면 상태값 y로 바꿔줌
				}
			}
			for (int j = 0; j < mywaitlist.size(); j++) {
				if (list.get(i).getId().equals(mywaitlist.get(j).getId())) {
					list.get(i).setStatus("wait");// 내 팔로우 요청리스트에 있으면 상태값 wait로 바꿔줌
				}
			}
			if (list.get(i).getStatus() == null)
				list.get(i).setStatus("no");
			if (list.get(i).getId().equals(user_id))
				list.get(i).setStatus("me");
		}
		mav.addObject("fri", fri);
		mav.addObject("list", list);
		return mav;
	}
	
	@RequestMapping(value = "/friend/subscribe.do") 
	public String subscribe(HttpServletRequest req ,@RequestParam(value="writer") String id) {
	  HttpSession session = req.getSession(false); 
	  Member mem = (Member) session.getAttribute("memInfo"); 
	  String user_id = mem.getId(); 
//		  String friend_id = (String) session.getAttribute("friendId");
	  Relationship subscribe = new Relationship(user_id, id); 
	  service.subscribe(subscribe);
	  
	  ArrayList<Integer> count = service.profileCount(user_id);
	  session.setAttribute("followerCount", count.get(0));
	  session.setAttribute("followingCount", count.get(1));
	  session.setAttribute("subscribeCount", count.get(2)); 
		
	  return "redirect:/board/friList.do?writer="+id; 
	}
 
	@RequestMapping(value = "/friend/cancelFollow.do")
	public String cancelFollow(HttpServletRequest req, @RequestParam(value = "writer") String writer,
			@RequestParam(value = "type") int type) {// 팔로우 취소
		HttpSession session = req.getSession(false);
		String url = null;
		Member mem = (Member) session.getAttribute("memInfo");
		String user_id = mem.getId();
		String friend_id = (String) session.getAttribute("friendId");
		Relationship relation = new Relationship(user_id, writer);
		
		//alerm 테이블에 user_id, writer, type='n'이 있으면(팔로우요청취소 이므로 테이블에서 삭제해주기, 팔로잉 취소면 삭제 해줄 필요 없음
		Alerm alerm = new Alerm(relation.getSender(), relation.getReceiver());
		Alerm al = service.findAlerm(alerm);
		if(al!=null) {//팔로우 요청 취소
			service.deleteAlerm(alerm);
		}
		
		service.cancelfollow(relation);//팔로우 취소
		
		ArrayList<Integer> count = service.profileCount(user_id);
		session.setAttribute("followerCount", count.get(0));
		session.setAttribute("followingCount", count.get(1));
		session.setAttribute("subscribeCount", count.get(2));

		if (type == 1) {// 내 팔로잉 리스트
			url = "redirect:/friend/followinglist.do?id=" + user_id;
		} else if (type == 2) {// 내 팔로워 리스트
			url = "redirect:/friend/followerlist.do?id=" + user_id;
		} else if (type == 3) {
			url = "redirect:/friend/friendfollowerlist.do?id=" + friend_id;
		} else if (type == 4) {
			url = "redirect:/friend/friendfollowinglist.do?id=" + friend_id;
		} else if (type == 5) {
			url = "redirect:/board/friList.do?writer=" + friend_id;
		} else if (type == 6) {
			url = "redirect:/friend/knownfriend.do?id=" + user_id;
		}
		return url;
	}

	@RequestMapping(value = "/friend/cancelSubscribe.do")
	public String cancelSubscribe(HttpServletRequest req, @RequestParam(value = "writer") String writer,
			@RequestParam(value = "type") int type) {// 내 구독 리스트에서
		// 구독 취소
		HttpSession session = req.getSession(false);
		Member mem = (Member) session.getAttribute("memInfo");
		String user_id = mem.getId();
		String friend_id = (String) session.getAttribute("friendId");
		String url=null;
		Relationship relation = new Relationship(user_id, writer);
		service.cancelsubscribe(relation);//구독취소

		ArrayList<Integer> count = service.profileCount(user_id);
		session.setAttribute("followerCount", count.get(0));
		session.setAttribute("followingCount", count.get(1));
		session.setAttribute("subscribeCount", count.get(2));

		if(type == 1) {
			url="redirect:/friend/subscribelist.do?id=" + user_id;
		}else if (type == 2){
			url = "redirect:/board/friList.do?writer=" + friend_id;
		}
		return url;
	}

	@RequestMapping(value = "/friend/addFollow.do")
	public String addFollow(HttpServletRequest req, @RequestParam(value = "writer") String writer,
			@RequestParam(value = "type") int type) {// 팔로우 요청
		HttpSession session = req.getSession(false);
		Member mem = (Member) session.getAttribute("memInfo");
		String user_id = mem.getId();
		String url = null;
		String friend_id = (String) session.getAttribute("friendId");
		Relationship relation = new Relationship(user_id, writer);
		service.addfollow(relation);//관계 테이블에 'N'추가
		
		Alerm alerm = new Alerm(relation.getSender(), relation.getReceiver());
		service.addfollowalerm(alerm);//알림테이블에 'N'추가

		ArrayList<Integer> count = service.profileCount(user_id);
		session.setAttribute("followerCount", count.get(0));
		session.setAttribute("followingCount", count.get(1));
		session.setAttribute("subscribeCount", count.get(2));
		
		if (type == 1) {// 내 팔로워 리스트에서 팔로우 할 경우
			url = "redirect:/friend/followerlist.do?id=" + user_id;
		} else if (type == 2) {
			url = "redirect:/friend/friendfollowerlist.do?id=" + friend_id;
		} else if (type == 3) {
			url = "redirect:/friend/friendfollowinglist.do?id=" + friend_id;
		} else if (type == 4) {
			url = "redirect:/board/friList.do?writer=" + friend_id;
		} else if (type == 5) {
			url = "redirect:/friend/knownfriend.do?id=" + user_id;
		}
		return url;
	}
	
	@RequestMapping(value = "/friend/successFriend.do")
	public String successFriend(HttpServletRequest req, @RequestParam(value = "receiver") String receiver,
			@RequestParam(value = "sender") String sender) {// 팔로우 요청 수락
		HttpSession session = req.getSession(false);
		Relationship relation = new Relationship(sender, receiver);
		service.successFollow(relation);//관계테이블에 'Y로  업데이트
		
		Alerm alerm = new Alerm(sender, receiver);
		service.updateRead(alerm);//해당알림 읽음 처리
		
		ArrayList<Integer> count = service.profileCount(receiver);
		session.setAttribute("followerCount", count.get(0));
		session.setAttribute("followingCount", count.get(1));
		session.setAttribute("subscribeCount", count.get(2));
		
		 ArrayList<Integer> fricount =  service.FriendprofileCount(sender);
		 session.setAttribute("friendfollowerCount", fricount.get(0));
		 session.setAttribute("friendfollowingCount",fricount.get(1));
		 session.setAttribute("friendsubscribeCount", fricount.get(2));
		
		return "redirect:/board/myList.do";
	}
	
}
