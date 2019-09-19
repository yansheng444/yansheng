$(function() {
			
			
			var userid = localStorage.getItem("user_id");
			
			
			
			
			$(".back").off("click").click(function(){
				$(".content1_sea1").css("width","100%");
				$(".search").css("display","none");
				$(".inputSearch1").val("");
				$(".back").css("display","none");
				$(".one").css("display","block");
				$(".two").css("display","none");
			})
			$(".inputSearch1").click(function(){
				$(".content1_sea1").css("width","85%");
				$(".content1_sea1 input").css("width","86%");
				$(".search").css("width","20%");
				$(".search").css("display","block");
				$(".back").css("width","8%");
				$(".back").css("display","block");
				$(".one").css("display","none");
				$(".two").css("display","block");
			})
			//点击搜索按钮 执行方法
			$("#search").off("click").click(function() {
				console.log($(".inputSearch1").val());
				var a = $(".inputSearch1").val();
				
				if(a == '') {
					alert("请输入要搜索的内容!")
				} else {
					localStorage.setItem("inputS",a);
					location.href = "search_result.html?userid="+userid
				}
				var keywords = $("#keywords").val();
				search_history(keywords); //添加到缓存
				update_history(); //更新搜索历史
			})
			
			
			update_history();
			// 绑定回车事件
			$(document).keydown(function(event) {
				if(event.keyCode == 13) {
					$("#search").click();
				}
			});
			//搜索历史记录事件
			$.each($(".hist_li"), function() {
				$(this).off("click").click(function(){
					console.log($(this).text());
					var history_txt = $(this).text();
					$(".inputSearch1").val(history_txt);
				})
			});
			
			
			// 搜索点击事件
//			$("#search").click(function() {
//				
//			})

			// 清空搜索历史
			$("#empty").click(function() {
				mystorage.remove('keywords');
				$("#history ul").html(" ");
			})
		})

		/**
		 * [update_history 更新搜索历史]
		 * @return {[type]} [description]
		 */
		function update_history() {
			console.log(mystorage.get("keywords"));
			var history_keywords = mystorage.get("keywords");
			if(history_keywords) {
				var html = "";
				$.each(history_keywords, function(i, v) {
					html += "<li class='hist_li left' href='javascript:;'>" + v + "</li>"
				})
				$("#history ul").html(html);
			};
		}

		/**
		 * [search_history //搜索历史函数存储]
		 * @param  {[type]} value [搜索词]
		 * @return {[type]}       [description]
		 */
		function search_history(value) {
			var data = mystorage.get("keywords");
			if(!data) {
				var data = []; //定义一个空数组
			} else if(data.length === 9) { //搜索历史数量
				data.shift(); //删除数组第一个元素有
			} else {

			};
			if(value) { //判断搜索词是否为空
				if(data.indexOf(value) < 0) { //判断搜索词是否存在数组中
					data.push(value); //搜索词添加到数组中
					mystorage.set("keywords", data); //存储到本地化存储中
				};
			};
		}

		/**
		 * [mystorage 存储localstorage时候最好是封装一个自己的键值，在这个值里存储自己的内容对象，封装一个方法针对自己对象进行操作。避免冲突也会在开发中更方便。]
		 * @param  {String} ){                 var ms [description]
		 * @return {[type]}     [description]
		        console.log(mystorage.set('tqtest','tqtestcontent'));//存储
		        console.log(mystorage.set('aa','123'));//存储
		        console.log(mystorage.set('tqtest1','tqtestcontent1'));//存储
		        console.log(mystorage.set('tqtest1','newtqtestcontent1'));//修改
		        console.log(mystorage.get('tqtest'));//读取
		        console.log(mystorage.remove('tqtest'));//删除
		        mystorage.clear();//整体清除
		 */
		var mystorage = (function mystorage() {
			var ms = "mystorage";
			var storage = window.localStorage;
			if(!window.localStorage) {
				alert("浏览器不支持localstorage");
				return false;
			}
			var set = function(key, value) {
				//存储
				var mydata = storage.getItem(ms);
				if(!mydata) {
					this.init();
					mydata = storage.getItem(ms);
				}
				mydata = JSON.parse(mydata);
				mydata.data[key] = value;
				storage.setItem(ms, JSON.stringify(mydata));
				return mydata.data;
			};
			var get = function(key) {
				//读取
				var mydata = storage.getItem(ms);
				if(!mydata) {
					return false;
				}
				mydata = JSON.parse(mydata);
				return mydata.data[key];
			};
			var remove = function(key) {
				//读取
				var mydata = storage.getItem(ms);
				if(!mydata) {
					return false;
				}
				mydata = JSON.parse(mydata);
				delete mydata.data[key];
				storage.setItem(ms, JSON.stringify(mydata));
				return mydata.data;
			};
			var clear = function() {
				//清除对象
				storage.removeItem(ms);
			};
			var init = function() {
				storage.setItem(ms, '{"data":{}}');
			};
			return {
				set: set,
				get: get,
				remove: remove,
				init: init,
				clear: clear
			};
		})();