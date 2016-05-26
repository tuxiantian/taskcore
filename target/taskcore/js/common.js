Util = {
	/**
	 * 取消事件冒泡
	 * @param {Object}
	 *            e 事件对象
	 */
	stopBubble : function(e) {
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			// ie
			window.event.cancelBubble = true;
		}
	},
	/**
	 * 入参转码
	 * @param {string}
	 * 		json格式
	 */
	transCoding : function(json){
		var temp=encodeURIComponent(json);
		temp=CryptoJS.enc.Utf8.parse(temp);
		temp=CryptoJS.enc.Base64.stringify(temp);
		return temp;
	},
	/**
	 * 入参转码
	 * @param {string}
	 * 		json格式
	 */
	transDecoding : function(objStr){
		var words = CryptoJS.enc.Base64.parse(objStr);
		words = words.toString(CryptoJS.enc.Utf8);
		words = decodeURIComponent(words)
		return words;
	}
};
/**
 * 日期时间处理工具
 * 
 * @namespace Util
 * @class date
 */
Util.date = {
	/**
	 * 格式化日期时间字符串
	 * 
	 * @method dateTime2str
	 * @param {Date}
	 *            dt 日期对象
	 * @param {String}
	 *            fmt 格式化字符串，如：'yyyy-MM-dd hh:mm:ss'
	 * @return {String} 格式化后的日期时间字符串
	 */
	dateTime2str : function(dt, fmt) {
		var z = {
			M : dt.getMonth() + 1,
			d : dt.getDate(),
			h : dt.getHours(),
			m : dt.getMinutes(),
			s : dt.getSeconds()
		};
		fmt = fmt.replace(/(M+|d+|h+|m+|s+)/g, function(v) {
			return ((v.length > 1 ? "0" : "") + eval('z.' + v.slice(-1)))
					.slice(-2);
		});
		return fmt.replace(/(y+)/g, function(v) {
			return dt.getFullYear().toString().slice(-v.length);
		});
	},
	/**
	 * 根据日期时间格式获取获取当前日期时间
	 * 
	 * @method dateTimeWrapper
	 * @param {String}
	 *            fmt 日期时间格式，如："yyyy-MM-dd hh:mm:ss";
	 * @return {String} 格式化后的日期时间字符串
	 */
	dateTimeWrapper : function(fmt) {
		if (arguments[0])
			fmt = arguments[0];
		return this.dateTime2str(new Date(), fmt);
	},
	/**
	 * 获取当前日期时间
	 * 
	 * @method getDatetime
	 * @param {String}
	 *            fmt [optional,default='yyyy-MM-dd hh:mm:ss'] 日期时间格式。
	 * @return {String} 格式化后的日期时间字符串
	 */
	getDatetime : function(fmt) {
		return this.dateTimeWrapper(fmt || 'yyyy-MM-dd hh:mm:ss');
	},
	/**
	 * 获取当前日期时间+毫秒
	 * 
	 * @method getDatetimes
	 * @param {String}
	 *            fmt [optional,default='yyyy-MM-dd hh:mm:ss'] 日期时间格式。
	 * @return {String} 格式化后的日期时间字符串
	 */
	getDatetimes : function(fmt) {
		var dt = new Date();
		return this.dateTime2str(dt, fmt || 'yyyy-MM-dd hh:mm:ss') + '.'
				+ dt.getMilliseconds();
	},
	/**
	 * 获取当前日期（年-月-日）
	 * 
	 * @method getDate
	 * @param {String}
	 *            fmt [optional,default='yyyy-MM-dd'] 日期格式。
	 * @return {String} 格式化后的日期字符串
	 */
	getDate : function(fmt) {
		return this.dateTimeWrapper(fmt || 'yyyy-MM-dd');
	},
	/**
	 * 获取当前时间（时:分:秒）
	 * 
	 * @method getTime
	 * @param {String}
	 *            fmt [optional,default='hh:mm:ss'] 日期格式。
	 * @return {String} 格式化后的时间字符串
	 */
	getTime : function(fmt) {
		return this.dateTimeWrapper(fmt || 'hh:mm:ss');
	}
};
/**
 * 通过 HTTP 请求加载远程数据，底层依赖jQuery的AJAX实现。当前接口实现了对jQuery AJAX接口的进一步封装。
 */
