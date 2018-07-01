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
		<h4 class="panel-heading panel-known">알 수도 있는 사람</h4>
		<hr>
		<div class="panel-body">
			<ul class="list-group">
				<c:forEach var="s" items="${list }">
				<li class="list-group-item"><img alt="No-Profile" class="img-circle"
					src="/board/${s.profile_img }"
					width="30"> <a href=# style="color: black"><span>${s.id }</span></a>
					<button type="button" class="btn btn-xs">팔로우</button>
				</li>
				</c:forEach>
				<!-- <li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User2</span></a>
					<button type="button" class="btn btn-xs">팔로우</button>
				</li>
				<li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User3</span></a>
					<button type="button" class="btn btn-xs ">팔로우</button>
				</li>
				<li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User4</span></a>
					<button type="button"  class="btn btn-xs">팔로우</button>
				</li>
				<li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User5</span></a>
					<button class="btn btn-xs">팔로우</button>
				</li>
				<li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User6</span></a>
					<button class="btn btn-xs">팔로우</button>
				</li>
				<li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User7</span></a>
					<button class="btn btn-xs">팔로우</button>
				</li>
				<li class="list-group-item"><img alt="" class="img-circle"
					src="https://static1.squarespace.com/static/55198f1ce4b00c2cab3e5e30/t/5526d500e4b009f3ec94b422/1428608282728/600x600%26text%3Dprofile+img.gif?format=300w"
					width="30"> <a href=# style="color: black"><span>User8</span></a>
					<button class="btn btn-xs">팔로우</button>
				</li> -->
			</ul>
		</div>
	</div>
</div>
<%@ include file="/WEB-INF/views/container/footer.jsp"%>
	