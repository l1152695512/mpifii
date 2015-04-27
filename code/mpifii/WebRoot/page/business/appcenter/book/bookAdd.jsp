<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/content');">主页</a>
			<span class="divider">/</span></li>
		<li><a href="#" onclick="ajaxContent('/business/app/index');">应用管理</a> <span
			class="divider">/</span></li>
			<li><a href="#" onclick="ajaxContent('/business/app/book/index');">小说管理</a> <span
			class="divider">/</span></li>
		<li><a href="#">添加小说</a></li>
	</ul>
</div>
<div class="row-fluid sortable">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-edit"></i> 添加小说
			</h2>
			<div class="box-icon">
				<a href="#" class="btn btn-minimize btn-round"><i
					class="icon-chevron-up"></i></a> <a href="#"
					class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<form class="form-horizontal" id="book_add_from" enctype="multipart/form-data"
				action="${cxt}/business/app/book/save" method="POST">
				<input type="hidden" name="book.id" value="${book.id}">
				<div class="control-group">
					<label class="control-label">小说名称</label>
					<div class="controls">
						<input type="text" name="book.name" value="${book.name}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">作者</label>
					<div class="controls">
						<input type="text" name="book.author" value="${book.author}" class="input-xlarge"
							maxlength="50" vMin="1" vType="length"
							onblur="onblurVali(this);"> <span class="help-inline">1-50位</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">小说字数</label>
					<div class="controls">
						<input type="text" name="book.words" value="${book.words}" class="input-xlarge"
							maxlength="10" vMin="1" vType="numberZ"
							onblur="onblurVali(this);"> <span class="help-inline">1-10位正整数</span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">小说图标</label>
					<div class="controls">
						 <img  src="${cxt}/${book.img}" id="book_icon" style="width: 48px;height: 48px" onerror="${cxt}/images/guest.jpg">
						<input  type="file" name="bookImage" id="bookImage" accept="*.jpg,*.png,*.jpeg" value="上传图像"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">上传小说</label>
					<div class="controls">
						<input  type="file" name="bookInfo" id="bookInfo"  value="上传小说"  />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">小说类型</label>
					<div class="controls">
						<select name="book.type" class="combox">
							<c:forEach var="vtype" items="${typeList}">
								<option value="${vtype.id}" <c:if test="${vtype.id==book.type}"> selected="selected"</c:if>>${vtype.name}</option>
							</c:forEach>
						</select>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">小说主题</label>
					<div class="controls">
						<select name="book.theme" class="combox">
							<c:forEach var="vtype" items="${themeList}">
								<option value="${vtype.id}" <c:if test="${vtype.id==book.theme}"> selected="selected"</c:if>>${vtype.name}</option>
							</c:forEach>
						</select>
						<span class="help-inline"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">描述</label>
					<div class="controls">
						<input type="text" name="book.des" value="${book.des}" class="input-xlarge"
							maxlength="500" vMin="1" vType="length"> <span class="help-inline">1-500位</span>
					</div>
				</div>
				<div class="form-actions">
					<button class="btn btn-primary" type="button"
						onclick="submitInfo(this.form);">提交</button>
					<button class="btn" type="button" onclick="ajaxContent('/business/app/book');">取消</button>
				</div>
			</form>
		</div>
	</div>
	<!--/span-->
</div>
<!--/row-->
<script type="text/javascript">
		function submitInfo(form){
			var errorCount = formVali(form);
			if(errorCount != 0){
				return;
			}
			 $("#book_add_from").ajaxSubmit({
			       success: function(resp){
			    	   myAlert("保存成功",function(){
			    		   ajaxContent('/business/app/book');
			    	   });
			       },
			       error: function( err ){
			    	   myAlert("保存失败",function(){
			    		   ajaxContent('/business/app/book');
			    	   });
			       }
			  	});
		}
		$("#bookImage").on("change", function() {
			var files = !!this.files ? this.files : [];
			if (!files.length || !window.FileReader)
				return;
			if (/^image/.test(files[0].type)) {
				var reader = new FileReader();
				reader.readAsDataURL(files[0]);
				reader.onloadend = function() {
					$("#book_icon").attr("src", this.result);
				};
			}
		});
</script>