Util.ajax = {
	/**
	 * 请求状态码
	 * 
	 * @type {Object}
	 */
	reqCode : {
		/**
		 * 成功返回码 0000
		 * 
		 * @type {Number} 1
		 * @property SUCC
		 */
		SUCC : 0
	},
	/**
	 * 请求的数据类型
	 * 
	 * @type {Object}
	 * @class reqDataType
	 */
	dataType : {
		/**
		 * 返回html类型
		 * 
		 * @type {String}
		 * @property HTML
		 */
		HTML : "html",
		/**
		 * 返回json类型
		 * 
		 * @type {Object}
		 * @property JSON
		 */
		JSON : "json",
		/**
		 * 返回text字符串类型
		 * 
		 * @type {String}
		 * @property TEXT
		 */
		TEXT : "text"
		//JSONP : "jsonp"
	},
	/**
	 * 超时,默认超时30000ms
	 * 
	 * @type {Number} 10000ms
	 * @property TIME_OUT
	 */
	TIME_OUT : 60000,
	/**
	 * 显示请求成功信息
	 * 
	 * @type {Boolean} false
	 * @property SHOW_SUCC_INFO
	 */
	SHOW_SUCC_INFO : false,
	/**
	 * 显示请求失败信息
	 * 
	 * @type {Boolean} false
	 * @property SHOW_ERROR_INFO
	 */
	SHOW_ERROR_INFO : false,
	/**
	 * GetJson是对Util.ajax的封装,为创建 "GET" 请求方式返回 "JSON"(text) 数据类型
	 * @param {String}
	 *            url HTTP(GET)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] GET请求成功回调函数
	 */
	getJson : function(url, cmd, callback) {
		if (arguments.length !== 3)
			callback = cmd, cmd = '';
		dataType = this.dataType.TEXT;
		// var _this = this;
		// setTimeout( function(){_this.ajax(url, 'GET', cmd, dataType,
		// callback)},1000);
		this.ajax(url, 'GET', cmd, dataType, callback);
	},
	/**
	 * PostJsonAsync是对Util.ajax的封装,为创建 "POST" 请求方式返回 "JSON"(text) 数据类型,
	 * 采用同步阻塞的方式调用ajax
	 * @param {String}
	 *            url HTTP(POST)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] POST请求成功回调函数
	 */
	postJsonSync : function(url, cmd, callback) {
		dataType = this.dataType.TEXT;
		// dataType = this.dataType.JSONP;
		this.ajax(url, 'GET', cmd, dataType, callback, true);
	},
	/**
	 * PostJson是对Util.ajax的封装,为创建 "POST" 请求方式返回 "JSON"(text) 数据类型
	 * @param {String}
	 *            url HTTP(POST)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] POST请求成功回调函数
	 */
	postJson : function(url, cmd, callback,flag) {
        // if(!flag){Util.loading.showLoading();}
		dataType = this.dataType.TEXT;
		// dataType = this.dataType.JSONP;
		// var _this = this;
		// setTimeout( function(){_this.ajax(url, 'POST', cmd, dataType,
		// callback)},1000);
		this.ajax(url, 'GET', cmd, dataType, callback,'',flag);
	},
	/**
	 * loadHtml是对Ajax load的封装,为载入远程 HTML 文件代码并插入至 DOM 中
	 * @param {Object}
	 *            obj Dom对象
	 * @param {String}
	 *            url HTML 网页网址
	 * @param {Function}
	 *            callback [optional,default=undefined] 载入成功时回调函数
	 */
	loadHtml : function(obj, url, data, callback) {
		$(obj).load(url, data, function(response, status, xhr) {
			callback = callback ? callback : function() {
			};
			status == "success" ? callback(true) : callback(false);
		});
	},
	/**
	 * loadTemp是对handlebars 的封装,请求模版加载数据
	 * @param {Object}
	 *            obj Dom对象
	 * @param {Object}
	 *            temp 模版
	 * @param {Object}
	 *            data 数据
	 */
	loadTemp : function(obj, temp, data) {
		var template = Handlebars.compile((temp instanceof jQuery)?temp.html():temp);
		obj = (obj instanceof jQuery)?obj:$(obj);
		obj.html(template(data));
	},
	/**
	 * GetHtml是对Util.ajax的封装,为创建 "GET" 请求方式返回 "hmtl" 数据类型
	 * @param {String}
	 *            url HTTP(GET)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] GET请求成功回调函数
	 */
	getHtml : function(url, cmd, callback) {
		if (arguments.length !== 3)
			callback = cmd, cmd = '';
		dataType = this.dataType.HTML;
		this.ajax(url, 'GET', cmd, dataType, callback);
	},
	/**
	 * GetHtmlSync是对Util.ajax的封装,为创建 "GET" 请求方式返回 "hmtl" 数据类型
	 * 采用同步阻塞的方式调用ajax
	 * @param {String}
	 *            url HTTP(GET)请求地址
	 * @param {Object}
	 *            cmd json对象参数
	 * @param {Function}
	 *            callback [optional,default=undefined] GET请求成功回调函数
	 */
	getHtmlSync : function(url, cmd, callback) {
		if (arguments.length !== 3)
			callback = cmd, cmd = '';
		dataType = this.dataType.HTML;
		this.ajax(url, 'GET', cmd, dataType, callback,true);
	},
	/**
	 * 基于jQuery ajax的封装，可配置化
	 * 
	 * @method ajax
	 * @param {String}
	 *            url HTTP(POST/GET)请求地址
	 * @param {String}
	 *            type POST/GET
	 * @param {Object}
	 *            cmd json参数命令和数据
	 * @param {String}
	 *            dataType 返回的数据类型
	 * @param {Function}
	 *            callback [optional,default=undefined] 请求成功回调函数,返回数据data和isSuc
	 */
	ajax : function(url, type, cmd, dataType, callback, sync,flag) {
		var param = "";
		/*if (typeof (cmd) == "object"){
			param = JSON.stringify(cmd);
		}else if(typeof(cmd)=="string"){
			param = cmd;
		}*/
		//cmd = this.jsonToUrl(cmd);
		async = sync ? false : true;
		var thiz = Util.ajax;
		var cache = (dataType == "html") ? true : false;
		$.ajax({
			url : url,
			type : type,
			data : cmd,
			// data : encodeURI(cmd),
			/*processData: false,  	// 告诉jQuery不要去处理发送的数据
			contentType: false,		// 告诉jQuery不要去设置Content-Type请求头*/
			cache : cache,
			dataType : dataType,
			// jsonp: "jsonpcallback",
			// contentType: "application/jsonp; charset=utf-8",
			async : async,
			timeout : thiz.TIME_OUT,
			beforeSend : function(xhr) {
				xhr.overrideMimeType("text/plain; charset=utf-8");
			},
			success : function(data) {
				if (!data) {
					return;
				}
				if (dataType == "html") {
					callback(data, true);
					return;
				}
				try {
					data = eval('(' + data + ')');
					if (data.returnCode=='BUSIOPER=RELOGIN') {
						var postId = Util.cookie.get('postId');
						if (postId=='13' || postId=='14' || postId=='15') {
							if (top) {
				                top.document.location.href = '../../login86.html';
				            }else{
				                window.location.href = '../../login86.html';
				            }
						}else{
							if (top) {
				                top.document.location.href = '../../login.html';
				            }else{
				                window.location.href = '../../login.html';
				            }
						}
						return;
					}
				} catch (e) {
					alert("JSON Format Error:" + e.toString());
				}
				var isSuc = thiz.printReqInfo(data);
				if (callback && data) {
					callback(data || {}, isSuc);
				}
			},
			error : function() {
			    var retErr ={};
			    retErr['returnCode']="404";
			    retErr['returnMessage']="网络异常或超时，请稍候再试！"; 
				callback(retErr, false);
			},
            complete:function(){
                //if(!flag){Util.loading.hideLoading();}
            }
		});
	},
	/**
	 * 打开请求返回代码和信息
	 * 
	 * @method printRegInfo
	 * @param {Object}
	 *            data 请求返回JSON数据
	 * @return {Boolean} true-成功; false-失败
	 */
	printReqInfo : function(data) {
		if (!data)
			return false;
		var code = data.returnCode, msg = data.returnMessage, succ = this.reqCode.SUCC;
		if (code == succ) {
			if (this.SHOW_SUCC_INFO) {
				// Util.msg.infoCorrect([ msg, ' [', code, ']' ].join(''));
				Util.msg.infoCorrect(msg);
			}
		} else {
			// Util.msg.infoAlert([ msg, ' [', code, ']' ].join(''));
			if (this.SHOW_ERROR_INFO) {
				Util.dialog.tips(msg);
			}
		}
		return !!(code == succ);
	},
	/**
	 * JSON对象转换URL参数
	 * 
	 * @method printRegInfo
	 * @param {Object}
	 *            json 需要转换的json数据
	 * @return {String} url参数字符串
	 */
	jsonToUrl : function(json) {
		var temp = [];
		for ( var key in json) {
			if (json.hasOwnProperty(key)) {
				var _key = json[key] + "";
				_key = _key.replace(/\+/g, "%2B");
				_key = _key.replace(/\&/g, "%26");
				temp.push(key + '=' + _key);
			}
		}
		return temp.join("&");
	},
	msg : {
		"suc" : function(obj, text) {
			var _text = text || "数据提交成功！";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-suc-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"war" : function(obj, text) {
			var _text = text || "数据异常，请稍后尝试!";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-war-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"err" : function(obj, text) {
			var _text = text || "数据提交失败!";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-err-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"load" : function(obj, text) {
			var _text = text || "正在加载中，请稍候...";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-loader"></i>' + _text + '</h3>'
							+ '</div>').show();
		},
		"inf" : function(obj, text) {
			var _text = text || "数据提交中，请稍等...";
			$(obj).html(
					'<div class="msg-hint">' + '<h3 title=' + _text
							+ '><i class="hint-icon hint-inf-s"></i>' + _text
							+ '</h3>' + '</div>').show();
		},
		"errorInfo" : function(obj, text) {
			var _text = text || "数据提交失败!";
			$(obj)
					.html(
							'<div class="ui-tiptext-container ui-tiptext-container-message"><p class="ui-tiptext ui-tiptext-message">'
									+ '<i class="ui-tiptext-icon icon-message" title="阻止"></i>'
									+ _text + '</p>' + '</div>').show();
		}
	}
};

Util.browser = {
	/**
	 * 获取URL地址栏参数值
	 * name 参数名
	 * url [optional,default=当前URL]URL地址
	 * @return {String} 参数值
	 */
	getParameter : function(name, url) {
		var paramStr = url || window.location.search;
		paramStr = paramStr.split('?')[1];
		if ((!paramStr)||paramStr.length == 0) {return null;}
		var params = paramStr.split('&');
		for ( var i = 0; i < params.length; i++) {
			var parts = params[i].split('=', 2);
			if (parts[0] == name) {
				if (parts.length < 2 || typeof (parts[1]) === "undefined"
						|| parts[1] == "undefined" || parts[1] == "null")
					return '';
				return parts[1];
			}
		}
		return null;
	}
};
/**
 * 常用正则表达式
 */
Util.validate = {
	/**
	 * 格式校验方法
	 * 
	 * @method Check
	 * @param {String}
	 *            type 验证类型
	 * @param {String}
	 *            value 验证值
	 */
	Check : function(type, value) {
		var _reg = this.regexp[type];
		if (_reg == undefined) {
			alert("Type " + type + " is not in the data");
			return false;
		}
		var reg;
		if (typeof _reg == "string") {
			reg = new RegExp(_reg);
		} else if ((typeof _reg) == "function") {
			return _reg(value);
		} else {
			reg = _reg[type];
		}
		return reg.test(value);
	}
};
Util.sms = {};
Util.sms.formatStr = function(value) {
    if (value) {	
        if (arguments.length > 1) {
            for (var i = 1; i < arguments.length; i++) {
                value = value.replace(new RegExp("\\{" + (i - 1) + "\\}", 'g'), arguments[i]);
            }
        }
    }
    return value;
};

/*
*	分页
*/
Util.pagination = function( pindex , onepage , obj , formStr ){
    Util.loading.create('.tablewidth');//添加loading提示
    var pageIndex = pindex;
    var pageParams = obj;
    var str = formStr; //form序列化的数据 
	pageParams.page_index = pindex;   //弹出窗口修改数据后，刷新当前页的数据需要用到这些数据.
	pageParams.page_params = formStr;
    Util.ajax.postJson( pageParams.url ,'start='+(pageIndex*pageParams.items_per_page)+'&limit='+pageParams.items_per_page+'&'+str , function(json,state){
		if (pageParams.pagination instanceof jQuery) {
			var _page = pageParams.pagination;
		}else{
			var _page = $("#"+pageParams.pagination);
		}
    	var _jcontrol = $("#J_table_control");
		if(state){
			if (pageParams.tablewrap instanceof jQuery) {
				if (typeof(pageParams.tabletpl) == 'function'){
					var template = pageParams.tabletpl;
					pageParams.tablewrap.html(template(json));
				}else{
					var template = Handlebars.compile(pageParams.tabletpl.html());
					pageParams.tablewrap.html(template(json));
				}
			}else{
	            Util.ajax.loadTemp('#'+pageParams.tablewrap,$('#'+pageParams.tabletpl),json);//加载模板
			}
            //触发回调函数
            if (typeof obj.pageCallback == 'function') {
                obj.pageCallback.call(_page, json);
            }
			//分页调用-只初始化一次  
	        if( onepage ){
	    		if(json.bean.total<1){
	    			_jcontrol.hide();
	    			_page.html('<p class="ui-tiptext ui-tiptext-warning">'+
							    '<i class="ui-tiptext-icon" title="警告"></i>'+
							    '没有查询到数据,请更换查询条件!'+
								'</p>');
	    			_page.next().hide();
                    _page.prev().hide();
	    		}else{
	    			_jcontrol.show();
		            _page.pagination( json.bean.total , {
		                'items_per_page'      : pageParams.items_per_page,
		                'current_page': pageIndex ,
		                'num_display_entries' : 3,
		                'num_edge_entries'    : 1,  
		                'link_to': '#tradeRecordsIndex' ,
		                'prev_text'           : "<",  
		                'next_text'           : ">",  
		                'call_callback_at_once' : false,  //控制分页控件第一次不触发callback.
		                'callback'            : function(page_index, jq){  
													Util.pagination(page_index , false , pageParams , str );  
												}  
		            });
		            _page.next().text("共"+json.bean.total+"条").show();

		            if(_page.prev().length<1){
                        /*var $bf = $('<div class="fn-right fn-pt5 fn-pr10">'+
                            '每页<input type="text" class="element text" style="width:24px;" id="J_pagenum" title="输入数量后,请按回车" />条'+
                        '</div>');
			            _page.before($bf);
			            $bf.find("input").keyup(function(e){
                            var _self = $(this);
                            var newVal = _self.val().replace(/[^\d]/g,'');
                            //newVal = (newVal<1) ? 10 : newVal;
                            newVal = (newVal>500) ? 500 : newVal ;
                            _self.val(newVal);
                        }).keypress(function(e){
			                if(e.which==13){
                                var _self = $(this);
                                if(_self.val()<10){
                                    _self.val(10);
                                }
			                	pageParams.items_per_page = _self.val();
			                	$("#J_search").click();
			                }
			            });*/
		            }else{
                        _page.prev().show();
                    }
	    		}
	        }
		}else{
			var _errorMsg = json.returnMessage ? ('查询数据失败！原因：'+json.returnMessage) : '加载数据失败,请稍后再试!' ;
			_page.html('<p class="ui-tiptext ui-tiptext-warning">'+
				    '<i class="ui-tiptext-icon" title="警告"></i>'+
				    ''+_errorMsg+
					'</p>');
			_jcontrol.hide();
			_page.next().hide();
            _page.prev().hide();
            var nothingHtml = '<div class="ui-loading"><h1>暂时没有数据!</h1></div>';
            if (pageParams.tablewrap instanceof jQuery) {
				pageParams.tablewrap.html(nothingHtml);
			}else{
				$('#'+pageParams.tablewrap).html(nothingHtml);
			}
		}
        Util.loading.close('.tablewidth'); //隐藏loading提示
	});
};

/*
 *	Loading
 */
Util.loading = {
	create:function(obj,text){
		text = text?text:'正在加载中，请稍候...';
        $(obj).block({
            message: '<div class="fn-loading">'+text+'</div>', 
            css: { border:'1px solid #DDD', padding:"10px 20px",textAlign:"left",width:'20%'},
            overlayCSS:{
                backgroundColor: '#333', 
                opacity:  0.2, 
                cursor: 'wait' 
            }
        });
	},
	close:function(obj){
    	$(obj).unblock(); 
	}
};


/*
*	窗口控制
*	如果在页面嵌入的iframe中打开dialog，需要在params中传入top
*	top: 顶层页面
*/
Util.dialog = {
	openDiv: function(params){
		if (params.top) {
			if (top.dialog.get(params.id)) {
				top.dialog.get(params.id).remove();
			};
			var d = params.top.dialog({
				id:params.id,
				fixed: true,
				// quickClose: true,	//点击空白处弹出框消失
			    title: params.title,
			    content: params.content,
			    okValue: params.okVal,
		        ok: params.okCallback,
		        cancelValue: params.cancelVal,
		        cancel: params.cancelCallback,
		        onclose: params.closeCallback	//关闭对话框回调函数
			});
		}else{
			var d = dialog({
				id:params.id,
				fixed: true,
				// quickClose: true,	//点击空白处弹出框消失
			    title: params.title,
			    content: params.content,
			    okValue: params.okVal,
		        ok: params.okCallback,
		        cancelValue: params.cancelVal,
		        cancel: params.cancelCallback,
		        onclose: params.closeCallback	//关闭对话框回调函数
			});
		}
		d.width(params.width);
		d.height(params.height);
		if (params.modal) {
			d.showModal();
		}else{
			d.show();
		}
		return d;
	},
	tips: function(content,top, delay){
		if (top) {
			var d = top.dialog({
				fixed: true,
				quickClose: true,	//点击空白处弹出框消失
			    content: content
			});
		}else{
			var d = dialog({
				fixed: true,
				quickClose: true,	//点击空白处弹出框消失
			    content: content
			});
		}
		d.show();
		setTimeout(function () {
		    d.close().remove();
		}, delay || 2000);
	},
	confirm: function(params){
		if (params.top) {
			var d = params.top.dialog({
				id:'D_confirm',
	        	title: '提示',
				fixed: true,
			    content: params.content,
			    okValue: params.okVal?params.okVal:'确认',
		        ok: params.okCallback,
		        cancelValue: params.cancelVal?params.cancelVal:'取消',
		        cancel :function(){
		            return;
		        }
			});	
		}else{
			var d = dialog({
				id:'D_confirm',
	        	title: '提示',
				fixed: true,
			    content: params.content,
			    okValue: params.okVal?params.okVal:'确认',
		        ok: params.okCallback,
		        cancelValue: params.cancelVal?params.cancelVal:'取消',
		        cancel :function(){
		            return;
		        }
			});	
		}
		d.showModal();
	},
	close: function(id,top){
		if (top) {
	        top.dialog.get(id).close();
		}else{
	        dialog.get(id).close();
		}
	}, 
	bubble:function(arguments){
		//console.log(typeof(arguments));
		/* var d = dialog({
		    content: 'Hello World!',
		    quickClose: true// 点击空白处快速关闭
		}); */
		var d = null;
		if (typeof(arguments) === 'object' && arguments.element){
			arguments.content = arguments.content || '没有内容';
			arguments.quickClose = arguments.quickClose == null ? true : arguments.quickClose ;
			d = dialog(arguments);
			d.show(arguments.element.length ? arguments.element[0] :arguments.element);
		}else{
			d.show();
		}
		
	}, 
	zTree:function(userSetting){
		var d = null;
		var zTree = null;
		var params = {
			id : 'd1',    //弹出对话框的id
			skin:'zx-popup-tree', 
			fixed:true, 
			padding: 3, 
			title : '提示信息', //左上角提示标题
			content : $('#dialogContent').html(), //具体提示内容
			button: [
				{
					value: '确定',
					callback: function () {
						var nodes = zTree.getSelectedNodes();
						userSetting && userSetting.onSelect(nodes[0]);
						//return false;
					},
					autofocus: true
				},
				{
					value: '取消',
					callback: function () { }
				}
			], 
			width : '270',  //对话框宽度
			height : '400'  //对话框高度
		}
		if (userSetting && userSetting.top){
			d = new top.dialog(params);
		}else{
			d = new dialog(params);
		}
		
		d.show();
		var setting = { };
		if (userSetting && userSetting.dataUrl){
			Util.ajax.postJson(userSetting.dataUrl,{},function(json,status){
	            if (status) {
	            	zTree = $.fn.zTree.init($('.ztree', d.node), setting, json.beans);
	            }else{
	                Util.dialog.tips(json.returnMessage||'查询失败，请重试！');
	            }
	        })
		}else{
			zTree = $.fn.zTree.init($('.ztree', d.node), setting, userSetting.zNodes);
		}
		
		
		//return zTree;
	}
};


/*
 * 功能:删除数组元素.
 * 返回:在原数组上删除后的数组
 */
Util.Arrays = {
	// 参数:dx删除元素的下标.
	removeByIndex : function(arrays , dx){
		if(isNaN(dx)||dx>arrays.length){return false;}
		for(var i=0,n=0;i<arrays.length;i++){
			if(arrays[i]!=arrays[dx]){
				arrays[n++]=arrays[i]
			}
		}
		arrays.length-=1
		return arrays;
	},
	//删除指定的item,根据数组中的值
	removeByValue : function(arrays, item ){
		for( var i = 0 ; i < arrays.length ; i++ ){
			if( item == arrays[i] ){
				break;
			}
		}
		if( i == arrays.length ){return;}
		for( var j = i ; j < arrays.length - 1 ; j++ ){
			arrays[ j ] = arrays[ j + 1 ];
		}
		arrays.length--;
		return arrays;
	}
};

/**
 * cookie 操作，设置，取出，删除
 *
 * @namespace Rose
 * @class string
 */
Util.cookie = {
	/**
	 * 显示当前对象名称路径
	 * 
	 * @method toString
	 * @return {String} 'Rose.string'
	 */
	toString : function() {
		return 'Rose.cookie';
	},  
    /**
	 * 设置一个cookie
	 * @method set
	 * @param {String} name cookie名称
	 * @param {String} value cookie值
	 * @param {String} path 所在路径
	 * @param {Number} expires 存活时间，单位:小时
	 * @param {String} domain 所在域名
	 * @return {Boolean} 是否成功
	 */
    set : function(name, value, expires, path, domain) {
       	var str = name + "=" + encodeURIComponent(value);
   		if (expires != undefined && expires != null && expires != '') {
   			if (expires == 0) {expires = 100*365*24*60;}
   			var exp = new Date();
   			exp.setTime(exp.getTime() + expires*60*1000);
   			str += "; expires=" + exp.toGMTString();
   		}
   		if (path) {
   			str += "; path=" + path;
   		} else {
   			str += "; path=/";
   		}
   		if (domain) {str += "; domain=" + domain;}
   		document.cookie = str;
    },
    /**
	 * 获取指定名称的cookie值
	 * @method get
	 * @param {String} name cookie名称
	 * @return {String} 获取到的cookie值
	 */
	get : function(name) {
		var v = document.cookie.match('(?:^|;)\\s*' + name + '=([^;]*)');
		return v ? decodeURIComponent(v[1]) : null;
	},
	/**
	 * 删除指定cookie,复写为过期
	 * @method remove 
	 * @param {String} name cookie名称
	 * @param {String} path 所在路径
	 * @param {String} domain 所在域
	 */
	remove : function(name, path, domain) {
		document.cookie = name + "=" +
			((path) ? "; path=" + path : "") +
			((domain) ? "; domain=" + domain : "") +
			"; expires=Thu, 01-Jan-70 00:00:01 GMT";
	}
};

//将Util对象注册为符合AMD规范的模块，可使用requireJS模块化加载
if (typeof define === "function" && define.amd) {
    define('Util',[], function () {
    	return Util;
    });
}

/*
    NiceSelect ： 获取下拉框
    参数设置：
    {
        url:"添加",
        datas:"",
        id : "J_form_add",
        name : "J_form_add",
        handler:function(){
          //do...
        }
    }
    eg:
    {
        url:"business?service=ajax&page=Common&listener=getStaticData",
        datas:"codeType="+codeType,
        id:"testId",
        name:"testId"
    }
*/
(function($){
    $.fn.extend({
        "NiceSelect":function(options){
            var _self = this;
            options = $.extend({
                url:"../../data/selectDatas.json",
                datas:"codeType=test",
                id:"testId",
                name:"testId",
                key :"value",
                value:"text",
                defaultValue:"",
                allJson:"",
                all:false, //是否显示"所有",值是"" 。 默认显示"请选择"，值是"" 
                allVal:"",
                handler:function(){ //onchange事件
                }, 
                callback:function(){ //回调事件
                }
            },options);
            sendAjax();
            function sendAjax(){
                Util.ajax.postJson(options.url, options.datas , ajaxCallback);
            }
            function errorAjax(){
                var $a = $('<a href="javascript:;">重新加载数据</a>')
                .bind("click",function(){
                    sendAjax();
                });
                _self.html($a);
            }
            function ajaxCallback(json,state){
                //判断状态,是否成功
                if(state){
                    var ops = '<select class="element text" id="'+options.id+'" name="'+options.name+'" >';
                    if(json.beans.length!=1){
                        if( (typeof options.all=="boolean")&&(options.all.constructor==Boolean) ){
                            if(options.all){
                                ops += '<option value="">所有</option>'; 
                            }else{
                                ops += '<option value="">请选择</option>';
                            }
                        }else{
                            if(options.all!=""){
                                ops += '<option value="'+options.allVal+'">'+options.all+'</option>';
                            }else{
                                ops += '';
                            }
                        }
                    }else{
                    	ops += '<option value="">请选择</option>';
                    }


                    for(var i=0;i<json.beans.length;i++){
                    	//添加设置默认值
                    	var sel = "", allJson= '';
                    	if(options.defaultValue){
                    		sel = (json.beans[i][options.key]==options.defaultValue) ? "selected='selected'" :"" ;
                    	}
                    	if (options.allJson) {
                    		allJson = "alljson='"+JSON.stringify(json.beans[i])+"'";
                    	};
                    	ops += '<option value="'+json.beans[i][options.key]+'" '+sel+' '+allJson+' >'+json.beans[i][options.value]+'</option>';
                    }
                    ops += '</select>';
                    _self.html( $(ops).bind("change",options.handler) );
                    //触发回调函数
                    if (typeof options.callback == 'function') {
                    	options.callback.call(_self.find("select")[0]);
                    }
                	if(options.muti){
                	}else{
                        //把下拉框变成可以输入的下拉框
                        //_self.find("select").combobox();
                	}
                }else{
                    errorAjax();
                }
            }
            _self.setValue = function(val){
            	var $select = $('select', _self);
            	$select.val(val);
            }
            return this;
        }
    });
})(jQuery);

//去掉字符串头尾空格
String.prototype.trim = function(){
    return this.replace(/(^\s*)|(\s*$)/g,"");
}

function showDom(domTag){
	$(domTag).toggle();
}

function outCallShowDom(domTag){
	var left_height = $(".left").height();
	var J_outcall_height = left_height - 120;

	if($(domTag).hasClass("fn-hide")){
		$(domTag).removeClass("fn-hide");
		$("#J_contact_left").css("height","120px");
		$(".J_outcall_contact_list").css("height",J_outcall_height+"px");
	}else{
		$(domTag).addClass("fn-hide");
		$("#J_contact_left").css("height","");
		$(".J_outcall_contact_list").css("height",left_height-40+"px");
	}
}
function setTab(name,cursel,n){
    for(i=1;i<=n;i++){
    var menu=document.getElementById(name+i);
    var con=document.getElementById(name+"_"+i);
    menu.className=i==cursel?"tabCurrent":"tabNormal";
    con.style.display=i==cursel?"block":"none";
    }
}


var curDate = new Date();
function endDateMax(){
    var startDate = $("#sDate").val();
    var y = startDate.substring(0,4);
    var maxDate = y+'-12-31';
    
    var y1 = JSON.stringify(curDate).substring(1,5);
    var y2 = startDate.substring(0,4);
    if(y2 < y1){
        return maxDate;
    }else{
        return curDate;
    }
}

function compareVal(){
    var startDate = $("#sDate").val();
    var endDate = $("#eDate").val();
    var y1 = startDate.substring(0,4);
    var y2 = endDate.substring(0,4);
    var cy =  JSON.stringify(curDate).substring(1,5);
    var ytempDate = y1+'-12-31';
    var ytempDateAll = ytempDate+' 23:59:59';
    if(y1!==y2){
        $("#eDate").val(ytempDate);
        $("#CREATE_DATE").val(ytempDateAll);
    }
    if(y1 == cy){
        $("#eDate").val(curDate.formatDD( "yyyy-MM-DD"));
        $("#CREATE_DATE").val(curDate.formatDD( "yyyy-MM-DD hh:mm:ss"));
    }
}
//当文本框失去焦点时，自动设置默认值
function setInputDefaultDay(obj ,startInput , endInput){
    if(obj.value==""){
        $("#sDate").val( $("#sDate").attr("defaultValue") );
        $("#eDate").val( $("#eDate").attr("defaultValue") );
        $("#"+startInput).val( $("#"+startInput).attr("defaultValue") );
        $("#"+endInput).val( $("#"+endInput).attr("defaultValue") );
        return false;
    }
}
//获取当前月的第一天和当天
function getFirstAndLastMonthDay(startInput, endInput , flag ){
	var curDate = new Date();
	//var _m = ((curDate.getMonth()+1)<10) ? ("0"+(curDate.getMonth()+1)) : (curDate.getMonth()+1);
	//var firstdate = curDate.getFullYear() + "-"+ _m + "-01";
    //var day = new Date( curDate.getFullYear() , _m , 0 );    
    //获取当月最后一天日期
	//var lastdate =  curDate.getFullYear() + "-"+ _m + "-"+ day.getDate();
	var fdate = curDate.formatDD( "yyyy-MM-01");
	var ldate = curDate.formatDD( "yyyy-MM-DD");
	$("#sDate").val(fdate).attr("defaultValue",fdate);
	$("#eDate").val(ldate).attr("defaultValue",ldate);
	if(!flag){
		fdate = curDate.formatDD( "yyyy-MM-01 00:00:00");
		ldate = curDate.formatDD( "yyyy-MM-DD 23:59:59");
	}
	$("#"+startInput).val(fdate).attr("defaultValue",fdate);
	$("#"+endInput).val(ldate).attr("defaultValue",ldate);
	return fdate+","+ldate;  
}

//获取当前年的第一天和当前天
function getFirstAndLastYearDay(startInput, endInput , flag ){
	var curDate = new Date();
	var fdate = curDate.formatDD( "yyyy-01-01");
	var ldate = curDate.formatDD( "yyyy-MM-DD");
	$("#sDate").val(fdate).attr("defaultValue",fdate);
	$("#eDate").val(ldate).attr("defaultValue",ldate);
	if(!flag){
		fdate = curDate.formatDD( "yyyy-01-01 00:00:00");
		ldate = curDate.formatDD( "yyyy-MM-DD 23:59:59");
	}
	$("#"+startInput).val(fdate).attr("defaultValue",fdate);
	$("#"+endInput).val(ldate).attr("defaultValue",ldate);
	return fdate+","+ldate;  
}

//获取当前年的第一天和当前天
function setNoticeDate(startInput, endInput , flag ){
	var curDate = new Date();
	var fdate = curDate.formatDD( "yyyy-MM-DD");
	var ldate = curDate.formatDD( "2020-12-31");
	if(!flag){
		fdate = curDate.formatDD( "yyyy-MM-DD 00:00:00");
		ldate = curDate.formatDD( "2020-12-31 23:59:59");
	}
	$("#"+startInput).val(fdate).attr("defaultValue",fdate);
	$("#"+endInput).val(ldate).attr("defaultValue",ldate);
	return fdate+","+ldate;  
}
// data.formatDD( "yyyy-MM-DD hh:mm:ss");
Date.prototype.formatDD = function( formatStr){ 
	var date = this;
	var str = formatStr; 
	str=str.replace(/yyyy|YYYY/,date.getFullYear()); 
	str=str.replace(/yy|YY/,(date.getYear() % 100)>9?(date.getYear() % 100).toString():"0" + (date.getYear() % 100)); 
	str=str.replace(/MM/,date.getMonth()>8?(date.getMonth()+1).toString():"0" + (date.getMonth()+1)); 
	str=str.replace(/M/g,date.getMonth()+1); 
	str=str.replace(/dd|DD/,date.getDate()>9?date.getDate().toString():"0" + date.getDate()); 
	str=str.replace(/d|D/g,date.getDate()); 
	str=str.replace(/hh|HH/,date.getHours()>9?date.getHours().toString():"0" + date.getHours()); 
	str=str.replace(/h|H/g,date.getHours()); 
	str=str.replace(/mm/,date.getMinutes()>9?date.getMinutes().toString():"0" + date.getMinutes()); 
	str=str.replace(/m/g,date.getMinutes()); 
	str=str.replace(/ss|SS/,date.getSeconds()>9?date.getSeconds().toString():"0" + date.getSeconds()); 
	str=str.replace(/s|S/g,date.getSeconds()); 
	return str; 
}

//字符串转换成日期
function strToDate(dateStr,formatStr){
    //YYYY是年
    //MM是“01”月的格式
    //DD是“01”日的格式
    //HH是小时、MN是分、SS是秒
    var digit=0;//退位计数器
    var date=new Date();
    var newFormat=formatStr.toUpperCase();
    var year=getNumFromStr(dateStr,newFormat,'YYYY');
    var month=getNumFromStr(dateStr,newFormat,'MM');
    var da=getNumFromStr(dateStr,newFormat,'DD');
    var hour=getNumFromStr(dateStr,newFormat,'HH');
    var mn=getNumFromStr(dateStr,newFormat,'MN');
    var ss=getNumFromStr(dateStr,newFormat,'SS');
    if (year > 0)
        date.setFullYear(year);
    if (month > 0)
        date.setMonth(month - 1);
    if (da > 0)
        date.setDate(da);
    if (hour > 0)
        date.setHours(hour);
    if (mn > 0)
        date.setMinutes(mn);
    if (ss > 0)
        date.setSeconds(ss);
    return date;
    
    function getNumFromStr(target,frm,s){
        //target是目标字符串，frm是模板字符串，s是匹配字符
        var len=s.length;
        var index=frm.indexOf(s);
        if(index<0)return index;
        var reStr=target.substr(index-digit,len);
        var result=parseInt(reStr,10);//(s=='SM'||s=='SD')&&
        if(result<10&&(reStr.charAt(0)!=0)){
            digit++;
        }
        return result;
    }
}

function compareDate(date1, date2){
    date1 = date1.getTime();
    date2 = date2.getTime();

    if(date1 > date2){
        return false;
    }
    return true;
}




$(function(){
	//展开更多查询
	if ($("#J_toggleSearch").length) {
		$("#J_toggleSearch").toggle(
			function(){
		        $("#JS_filterMore").show();
		        $("#J_toggleSearch").removeClass("down").addClass("up");
		    },
			function(){
		        $("#JS_filterMore").hide();
		        $("#J_toggleSearch").removeClass("up").addClass("down");
		    }
		);	
	};
});

/**
 * 单击表格，是选择单选还是多选
 * @method clickchange 
 * @param {Object} obj 当前被点击的checkbox,一般是this
 * @pa1ram {Boolean} multi 是否支持多选
 * @param {String} id 该单选框所在容器ID
 */
function clickchange(obj,multi,id){
    if(!multi){
        //$(obj).addClass("active").attr("checked",true).parent().parent().parent().siblings().find("td:eq(0) div input[type='checkbox']").attr("checked",false).removeClass("active");
        var $obj = $(obj);
        $obj.parent().parent().parent().parent().find("tr td input[type='checkbox']").removeAttr("checked").removeClass("active");
        $obj.addClass("active").attr("checked",true);
    }else{
        if($(obj).hasClass("active")){
            $(obj).removeClass("active").attr("checked",false);
        }else{
            $(obj).addClass("active").attr("checked",true);
        }
    }
}

/*
	让indexOf()方法兼容IE8
 */
(function(){
	if (!Array.prototype.indexOf){
        Array.prototype.indexOf = function(elt, from){
			var len = this.length >>> 0;
			var from = Number(arguments[1]) || 0;
			from = (from < 0) ? Math.ceil(from) : Math.floor(from);
			if (from < 0)
			from += len;
			for (; from < len; from++){
			if (from in this && this[from] === elt)
			  return from;
			}
  return -1;
        };
    }
})();

//解决ie下console.log()报错问题
window.console = window.console || (function(){
    var c = {};
    c.log = c.warn = c.debug = c.info = c.error = c.time = c.dir = c.profile = c.clear = c.exception = c.trace = c.assert = function(){};
    return c;
})();

// iframe自动获取高度
function initIframe(){
    var iframe = $("#J_busi_iframe:visible,#I_term:visible,#I_market:visible,#I_marketDetail:visible");
    try{
    	var _height = $(iframe).contents().find('body').height()+30;
        $(iframe).height((_height>600)?_height:600);
    }catch (ex){
    }
}

//表单开关
var $search_table = $('.search_table');
if ($search_table.length > 0 ) {
    var $seeMoreFilter = $('.seeMoreFilter', $search_table.el);
    var $searchMore = $('.searchMore', $search_table.el);
    
    if ($searchMore.length <= 0) {
        $seeMoreFilter.length && $seeMoreFilter.hide();
    } else if ($seeMoreFilter.length > 0) {
        $seeMoreFilter.find('a').on('click', function(e){
            var $el = $(e.currentTarget);
            var $link = $el;
            if ($link.attr('class').indexOf('down') >= 0) {
                $link.removeClass('down').addClass('up');
                $searchMore.show();
            }else{
                $link.removeClass('up').addClass('down');
                $searchMore.hide();
                $searchMore.find('input[type=text]').val('');
            }
        });
    }
    
    $search_table.delegate('input', 'keypress', function(e){
        if (e.keyCode == 13) {
            $('#J_search').trigger("click");
            return false;
        }
    })
};