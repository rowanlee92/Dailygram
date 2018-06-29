package com.kitri.daily.board;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BoardController {
   String basePath = "D:\\apache-tomcat-8.5.30-windows-x64\\apache-tomcat-8.5.30\\webapps";

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
		return "redirect:/board/post.do";
	} 

   @RequestMapping(value = "/board/updateBoard.do")
   public ModelAndView editBoard(Board b, HttpServletRequest req) {
      ModelAndView mav = new ModelAndView("board/editForm");
      HttpSession session = req.getSession(false);
      String id = (String) session.getAttribute("id");
      Board update = service.detailBoard(b.getBoard_seq(), id);
      mav.addObject("update", update);
      /*
       * String originpath = update.getImg(); // 파일경로를 가져옴 int index =
       * originpath.lastIndexOf("\\"); String path = originpath.substring(index + 1);
       * System.out.println(path); mav.addObject("path", path);
       */
      return mav;
   }

   @RequestMapping(value = "/board/edit.do")
   public String edit(Board b) {
      String originPath = basePath + "\\board\\"; // 원본파일 경로
      String upfolder = basePath + "\\thumbnail\\"; // 썸네일 처리한 파일 경로
      MultipartFile file = b.getFile(); // form.jsp에서 선택한 파일 가져오기
      if (file != null && !file.equals("")) {
         File dir = new File(originPath);
         Board d = service.detailBoard(b.getBoard_seq(), b.getWriter());
         String del = originPath + d.getImg(); // 원본파일 경로와 파일명
         System.out.println("파일" + del);
         File delete = new File(del);
         if (!dir.exists()) {
            dir.mkdirs();
         }
         // 파일 중복방지 처리
         String[] extension = file.getOriginalFilename().split("\\.");
         String FileType = extension[extension.length - 1];
         String img = b.getWriter() + "_" + System.currentTimeMillis() + "." + FileType;
         b.setImg(img); // img 경로 set
         File f = new File(originPath + "\\" + img);

         try {
            file.transferTo(f); // 새로운 파일을 넣음
            delete.delete();
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
      return "redirect:/board/post.do";
   }

   @RequestMapping(value = "/board/post.do")
   public ModelAndView detail(HttpSession session, HttpServletRequest req) {
      ModelAndView mav = new ModelAndView("board/post");
      session = req.getSession(false);
      String id = (String) session.getAttribute("id");
//      String id = "ryeonzzang";
      System.out.println("id ~~~~~" +id);
      Board b = service.detailBoard(145, id);
      mav.addObject("b", b);
      /*int index = originpath.lastIndexOf("\\");
      String path = originpath.substring(index + 1); // 파일명만 가져온다. */
      String upfolder = basePath + "\\board\\"; // 썸네일 처리한 파일 경로
      String path = upfolder + b.getImg();
      System.out.println(path);
      mav.addObject("path", path);
      return mav;
   }
}