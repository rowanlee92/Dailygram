<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="/WEB-INF/views/container/header.jsp"%>
<style>
div.panel-group {
	width:65%;
	margin: auto;
	margin-top: 30px;
	margin-bottom: 50px;
	min-height: 100%;
}

ul.list-group>li>button{
	float:right;
	background-color: #9770f9;
	color:white;
}
</style>
<div class="panel-group">
	<div class="panelspace">
		<h4 class="panel-heading panel-known"><strong>추천 인물</strong></h4>
		<span class="panel-heading panel-known">최근 인기 게시물과 회원님의 소개글을 바탕으로 추천됩니다. 다른 회원님들과 폭넓은 소통을 나눠보시는건 어떠신가요?</span>
		<hr>
		<div class="panel-body">
			<ul class="list-group">
			<c:if test="${empty list }">다시한번 조회해주시기 바랍니다. </c:if>
			<c:if test="${not empty list }">
			<c:forEach var="p" items="${list }">
				<li class="list-group-item">
					<c:choose>
						<c:when test="${p.profile_img ne null}"> 
							<img alt="" class="img-circle" src="/dailygram/thumbnail_mem/${p.profile_img}" width="30"> 
						</c:when>
						<c:otherwise>
							<img alt="" class="img-circle" src="http://www.technifroid-pro.fr/wp-content/uploads/2014/02/Technifroid-F.jpg" width="30">
						</c:otherwise>
					</c:choose>
					<a href='${pageContext.request.contextPath }/board/friList.do?writer=${p.id }' style="color: black"><span>${p.id }</span></a>
					<button type="button" class="btn btn-xs">팔로우</button>
				</li>
			</c:forEach>
			</c:if>
			</ul>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/views/container/footer.jsp"%>
	