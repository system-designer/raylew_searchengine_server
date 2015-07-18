var swfu;
var insertAllStatus=true; //全部插入按钮的状态
//初始化 swf
function initSwfupload(url,sizeLimit,types,typsdesc,uploadLimit){
		//初始化界面
		init(uploadLimit,sizeLimit);
		//初始化 swfupload
		var settings={
			// Flash Settings flash9_url :"http://192.168.1.115:8080/bjsearch/js/plugins/multiimage/js/swfupload_fp9.swf",
			flash_url : contextPath+"/js/swfupload.swf",
			file_post_name:"filedata",
			
			upload_url: url,// contextPath+"/servlet/FileUploadServlet.htm?jsessionId=${pageContext.session.id}",
			post_params: {"name" : "test"},
			
			// File Upload Settings
			file_size_limit : sizeLimit,  	// 1000MB
			file_types : types,    //*.*
			file_types_description : typsdesc,//"Web Image Files",
			file_upload_limit : uploadLimit,//20
			file_queue_limit : 0,
			
			file_queue_error_handler : fileQueueError,
			file_dialog_complete_handler : fileDialogComplete,//选择好文件后提交
			file_queued_handler : fileQueued,
			swfupload_load_failed_handler : swfUploadLoadFailed, //swf 加载失败
			swfupload_loaded_handler : swfUploadLoaded,  //swf 加载完成
			
			upload_start_handler: uploadStart,
			upload_progress_handler : uploadProgress,
			upload_error_handler : uploadError,
			upload_success_handler : uploadSuccess,
			upload_complete_handler : uploadComplete,

			// Button Settings
			button_image_url : contextPath+"/images/select-files-zh_CN.png",
			button_placeholder_id : "buttonPlaceholder",
			button_width: 72,
			button_height: 23,
			button_text : '<span style="display:block;"></span>',
			custom_settings : {
				photoContainer_Id   : "photoContainer",  	//图片的容器id
				btnUpload_ID		: "btnUpload",          //上传按钮
				insertAll_Id		: "insertAll", 			//全部插入
				clearAll_Id			: "clearAll", 			//全部清空
			   	errorMsg_Id			: "errorMsg",  			//错误信息
			  	errorMsg_fadeOutTime: 2000  				//错误信息谈出的时间
			},
			// Debug Settings
			debug: false,   //是否显示调试窗口
			auto_upload:false
		};
	try{
		//等 1秒后初始化 swf
		setTimeout(function (){swfu = new SWFUpload(settings);},1000);
	}catch (ex) {
		var message='<div id="divAlternateContent" style="background-color: #FFFF66; text-align:center;">'
			+'<font color="red">工具加载失败,请刷新再试!</div>';
		addErrorMsg(message,false);
		this.debug(ex);
	}
	
}
//初始化 界面
function init(uploadLimit,sizeLimit){
	if($(".ke-dialog-upload").length>0){
		$(".ke-dialog-upload").remove();
	}
	var uploadbody='<div id="uploadBody" class="ke-dialog-upload">'
		+'<div id="uploadHead" class="ke-dialog-upload-head">'
		+'  <div style="float: left">批量图片上传</div><div id="iconClose" class="ke-dialog-icon-close" title="关闭"></div><div style="clear: both"></div>'
		+'	</div>'
		+'	<!--content-->'
		+'	<div class="ke-dialog-content">'
		+'		<div class="ke-dialog-content-head">'
		+'		  <div id="errorMsg" class="ke-dialog-content-error"></div>'
		+'		  <div id="swfbuttonParent" class="ke-swfupload-button"><span id="buttonPlaceholder" /></div>'
		+'		  <div class="ke-inline-block ke-swfupload-desc">允许用户同时上传&nbsp;<font color="red">'+uploadLimit+'</font>&nbsp;张图片，单张图片容量不超过<font color="red">'+sizeLimit+'</font></div>'
		+'			<div class="ke-button-common ke-button-outer ke-swfupload-startupload"><input id="btnUpload" type="button" class="ke-button-common ke-button" value="开始上传"></div>'
		+'			<div style="clear: both;"></div>'		
		+' 		</div>'
		+'		<div id="photoContainer" class="ke-swfupload-body">'
		+'		</div>'
		+'	</div>'
		+'	<!--bottom-->'
		+'	<div class="ke-dialog-footer">'
		+'	<span class="ke-button-common ke-button-outer" title="全部插入"><input id="insertAll" class="ke-button-common ke-button" type="button" value="全部插入"></span>'
		+'	<span class="ke-button-common ke-button-outer" title="全部清空"><input id="clearAll" class="ke-button-common ke-button" type="button" value="全部清空"></span>'
		+'	<span class="ke-button-common ke-button-outer" title="取消"><input id="btncancel" class="ke-button-common ke-button" type="button" value="取消"></span></div>'
		+'	</div>'
		+' <div style="display: block; width: 100%; height: 1200px; position: absolute; left: 0px; top: 0px; z-index: 811212;" class="ke-dialog-mask"></div>';
	$('body').append(uploadbody);
	$("#iconClose").bind("click",{uploadBodyId:'uploadBody'},iconClose);
	$("#btncancel").bind("click",{uploadBodyId:'uploadBody'},iconClose);
	$("#insertAll").bind("click",insertAllInit);
	var dragging = false;
	var iX, iY;
	$("#uploadHead").mousedown(function(e) {
		dragging = true;
		iX = e.clientX - this.offsetParent.offsetLeft;
		iY = e.clientY - this.offsetParent.offsetTop;
		return false;
	});
	document.onmousemove = function(e) {
		if (dragging) {
			var e = e || window.event;
			var oX = e.clientX - iX;
			var oY = e.clientY - iY;
			$("#uploadBody").css({"left":oX<0?0:oX + "px", "top":oY<0?0:oY + "px"});
			return false;
		}
	};
	$(document).mouseup(function(e) {
		dragging = false;
		e.cancelBubble = true;
	});
}
/**
 * 当文件选择对话框关闭消失时，如果选择的文件成功加入上传队列，
 * 那么针对每个成功加入的文件都会触发一次该事件（N个文件成功加入队列，就触发N次此事件）。
 * @param {} file
 * id : string,			    // SWFUpload控制的文件的id,通过指定该id可启动此文件的上传、退出上传等
 * index : number,			// 文件在选定文件队列（包括出错、退出、排队的文件）中的索引，getFile可使用此索引
 * name : string,			// 文件名，不包括文件的路径。
 * size : number,			// 文件字节数
 * type : string,			// 客户端操作系统设置的文件类型
 * creationdate : Date,		// 文件的创建时间
 * modificationdate : Date,	// 文件的最后修改时间
 * filestatus : number		// 文件的当前状态，对应的状态代码可查看SWFUpload.FILE_STATUS 
 */
 
