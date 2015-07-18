## iSearches Server Api接口定义 ##

统一约定： 

1) 域名为，http://127.0.0.1:8081，api中直接省略

2) 如果不指定Request Method，默认为同时支持 GET/POST

3) 如果是POST请求，请将Content-Type设置为：application/x-www-form-urlencoded

4）返回数据格式约定：

	code 	int		请求结果状态码 （0:失败，1：成功）
	msg 	String 	请求结果描述 
	data 	JSON 	业务JSON对象
	timestamp	Date 响应时间戳

## 搜索相关 ##

返回搜索结果

	请求地址：/api/query/getList
	请求参数：
			queryContent String 搜索内容  
	返回结果:
			1）空-没有搜索结果
			2）非空-搜索结果json
	返回结果示例：
		    "data": [
				{
				"id": 1061904154247168,
				"url": "http://-iven-.lofter.com/post/bf79_63540c0",
				"title": "你的手温,像一团小小的火焰,在-IVEN-木罐",
				"charset": "utf-8",
				"keywords": "创意,设计,日绘,插画,插画推荐,平面设计,伊文兔,漫画,晚安,日记",
				"description": "你的手温,像一团小小的火焰,在你我之间，我看到了它的光亮。",
				"wordCount": 416,
				"domain": "http://-iven-.lofter.com",
				"savePath": "lofter.com/-iven-/post/1426567227804.html",
				"rootDomain": "lofter.com",
				"protocol": "http",
				"contentMD5": "0bfbe584a19f14fbf88351c922411150",
				"createdTime": "2015-03-17 12:40:27",
				"lastUpdatedTime": "2015-03-17 12:40:27",
				"analyzed": true
				}
				......
		    ]


记录用户搜索行为

	请求地址：/api/query/record
	请求参数：
			userId Long 用户
			queryId Long 搜索标识 
	返回结果:
			1）true-记录成功
			2）false-记录失败
	返回结果示例：
		    "data": {
		        true
		    }

