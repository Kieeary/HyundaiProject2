package com.chysk5.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chysk5.domain.AllBuyProductDTO;
import com.chysk5.domain.CancelProductDTO;
import com.chysk5.domain.MemberDTO;
import com.chysk5.domain.MyResellProductDTO;
import com.chysk5.domain.ReplyDTO;
import com.chysk5.domain.SoldOutProductDTO;
import com.chysk5.domain.TalksDTO;
import com.chysk5.service.MemberService;
import com.chysk5.service.MyPageService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@Secured({"ROLE_MEMBER"})
@RequestMapping("/mypage/")
@AllArgsConstructor

public class MypageController {
	
	private MyPageService service;
	
	private MemberService mService;

	// 마이페이지 메인 화면으로 이동
	@GetMapping("/index")
	public void index(Model model, Principal prin) {
		String mem_id = prin.getName();
		
		int totalOrderPrice = service.totalOrderPrice(mem_id);
		int totalOrderCount = service.totalOrderCount(mem_id);
		
		List<AllBuyProductDTO> allList = service.getAllBuyList(mem_id, null, null);
		
		for(AllBuyProductDTO a : allList) {
			log.info(a);
		}
		
		model.addAttribute("totalOrderPrice", totalOrderPrice);
		model.addAttribute("totalOrderCount", totalOrderCount);	
		model.addAttribute("allList", allList);
		
		
	}
	
	/*
	 * 정기범 작성
	 * 내가 주문한 내역을 조회하기 위한 역할
	 * start_date, end_date는 날짜 검색시에만 사용하므로 @Nullable 어노테이션 사용
	 */
	// 주문내역 페이지로 이동
	@GetMapping("/myorder")
	public String myorder(Principal prc, Model model, @Nullable String start_date, @Nullable  String end_date) {
		
		 String mem_id=prc.getName();
		 
		 List<AllBuyProductDTO> allList = service.getAllBuyList(mem_id, start_date, end_date); // 내가 주문한 모든 리스트 조회
		 List<CancelProductDTO> cancelList = service.getCancelList(mem_id, start_date, end_date); // 주문 취소한 모든 리스트 조회
		 
		 model.addAttribute("allList", allList); 
		 model.addAttribute("cancelList", cancelList); // model 이용하여 데이터 전달
		 
		 for(AllBuyProductDTO a : allList) {
			 log.info(a);
		 }
		 
		 for(CancelProductDTO a : cancelList) {
			 log.info("취소 목록: " + a);
		 }
		 
		 return "mypage/myorder";
	}
	
	// 최근 본 상품 페이지로 이동
	@GetMapping("/recent_view_product")
	public void recentView() {}
	
	// 내가 쓴 글 페이지로 이동
	@GetMapping("/myarticle")
	public void myarticle(Principal prin, Model model) {
		log.info("mytalks controller.....");
		
		String mem_id = prin.getName();
		
		List<TalksDTO> talksList = service.getMyTalks(mem_id);
		log.info("mytalks list : " + talksList);
		
		List<ReplyDTO> replyList = service.getMyReply(mem_id);
		log.info("myrelpy list: " + replyList);
		
		model.addAttribute("tList", talksList);
		model.addAttribute("rList", replyList);
	}
	
	// 회원 정보 수정 페이지로 이동
	@GetMapping("/modify")
	public void modify(Model model, Principal prin) {
		
		String mem_id = prin.getName();
		
		MemberDTO member = mService.selectMember(mem_id);
		
		log.info("modify get ..... " + member);
		
		model.addAttribute("member", member);
	}
	
	/*
	 * 정기범 작성
	 * 내가 등록한 상품 페이지 이동
	 */
	@GetMapping("/myResell")
	public String getMyResellList(Model model, @Nullable String start_date, @Nullable  String end_date) {
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    User user = (User)authentication.getPrincipal();    
	    String mem_id = user.getUsername();
	    
	    List<MyResellProductDTO> myResellList = service.getMyResellList(mem_id, start_date, end_date); // 내가 판매 등록한 상품 리스트 조회
	    List<SoldOutProductDTO> mySoldOutList = service.getSoldOutList(mem_id, start_date, end_date); // 내가 등록한 상품 중 팔린 상품 조회
	    
		
		model.addAttribute("myResellList", myResellList); 
		model.addAttribute("mySoldOutList", mySoldOutList); // model 이용하여 데이터 전달
		
		return "mypage/myResellPage";
	}
	
	/*
	 * 정기범 작성
	 * 리셀 등록한 상품 등록 취소
	 */
	@ResponseBody
	@DeleteMapping(value="/myResell/{pro_opt_id}")
	public void removeMyResellProduct(@PathVariable("pro_opt_id") String pro_opt_id, Principal prc) {
		String mem_id=prc.getName();
		int result = service.removeMyResellProduct(pro_opt_id, mem_id); // 등록한 상품 삭제하는 역할
		
		return;
	}
	
	/*
	 * 정기범 작성
	 * 등록한 상품 가격 수정
	 */
	@PostMapping("/myResell")
	public String modify(@RequestParam(name="re_id") String re_id, @RequestParam(name="re_price") int re_price) {
		
		log.info("가격 수정할 re_id: " + re_id);
		log.info("수정할 가격: " + re_price);
		service.modifyPrice(re_id, re_price);
		
		return "redirect:/mypage/myResell";
	}
	
	/*
	 * 정기범 작성
	 * 내가 주문한 상품을 주문 취소
	 */
	@PostMapping("myorder/cancel")
	public String cancel(@RequestParam("order_no") String order_no, @RequestParam("pro_opt_id") String pro_opt_id) {
		
		log.info("주문취소 상품 id: " + pro_opt_id);
		log.info("주문 취소 주문 id: " + order_no);
		service.cancelOrder(pro_opt_id, order_no);
		
		return "redirect:/mypage/myorder";
	}
}