// 关闭 /取消 按钮 事件
function iconClose(event){
	if(typeof swfu != 'undefined' || swfu!=null){
		swfu.destroy();
	}
	$("#"+event.data.uploadBodyId).remove();
	$(".ke-dialog-mask").remove();
	insertAllStatus=true;
}

//全部清空
function clearUpload(){
	$("div[id^='SWFUpload_']").each(function(i,obj){
		deleteFile($(obj).attr('id'));
	});
}
//全部插入 初试话的提示 
function insertAllInit(){
	var message="<font color='red'>请先点击右上角的“开始上传”按钮上传图片</font>";
	addErrorMsg(message,true);
}

// 全部插入
function insertAllevent(){
	//alert(swfu.getStats().successful_uploads);//successful_uploads
	$("img[data-status='0']").each(function(i,obj){
			var showimg = new Image();
		  	showimg.src=$(obj).attr("src");
			var width = showimg.width;
			var height = showimg.height;
			var title=$(obj).attr("title");
			var url=$(obj).attr("src");
			//KE.plugin['multiimage'].insert("ArticleBody",url, title,width,height,"0","");
	});
	$("#iconClose").trigger("click");
}

// 添加错误信息
function addErrorMsg(message,isFadeOut){
	$("#"+swfu.customSettings.errorMsg_Id).empty().html(message);
	if(isFadeOut){
		setTimeout(function () {
			$("#"+swfu.customSettings.errorMsg_Id).children().fadeOut(1000);
		},parseInt(swfu.customSettings.errorMsg_fadeOutTime));
	}
}

//鼠标 移入 移出的 背景效果
function photoMouseOver(){
	$(this).addClass("ke-on");
}
function photoMouseOut(obj){
	$(this).removeClass("ke-on");
}

//swf 准备加载  为使用
function swfUploadPreLoad() {
	var self = this;
	var loading = function () {
		var longLoad = function () {
			document.getElementById("divLoadingContent").style.display = "none";
			document.getElementById("divLongLoading").style.display = "";
		};
		this.customSettings.loadingTimeout = setTimeout(function () {
				longLoad.call(self)
			},
			15 * 1000
		);
	};
	
	this.customSettings.loadingTimeout = setTimeout(function () {
			loading.call(self);
		},
		1*1000
	);
}

//swf 加载失败
function swfUploadLoadFailed() {
	//clearTimeout(this.customSettings.loadingTimeout);
	var message='<div id="divAlternateContent" style="background-color: #FFFF66; text-align:center;">'
			+'<a href="http://www.adobe.com/shockwave/download/download.cgi?P1_Prod_Version=ShockwaveFlash" target="_blank"><font color="red">请安装或者升级您的Flash插件!</font></a></div>';
	addErrorMsg(message,false);
}

