/**
 * @author WJ
 */
var showType=0;
var isLoading=0;
var lastRow={}; //加载的最后一条数据
var recordIds=[];//记录已经下载到本地的记录Id
lastRow.id="";
lastRow.lastDateTime="";
function chooseinfo(type){
	showType=type;
	if(type==0){//显示评价
		$("#div_Metric").show();
		$("#div_Comment").hide();
		$("#a1").addClass("selected");
		$("#a2").removeClass("selected");
	} else {//显示评语
		$("#div_Metric").hide();
		$("#div_Comment").show();
		$("#a2").addClass("selected");
		$("#a1").removeClass("selected");
        if(isLoading==0)
            loadData();
	}
}

$(document).ready(function(){  
    var range = 250;             //距下边界长度/单位px  
    var totalheight = 0;   
    $(window).scroll(function(){  
    	if(showType==1&&isLoading==0){ //显示评语，下拉才会加载评语
        var srollPos = $(window).scrollTop();    //滚动条距顶部距离(页面超出窗口的高度)
        totalheight = parseFloat($(window).height()) + parseFloat(srollPos);  
        if(($(document).height()-range) <= totalheight ) {  
        	loadData();
        }  }
    });  
}); 
function loadData(){
    isLoading=1;//加载中
    $("#div_showMore").hide();
	$("#div_loading").show();
    $.ajax({ 
                url: "/wapi/report/EvalutionSummaryReportComment?classId="+classId+"&studentUserId="+studentUserId+"&teacherUserId="+teacherUserId+"&dateType="+dateType+"&courseId="+courseId+"&resultType=2&lastId="+lastRow.id+"&lastDateTime="+lastRow.lastDateTime, 
                context: document.body, 
                success: function(res){
                    if(res.data!=null){ 
                        if(res.data.lsCommentDetail!=null) commentCount=res.data.lsCommentDetail.length;//记忆评论数量，如果大于0，下次不加载
                        for(var i=0;i<res.data.lsCommentDetail.length;i++){
                        	//判断界面是否已经加载，如果加载了，就不再加载了
                        	var hasShow=false;
                        	for(var j=0;j<recordIds.length;j++){
                        		if(res.data.lsCommentDetail[i].recordId.toString()==recordIds[j]){
                        				hasShow=true;    
                        				break;
                        		}
                        	}
                        	if(hasShow==true){//如果界面已经渲染了，则不显示
                        		continue;
                        	}else{
                        		recordIds.push(res.data.lsCommentDetail[i].recordId);
                        	}
                        	if(i==res.data.lsCommentDetail.length-1){//记忆最旧的数据
                        		lastRow.id=res.data.lsCommentDetail[i].recordId;
                        		lastRow.lastDateTime=res.data.lsCommentDetail[i].createTime;
                        	}
                            var clsName="divNormalImg";
                            var levelName="teacherName";
                            if(res.data.lsCommentDetail[i].score=="-1"){
                                clsName="divBadImg";
                                levelName="levelNameBad";
                            } else if (res.data.lsCommentDetail[i].score=="1"){
                                clsName="divGoodImg";
                                levelName="levelNameGood";
                            }
                            if(res.data.lsCommentDetail[i].levelName==""){
                            	 clsName="divOnlyCommentImg";
                                 levelName="levelNameGood";
                            }                            	
                            var str  ="<div class='div_commentItem b d1 k'>";
                                    str +="<div class='divCommentImg "+clsName+"'></div>";
                                            str +="<div id='div_line1'>";
                                                str +="<div style='float:left' >";
                                                    str +="<span class='teacherName'>"+res.data.lsCommentDetail[i].teacherUserName+"</span> ";
                                                    str +="<span style='font-size:15px'>评价了</span> <span class='teacherName'>"+res.data.lsCommentDetail[i].studentUserName+"</span>";
                                                str +="</div>";
                                                str +="<div  style='float:right'>";
                                                    str +="<span class='date'>"+res.data.lsCommentDetail[i].updateTime+"</span>";
                                                str +="<div>";
                                            str +="</div>";
                                        str +="</div>";
                                    str +="<div id='div_line2' style='clear:both;padding-top:5px'>";
                                    str +="<span class='"+levelName+"'>"+res.data.lsCommentDetail[i].levelName+"</span>";
                                    str +="<div id='div_line3' style='padding-top:5px'>";
                                    str +="<span class='sp_comment'>"+ res.data.lsCommentDetail[i].comment+"<span>" ;
                                    str +="</div>";
                                    str+="</div>";
                                str +="</div>";
                            $("#div_main").append(str);
                        }
                    }
                    $("#div_loading").hide(); 
                    $("#div_showMore").show();
                    isLoading=0;
                },
                error:function(data){
                    $("#div_loading").hide();
                    $("#div_showMore").show();
                    isLoading=0;
                }
            }
        );
}
 