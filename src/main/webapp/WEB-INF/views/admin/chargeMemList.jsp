<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<link
	href="//netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet" id="bootstrap-css">
<script
	src="//netdna.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<head>
<title>신고자 리스트</title>
</head>
<script>
	$(function() {
		$('button#close').click(function() {
			location.href="${pageContext.request.contextPath}/admin/chargelist.do";
		});
	});
</script>
<body>
	<div class="container">
		<div class="col-md-12">
			<div class="row">
				<div class="panel-group">
					<div class="panelspace">
						<h3 class="panel-heading panel-danger">해당 게시물 신고자 리스트</h3>
						<hr>
						<div class="panel-body">
							<c:forEach var="pl" items="${personList}">
								<ul class="list-group">
									<li class="list-group-item">
										<c:choose>
											<c:when test="${pl.profile_img ne null}">
												<img alt="" class="img-circle"
													src="/dailygram/thumbnail_mem/${pl.profile_img}" width="30">
											</c:when>
											<c:otherwise>
												<img alt="" class="img-circle"
													src="http://www.technifroid-pro.fr/wp-content/uploads/2014/02/Technifroid-F.jpg"
													width="30">
											</c:otherwise>
										</c:choose> 
										<a href="${pageContext.request.contextPath}/board/friList.do?writer=${pl.sender}" style="color: black"><span>${pl.sender}</span></a>
									</li>
								</ul>
							</c:forEach>
							<div class="pull-right">
								<button id="close" class="btn btn-success">닫기</button>	
							</div>
							<br><br><br><br>
						</div>
					</div>
				</div>
			</div>
			<!-- /.row -->
		</div>
	</div>
</body>
</html>