//swf 加载 完成
function swfUploadLoaded() {
	//var self = this;
	//clearTimeout(this.customSettings.loadingTimeout);
	addErrorMsg("",false);
	$("#"+this.customSettings.btnUpload_ID).click(function (){swfu.startUpload()});
}


 //每次被加入 到列队中
function fileQueued(file){
	addReadyFileInfo(file.id,file.name,"等待上传");
}

//文件对话框选择完成
function fileDialogComplete(numFilesSelected, numFilesQueued) {
	try {
		if (numFilesQueued > 0) {
			$("#"+this.customSettings.clearAll_Id).bind("click",clearUpload);
		}
		if(this.settings.auto_upload){//是否要上传
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}
}
//都加入列队中 的错误信息
function fileQueueError(file, errorCode, message) {
	try {
		switch (errorCode) {
		case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:
			message = "<font color='red'>数量超过"+swfu.getSetting('file_upload_limit')+"张啦!</font>";
			break;
		case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:
			message =  "<font color='red'>文件超过"+swfu.getSetting('file_size_limit')+"啦!</font>";
			break;
		case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:
			message = "<font color='red'>文件不能为空哦!";
		case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:
			message = "<font color='red'>文件格式只能为"+swfu.getSetting('fileTypes')+"!</font>";
			break;
		default:
			message="<font color='red'>选择文件发生错误!"+"</font>";
			return;
		}
		addErrorMsg(message,true);
		//addReadyFileInfo(file.id,file.name,"上传错误")+"</font>";
	} catch (ex) {
		this.debug(ex);
	}
}


// 开始上传 
function uploadStart(file){
	$("#"+file.id).find(".ke-progressbar").css("display","block");
	$("#"+file.id).find(".ke-message").css("display","none");		
}
// 上传的进度
function uploadProgress(file, bytesLoaded) {
	try {
		var percent = Math.ceil((bytesLoaded / file.size) * 100);
		
		if(percent>100){
			percent=100;
		}
		var progress = new FileProgress(file);
		progress.setProgress(percent);
		if (percent === 100) {
			//progress.setStatus("建缩略图...");//正在创建缩略图...
			//progress.toggleCancel(false, this);
		}else {
			progress.setStatus("正在上传...");
			progress.toggleCancel(true, this);
		}
	}catch (ex) {
		this.debug(ex);
	}
}

// 上传到服务器后 的放回信息
function uploadSuccess(file, serverData) {
	try {
		var progress = new FileProgress(file);
		
		var data = eval("(" + serverData + ")");
		$("#"+file.id).find(".ke-img").attr("data-status",data.status);
		if (data.status == 0 || data.status == "0") {
			$("#"+file.id).find(".ke-img").attr("src",contextPath+data.message);
			progress.setStatus("上传成功");
			progress.toggleCancel();
			if(insertAllStatus){
				if(swfu.getStats().successful_uploads>0){
					$("#"+swfu.customSettings.insertAll_Id).unbind("click").bind("click",insertAllevent);
				}
				insertAllStatus=false;
			}
			return;
		}else if(data.status == 1 || data.status == "1"){
			progress.setStatus("上传失败");
		}else if(data.status == 2 || data.status == "2"){
			progress.setStatus("格式不对");
		}else if(data.status == 3 || data.status == "3"){
			progress.setStatus("文件超大");
		}else if(data.status == 4 || data.status == "4"){
			progress.setStatus("文件为空");
		}else if(data.status == 5 || data.status == "6"){
			progress.setStatus("路径不对");
		}else{
			progress.setStatus("上传失败");
		}
		progress.toggleCancel(false);
	} catch (ex) {
		this.debug(ex);
	}
}

// 选择文件后 添加到 操作层中
function addReadyFileInfo(fileid,fileName,message){
	var photoDiv='<div class="ke-inline-block ke-item" id="'+fileid+'">'
            +'<div class="ke-inline-block ke-photo" style="height:80px; width:80px;">'
			+'	<img src="'+contextPath+'/images/image.png" class="ke-img" data-status="-1" width="80" height="80" />'
			+'		<span class="ke-delete"></span>'
           	+'			<div class="ke-status">'
            +'				<div class="ke-progressbar" style="display: none;">'
			+'          		 <div class="ke-progressbar-bar">'
            +'               	 	<div class="ke-progressbar-bar-inner"></div>'
            +'           		 </div>'
            +'            		<div class="ke-progressbar-percent">0%</div>'
            +'        		 </div>'
            +'				<div class="ke-message">'+message+'</div>'
            +'   	 	</div>'
            +'    	</div>'
            +'  <div class="ke-name" alt="'+fileName+'">'+fileName+'</div>'
        	+'</div>';
        	
	$("#"+swfu.customSettings.photoContainer_Id).append(photoDiv);
	$("#"+fileid).find(".ke-photo").bind("mouseover",photoMouseOver).bind("mouseout",photoMouseOut);
	$("#"+fileid).find(".ke-delete").bind("click",function(){deleteFile(fileid)});
}


// 删除 单个文件 function deleteFile(fileId){
	$("#"+fileId).remove();
	swfu.cancelUpload(fileId,false);
}

