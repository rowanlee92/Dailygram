package com.kitri.daily.board;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kitri.daily.member.Member;

@Controller
public class BoardController {
   String basePath = "D:\\driver\\apache-tomcat-8.5.30\\webapps";

   @Resource(name = "boardService")
   private BoardService service;

   public void setService(BoardService service) {
      this.service = service;
   }
   
   @RequestMapping(value = "/board/form.do")
	void form() {
		
	}
      
   @RequestMapping(value = "/board/upload.do")
	public String upload(HttpServletRequest req, Board b) {
		String originPath = basePath + "\\board\\";		//원본파일 경로
		String upfolder = basePath + "\\thumbnail\\";	//썸네일 처리한 파일 경로
		MultipartFile file = b.getFile();				//form.jsp에서 선택한 파일 가져오기
		if (file != null && !file.equals("")) {
			File dir = new File(originPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			//파일 중복방지 처리
			String[] extension = file.getOriginalFilename().split("\\.");	
			String FileType = extension[extension.length-1];
			String img = b.getWriter() + "_" + System.currentTimeMillis() + "." + FileType;
			b.setImg(img);		//img 경로 set
			File f = new File(originPath + "\\" + img);
			try {
				file.transferTo(f);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//썸네일 처리
			int thumbnail_width = 375;
			int thumbnail_height = 350;
			File dir2 = new File(upfolder);
			if (!dir2.exists()) {
				dir2.mkdirs();
			}
			try {
				BufferedImage srcImg = ImageIO.read(f);		//원본파일 읽어오기
				BufferedImage thumbImg;
				thumbImg = new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);
				java.awt.Graphics2D g = thumbImg.createGraphics();
				g.drawImage(srcImg, 0, 0, thumbnail_width, thumbnail_height, null);
				File outFile = new File(upfolder + "\\" + img);
				ImageIO.write(thumbImg, "PNG", outFile);	//썸네일 파일 저장

			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		service.uploadBoard(b);
		return "redirect:/board/myList.do";
	} 

   @RequestMapping(value = "/board/updateBoard.do")
   public ModelAndView editBoard(@RequestParam(value="bseq") int bseq,
		   						HttpServletRequest req) {
      ModelAndView mav = new ModelAndView("board/editForm");
      Board update = service.detailBoard(bseq);
      mav.addObject("update", update);
      return mav;
   }

   @RequestMapping(value = "/board/edit.do")
   public String edit(Board b) {
      String originPath = basePath + "\\board\\"; // 원본파일 경로
      String upfolder = basePath + "\\thumbnail\\"; // 썸네일 처리한 파일 경로
      MultipartFile file = b.getFile(); // form.jsp에서 선택한 파일 가져오기
      if (file != null && !file.equals("")) {
         Board d = service.detailBoard(b.getBoard_seq());
         String del = originPath + d.getImg(); // 원본파일 경로와 파일명
         String del2 = upfolder + d.getImg(); // 원본파일 경로와 파일명
         File delete = new File(del);
         File delete2 = new File(del2);
         // 파일 중복방지 처리
         String[] extension = file.getOriginalFilename().split("\\.");
         String FileType = extension[extension.length - 1];
         String img = b.getWriter() + "_" + System.currentTimeMillis() + "." + FileType;
         b.setImg(img); // img 경로 set
         File f = new File(originPath + "\\" + img);

         try {
            file.transferTo(f); // 새로운 파일을 넣음
            delete.delete();
            delete2.delete();
         } catch (IllegalStateException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }

         // 썸네일 처리
         int thumbnail_width = 375;
         int thumbnail_height = 350;
         File dir2 = new File(upfolder);
         if (!dir2.exists()) {
            dir2.mkdirs();
         }
         try {
            BufferedImage srcImg = ImageIO.read(f); // 원본파일 읽어오기
            BufferedImage thumbImg;
            thumbImg = new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);
            java.awt.Graphics2D g = thumbImg.createGraphics();
            g.drawImage(srcImg, 0, 0, thumbnail_width, thumbnail_height, null);
            File outFile = new File(upfolder + "\\" + img);
            ImageIO.write(thumbImg, "PNG", outFile); // 썸네일 파일 저장

         } catch (IllegalStateException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      service.editBoard(b);
      System.out.println(b);
      return "redirect:/board/post.do?bseq="+b.getBoard_seq();
   }

   @RequestMapping(value = "/board/post.do")
   public ModelAndView detail(HttpServletRequest req ,@RequestParam(value="bseq") int bseq) {
      ModelAndView mav = new ModelAndView("board/post");
      Board b = service.detailBoard(bseq);
      HttpSession session = req.getSession(false);
	  Member mem  = (Member) session.getAttribute("memInfo");
	  String id = mem.getId();
	  Like like = new Like(bseq,id);
      Like l = service.getType(like);
      mav.addObject("b", b);
      mav.addObject("l", l);
      return mav;
   }
   
   @RequestMapping(value = "/board/myList.do")
   public ModelAndView list(HttpServletRequest req) {
	   HttpSession session = req.getSession(false);
	   Member mem  = (Member) session.getAttribute("memInfo");
	   String id = mem.getId();
	   List<Board> boardlist = (ArrayList<Board>) service.getMyList(id);
	   ModelAndView mav = new ModelAndView("board/myList");
	   mav.addObject("list", boardlist);
	   return mav;
   }
   
   @RequestMapping(value = "/board/delType.do")
   public String delType (HttpServletRequest req ,@RequestParam(value="bseq") int bseq) {
	   HttpSession session = req.getSession(false);
	   Member mem  = (Member) session.getAttribute("memInfo");
	   String id = mem.getId();
	   Like like = new Like(bseq, id);
	   service.delType(like);
	   return "redirect:/board/post.do?bseq="+bseq;
   }
   
   @RequestMapping(value = "/board/like.do")
   public String like (HttpServletRequest req ,@RequestParam(value="bseq") int bseq) {
	   HttpSession session = req.getSession(false);
	   Member mem  = (Member) session.getAttribute("memInfo");
	   String id = mem.getId();
	   Like like = new Like(bseq, id);
	   service.addLike(like);
	   return "redirect:/board/post.do?bseq="+bseq;
   }
   
   @RequestMapping(value = "/board/siren.do")
   public String siren (HttpServletRequest req ,@RequestParam(value="bseq") int bseq) {
	   HttpSession session = req.getSession(false);
	   Member mem  = (Member) session.getAttribute("memInfo");
	   String id = mem.getId();
	   Like like = new Like(bseq, id);
	   service.addSiren(like);
	   return "redirect:/board/post.do?bseq="+bseq;
   }
}