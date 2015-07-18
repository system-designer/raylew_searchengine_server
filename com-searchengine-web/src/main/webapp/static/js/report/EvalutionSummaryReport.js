/**
 * @author WJ
 */
function chooseinfo(type){
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
		if(commentCount>0) return ;//如果已经加载评论，并且已经有数据了，则再次点击页签的时候，不重复加载
		$("#div_main").html("");
		$("#div_loading").show();
		commentCount=1;//需要加载数据的时候，默认+1，这样防止用户重复点击，多次请求，只请求一次，后台继续加载
		// 异步加载评语信息
		$.ajax({ 
			url: "/wapi/report/EvalutionSummaryReportComment?classId="+classId+"&studentUserId="+studentUserId+"&teacherUserId="+teacherUserId+"&dateType="+dateType+"&courseId="+courseId+"&resultType=2", 
			context: document.body, 
			success: function(res){
				if(res.data!=null){	
					if(res.data.lsCommentDetail!=null) commentCount=res.data.lsCommentDetail.length;//记忆评论数量，如果大于0，下次不加载
					for(var i=0;i<res.data.lsCommentDetail.length;i++){
						var clsName="divNormalImg";
						var levelName="teacherName";
						if(res.data.lsCommentDetail[i].score=="-1"){
							clsName="divBadImg";
							levelName="levelNameBad";
						} else if (res.data.lsCommentDetail[i].score=="1"){
							clsName="divGoodImg";
							levelName="levelNameGood";
						}
						var str  ="<div class='div_commentItem'>";
								str +="<div class='divCommentImg "+clsName+"'></div>";
										str +="<div id='div_line1'>";
											str +="<div style='float:left' >";
												str +="<span class='teacherName'>"+res.data.lsCommentDetail[i].teacherUserName+"</span> ";
												str +="评价了 <span class='teacherName'>"+res.data.lsCommentDetail[i].studentUserName+"</span>";
											str +="</div>";
											str +="<div  style='float:right'>";
												str +="<span class='date'>"+res.data.lsCommentDetail[i].updateTime+"</span>";
											str +="<div>";
										str +="</div>";
									str +="</div>";
								str +="<div id='div_line2' style='clear:both'>";
								str +="<span class='"+levelName+"'>"+res.data.lsCommentDetail[i].levelName+"</span>";
								str +="<div id='div_line3'>";
								str +="<span class='sp_comment'>"+ res.data.lsCommentDetail[i].comment+"<span>" ;
								str +="</div>";
								str+="</div>";
							str +="</div>";
						$("#div_main").append(str);
					}
				}
				$("#div_loading").hide(); 
			},
			error:function(data){
				commentCount=0;//加载错误后，置为0，表示，用户可以重复点击进行加载数据
				$("#div_main").html("加载失败，请稍后再试..");
				$("#div_loading").hide();
			}
		}
	);
	}
}