// 单个 文件上传完  
function uploadComplete(file) { 
	try {
		/*  I want the next upload to continue automatically so I'll call startUpload here */
		if (this.getStats().files_queued > 0) {
			this.startUpload();
		}
	} catch (ex) {
		this.debug(ex);
	}
}
//上传的错误
function uploadError(file, errorCode, message) {
	var message =  "<font color='red'>文件上传出错!</font>";
	var progress = new FileProgress(file);
	var flag=false;
	try {
		switch (errorCode) {
		case SWFUpload.UPLOAD_ERROR.FILE_CANCELLED:
				progress.setStatus("取消上传");
				progress.toggleCancel(false);
				break;
		case SWFUpload.UPLOAD_ERROR.UPLOAD_STOPPED:
				progress.setStatus("停止上传");
				progress.toggleCancel(true);
		case SWFUpload.UPLOAD_ERROR.UPLOAD_LIMIT_EXCEEDED:
				progress.setStatus("文件超大");
				progress.toggleCancel(false);
				break;
		default:
			addErrorMsg(message,true);
			break;
		}
		//addFileInfo(file.id,imageName);
	} catch (ex3) {
		this.debug(ex3);
	}

}

// 根据 src 添加 图片
function addImage(src) {
	var newImg = document.createElement("img");
	newImg.style.margin = "5px";

	//document.getElementById("thumbnails").appendChild(newImg);
	if (newImg.filters) {
		try {
			newImg.filters.item("DXImageTransform.Microsoft.Alpha").opacity = 0;
		} catch (e) {
			// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
			newImg.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + 0 + ')';
		}
	} else {
		newImg.style.opacity = 0;
	}

	newImg.onload = function () {
		fadeIn(newImg, 0);
	};
	newImg.src = src;
}

function fadeIn(element, opacity) {
	var reduceOpacityBy = 5;
	var rate = 30;	// 15 fps


	if (opacity < 100) {
		opacity += reduceOpacityBy;
		if (opacity > 100) {
			opacity = 100;
		}

		if (element.filters) {
			try {
				element.filters.item("DXImageTransform.Microsoft.Alpha").opacity = opacity;
			} catch (e) {
				// If it is not set initially, the browser will throw an error.  This will set it if it is not set yet.
				element.style.filter = 'progid:DXImageTransform.Microsoft.Alpha(opacity=' + opacity + ')';
			}
		} else {
			element.style.opacity = opacity / 100;
		}
	}

	if (opacity < 100) {
		setTimeout(function () {
			fadeIn(element, opacity);
		}, rate);
	}
}

/* ******************************************
 *	This event comes from the Queue Plugin
 * ****************************************** */
function queueComplete(numFilesUploaded) {
	 //this.getStats().successful_uploads;
}

/* ******************************************
 *	FileProgress Object
 *	Control object for displaying file info
 * ****************************************** */

function FileProgress(file) {
	var fileID = file.id;
	this.fileProgressWrapper = $("#"+file.id).find(".ke-status"); //document.getElementById(this.fileProgressID);
	if (!this.fileProgressWrapper) {
		fadeIn(this.fileProgressWrapper, 0);
	}
}

FileProgress.prototype.setProgress = function (percentage) {
	$(this.fileProgressWrapper).find(".ke-progressbar-bar-inner").css("width",percentage + "%");
	$(this.fileProgressWrapper).find(".ke-progressbar-percent").text(percentage + "%");
};

FileProgress.prototype.setComplete = function () {
	$(this.fileProgressWrapper).find(".ke-progressbar-bar-inner").css("width","");
};

FileProgress.prototype.setCancelled = function () {
	$(this.fileProgressWrapper).find(".ke-progressbar-bar-inner").css("width","");
};

FileProgress.prototype.setStatus = function (status) {
	$(this.fileProgressWrapper).find(".ke-message").text(status);
};

FileProgress.prototype.toggleCancel = function (show) {
	if(typeof show =='undefined'){
		$(this.fileProgressWrapper).find(".ke-progressbar").css("display", "none");
		$(this.fileProgressWrapper).find(".ke-message").css("display","none");
	}else{
		$(this.fileProgressWrapper).find(".ke-progressbar").css("display", show ? "block" : "none");
		$(this.fileProgressWrapper).find(".ke-message").css("display", show ? "none" : "block");
	}
};
